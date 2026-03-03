package ca.kieve.formatter.rules.style;

import ca.kieve.formatter.FormatConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Joins wrapped content back onto the previous line when the single-line
 * form fits within the configured max line length. Only applies to
 * parenthesized constructs: method parameters, constructor parameters,
 * method arguments, and try-with-resources.
 * <p>
 * After {@link ClosingBracketNewline} runs, wrapped constructs have this
 * canonical shape:
 * <pre>
 *     someDeclaration(
 *         content lines...
 *     ) {
 * </pre>
 * This rule detects that shape and joins when the result fits within the
 * max line length.
 */
public final class WrappedLineJoining {
    private record Replacement(int startLine, int endLine, List<String> newLines) {
    }

    private WrappedLineJoining() {
    }

    public static String apply(String source, FormatConfig config) {
        int limit = config.getMaxLineLength();
        String result = source;

        // Multi-pass to handle nested wrapping (inner joins first,
        // then outer may become eligible)
        for (int pass = 0; pass < 10; pass++) {
            String next = applyOnePass(result, limit);
            if (next.equals(result)) {
                break;
            }
            result = next;
        }

        return result;
    }

    private static String applyOnePass(String source, int limit) {
        String[] lines = source.split("\n", -1);
        List<Replacement> replacements = new ArrayList<>();
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];
            int lastCodePos = -1;

            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);

                if (inTextBlock) {
                    if (ch == '\\' && col + 1 < line.length()) {
                        col++;
                        continue;
                    }
                    if (ch == '"' && col + 2 < line.length()
                        && line.charAt(col + 1) == '"'
                        && line.charAt(col + 2) == '"') {
                        inTextBlock = false;
                        col += 2;
                    }
                    continue;
                }

                if (inBlockComment) {
                    if (ch == '*' && col + 1 < line.length()
                        && line.charAt(col + 1) == '/') {
                        inBlockComment = false;
                        col++;
                    }
                    continue;
                }

                // Check for text block start
                if (ch == '"' && col + 2 < line.length()
                    && line.charAt(col + 1) == '"'
                    && line.charAt(col + 2) == '"') {
                    inTextBlock = true;
                    col += 2;
                    continue;
                }

                // Check for string literal
                if (ch == '"') {
                    col = skipStringLiteral(line, col);
                    continue;
                }

                // Check for char literal
                if (ch == '\'') {
                    col = skipCharLiteral(line, col);
                    continue;
                }

                // Check for block comment start
                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '*') {
                    inBlockComment = true;
                    col++;
                    continue;
                }

                // Check for line comment
                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '/') {
                    break;
                }

                // Code character
                if (Character.isWhitespace(ch)) {
                    continue;
                }
                lastCodePos = col;
            }

            if (lastCodePos < 0) {
                continue;
            }

            char lastChar = line.charAt(lastCodePos);
            if (lastChar != '(') {
                continue;
            }

            // Find matching closer
            int[] closerPos = findMatchingCloser(lines, lineIdx, lastCodePos);
            if (closerPos == null) {
                continue;
            }

            int closerLine = closerPos[0];
            int closerCol = closerPos[1];

            // The closer must be on a different line
            if (closerLine == lineIdx) {
                continue;
            }

            // Validate that ')' is the first non-whitespace char on its line
            boolean isFirstNonWhitespace = true;
            for (int j = 0; j < closerCol; j++) {
                if (!Character.isWhitespace(lines[closerLine].charAt(j))) {
                    isFirstNonWhitespace = false;
                    break;
                }
            }
            if (!isFirstNonWhitespace) {
                continue;
            }

            // Collect content lines between opener and closer
            List<String> contentLines = new ArrayList<>();
            for (int i = lineIdx + 1; i < closerLine; i++) {
                contentLines.add(lines[i]);
            }

            if (contentLines.isEmpty()) {
                continue;
            }

            // Guard: skip if any content line starts with @ (annotations)
            if (hasAnnotation(contentLines)) {
                continue;
            }

            // Guard: skip if any content line contains { or } outside
            // strings/comments (array initializers, lambdas)
            if (hasBraces(contentLines)) {
                continue;
            }

            // Build joined content
            String joinedContent = joinContent(contentLines);

            // Build opener prefix (up to and including the '(')
            String openerPrefix = line.substring(0, lastCodePos + 1);

            // Build closer suffix (everything from ')' onward)
            String closerSuffix = lines[closerLine].substring(closerCol);

            // Build hypothetical single line
            String singleLine = openerPrefix + joinedContent + closerSuffix;

            // Guard: skip if too long
            if (singleLine.length() > limit) {
                continue;
            }

            replacements.add(new Replacement(lineIdx, closerLine, List.of(singleLine)));
        }

        if (replacements.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top, skip overlapping replacements
        replacements.sort(Comparator.comparingInt((Replacement r) -> r.startLine).reversed());

        int lowestConsumed = Integer.MAX_VALUE;
        List<String> result = new ArrayList<>(Arrays.asList(lines));
        for (Replacement r : replacements) {
            // Skip if this replacement overlaps with a later one
            if (r.endLine >= lowestConsumed) {
                continue;
            }

            // Remove lines from startLine to endLine, replace with newLines
            for (int i = r.endLine; i >= r.startLine; i--) {
                result.remove(i);
            }
            for (int i = 0; i < r.newLines.size(); i++) {
                result.add(r.startLine + i, r.newLines.get(i));
            }

            lowestConsumed = r.startLine;
        }

        return String.join("\n", result);
    }

    private static boolean hasAnnotation(List<String> contentLines) {
        for (String line : contentLines) {
            String trimmed = line.stripLeading();
            if (trimmed.startsWith("@")) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasBraces(List<String> contentLines) {
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (String line : contentLines) {
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);

                if (inTextBlock) {
                    if (ch == '\\' && col + 1 < line.length()) {
                        col++;
                        continue;
                    }
                    if (ch == '"' && col + 2 < line.length()
                        && line.charAt(col + 1) == '"'
                        && line.charAt(col + 2) == '"') {
                        inTextBlock = false;
                        col += 2;
                    }
                    continue;
                }

                if (inBlockComment) {
                    if (ch == '*' && col + 1 < line.length()
                        && line.charAt(col + 1) == '/') {
                        inBlockComment = false;
                        col++;
                    }
                    continue;
                }

                if (ch == '"' && col + 2 < line.length()
                    && line.charAt(col + 1) == '"'
                    && line.charAt(col + 2) == '"') {
                    inTextBlock = true;
                    col += 2;
                    continue;
                }

                if (ch == '"') {
                    col = skipStringLiteral(line, col);
                    continue;
                }

                if (ch == '\'') {
                    col = skipCharLiteral(line, col);
                    continue;
                }

                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '*') {
                    inBlockComment = true;
                    col++;
                    continue;
                }

                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '/') {
                    break;
                }

                if (ch == '{' || ch == '}') {
                    return true;
                }
            }
        }
        return false;
    }

    private static String joinContent(List<String> contentLines) {
        StringBuilder sb = new StringBuilder();
        for (String contentLine : contentLines) {
            String stripped = contentLine.strip();
            if (stripped.isEmpty()) {
                continue;
            }
            if (stripped.startsWith(".")) {
                // Method chain continuation — no space separator
                sb.append(stripped);
            } else if (sb.isEmpty()) {
                sb.append(stripped);
            } else {
                sb.append(' ').append(stripped);
            }
        }
        return sb.toString();
    }

    /**
     * Skip past a string literal starting at the opening quote.
     * Returns the index of the closing quote (the for loop will
     * increment past it).
     */
    private static int skipStringLiteral(String line, int openQuote) {
        int col = openQuote + 1;
        while (col < line.length()) {
            if (line.charAt(col) == '\\') {
                col++;
            } else if (line.charAt(col) == '"') {
                return col;
            }
            col++;
        }
        return col - 1;
    }

    /**
     * Skip past a char literal starting at the opening quote.
     * Returns the index of the closing quote.
     */
    private static int skipCharLiteral(String line, int openQuote) {
        int col = openQuote + 1;
        while (col < line.length()) {
            if (line.charAt(col) == '\\') {
                col++;
            } else if (line.charAt(col) == '\'') {
                return col;
            }
            col++;
        }
        return col - 1;
    }

    private static int[] findMatchingCloser(String[] lines, int startLine, int startCol) {
        int depth = 1;
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (int lineIdx = startLine; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];
            int startPos = (lineIdx == startLine) ? startCol + 1 : 0;

            for (int col = startPos; col < line.length(); col++) {
                char ch = line.charAt(col);

                if (inTextBlock) {
                    if (ch == '\\' && col + 1 < line.length()) {
                        col++;
                        continue;
                    }
                    if (ch == '"' && col + 2 < line.length()
                        && line.charAt(col + 1) == '"'
                        && line.charAt(col + 2) == '"') {
                        inTextBlock = false;
                        col += 2;
                    }
                    continue;
                }

                if (inBlockComment) {
                    if (ch == '*' && col + 1 < line.length()
                        && line.charAt(col + 1) == '/') {
                        inBlockComment = false;
                        col++;
                    }
                    continue;
                }

                // Check for text block start
                if (ch == '"' && col + 2 < line.length()
                    && line.charAt(col + 1) == '"'
                    && line.charAt(col + 2) == '"') {
                    inTextBlock = true;
                    col += 2;
                    continue;
                }

                // Check for string literal
                if (ch == '"') {
                    col = skipStringLiteral(line, col);
                    continue;
                }

                // Check for char literal
                if (ch == '\'') {
                    col = skipCharLiteral(line, col);
                    continue;
                }

                // Check for block comment start
                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '*') {
                    inBlockComment = true;
                    col++;
                    continue;
                }

                // Check for line comment
                if (ch == '/' && col + 1 < line.length()
                    && line.charAt(col + 1) == '/') {
                    break;
                }

                // Track depth
                if (ch == '(') {
                    depth++;
                    continue;
                }
                if (ch != ')') {
                    continue;
                }
                depth--;
                if (depth == 0) {
                    return new int[] { lineIdx, col };
                }
            }
        }

        return null;
    }
}
