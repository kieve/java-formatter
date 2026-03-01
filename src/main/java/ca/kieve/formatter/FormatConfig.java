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

    public FormatConfig(int maxLineLength) {
        this(maxLineLength, DEFAULT_IMPORT_LAYOUT);
    }

    public FormatConfig(
        int maxLineLength,
        List<ImportGroup> importLayout
    ) {
        this.maxLineLength = maxLineLength;
        this.importLayout = List.copyOf(importLayout);
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
}
