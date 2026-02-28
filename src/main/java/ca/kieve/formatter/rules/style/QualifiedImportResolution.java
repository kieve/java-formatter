package ca.kieve.formatter.rules.style;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import ca.kieve.formatter.FormatterTags;
import ca.kieve.formatter.FormatterTags.ProtectedSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Replaces fully qualified class references with simple names and adds
 * corresponding import statements. Handles type references, annotations,
 * and static member access chains.
 * <p>
 * This rule must run before Eclipse JDT formatting so that newly added
 * imports can be sorted and grouped by downstream rules.
 */
public final class QualifiedImportResolution {
    private QualifiedImportResolution() {
    }

    public static String apply(String source) {
        // Phase 1: Protect formatter-off blocks
        ProtectedSource ps = FormatterTags.protect(source);
        String working = ps.source();

        // Phase 2: Parse with JavaParser
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(working);
        } catch (Exception e) {
            return source;
        }

        // Phase 3: Collect existing imports
        Map<String, String> existingImports = new HashMap<>();
        Set<String> wildcardPackages = new HashSet<>();
        for (ImportDeclaration imp : cu.getImports()) {
            if (imp.isStatic()) {
                continue;
            }
            if (imp.isAsterisk()) {
                wildcardPackages.add(imp.getName().asString());
            } else {
                String fqcn = imp.getName().asString();
                String simple = fqcn.substring(fqcn.lastIndexOf('.') + 1);
                existingImports.put(simple, fqcn);
            }
        }

        String currentPackage = cu.getPackageDeclaration()
            .map(PackageDeclaration::getName)
            .map(Name::asString)
            .orElse("");

        // Phase 4: Find qualified references
        Map<String, Set<String>> candidates = new LinkedHashMap<>();
        List<Occurrence> occurrences = new ArrayList<>();

        // A) Type references (ClassOrInterfaceType with scope)
        cu.findAll(ClassOrInterfaceType.class).stream()
            .filter(t -> t.getScope().isPresent())
            .forEach(t -> collectTypeReference(t, candidates, occurrences));

        // B) Annotation references
        cu.findAll(MarkerAnnotationExpr.class)
            .forEach(
                a -> collectAnnotationReference(
                    a.getName(),
                    candidates,
                    occurrences));
        cu.findAll(SingleMemberAnnotationExpr.class)
            .forEach(
                a -> collectAnnotationReference(
                    a.getName(),
                    candidates,
                    occurrences));
        cu.findAll(NormalAnnotationExpr.class)
            .forEach(
                a -> collectAnnotationReference(
                    a.getName(),
                    candidates,
                    occurrences));

        // C) Static access chains
        cu.findAll(MethodCallExpr.class)
            .forEach(
                m -> collectStaticMethodAccess(
                    m,
                    candidates,
                    occurrences));
        cu.findAll(FieldAccessExpr.class)
            .forEach(
                f -> collectStaticFieldAccess(
                    f,
                    candidates,
                    occurrences));

        if (occurrences.isEmpty()) {
            return source;
        }

