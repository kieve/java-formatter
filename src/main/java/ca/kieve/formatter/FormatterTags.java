package ca.kieve.formatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Protects {@code // @formatter:off} / {@code // @formatter:on} blocks from
 * custom formatting rules by replacing them with placeholder comments before
 * rules run, then restoring the original content afterward.
 */
public final class FormatterTags {
    public record ProtectedSource(String source, List<String> protectedBlocks) {
        public String restore(String formatted) {
            String result = formatted;
            for (int i = 0; i < protectedBlocks.size(); i++) {
                result = result.replace(
                    PLACEHOLDER_PREFIX + i + PLACEHOLDER_SUFFIX,
                    protectedBlocks.get(i));
            }
            return result;
        }
    }

    private static final String OFF_TAG = "// @formatter:off";
    private static final String ON_TAG = "// @formatter:on";
    private static final String PLACEHOLDER_PREFIX = "// __PROTECTED_";
    private static final String PLACEHOLDER_SUFFIX = "__";

    private FormatterTags() {
    }

    public static ProtectedSource protect(String source) {
        List<String> protectedBlocks = new ArrayList<>();
        String[] lines = source.split("\n", -1);
        StringBuilder output = new StringBuilder();
        StringBuilder block = null;
        boolean inside = false;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();

            if (!inside && trimmed.equals(OFF_TAG)) {
                inside = true;
                block = new StringBuilder();
                block.append(line);
            } else if (inside && trimmed.equals(ON_TAG)) {
                block.append("\n").append(line);
                int index = protectedBlocks.size();
                protectedBlocks.add(block.toString());
                output.append(PLACEHOLDER_PREFIX).append(index)
                    .append(PLACEHOLDER_SUFFIX);
                block = null;
                inside = false;
            } else if (inside) {
                block.append("\n").append(line);
            } else {
                output.append(line);
            }

            if (!inside && i < lines.length - 1) {
                output.append("\n");
            }
        }

        // Unclosed @formatter:off — protect to end of file
        if (inside && block != null) {
            int index = protectedBlocks.size();
            protectedBlocks.add(block.toString());
            output.append(PLACEHOLDER_PREFIX).append(index)
                    .append(PLACEHOLDER_SUFFIX);
        }

        return new ProtectedSource(output.toString(), protectedBlocks);
    }
}
