package ca.kieve.formatter;

import java.io.Serializable;
import java.util.List;

/**
 * Represents one group in the import layout configuration.
 * <p>
 * Each group matches imports by their package prefixes. A group with empty
 * prefixes acts as a catch-all for its category (static or non-static).
 * Multiple prefixes in one group means those imports are sub-ordered by
 * prefix list order with no blank line between them.
 *
 * @param prefixes  package prefixes to match (e.g., {@code "ca.kieve."},
 *                  {@code "javax."}). Empty list = catch-all.
 * @param isStatic  whether this group matches static or regular imports
 */
public record ImportGroup(List<String> prefixes, boolean isStatic) implements Serializable {
    public ImportGroup {
        prefixes = List.copyOf(prefixes);
    }

    /**
     * Create a non-static group matching the given prefixes.
     */
    public static ImportGroup of(String... prefixes) {
        return new ImportGroup(List.of(prefixes), false);
    }

    /**
     * Create a static group matching the given prefixes.
     */
    public static ImportGroup ofStatic(String... prefixes) {
        return new ImportGroup(List.of(prefixes), true);
    }

    /**
     * Create a non-static catch-all group (matches all non-static imports
     * not matched by any other group).
     */
    public static ImportGroup catchAll() {
        return new ImportGroup(List.of(), false);
    }

    /**
     * Create a static catch-all group (matches all static imports not
     * matched by any other group).
     */
    public static ImportGroup staticCatchAll() {
        return new ImportGroup(List.of(), true);
    }

    /**
     * Whether this group is a catch-all (no specific prefixes).
     */
    public boolean isCatchAll() {
        return prefixes.isEmpty();
    }
}
