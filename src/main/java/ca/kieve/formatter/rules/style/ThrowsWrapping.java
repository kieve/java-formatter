package ca.kieve.formatter.rules.style;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.type.ReferenceType;

import ca.kieve.formatter.FormatConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Normalizes the formatting of {@code throws} clauses in method and
 * constructor declarations.
 * <p>
 * When the entire signature (up to and including the opening brace or
 * semicolon) fits within the configured line length, the throws clause is
 * kept on a single line. When it does not fit, the clause is wrapped with
 * each exception on its own line, indented by one continuation unit:
 * <pre>
 *     void method() throws
 *         FirstException,
 *         SecondException
 *     {
 *     }
 * </pre>
 * Eclipse JDT keeps {@code throws} + the first exception as an inseparable
 * unit, so Eclipse's throws wrapping is disabled and this rule handles all
 * throws formatting instead.
 */
public final class ThrowsWrapping {
    private record Replacement(
        int startLine,
        int endLine,
        List<String> newLines
    ) {
    }

    private ThrowsWrapping() {
    }

    public static String apply(String source, FormatConfig config) {
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(source);
        } catch (Exception e) {
            return source;
        }

        int limit = config.getMaxLineLength();
        String[] sourceLines = source.split("\n", -1);
        List<Replacement> replacements = new ArrayList<>();

        cu.findAll(CallableDeclaration.class).stream()
            .filter(c -> !c.getThrownExceptions().isEmpty())
            .forEach(
                c -> buildReplacement(c, sourceLines, limit)
                    .ifPresent(replacements::add)
            );

        if (replacements.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top so line numbers stay valid
        replacements.sort(
            Comparator.comparingInt(
                (Replacement r) -> r.startLine
            ).reversed()
        );

        String[] lines = sourceLines;
        for (Replacement r : replacements) {
            lines = applyReplacement(lines, r);
        }

        return String.join("\n", lines);
    }

