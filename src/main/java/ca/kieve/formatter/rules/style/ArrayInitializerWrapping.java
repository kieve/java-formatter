package ca.kieve.formatter.rules.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Moves content off the {@code {`} line when the corresponding {@code }}
 * appears on a different line (i.e., the initializer wraps).
 * <p>
 * Eclipse JDT's wrap-where-necessary mode produces:
 * <pre>
 *     int[] values = { 1, 2, 3, ..., 22,
 *         23, 24, 25 };
 * </pre>
 * This rule rewrites it to:
 * <pre>
 *     int[] values = {
 *         1, 2, 3, ..., 22,
 *         23, 24, 25 };
 * </pre>
 * The existing {@link ClosingBracketNewline} rule then moves the closing
 * {@code }} to its own line.
 */
public final class ArrayInitializerWrapping {
    private record Edit(int line, int col, String nextLineIndent) {
    }

    private ArrayInitializerWrapping() {
    }

    public static String apply(String source) {
        String[] lines = source.split("\n", -1);
        List<Edit> edits = new ArrayList<>();
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];

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

                if (ch != '{') {
                    continue;
                }

                // Found a '{'. Check if content follows on this line.
                boolean hasContentAfter = false;
                for (int j = col + 1; j < line.length(); j++) {
                    char after = line.charAt(j);
                    // Skip into line comment — no more code on this line
                    if (after == '/' && j + 1 < line.length()
                        && line.charAt(j + 1) == '/') {
                        break;
                    }
                    if (!Character.isWhitespace(after)) {
                        hasContentAfter = true;
                        break;
                    }
                }

                if (!hasContentAfter) {
                    continue;
                }

                // Check if the matching '}' is on a different line
                int[] closerPos = findMatchingCloser(lines, lineIdx, col);
                if (closerPos == null) {
                    continue;
                }

                int closerLine = closerPos[0];
                if (closerLine == lineIdx) {
                    // Same line — single-line initializer, leave it alone
                    continue;
                }

                // The initializer wraps. Determine indentation from the
                // next line (Eclipse already indented continuation correctly).
                String nextIndent = (lineIdx + 1 < lines.length)
                    ? extractIndent(lines[lineIdx + 1])
                    : extractIndent(line) + "    ";

                edits.add(new Edit(lineIdx, col, nextIndent));
            }
        }

        if (edits.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top so line indices stay valid
        edits.sort((a, b) -> {
            if (a.line != b.line) {
                return Integer.compare(b.line, a.line);
            }
            return Integer.compare(b.col, a.col);
        });

        List<String> result = new ArrayList<>(Arrays.asList(lines));
        for (Edit edit : edits) {
            String originalLine = result.get(edit.line);
            String keepLine = originalLine.substring(0, edit.col + 1);
            String movedContent = originalLine.substring(edit.col + 1).stripLeading();
            String newLine = edit.nextLineIndent + movedContent;
            result.set(edit.line, keepLine);
            result.add(edit.line + 1, newLine);
        }

        return String.join("\n", result);
    }

    private static String extractIndent(String line) {
        int i = 0;
        while (i < line.length()
            && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(0, i);
    }

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
        int startCol
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
                if (ch == '{') {
                    depth++;
                } else if (ch == '}') {
                    depth--;
                    if (depth == 0) {
                        return new int[] { lineIdx, col };
                    }
                }
            }
        }

        return null;
    }
}