        // Phase 5: Resolve collisions
        Set<String> newImports = new LinkedHashSet<>();
        Set<String> simplifiableNames = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : candidates.entrySet()) {
            String simpleName = entry.getKey();
            Set<String> fqcns = entry.getValue();

            if (fqcns.size() > 1) {
                continue;
            }

            String fqcn = fqcns.iterator().next();
            String fqcnPackage = fqcn.contains(".")
                ? fqcn.substring(0, fqcn.lastIndexOf('.'))
                : "";

            if (existingImports.containsKey(simpleName)) {
                if (existingImports.get(simpleName).equals(fqcn)) {
                    simplifiableNames.add(simpleName);
                }
                continue;
            }

            if (fqcnPackage.equals("java.lang")) {
                simplifiableNames.add(simpleName);
            } else if (!currentPackage.isEmpty()
                && fqcnPackage.equals(currentPackage)) {
                simplifiableNames.add(simpleName);
            } else if (wildcardPackages.contains(fqcnPackage)) {
                simplifiableNames.add(simpleName);
            } else {
                newImports.add(fqcn);
                simplifiableNames.add(simpleName);
            }
        }

        // Phase 6: Apply replacements
        List<Replacement> replacements = new ArrayList<>();
        for (Occurrence occ : occurrences) {
            if (!simplifiableNames.contains(occ.simpleName)) {
                continue;
            }
            Set<String> fqcns = candidates.get(occ.simpleName);
            if (fqcns != null && fqcns.size() == 1) {
                replacements.add(
                    new Replacement(
                        occ.startLine,
                        occ.startCol,
                        occ.endLine,
                        occ.endCol,
                        occ.simplifiedText));
            }
        }

        if (replacements.isEmpty() && newImports.isEmpty()) {
            return source;
        }

        // Sort bottom-to-top, right-to-left so offsets stay valid
        replacements.sort(
            Comparator
                .comparingInt((Replacement r) -> r.startLine)
                .thenComparingInt(r -> r.startCol)
                .reversed());

        String[] lines = working.split("\n", -1);
        for (Replacement r : replacements) {
            lines = applyReplacement(lines, r);
        }

        String result = String.join("\n", lines);

        // Phase 7: Insert new imports
        if (!newImports.isEmpty()) {
            result = insertImports(result, newImports);
        }

        // Phase 8: Restore protected blocks
        return ps.restore(result);
    }

    private static void collectTypeReference(
        ClassOrInterfaceType type,
        Map<String, Set<String>> candidates,
        List<Occurrence> occurrences) {
        // Skip if this type is the scope of a parent ClassOrInterfaceType
        if (type.getParentNode().isPresent()
            && type.getParentNode().get() instanceof ClassOrInterfaceType parent
            && parent.getScope().isPresent()
            && parent.getScope().get() == type) {
            return;
        }

        // Walk the scope chain to reconstruct the full name
        List<String> parts = new ArrayList<>();
        ClassOrInterfaceType current = type;
        parts.add(0, current.getNameAsString());
        while (current.getScope().isPresent()) {
            current = current.getScope().get();
            parts.add(0, current.getNameAsString());
        }

        int classIndex = firstUppercaseIndex(parts);
        if (classIndex <= 0) {
            return;
        }

        String fqcn = String.join(".", parts.subList(0, classIndex + 1));
        String simpleName = parts.get(classIndex);
        String simplifiedText = String.join(
            ".",
            parts.subList(classIndex, parts.size()));

        // Use the name's end position (not the type's, which includes
        // type arguments like <String>)
        if (type.getBegin().isEmpty()
            || type.getName().getEnd().isEmpty()) {
            return;
        }

        int startLine = type.getBegin().get().line - 1;
        int startCol = type.getBegin().get().column - 1;
        int endLine = type.getName().getEnd().get().line - 1;
        int endCol = type.getName().getEnd().get().column;

        candidates.computeIfAbsent(simpleName, k -> new LinkedHashSet<>())
            .add(fqcn);
        occurrences.add(
            new Occurrence(
                startLine,
                startCol,
                endLine,
                endCol,
                simpleName,
                simplifiedText));
    }

    private static void collectAnnotationReference(
        Name name,
        Map<String, Set<String>> candidates,
        List<Occurrence> occurrences) {
        if (name.getQualifier().isEmpty()) {
            return;
        }

        List<String> parts = new ArrayList<>();
        Name current = name;
        parts.add(0, current.getIdentifier());
        while (current.getQualifier().isPresent()) {
            current = current.getQualifier().get();
            parts.add(0, current.getIdentifier());
        }

        int classIndex = firstUppercaseIndex(parts);
        if (classIndex <= 0) {
            return;
        }

        String fqcn = String.join(".", parts.subList(0, classIndex + 1));
        String simpleName = parts.get(classIndex);
        String simplifiedText = String.join(
            ".",
            parts.subList(classIndex, parts.size()));

        if (name.getBegin().isEmpty() || name.getEnd().isEmpty()) {
            return;
        }

        int startLine = name.getBegin().get().line - 1;
        int startCol = name.getBegin().get().column - 1;
        int endLine = name.getEnd().get().line - 1;
        int endCol = name.getEnd().get().column;

        candidates.computeIfAbsent(simpleName, k -> new LinkedHashSet<>())
            .add(fqcn);
        occurrences.add(
            new Occurrence(
                startLine,
                startCol,
                endLine,
                endCol,
                simpleName,
                simplifiedText));
    }

    private static void collectStaticMethodAccess(
        MethodCallExpr methodCall,
        Map<String, Set<String>> candidates,
        List<Occurrence> occurrences) {
        if (methodCall.getScope().isEmpty()) {
            return;
        }

        List<String> scopeParts = new ArrayList<>();
        var scope = methodCall.getScope().get();
        if (!buildAccessChain(scope, scopeParts)) {
            return;
        }

        int classIndex = findClassBoundary(scopeParts);
        if (classIndex < 0) {
            return;
        }

        String fqcn = String.join(
            ".",
            scopeParts.subList(0, classIndex + 1));
        String simpleName = scopeParts.get(classIndex);
        String simplifiedScope = String.join(
            ".",
            scopeParts.subList(classIndex, scopeParts.size()));

        if (scope.getBegin().isEmpty() || scope.getEnd().isEmpty()) {
            return;
        }

        int startLine = scope.getBegin().get().line - 1;
        int startCol = scope.getBegin().get().column - 1;
        int endLine = scope.getEnd().get().line - 1;
        int endCol = scope.getEnd().get().column;

        candidates.computeIfAbsent(simpleName, k -> new LinkedHashSet<>())
            .add(fqcn);
        occurrences.add(
            new Occurrence(
                startLine,
                startCol,
                endLine,
                endCol,
                simpleName,
                simplifiedScope));
    }

    private static void collectStaticFieldAccess(
        FieldAccessExpr fieldAccess,
        Map<String, Set<String>> candidates,
        List<Occurrence> occurrences) {
        // Skip if parent is a FieldAccessExpr or MethodCallExpr scope
        if (fieldAccess.getParentNode().isPresent()) {
            var parent = fieldAccess.getParentNode().get();
            if (parent instanceof FieldAccessExpr) {
                return;
            }
            if (parent instanceof MethodCallExpr mc
                && mc.getScope().isPresent()
                && mc.getScope().get() == fieldAccess) {
                return;
            }
        }

        List<String> parts = new ArrayList<>();
        parts.add(fieldAccess.getNameAsString());
        if (!buildAccessChain(fieldAccess.getScope(), parts)) {
            return;
        }

        int classIndex = findClassBoundary(parts);
        if (classIndex < 0) {
            return;
        }

        List<String> scopeParts = parts.subList(0, parts.size() - 1);
        if (scopeParts.isEmpty()) {
            return;
        }

        int scopeClassIndex = findClassBoundary(scopeParts);
        if (scopeClassIndex < 0) {
            return;
        }

        String fqcn = String.join(
            ".",
            scopeParts.subList(0, scopeClassIndex + 1));
        String simpleName = scopeParts.get(scopeClassIndex);
        String simplifiedScope = String.join(
            ".",
            scopeParts.subList(scopeClassIndex, scopeParts.size()));

        var scopeExpr = fieldAccess.getScope();
        if (scopeExpr.getBegin().isEmpty()
            || scopeExpr.getEnd().isEmpty()) {
            return;
        }

        int startLine = scopeExpr.getBegin().get().line - 1;
        int startCol = scopeExpr.getBegin().get().column - 1;
        int endLine = scopeExpr.getEnd().get().line - 1;
        int endCol = scopeExpr.getEnd().get().column;

        candidates.computeIfAbsent(simpleName, k -> new LinkedHashSet<>())
            .add(fqcn);
        occurrences.add(
            new Occurrence(
                startLine,
                startCol,
                endLine,
                endCol,
                simpleName,
                simplifiedScope));
    }

    private static boolean buildAccessChain(
        com.github.javaparser.ast.expr.Expression expr,
        List<String> parts) {
        if (expr instanceof NameExpr nameExpr) {
            parts.add(0, nameExpr.getNameAsString());
            return true;
        } else if (expr instanceof FieldAccessExpr fa) {
            parts.add(0, fa.getNameAsString());
            return buildAccessChain(fa.getScope(), parts);
        }
        return false;
    }

    private static int firstUppercaseIndex(List<String> parts) {
        for (int i = 0; i < parts.size(); i++) {
            if (Character.isUpperCase(parts.get(i).charAt(0))) {
                return i;
            }
        }
        return -1;
    }

    private static int findClassBoundary(List<String> parts) {
        int lowercaseCount = 0;
        for (int i = 0; i < parts.size(); i++) {
            if (Character.isUpperCase(parts.get(i).charAt(0))) {
                if (lowercaseCount >= 2) {
                    return i;
                }
                return -1;
            }
            lowercaseCount++;
        }
        return -1;
    }

    private static String[] applyReplacement(
        String[] lines,
        Replacement r) {
        if (r.startLine == r.endLine) {
            String line = lines[r.startLine];
            lines[r.startLine] = line.substring(0, r.startCol)
                + r.replacement
                + line.substring(r.endCol);
        } else {
            String firstLine = lines[r.startLine];
            String lastLine = lines[r.endLine];
            lines[r.startLine] = firstLine.substring(0, r.startCol)
                + r.replacement
                + lastLine.substring(r.endCol);
            List<String> result = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                if (i > r.startLine && i <= r.endLine) {
                    continue;
                }
                result.add(lines[i]);
            }
            return result.toArray(new String[0]);
        }
        return lines;
    }

    private static String insertImports(
        String source,
        Set<String> newImports) {
        String[] lines = source.split("\n", -1);
        int lastImportLine = -1;
        boolean hasImports = false;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().startsWith("import ")) {
                lastImportLine = i;
                hasImports = true;
            }
        }

        List<String> sortedImports = new ArrayList<>(newImports);
        sortedImports.sort(String::compareTo);
        List<String> importLines = new ArrayList<>();
        for (String fqcn : sortedImports) {
            importLines.add("import " + fqcn + ";");
        }

        StringBuilder result = new StringBuilder();

        if (hasImports) {
            // Insert after the last existing import
            for (int i = 0; i <= lastImportLine; i++) {
                result.append(lines[i]).append("\n");
            }
            for (String imp : importLines) {
                result.append(imp).append("\n");
            }
            for (int i = lastImportLine + 1; i < lines.length; i++) {
                result.append(lines[i]);
                if (i < lines.length - 1) {
                    result.append("\n");
                }
            }
        } else {
            // No existing imports — insert after package statement
            int packageLine = -1;
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().startsWith("package ")) {
                    packageLine = i;
                    break;
                }
            }

            if (packageLine >= 0) {
                for (int i = 0; i <= packageLine; i++) {
                    result.append(lines[i]).append("\n");
                }
                result.append("\n");
                for (String imp : importLines) {
                    result.append(imp).append("\n");
                }
                for (int i = packageLine + 1; i < lines.length; i++) {
                    result.append(lines[i]);
                    if (i < lines.length - 1) {
                        result.append("\n");
                    }
                }
            } else {
                // No package statement — insert at top
                for (String imp : importLines) {
                    result.append(imp).append("\n");
                }
                for (int i = 0; i < lines.length; i++) {
                    result.append(lines[i]);
                    if (i < lines.length - 1) {
                        result.append("\n");
                    }
                }
            }
        }

        return result.toString();
    }

    private record Occurrence(
        int startLine,
        int startCol,
        int endLine,
        int endCol,
        String simpleName,
        String simplifiedText) {
    }

    private record Replacement(
        int startLine,
        int startCol,
        int endLine,
        int endCol,
        String replacement) {
    }
}
