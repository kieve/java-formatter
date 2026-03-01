package ca.kieve.formatter.rules.style;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts field declarations within type bodies into groups ordered by
 * static &gt; public &gt; final priority, and ensures all fields precede
 * non-field members (constructors, methods, initializer blocks).
 * <p>
 * Fields are classified into 8 groups by three binary criteria:
 * <ul>
 * <li>Group 0: static, public, final</li>
 * <li>Group 1: static, public, non-final</li>
 * <li>Group 2: static, non-public, final</li>
 * <li>Group 3: static, non-public, non-final</li>
 * <li>Group 4: instance, public, final</li>
 * <li>Group 5: instance, public, non-final</li>
 * <li>Group 6: instance, non-public, final</li>
 * <li>Group 7: instance, non-public, non-final</li>
 * </ul>
 * Within each group, the relative order of fields is preserved.
 * A single blank line separates different groups.
 * <p>
 * Uses an iterative convergence loop to handle nested types that both
 * need reordering: each pass reorders one type, then re-parses.
 */
public final class FieldOrdering {
    private record Region(List<String> lines, int group) {
    }

    private static final int MAX_PASSES = 10;

    private static final int NON_FIELD = -1;

    private FieldOrdering() {
    }

    public static String apply(String source) {
        String current = source;
        for (int i = 0; i < MAX_PASSES; i++) {
            String result = applyOnce(current);
            if (result.equals(current)) {
                return result;
            }
            current = result;
        }
        return current;
    }

    private static String applyOnce(String source) {
        CompilationUnit cu;
        ParserConfiguration config = StaticJavaParser.getParserConfiguration();
        ParserConfiguration.LanguageLevel originalLevel = config.getLanguageLevel();
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        try {
            cu = StaticJavaParser.parse(source);
        } catch (Exception e) {
            return source;
        } finally {
            config.setLanguageLevel(originalLevel);
        }

        String[] lines = source.split("\n", -1);

        // Find all type declarations, sorted by begin line descending
        // (process deepest/latest first so line numbers stay valid)
        List<TypeDeclaration<?>> types = new ArrayList<>();
        cu.findAll(TypeDeclaration.class).forEach(types::add);
        types.sort(
            Comparator.comparingInt(
                (TypeDeclaration<?> t) -> t.getBegin()
                    .map(p -> p.line)
                    .orElse(0))
                .reversed());

        for (TypeDeclaration<?> type : types) {
            String result = tryReorder(type, lines);
            if (result != null) {
                return result;
            }
        }

        return source;
    }

