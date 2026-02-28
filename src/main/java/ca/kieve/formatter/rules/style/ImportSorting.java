package ca.kieve.formatter.rules.style;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.ImportGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sorts and groups import statements according to the configured import layout.
 * <p>
 * Imports are organized into groups separated by blank lines. Within each group,
 * imports are sorted alphabetically.
 */
public final class ImportSorting {
    private ImportSorting() {
    }

    public static String apply(String source, FormatConfig config) {
        String[] lines = source.split("\n", -1);

        int importStart = -1;
        int importEnd = -1;

        for (int i = 0; i < lines.length; i++) {
            String trimmed = lines[i].trim();
            if (trimmed.startsWith("import ")) {
                if (importStart == -1) {
                    importStart = i;
                }
                importEnd = i;
            }
        }

        if (importStart == -1) {
            return source;
        }

        // Collect import statements from the block (skip blank lines and comments)
        List<String> imports = new ArrayList<>();
        for (int i = importStart; i <= importEnd; i++) {
            String trimmed = lines[i].trim();
            if (trimmed.startsWith("import ")) {
                imports.add(trimmed);
            }
        }

        // Deduplicate while preserving order
        imports = new ArrayList<>(new LinkedHashSet<>(imports));

        // Separate static and non-static
        List<String> staticImports = new ArrayList<>();
        List<String> regularImports = new ArrayList<>();
        for (String imp : imports) {
            if (imp.startsWith("import static ")) {
                staticImports.add(imp);
            } else {
                regularImports.add(imp);
            }
        }

        // Assign imports to groups
        List<ImportGroup> layout = config.getImportLayout();
        Map<Integer, List<String>> groupedImports = new LinkedHashMap<>();
        for (int i = 0; i < layout.size(); i++) {
            groupedImports.put(i, new ArrayList<>());
        }

        assignToGroups(regularImports, false, layout, groupedImports);
        assignToGroups(staticImports, true, layout, groupedImports);

        // Sort within each group
        for (Map.Entry<Integer, List<String>> entry : groupedImports.entrySet()) {
            ImportGroup group = layout.get(entry.getKey());
            sortWithinGroup(entry.getValue(), group);
        }

        // Reconstruct import block
        StringBuilder importBlock = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < layout.size(); i++) {
            List<String> groupImports = groupedImports.get(i);
            if (groupImports.isEmpty()) {
                continue;
            }
            if (!first) {
                importBlock.append("\n");
            }
            for (String imp : groupImports) {
                importBlock.append(imp).append("\n");
            }
            first = false;
        }

        // Remove trailing newline from import block (we'll join with surrounding content)
        if (!importBlock.isEmpty()
            && importBlock.charAt(importBlock.length() - 1) == '\n') {
            importBlock.setLength(importBlock.length() - 1);
        }

        // Rebuild source: everything before imports + new imports + everything after
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < importStart; i++) {
            result.append(lines[i]).append("\n");
        }
        result.append(importBlock);
        for (int i = importEnd + 1; i < lines.length; i++) {
            result.append("\n").append(lines[i]);
        }

        return result.toString();
    }

    private static void assignToGroups(
        List<String> imports,
        boolean isStatic,
        List<ImportGroup> layout,
        Map<Integer, List<String>> groupedImports) {
        int catchAllIndex = -1;
        for (int i = 0; i < layout.size(); i++) {
            ImportGroup group = layout.get(i);
            if (group.isStatic() == isStatic && group.isCatchAll()) {
                catchAllIndex = i;
                break;
            }
        }

        for (String imp : imports) {
            String packagePart = extractPackage(imp);
            int matchedGroup = -1;

            for (int i = 0; i < layout.size(); i++) {
                ImportGroup group = layout.get(i);
                if (group.isStatic() != isStatic || group.isCatchAll()) {
                    continue;
                }
                for (String prefix : group.prefixes()) {
                    if (packagePart.startsWith(prefix)) {
                        matchedGroup = i;
                        break;
                    }
                }
                if (matchedGroup != -1) {
                    break;
                }
            }

            if (matchedGroup == -1) {
                matchedGroup = catchAllIndex;
            }

            if (matchedGroup != -1) {
                groupedImports.get(matchedGroup).add(imp);
            }
        }
    }

    private static String extractPackage(String importStatement) {
        // "import com.example.Foo;" -> "com.example.Foo"
        // "import static com.example.Foo.bar;" -> "com.example.Foo.bar"
        String s = importStatement;
        if (s.startsWith("import static ")) {
            s = s.substring("import static ".length());
        } else {
            s = s.substring("import ".length());
        }
        if (s.endsWith(";")) {
            s = s.substring(0, s.length() - 1);
        }
        return s.trim();
    }

    private static void sortWithinGroup(List<String> imports, ImportGroup group) {
        if (group.isCatchAll() || group.prefixes().size() <= 1) {
            imports.sort(ImportSorting::compareImports);
            return;
        }

        // Multi-prefix group: sub-sort by prefix order, then alphabetically
        List<String> prefixes = group.prefixes();
        imports.sort((a, b) -> {
            String pkgA = extractPackage(a);
            String pkgB = extractPackage(b);
            int prefixIndexA = findPrefixIndex(pkgA, prefixes);
            int prefixIndexB = findPrefixIndex(pkgB, prefixes);
            if (prefixIndexA != prefixIndexB) {
                return Integer.compare(prefixIndexA, prefixIndexB);
            }
            return compareImports(a, b);
        });
    }

    private static int findPrefixIndex(String pkg, List<String> prefixes) {
        for (int i = 0; i < prefixes.size(); i++) {
            if (pkg.startsWith(prefixes.get(i))) {
                return i;
            }
        }
        return prefixes.size();
    }

    private static int compareImports(String a, String b) {
        return extractPackage(a).compareTo(extractPackage(b));
    }
}
