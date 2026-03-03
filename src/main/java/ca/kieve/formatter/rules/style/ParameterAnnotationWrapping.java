package ca.kieve.formatter.rules.style;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Moves parameter annotations onto their own lines when parameters are
 * wrapped one-per-line.
 * <p>
 * After Eclipse JDT wraps parameters, annotations stay inline:
 * <pre>
 *     void method(
 *         @SuppressWarnings("unchecked") @Deprecated String param,
 *         @Nullable String other
 *     ) {
 *     }
 * </pre>
 * This rule rewrites each annotated parameter so that every annotation
 * appears on its own line above the type:
 * <pre>
 *     void method(
 *         @SuppressWarnings("unchecked")
 *         @Deprecated
 *         String param,
 *         @Nullable
 *         String other
 *     ) {
 *     }
 * </pre>
 */
public final class ParameterAnnotationWrapping {
    private record Replacement(int startLine, int endLine, List<String> newLines) {
    }

    private ParameterAnnotationWrapping() {
    }

    public static String apply(String source) {
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(source);
        } catch (Exception e) {
            return source;
        }

        String[] sourceLines = source.split("\n", -1);
        List<Replacement> replacements = new ArrayList<>();

        cu.findAll(CallableDeclaration.class).forEach(
            c -> collectReplacements(c, sourceLines, replacements)
        );

        if (replacements.isEmpty()) {
            return source;
        }

        // Apply bottom-to-top so line numbers stay valid
        replacements.sort(Comparator.comparingInt((Replacement r) -> r.startLine).reversed());

        String[] lines = sourceLines;
        for (Replacement r : replacements) {
            lines = applyReplacement(lines, r);
        }

        return String.join("\n", lines);
    }

    private static void collectReplacements(
        CallableDeclaration<?> callable,
        String[] lines,
        List<Replacement> replacements
    ) {
        List<Parameter> params = callable.getParameters();
        if (params.size() < 1) {
            return;
        }

        // Check if parameters are wrapped: first parameter starts on a
        // later line than the callable name
        Parameter firstParam = params.get(0);
        if (callable.getName().getBegin().isEmpty()
            || firstParam.getBegin().isEmpty()) {
            return;
        }
        int callableLine = callable.getName().getBegin().get().line;
        int firstParamLine = firstParam.getBegin().get().line;
        if (firstParamLine <= callableLine) {
            return;
        }

        for (Parameter param : params) {
            List<AnnotationExpr> annotations = param.getAnnotations();
            if (annotations.isEmpty()) {
                continue;
            }

            if (param.getBegin().isEmpty() || param.getType().getBegin().isEmpty()) {
                continue;
            }

            int paramLine = param.getBegin().get().line - 1;
            int typeLine = param.getType().getBegin().get().line - 1;

            // Only act when annotations are on the same line as the type
            if (paramLine != typeLine) {
                continue;
            }

            String originalLine = lines[paramLine];
            String indent = extractIndent(originalLine);

            // Build new lines: one per annotation, then the rest (type + name + trailing)
            List<String> newLines = new ArrayList<>();
            for (AnnotationExpr ann : annotations) {
                if (ann.getBegin().isEmpty() || ann.getEnd().isEmpty()) {
                    continue;
                }
                int annStartCol = ann.getBegin().get().column - 1;
                int annEndCol = ann.getEnd().get().column; // inclusive, so this is one-past
                String annText = originalLine.substring(annStartCol, annEndCol);
                newLines.add(indent + annText);
            }

            // The "rest" starts at the type's begin column
            int typeCol = param.getType().getBegin().get().column - 1;
            String rest = originalLine.substring(typeCol);
            newLines.add(indent + rest);

            replacements.add(new Replacement(paramLine, paramLine, newLines));
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

    private static String[] applyReplacement(String[] lines, Replacement replacement) {
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