    private static String tryReorder(
        TypeDeclaration<?> type,
        String[] lines) {
        List<BodyDeclaration<?>> members = type.getMembers();
        if (members.isEmpty()) {
            return null;
        }

        // Partition into fields and non-fields.
        // Static initializer blocks that follow a field are treated as
        // companions (they initialize the preceding field) and excluded
        // from the non-field list so they move with their field.
        List<FieldDeclaration> fields = new ArrayList<>();
        List<BodyDeclaration<?>> nonFields = new ArrayList<>();
        boolean lastWasFieldOrCompanion = false;
        for (BodyDeclaration<?> member : members) {
            if (member instanceof FieldDeclaration fd) {
                fields.add(fd);
                lastWasFieldOrCompanion = true;
            } else if (member instanceof InitializerDeclaration id
                && id.isStatic() && lastWasFieldOrCompanion) {
                // Static init block attached to preceding field — skip
            } else {
                nonFields.add(member);
                lastWasFieldOrCompanion = false;
            }
        }

        if (fields.isEmpty()) {
            return null;
        }

        // Check if already ordered
        if (isAlreadyOrdered(fields, nonFields)) {
            return null;
        }

        // Find body boundaries (0-based line indices)
        int openBraceLine = findOpenBraceLine(type, lines);
        if (openBraceLine < 0) {
            return null;
        }

        int bodyStart;
        if (type instanceof EnumDeclaration) {
            bodyStart = findEnumBodyStart(
                (EnumDeclaration) type,
                openBraceLine,
                lines);
        } else {
            bodyStart = openBraceLine + 1;
        }

        int closeBraceLine = type.getEnd().map(p -> p.line - 1).orElse(-1);
        if (closeBraceLine < 0) {
            return null;
        }

        // Sort members by their begin line
        List<BodyDeclaration<?>> sortedMembers = new ArrayList<>(members);
        sortedMembers.sort(
            Comparator.comparingInt(
                m -> m.getBegin().map(p -> p.line).orElse(0)));

        // Build regions: each member "owns" the gap above it.
        // Static initializer blocks that follow a field get the same
        // group as that field so they sort together.
        List<Region> regions = new ArrayList<>();
        int lastFieldGroup = NON_FIELD;
        for (int i = 0; i < sortedMembers.size(); i++) {
            BodyDeclaration<?> member = sortedMembers.get(i);
            int regionStart;
            if (i == 0) {
                regionStart = bodyStart;
            } else {
                // Line after previous member's end (0-based)
                int prevEndJP = sortedMembers.get(i - 1).getEnd()
                    .map(p -> p.line).orElse(0);
                regionStart = prevEndJP; // JP line N -> 0-based index N
            }
            int memberEnd = member.getEnd()
                .map(p -> p.line - 1).orElse(0); // 0-based

            int group = NON_FIELD;
            if (member instanceof FieldDeclaration fd) {
                group = computeGroup(fd);
                lastFieldGroup = group;
            } else if (member instanceof InitializerDeclaration id
                && id.isStatic() && lastFieldGroup != NON_FIELD) {
                group = lastFieldGroup;
            } else {
                lastFieldGroup = NON_FIELD;
            }

            List<String> regionLines = new ArrayList<>();
            for (int li = regionStart; li <= memberEnd; li++) {
                regionLines.add(lines[li]);
            }
            regions.add(new Region(regionLines, group));
        }

        // Partition into field and non-field regions
        List<Region> fieldRegions = new ArrayList<>();
        List<Region> nonFieldRegions = new ArrayList<>();
        for (Region r : regions) {
            if (r.group != NON_FIELD) {
                fieldRegions.add(r);
            } else {
                nonFieldRegions.add(r);
            }
        }

        // Stable-sort field regions by group number
        fieldRegions.sort(Comparator.comparingInt(r -> r.group));

        // Build new body content
        List<String> newBody = new ArrayList<>();

        // Field regions with blank line separators between different groups
        int prevGroup = NON_FIELD;
        boolean first = true;
        for (Region r : fieldRegions) {
            List<String> rl = r.lines;
            if (first) {
                rl = stripLeadingBlankLines(rl);
                first = false;
            } else if (r.group != prevGroup) {
                // Strip trailing blank lines before adding separator
                while (!newBody.isEmpty()
                    && newBody.getLast().trim().isEmpty()) {
                    newBody.removeLast();
                }
                newBody.add("");
                rl = stripLeadingBlankLines(rl);
            }
            newBody.addAll(rl);
            prevGroup = r.group;
        }

        // Separator between fields and non-fields
        if (!nonFieldRegions.isEmpty()) {
            while (!newBody.isEmpty()
                && newBody.getLast().trim().isEmpty()) {
                newBody.removeLast();
            }
            newBody.add("");
        }

        // Non-field regions
        boolean firstNonField = true;
        for (Region r : nonFieldRegions) {
            List<String> rl = r.lines;
            if (firstNonField) {
                rl = stripLeadingBlankLines(rl);
                firstNonField = false;
            }
            newBody.addAll(rl);
        }

        // Strip trailing blank lines
        while (!newBody.isEmpty()
            && newBody.getLast().trim().isEmpty()) {
            newBody.removeLast();
        }

        // Reconstruct source
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= openBraceLine; i++) {
            result.add(lines[i]);
        }
        // Enum constants section (between { and bodyStart)
        if (bodyStart > openBraceLine + 1) {
            for (int i = openBraceLine + 1; i < bodyStart; i++) {
                result.add(lines[i]);
            }
            result.add(""); // Blank line after enum constants
        }
        result.addAll(newBody);
        for (int i = closeBraceLine; i < lines.length; i++) {
            result.add(lines[i]);
        }

