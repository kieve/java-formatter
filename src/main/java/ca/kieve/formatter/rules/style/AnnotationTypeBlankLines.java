package ca.kieve.formatter.rules.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Removes blank lines between members inside {@code @interface} bodies.
 * <p>
 * Eclipse JDT's {@code blank_lines_before_method} treats annotation members
 * like regular methods, inserting a blank line before each one. The
 * {@code blank_lines_before_abstract_method} setting doesn't cover them
 * either. This rule compensates by stripping blank lines at the top level
 * of each {@code @interface} body so annotation types remain compact.
 * <p>
 * Blank lines that separate inner type declarations from other members are
 * preserved (one blank line per group boundary).
 */
public final class AnnotationTypeBlankLines {
    private static final Pattern ANNOTATION_TYPE = Pattern.compile(
        "@interface\\s+\\w+"
    );

    private static final Pattern TYPE_KEYWORD = Pattern.compile(
        "\\b(?:class|interface|enum|record)\\s+\\w+"
    );

    private AnnotationTypeBlankLines() {
    }

    public static String apply(String source) {
        String[] lines = source.split("\n", -1);
        Set<Integer> linesToRemove = new HashSet<>();

        for (int i = 0; i < lines.length; i++) {
            String trimmed = lines[i].trim();

            if (trimmed.startsWith("//")
                || trimmed.startsWith("*")
                || trimmed.startsWith("/*")) {
                continue;
            }

            if (!ANNOTATION_TYPE.matcher(trimmed).find()) {
                continue;
            }

            int braceIdx = findOpeningBrace(lines, i);
            if (braceIdx < 0) {
                continue;
            }

            int depth = countBracesDelta(lines[braceIdx]);
            if (depth <= 0) {
                continue;
            }

            int j = braceIdx + 1;
            while (j < lines.length && depth > 0) {
                if (depth == 1 && lines[j].trim().isEmpty()) {
                    linesToRemove.add(j);
                }
                depth += countBracesDelta(lines[j]);
                j++;
            }
        }

        if (linesToRemove.isEmpty()) {
            return source;
        }

        preserveGroupSeparators(lines, linesToRemove);

        if (linesToRemove.isEmpty()) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < lines.length; k++) {
            if (linesToRemove.contains(k)) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(lines[k]);
        }
        return sb.toString();
    }

    /**
     * Un-marks blank lines that serve as group separators between inner type
     * declarations and other members. For each group of consecutive marked
     * blank lines, if the previous non-blank line closes a nested block
     * ({@code }}) or the next non-blank line starts a type declaration,
     * exactly one blank line is preserved.
     */
    private static void preserveGroupSeparators(
        String[] lines,
        Set<Integer> linesToRemove
    ) {
        List<Integer> sorted = new ArrayList<>(linesToRemove);
        Collections.sort(sorted);

        int i = 0;
        while (i < sorted.size()) {
            int groupStart = sorted.get(i);
            int groupEnd = groupStart;
            while (i + 1 < sorted.size()
                && sorted.get(i + 1) == groupEnd + 1) {
                i++;
                groupEnd = sorted.get(i);
            }

            int prevLine = findPrevNonBlank(lines, groupStart);
            int nextLine = findNextNonBlank(lines, groupEnd);

            boolean needsSeparator = false;
            if (prevLine >= 0 && lines[prevLine].trim().equals("}")) {
                needsSeparator = true;
            }
            if (nextLine >= 0 && isTypeDeclaration(lines[nextLine])) {
                needsSeparator = true;
            }

            if (needsSeparator) {
                linesToRemove.remove(groupStart);
            }

            i++;
        }
    }

    private static boolean isTypeDeclaration(String line) {
        String trimmed = line.trim();
        if (trimmed.startsWith("//")
            || trimmed.startsWith("/*")
            || trimmed.startsWith("*")) {
            return false;
        }
        return TYPE_KEYWORD.matcher(trimmed).find();
    }

    private static int findPrevNonBlank(String[] lines, int idx) {
        for (int i = idx - 1; i >= 0; i--) {
            if (!lines[i].trim().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static int findNextNonBlank(String[] lines, int idx) {
        for (int i = idx + 1; i < lines.length; i++) {
            if (!lines[i].trim().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static int findOpeningBrace(String[] lines, int start) {
        for (int i = start; i < lines.length; i++) {
            if (lines[i].indexOf('{') >= 0) {
                return i;
            }
        }
        return -1;
    }

    private static int countBracesDelta(String line) {
        int delta = 0;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inString) {
                if (c == '\\') {
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                if (c == '\\') {
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                break;
            }

            if (c == '"') {
                inString = true;
                continue;
            }
            if (c == '\'') {
                inChar = true;
                continue;
            }
            if (c == '{') {
                delta++;
                continue;
            }
            if (c != '}') {
                continue;
            }
            delta--;
        }

        return delta;
    }
}
