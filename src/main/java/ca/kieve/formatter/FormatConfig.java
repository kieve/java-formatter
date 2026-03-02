package ca.kieve.formatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for the custom formatter.
 * All formatting rules reference this for their settings.
 */
public final class FormatConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private static volatile String defaultProjectGroup;

    private final int maxLineLength;
    private final List<ImportGroup> importLayout;

    public FormatConfig(
        int maxLineLength,
        List<ImportGroup> importLayout
    ) {
        this.maxLineLength = maxLineLength;
        this.importLayout = List.copyOf(importLayout);
    }

    /**
     * Set the project group used for the default import layout
     * (e.g., {@code "ca.kieve"}). Called by the plugin at apply time.
     */
    public static void setDefaultProjectGroup(String group) {
        defaultProjectGroup = group;
    }

    public static FormatConfig defaults() {
        return new FormatConfig(100, defaultImportLayout());
    }

    private static List<ImportGroup> defaultImportLayout() {
        String group = defaultProjectGroup;
        List<ImportGroup> layout = new ArrayList<>();
        layout.add(ImportGroup.catchAll());
        if (group != null && !group.isEmpty()) {
            String prefix = group.endsWith(".")
                ? group
                : group + ".";
            layout.add(ImportGroup.of(prefix));
        }
        layout.add(ImportGroup.of("javax.", "java."));
        layout.add(ImportGroup.staticCatchAll());
        return layout;
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }

    public List<ImportGroup> getImportLayout() {
        return importLayout;
    }
}
