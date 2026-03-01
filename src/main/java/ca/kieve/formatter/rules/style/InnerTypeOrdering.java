package ca.kieve.formatter.rules.style;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Ensures inner type declarations (classes, interfaces, enums, records,
 * annotation types) appear before all other members (fields, methods,
 * constructors, initializer blocks) within any type body.
 * <p>
 * Uses an iterative convergence loop to handle nested types that both
 * need reordering: each pass reorders one type, then re-parses.
 */
public final class InnerTypeOrdering {
    private record Region(List<String> lines, boolean isType) {
    }

    private static final int MAX_PASSES = 10;

    private InnerTypeOrdering() {
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
                    .orElse(0)
            )
                .reversed()
        );

        for (TypeDeclaration<?> type : types) {
            String result = tryReorder(type, lines);
            if (result != null) {
                return result;
            }
        }

        return source;
    }

    private static String tryReorder(TypeDeclaration<?> type, String[] lines) {
        List<BodyDeclaration<?>> members = type.getMembers();
        if (members.isEmpty()) {
            return null;
        }

        // Partition into inner types vs others
        List<BodyDeclaration<?>> innerTypes = new ArrayList<>();
        List<BodyDeclaration<?>> others = new ArrayList<>();
        for (BodyDeclaration<?> member : members) {
            if (member instanceof TypeDeclaration) {
                innerTypes.add(member);
                continue;
            }
            others.add(member);
        }

        if (innerTypes.isEmpty() || others.isEmpty()) {
            return null;
        }

        // Check if already ordered: all inner types end before any other begins
        int lastInnerEnd = innerTypes.stream()
            .mapToInt(m -> m.getEnd().map(p -> p.line).orElse(0))
            .max().orElse(0);
        int firstOtherBegin = others.stream()
            .mapToInt(m -> m.getBegin().map(p -> p.line).orElse(Integer.MAX_VALUE))
            .min().orElse(Integer.MAX_VALUE);

        if (lastInnerEnd < firstOtherBegin) {
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
                lines
            );
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
                m -> m.getBegin().map(p -> p.line).orElse(0)
            )
        );

        // Build regions: each member "owns" the gap above it
        List<Region> regions = new ArrayList<>();
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
            boolean isType = member instanceof TypeDeclaration;

            List<String> regionLines = new ArrayList<>();
            for (int li = regionStart; li <= memberEnd; li++) {
                regionLines.add(lines[li]);
            }
            regions.add(new Region(regionLines, isType));
        }

        // Reorder: inner types first, then others, preserving relative order
        List<Region> innerRegions = new ArrayList<>();
        List<Region> otherRegions = new ArrayList<>();
        for (Region r : regions) {
            if (r.isType) {
                innerRegions.add(r);
                continue;
            }
            otherRegions.add(r);
        }

        // Build new body content
        List<String> newBody = new ArrayList<>();

        // Inner type regions (strip leading blank lines from the first)
        boolean first = true;
        for (Region r : innerRegions) {
            List<String> rl = r.lines;
            if (first) {
                rl = stripLeadingBlankLines(rl);
                first = false;
            }
            newBody.addAll(rl);
        }

        // Strip trailing blank lines from inner type group, add one separator
        while (!newBody.isEmpty() && newBody.getLast().trim().isEmpty()) {
            newBody.removeLast();
        }
        newBody.add("");

        // Other regions (strip leading blank lines from the first)
        boolean firstOther = true;
        for (Region r : otherRegions) {
            List<String> rl = r.lines;
            if (firstOther) {
                rl = stripLeadingBlankLines(rl);
                firstOther = false;
            }
            newBody.addAll(rl);
        }

        // Strip trailing blank lines
        while (!newBody.isEmpty() && newBody.getLast().trim().isEmpty()) {
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

    /**
     * Find the line (0-based) containing the opening {@code {}} of a type
     * declaration. Tracks parenthesis depth to skip braces inside annotation
     * arguments like {@code @SuppressWarnings({"unchecked"})}.
     */
    private static int findOpenBraceLine(
        TypeDeclaration<?> type,
        String[] lines
    ) {
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
                    continue;
                }
                if (c == ')') {
                    parenDepth--;
                    continue;
                }
                if (c == '{' && parenDepth == 0) {
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
        String[] lines
    ) {
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
                    continue;
                }
                if (c == '}') {
                    braceDepth--;
                    continue;
                }
                if (c == ';' && braceDepth == 0) {
                    return li + 1;
                }
            }
        }
        return openBraceLine + 1;
    }

    private static List<String> stripLeadingBlankLines(List<String> lines) {
        int start = 0;
        while (start < lines.size() && lines.get(start).trim().isEmpty()) {
            start++;
        }
        return new ArrayList<>(lines.subList(start, lines.size()));
    }
}
