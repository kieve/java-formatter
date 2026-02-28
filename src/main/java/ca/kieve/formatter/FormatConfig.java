package ca.kieve.formatter;

import java.io.Serializable;
import java.util.List;

/**
 * Configuration for the custom formatter.
 * All formatting rules reference this for their settings.
 */
public final class FormatConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final List<ImportGroup> DEFAULT_IMPORT_LAYOUT = List.of(
            ImportGroup.catchAll(),
            ImportGroup.of("ca.kieve."),
            ImportGroup.of("javax.", "java."),
            ImportGroup.staticCatchAll()
    );

    private final int maxLineLength;
    private final List<ImportGroup> importLayout;
    private final boolean banWildcardImports;

    public FormatConfig(int maxLineLength) {
        this(maxLineLength, DEFAULT_IMPORT_LAYOUT, true);
    }

    public FormatConfig(
            int maxLineLength,
            List<ImportGroup> importLayout,
            boolean banWildcardImports) {
        this.maxLineLength = maxLineLength;
        this.importLayout = List.copyOf(importLayout);
        this.banWildcardImports = banWildcardImports;
    }

    public static FormatConfig defaults() {
        return new FormatConfig(100);
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }

    public List<ImportGroup> getImportLayout() {
        return importLayout;
    }

    public boolean isBanWildcardImports() {
        return banWildcardImports;
    }
}
