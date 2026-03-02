package ca.kieve.formatter.rules.style;

import ca.kieve.formatter.FormatConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wraps long assignment statements after the {@code =} operator when the
 * entire statement fits on a single line (the RHS has no wrapping of its own).
 * <p>
 * Eclipse's {@code alignment_for_assignment} setting interferes with other
 * wrapping (e.g., method-invocation arg wrapping) when enabled. This custom
 * rule wraps only single-line assignments that exceed the line length limit,
 * leaving already-wrapped statements untouched.
 * <p>
 * Wrapping places the RHS on a new line indented by the base indentation
 * plus one continuation unit (4 spaces).
 */
public final class AssignmentWrapping {
    private static final String[] COMPOUND_SUFFIXES = {
        "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^="
    };

    private record Edit(int line, int col, String indent) {
    }

    private AssignmentWrapping() {
    }

    public static String apply(String source, FormatConfig config) {
        int limit = config.getMaxLineLength();
        String[] lines = source.split("\n", -1);
        List<Edit> edits = new ArrayList<>();
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];

            if (line.length() <= limit) {
                // Track state even for short lines
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
                }
                continue;
            }

            // Line exceeds limit — look for an assignment operator
            int parenDepth = 0;
            boolean inString = false;
            boolean inChar = false;
            int assignCol = -1;
            int assignLen = 0;
            boolean foundSemicolon = false;

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

                if (inString) {
                    if (ch == '\\') {
                        col++;
                    } else if (ch == '"') {
                        inString = false;
                    }
                    continue;
                }

                if (inChar) {
                    if (ch == '\\') {
                        col++;
                    } else if (ch == '\'') {
                        inChar = false;
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
                    inString = true;
                    continue;
                }

                // Check for char literal
                if (ch == '\'') {
                    inChar = true;
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

                // Track parenthesis depth
                if (ch == '(') {
                    parenDepth++;
                    continue;
                }
                if (ch == ')') {
                    parenDepth--;
                    continue;
                }

                // Track semicolons (only outside parens)
                if (ch == ';' && parenDepth == 0 && assignCol >= 0) {
                    foundSemicolon = true;
                    continue;
                }

                // Skip if inside parentheses
                if (parenDepth > 0) {
                    continue;
                }

                // Look for assignment operators
                if (ch != '=' && assignCol < 0) {
                    // Check compound operators
                    if (col + 1 < line.length()
                        && line.charAt(col + 1) == '=') {
                        String twoChar = line.substring(col, col + 2);
                        for (String compound : COMPOUND_SUFFIXES) {
                            if (twoChar.equals(compound)) {
                                assignCol = col;
                                assignLen = 2;
                                col++;
                                break;
                            }
                        }
                    }
                    continue;
                }

                if (ch == '=' && assignCol < 0) {
                    // Exclude ==, !=, <=, >=
                    if (col + 1 < line.length()
                        && line.charAt(col + 1) == '=') {
                        col++;
                        continue;
                    }
                    if (col > 0) {
                        char prev = line.charAt(col - 1);
                        if (prev == '!' || prev == '<' || prev == '>') {
                            continue;
                        }
                    }
                    assignCol = col;
                    assignLen = 1;
                }
            }

            if (assignCol < 0 || !foundSemicolon) {
                continue;
            }

            String baseIndent = extractIndent(line);
            String newIndent = baseIndent + "    ";
            edits.add(new Edit(lineIdx, assignCol + assignLen, newIndent));
        }

        if (edits.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top so line indices stay valid
        edits.sort((a, b) -> Integer.compare(b.line, a.line));

        List<String> result = new ArrayList<>(Arrays.asList(lines));
        for (Edit edit : edits) {
            String originalLine = result.get(edit.line);
            String keepLine = originalLine.substring(0, edit.col);
            String rhs = originalLine.substring(edit.col).stripLeading();
            result.set(edit.line, keepLine);
            result.add(edit.line + 1, edit.indent + rhs);
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
}
