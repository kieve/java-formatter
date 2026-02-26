package ca.kieve.formatter;

import java.io.Serializable;

/**
 * Configuration for the custom formatter.
 * All formatting rules reference this for their settings.
 */
public final class FormatConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int maxLineLength;

    public FormatConfig(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public static FormatConfig defaults() {
        return new FormatConfig(100);
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }
}
