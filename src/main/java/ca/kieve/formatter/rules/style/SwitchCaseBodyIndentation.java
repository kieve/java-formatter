package ca.kieve.formatter.rules.style;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixes the indentation of switch case bodies when the case expressions wrap
 * across multiple lines.
 * <p>
 * Eclipse JDT indents the body (after {@code ->} or {@code :}) at the same
 * level as the case expressions. This rule adds one extra indent level so the
 * body is visually distinct from the labels:
 * <pre>
 *     // Eclipse output (wrong)
 *     case
 *         "a",
 *         "b" ->
 *         body;
 *
 *     // Fixed
 *     case
 *         "a",
 *         "b" ->
 *             body;
 * </pre>
 */
public final class SwitchCaseBodyIndentation {
    private static final int INDENT_SIZE = 4;

    private SwitchCaseBodyIndentation() {
    }

    public static String apply(String source) {
        String[] lines = source.split("\n", -1);
        List<int[]> edits = new ArrayList<>();

        int i = 0;
        while (i < lines.length) {
            String trimmed = lines[i].stripLeading();

            if (isWrappedCaseStart(trimmed)) {
                int caseIndent = leadingSpaces(lines[i]);
                int exprIndent = -1;
                boolean foundTerminator = false;

                // Scan forward for the terminator line (ends with -> or :)
                int j = i + 1;
                while (j < lines.length) {
                    String jTrimmed = lines[j].stripLeading();
                    if (jTrimmed.isEmpty()) {
                        j++;
                        continue;
                    }

                    int jIndent = leadingSpaces(lines[j]);
                    if (jIndent <= caseIndent) {
                        break;
                    }

                    if (exprIndent == -1) {
                        exprIndent = jIndent;
                    }

                    if (jTrimmed.endsWith("->") || jTrimmed.endsWith(":")) {
                        foundTerminator = true;
                        break;
                    }
                    j++;
                }

                if (foundTerminator && exprIndent > caseIndent) {
                    // Re-indent body lines after the terminator
                    for (int k = j + 1; k < lines.length; k++) {
                        String kTrimmed = lines[k].stripLeading();
                        if (kTrimmed.isEmpty()) {
                            continue;
                        }

                        int kIndent = leadingSpaces(lines[k]);
                        if (kIndent <= caseIndent) {
                            break;
                        }

                        edits.add(new int[] { k, INDENT_SIZE });
                    }
                    i = j + 1;
                    continue;
                }
            }
            i++;
        }

        if (edits.isEmpty()) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        int editIdx = 0;
        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            if (editIdx < edits.size()
                && edits.get(editIdx)[0] == lineIdx) {
                sb.append(" ".repeat(edits.get(editIdx)[1]));
                editIdx++;
            }
            sb.append(lines[lineIdx]);
            if (lineIdx >= lines.length - 1) {
                continue;
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * A wrapped case start is a line beginning with {@code case} where the
     * expressions continue on the next line — i.e., the line does not contain
     * {@code ->} and does not end with {@code :}.
     */
    private static boolean isWrappedCaseStart(String trimmed) {
        if (!trimmed.startsWith("case ") && !trimmed.equals("case")) {
            return false;
        }
        if (trimmed.contains("->")) {
            return false;
        }
        return !trimmed.endsWith(":");
    }

    private static int leadingSpaces(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                break;
            }
            count++;
        }
        return count;
    }
}
