package ca.kieve.formatter.rules.style;

import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Splits multiple variable declarations on a single line into individual
 * declarations. For example, {@code int x, y, z;} becomes three separate
 * {@code int x;}, {@code int y;}, {@code int z;} declarations.
 * <p>
 * Handles both class fields and local variables (inside expression statements),
 * but does not touch for-loop initializers or try-with-resources.
 */
public final class SplitFieldDeclarations {
    private record Replacement(
        int startLine,
        int endLine,
        List<String> newLines) {
    }

    private SplitFieldDeclarations() {
    }

    public static String apply(String source) {
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(source);
        } catch (Exception e) {
            return source;
        }

        final String[] sourceLines = source.split("\n", -1);
        List<Replacement> replacements = new ArrayList<>();

        // Find multi-variable field declarations
        cu.findAll(FieldDeclaration.class).stream()
            .filter(f -> f.getVariables().size() > 1)
            .forEach(
                f -> buildReplacement(f, f.getVariables(), sourceLines)
                    .ifPresent(replacements::add));

        // Find multi-variable local declarations in expression statements
        cu.findAll(VariableDeclarationExpr.class).stream()
            .filter(v -> v.getVariables().size() > 1)
            .filter(SplitFieldDeclarations::isStandaloneStatement)
            .forEach(v -> {
                ExpressionStmt stmt = (ExpressionStmt) v.getParentNode().get();
                buildReplacementForLocal(stmt, v, sourceLines)
                    .ifPresent(replacements::add);
            });

        if (replacements.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top so line numbers stay valid
        replacements.sort(
            Comparator.comparingInt(
                (Replacement r) -> r.startLine).reversed());

        String[] lines = sourceLines;
        for (Replacement r : replacements) {
            lines = applyReplacement(lines, r);
        }

        return String.join("\n", lines);
    }

    private static boolean isStandaloneStatement(VariableDeclarationExpr expr) {
        return expr.getParentNode()
            .filter(p -> p instanceof ExpressionStmt)
            .isPresent();
    }

    private static java.util.Optional<Replacement> buildReplacement(
        FieldDeclaration field,
        List<VariableDeclarator> variables,
        String[] lines) {
        if (!field.getBegin().isPresent() || !field.getEnd().isPresent()) {
            return java.util.Optional.empty();
        }

        Position begin = field.getBegin().get();
        Position end = field.getEnd().get();
        int startLine = begin.line - 1;
        int endLine = end.line - 1;

        String fullText = extractText(
            lines,
            startLine,
            begin.column - 1,
            endLine,
            end.column);

        String firstName = variables.get(0).getName().asString();
        int nameIdx = findFirstVariableName(fullText, firstName);
        if (nameIdx < 0) {
            return java.util.Optional.empty();
        }

        String prefix = fullText.substring(0, nameIdx);
        String indent = extractIndent(lines[startLine]);
        String trailingComment = extractTrailingComment(lines, endLine, end.column);

        List<String> newLines = new ArrayList<>();
        if (trailingComment != null) {
            newLines.add(indent + trailingComment);
        }
        for (VariableDeclarator var : variables) {
            newLines.add(indent + prefix + var.toString() + ";");
        }

        return java.util.Optional.of(
            new Replacement(startLine, endLine, newLines));
    }

    private static java.util.Optional<Replacement> buildReplacementForLocal(
        ExpressionStmt stmt,
        VariableDeclarationExpr expr,
        String[] lines) {
        if (!stmt.getBegin().isPresent() || !stmt.getEnd().isPresent()) {
            return java.util.Optional.empty();
        }

        Position begin = stmt.getBegin().get();
        Position end = stmt.getEnd().get();
        int startLine = begin.line - 1;
        int endLine = end.line - 1;

        String fullText = extractText(
            lines,
            startLine,
            begin.column - 1,
            endLine,
            end.column);

        List<VariableDeclarator> variables = expr.getVariables();
        String firstName = variables.get(0).getName().asString();
        int nameIdx = findFirstVariableName(fullText, firstName);
        if (nameIdx < 0) {
            return java.util.Optional.empty();
        }

        String prefix = fullText.substring(0, nameIdx);
        String indent = extractIndent(lines[startLine]);
        String trailingComment = extractTrailingComment(lines, endLine, end.column);

        List<String> newLines = new ArrayList<>();
        if (trailingComment != null) {
            newLines.add(indent + trailingComment);
        }
        for (VariableDeclarator var : variables) {
            newLines.add(indent + prefix + var.toString() + ";");
        }

        return java.util.Optional.of(
            new Replacement(startLine, endLine, newLines));
    }

    private static String extractText(
        String[] lines,
        int startLine,
        int startCol,
        int endLine,
        int endCol) {
        if (startLine == endLine) {
            return lines[startLine].substring(startCol, endCol);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(lines[startLine].substring(startCol));
        for (int i = startLine + 1; i < endLine; i++) {
            sb.append("\n").append(lines[i]);
        }
        sb.append("\n").append(lines[endLine], 0, endCol);
        return sb.toString();
    }

    private static int findFirstVariableName(String text, String name) {
        // Search for the variable name that appears as a word boundary
        // (not preceded/followed by a letter, digit, or underscore)
        int searchFrom = 0;
        while (true) {
            int idx = text.indexOf(name, searchFrom);
            if (idx < 0) {
                return -1;
            }
            // Check it's at a word boundary
            boolean validBefore = idx == 0
                || !Character.isJavaIdentifierPart(text.charAt(idx - 1));
            int afterIdx = idx + name.length();
            boolean validAfter = afterIdx >= text.length()
                || !Character.isJavaIdentifierPart(text.charAt(afterIdx));
            if (validBefore && validAfter) {
                return idx;
            }
            searchFrom = idx + 1;
        }
    }

    private static String extractIndent(String line) {
        int i = 0;
        while (i < line.length()
            && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(0, i);
    }

    private static String extractTrailingComment(
        String[] lines,
        int endLine,
        int endCol) {
        String line = lines[endLine];
        if (endCol < line.length()) {
            String rest = line.substring(endCol).trim();
            if (rest.startsWith("//")) {
                return rest;
            }
        }
        return null;
    }

    private static String[] applyReplacement(
        String[] lines,
        Replacement replacement) {
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