        return String.join("\n", result);
    }

    private static boolean isAlreadyOrdered(
        List<FieldDeclaration> fields,
        List<BodyDeclaration<?>> nonFields) {
        // Check that fields are in non-decreasing group order
        int prevGroup = NON_FIELD;
        for (FieldDeclaration fd : fields) {
            int group = computeGroup(fd);
            if (group < prevGroup) {
                return false;
            }
            prevGroup = group;
        }

        // Check that all fields end before any non-field begins
        if (nonFields.isEmpty()) {
            return true;
        }

        int lastFieldEnd = fields.stream()
            .mapToInt(f -> f.getEnd().map(p -> p.line).orElse(0))
            .max().orElse(0);

        int firstNonFieldBegin = nonFields.stream()
            .mapToInt(
                m -> m.getBegin().map(p -> p.line)
                    .orElse(Integer.MAX_VALUE))
            .min().orElse(Integer.MAX_VALUE);

        return lastFieldEnd < firstNonFieldBegin;
    }

    private static int computeGroup(FieldDeclaration fd) {
        boolean isStatic = fd.isStatic();
        boolean isPublic = fd.isPublic();
        boolean isFinal = fd.isFinal();
        return (!isStatic ? 4 : 0)
            + (!isPublic ? 2 : 0)
            + (!isFinal ? 1 : 0);
    }

    /**
     * Find the line (0-based) containing the opening {@code {}} of a type
     * declaration. Tracks parenthesis depth to skip braces inside annotation
     * arguments like {@code @SuppressWarnings({"unchecked"})}.
     */
    private static int findOpenBraceLine(
        TypeDeclaration<?> type,
        String[] lines) {
        int beginLine = type.getBegin().map(p -> p.line - 1).orElse(-1);
        if (beginLine < 0) {
            return -1;
        }

        int parenDepth = 0;
        for (int li = beginLine; li < lines.length; li++) {
            String line = lines[li];
            for (int ci = 0; ci < line.length(); ci++) {
                char c = line.charAt(ci);
                if (c == '/' && ci + 1 < line.length()
                    && line.charAt(ci + 1) == '/') {
                    break; // Skip line comment
                }
                if (c == '(') {
                    parenDepth++;
                } else if (c == ')') {
                    parenDepth--;
                } else if (c == '{' && parenDepth == 0) {
                    return li;
                }
            }
        }
        return -1;
    }

    /**
     * Find the first line (0-based) after the enum constants section.
     * Scans for the {@code ;} that terminates enum constants, tracking
     * brace depth to skip constant bodies.
     */
    private static int findEnumBodyStart(
        EnumDeclaration enumDecl,
        int openBraceLine,
        String[] lines) {
        int searchStartLine;
        int searchStartCol;

        if (!enumDecl.getEntries().isEmpty()) {
            List<EnumConstantDeclaration> entries = enumDecl.getEntries();
            EnumConstantDeclaration lastEntry = entries.get(entries.size() - 1);
            searchStartLine = lastEntry.getEnd()
                .map(p -> p.line - 1).orElse(openBraceLine);
            searchStartCol = lastEntry.getEnd()
                .map(p -> p.column).orElse(0);
        } else {
            searchStartLine = openBraceLine;
            searchStartCol = lines[openBraceLine].indexOf('{') + 1;
        }

        int braceDepth = 0;
        for (int li = searchStartLine; li < lines.length; li++) {
            String line = lines[li];
            int startCol = (li == searchStartLine) ? searchStartCol : 0;
            for (int ci = startCol; ci < line.length(); ci++) {
                char c = line.charAt(ci);
                if (c == '/' && ci + 1 < line.length()
                    && line.charAt(ci + 1) == '/') {
                    break; // Skip line comment
                }
                if (c == '{') {
                    braceDepth++;
                } else if (c == '}') {
                    braceDepth--;
                } else if (c == ';' && braceDepth == 0) {
                    return li + 1;
                }
            }
        }
        return openBraceLine + 1;
    }

    private static List<String> stripLeadingBlankLines(List<String> lines) {
        int start = 0;
        while (start < lines.size()
            && lines.get(start).trim().isEmpty()) {
            start++;
        }
        return new ArrayList<>(lines.subList(start, lines.size()));
    }
}
