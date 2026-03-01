package ca.kieve.formatter.rules.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Moves closing brackets to their own lines when the corresponding opener
 * wraps (i.e., the opener is the last code character on its line). Applies to
 * {@code ()}, {@code []}, {@code <>}, and {@code {}}.
 * <p>
 * For example, Eclipse JDT produces:
 * <pre>
 *     void method(
 *         String firstParam,
 *         String fourthParam) {
 *     }
 * </pre>
 * This rule rewrites it to:
 * <pre>
 *     void method(
 *         String firstParam,
 *         String fourthParam
 *     ) {
 *     }
 * </pre>
 */
public final class ClosingBracketNewline {
    private record Split(int line, int col, String indent) {
    }

    private ClosingBracketNewline() {
    }

    public static String apply(String source) {
        String[] lines = source.split("\n", -1);
        List<Split> splits = new ArrayList<>();
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
            if (lastChar != '(' && lastChar != '['
                && lastChar != '{' && lastChar != '<') {
                continue;
            }

            // For '<', check preceding char is not '<' (avoid << shift)
            if (lastChar == '<' && lastCodePos > 0
                && line.charAt(lastCodePos - 1) == '<') {
                continue;
            }

            char closer = closerFor(lastChar);
            int[] closerPos = findMatchingCloser(
                lines,
                lineIdx,
                lastCodePos,
                lastChar,
                closer
            );
            if (closerPos == null) {
                continue;
            }

            int closerLine = closerPos[0];
            int closerCol = closerPos[1];

            // Check if there's non-whitespace before the closer on its line
            boolean hasContentBefore = false;
            for (int j = 0; j < closerCol; j++) {
                if (Character.isWhitespace(lines[closerLine].charAt(j))) {
                    continue;
                }
                hasContentBefore = true;
                break;
            }

            if (!hasContentBefore) {
                continue;
            }

            splits.add(
                new Split(
                    closerLine,
                    closerCol,
                    extractIndent(lines[lineIdx])
                )
            );
        }

        if (splits.isEmpty()) {
            return source;
        }

        // Sort bottom-to-top, right-to-left so line index shifts
        // don't affect earlier splits
        splits.sort((a, b) -> {
            if (a.line != b.line) {
                return Integer.compare(b.line, a.line);
            }
            return Integer.compare(b.col, a.col);
        });

        List<String> result = new ArrayList<>(Arrays.asList(lines));
        for (Split split : splits) {
            String originalLine = result.get(split.line);
            String before = originalLine.substring(0, split.col).stripTrailing();
            String after = split.indent + originalLine.substring(split.col);
            result.set(split.line, after);
            result.add(split.line, before);
        }

        return String.join("\n", result);
    }

    private static char closerFor(char opener) {
        return switch (opener) {
        case '(' -> ')';
        case '[' -> ']';
        case '{' -> '}';
        case '<' -> '>';
        default -> throw new IllegalArgumentException();
        };
    }

    private static String extractIndent(String line) {
        int i = 0;
        while (i < line.length()
            && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(0, i);
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

    private static int[] findMatchingCloser(
        String[] lines,
        int startLine,
        int startCol,
        char opener,
        char closer
    ) {
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
                if (ch == opener) {
                    depth++;
                    continue;
                }
                if (ch != closer) {
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
