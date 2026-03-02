package ca.kieve.formatter.rules.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Moves trailing binary operators to the beginning of the next line.
 * <p>
 * Eclipse's {@code wrap_before_binary_operator} setting only affects
 * wrapping it introduces — it does not fix existing wrap-after-operator
 * style. This rule detects a trailing binary operator at the end of a
 * line and moves it to the beginning of the next line.
 * <p>
 * Operators handled: single-char {@code + - * / % & | ^}; double-char
 * {@code && ||}. Increment/decrement ({@code ++ --}) are excluded.
 */
public final class OperatorWrapping {
    private record Edit(int lineIndex, String operator, int operatorStartCol) {
    }

    private static final String SINGLE_OPERATORS = "+-*/%&|^";

    private OperatorWrapping() {
    }

    public static String apply(String source) {
        String[] lines = source.split("\n", -1);
        List<Edit> edits = new ArrayList<>();
        boolean inBlockComment = false;
        boolean inTextBlock = false;

        for (int lineIdx = 0; lineIdx < lines.length - 1; lineIdx++) {
            String line = lines[lineIdx];
            int lastCodeEnd = -1;

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
                        lastCodeEnd = col + 1;
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
                    lastCodeEnd = col + 1;
                    continue;
                }

                if (ch == '\'') {
                    col = skipCharLiteral(line, col);
                    lastCodeEnd = col + 1;
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

                if (ch != ' ' && ch != '\t') {
                    lastCodeEnd = col + 1;
                }
            }

            if (lastCodeEnd <= 0) {
                continue;
            }

            String codePart = line.substring(0, lastCodeEnd);
            String operator = null;
            int operatorStart = -1;

            // Check for double-char operators: &&, ||
            if (codePart.length() >= 2) {
                String last2 =
                    codePart.substring(codePart.length() - 2);
                if (last2.equals("&&") || last2.equals("||")) {
                    operator = last2;
                    operatorStart = codePart.length() - 2;
                }
            }

            // Check for single-char operators
            if (operator == null) {
                char lastCh = codePart.charAt(codePart.length() - 1);
                if (SINGLE_OPERATORS.indexOf(lastCh) >= 0) {
                    // Exclude ++ and --
                    if ((lastCh == '+' || lastCh == '-')
                        && codePart.length() >= 2
                        && codePart.charAt(codePart.length() - 2)
                            == lastCh) {
                        continue;
                    }
                    operator = String.valueOf(lastCh);
                    operatorStart = codePart.length() - 1;
                }
            }

            if (operator == null) {
                continue;
            }

            // Skip if operator is the only content on the line
            if (codePart.substring(0, operatorStart).isBlank()) {
                continue;
            }

            edits.add(new Edit(lineIdx, operator, operatorStart));
        }

        if (edits.isEmpty()) {
            return source;
        }

        // Apply edits bottom-to-top so line indices stay valid
        edits.sort((a, b) -> Integer.compare(b.lineIndex, a.lineIndex));
        List<String> result = new ArrayList<>(Arrays.asList(lines));

        for (Edit edit : edits) {
            String originalLine = result.get(edit.lineIndex);
            String nextLine = result.get(edit.lineIndex + 1);

            result.set(
                edit.lineIndex,
                originalLine.substring(0, edit.operatorStartCol)
                    .stripTrailing()
            );

            String nextIndent = extractIndent(nextLine);
            String nextContent = nextLine.stripLeading();
            result.set(
                edit.lineIndex + 1,
                nextIndent + edit.operator + " " + nextContent
            );
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