    private static java.util.Optional<Replacement> buildReplacement(
        CallableDeclaration<?> callable,
        String[] lines,
        int limit
    ) {
        List<ReferenceType> thrownExceptions = callable.getThrownExceptions();
        ReferenceType firstEx = thrownExceptions.get(0);
        ReferenceType lastEx =
            thrownExceptions.get(thrownExceptions.size() - 1);

        if (firstEx.getBegin().isEmpty() || lastEx.getEnd().isEmpty()
            || callable.getBegin().isEmpty()) {
            return java.util.Optional.empty();
        }

        int firstExLine = firstEx.getBegin().get().line - 1;
        int firstExCol = firstEx.getBegin().get().column - 1;
        int lastExEndLine = lastEx.getEnd().get().line - 1;
        // JavaParser end column is inclusive
        int lastExEndCol = lastEx.getEnd().get().column;

        // Find "throws" keyword by searching backward from first exception
        int throwsLine = -1;
        int throwsCol = -1;
        for (int i = firstExLine;
            i >= Math.max(0, firstExLine - 10);
            i--) {
            int searchBefore =
                (i == firstExLine) ? firstExCol : lines[i].length();
            int col = findThrowsKeyword(lines[i], searchBefore);
            if (col >= 0) {
                throwsLine = i;
                throwsCol = col;
                break;
            }
        }
        if (throwsLine < 0) {
            return java.util.Optional.empty();
        }

        // Find ')' before "throws"
        int closeParenLine = -1;
        int closeParenCol = -1;
        for (int i = throwsLine;
            i >= Math.max(0, throwsLine - 10);
            i--) {
            int searchBefore =
                (i == throwsLine) ? throwsCol : lines[i].length();
            int col = findCloseParenBefore(lines[i], searchBefore);
            if (col >= 0) {
                closeParenLine = i;
                closeParenCol = col;
                break;
            }
        }
        if (closeParenLine < 0) {
            return java.util.Optional.empty();
        }

        // Find '{' or ';' after last exception
        int endLine = -1;
        int endCol = -1;
        char endChar = '\0';
        for (int i = lastExEndLine;
            i < Math.min(lines.length, lastExEndLine + 5);
            i++) {
            int startCol = (i == lastExEndLine) ? lastExEndCol : 0;
            for (int j = startCol; j < lines[i].length(); j++) {
                char ch = lines[i].charAt(j);
                if (ch == '{' || ch == ';') {
                    endLine = i;
                    endCol = j;
                    endChar = ch;
                    break;
                }
            }
            if (endLine >= 0) {
                break;
            }
        }
        if (endLine < 0) {
            return java.util.Optional.empty();
        }

        // Method indent from the callable's declaration start line
        String methodIndent =
            extractIndent(lines[callable.getBegin().get().line - 1]);
        String continuationIndent = methodIndent + "    ";

        // Get exception names from JavaParser
        List<String> exceptionNames = new ArrayList<>();
        for (ReferenceType type : thrownExceptions) {
            exceptionNames.add(type.asString());
        }

        // Content up to and including ')' on the close paren line
        String beforeCloseParen =
            lines[closeParenLine].substring(0, closeParenCol + 1);

        // Content after '{' or ';' on the end line (to preserve)
        String trailing = "";
        if (endCol + 1 < lines[endLine].length()) {
            trailing = lines[endLine].substring(endCol + 1);
        }

        // Build hypothetical single line
        String throwsList = String.join(", ", exceptionNames);
        String suffix = endChar == '{' ? " {" : ";";
        String singleLine =
            beforeCloseParen + " throws " + throwsList + suffix;

        if (singleLine.length() <= limit) {
            List<String> newLines = List.of(singleLine + trailing);
            return java.util.Optional.of(
                new Replacement(closeParenLine, endLine, newLines)
            );
        }

        // Multi-line format
        List<String> newLines = new ArrayList<>();
        newLines.add(beforeCloseParen + " throws");
        for (int i = 0; i < exceptionNames.size(); i++) {
            String name = exceptionNames.get(i);
            if (i < exceptionNames.size() - 1) {
                newLines.add(continuationIndent + name + ",");
            } else if (endChar == ';') {
                newLines.add(continuationIndent + name + ";");
            } else {
                newLines.add(continuationIndent + name);
            }
        }
        if (endChar == '{') {
            newLines.add(methodIndent + "{" + trailing);
        } else if (!trailing.isEmpty()) {
            int lastIdx = newLines.size() - 1;
            newLines.set(lastIdx, newLines.get(lastIdx) + trailing);
        }

        return java.util.Optional.of(
            new Replacement(closeParenLine, endLine, newLines)
        );
    }

    /**
     * Find the "throws" keyword in a line, searching backward from
     * {@code beforeCol}. Returns the column index or -1 if not found.
     */
    private static int findThrowsKeyword(String line, int beforeCol) {
        String keyword = "throws";
        int end = Math.min(beforeCol, line.length());
        for (int i = end - keyword.length(); i >= 0; i--) {
            if (!line.regionMatches(i, keyword, 0, keyword.length())) {
                continue;
            }
            if (i > 0
                && Character.isJavaIdentifierPart(line.charAt(i - 1))) {
                continue;
            }
            int after = i + keyword.length();
            if (after < line.length()
                && Character.isJavaIdentifierPart(line.charAt(after))) {
                continue;
            }
            return i;
        }
        return -1;
    }

    /**
     * Find the last ')' in a line before {@code beforeCol}.
     */
    private static int findCloseParenBefore(String line, int beforeCol) {
        for (int i = Math.min(beforeCol - 1, line.length() - 1);
            i >= 0;
            i--) {
            if (line.charAt(i) == ')') {
                return i;
            }
        }
        return -1;
    }

    private static String extractIndent(String line) {
        int i = 0;
        while (i < line.length()
            && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(0, i);
    }

    private static String[] applyReplacement(
        String[] lines,
        Replacement replacement
    ) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < replacement.startLine; i++) {
            result.add(lines[i]);
        }
        result.addAll(replacement.newLines);
        for (int i = replacement.endLine + 1; i < lines.length; i++) {
            result.add(lines[i]);
        }
        return result.toArray(new String[0]);
    }
}
