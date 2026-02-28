package ca.kieve.formatter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads a {@link FormatConfig} from a {@code kieve-formatter.yaml} file.
 * <p>
 * All fields are optional — missing fields use defaults from
 * {@link FormatConfig#defaults()}.
 */
public final class FormatConfigLoader {
    private static final String CONFIG_FILE_NAME = "kieve-formatter.yaml";
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory())
        .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private FormatConfigLoader() {
    }

    private record RawConfig(
        Integer maxLineLength,
        List<Object> importLayout) {
    }

    /**
     * Load config from the given YAML file, or return defaults if null.
     */
    public static FormatConfig load(File yamlFile) {
        if (yamlFile == null || !yamlFile.exists()) {
            return FormatConfig.defaults();
        }
        try (InputStream in = new FileInputStream(yamlFile)) {
            return load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Failed to read config file: " + yamlFile,
                e);
        }
    }

    /**
     * Load config from an input stream containing YAML.
     */
    public static FormatConfig load(InputStream in) {
        try {
            JsonNode tree = MAPPER.readTree(in);
            if (tree == null || tree.isNull() || tree.isMissingNode()) {
                return FormatConfig.defaults();
            }
            RawConfig raw = MAPPER.treeToValue(tree, RawConfig.class);
            return toFormatConfig(raw);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Failed to parse config YAML: " + e.getMessage(),
                e);
        }
    }

    /**
     * Look for {@value CONFIG_FILE_NAME} in the given directory
     * and load it if present.
     */
    public static FormatConfig loadFromDirectory(File projectDir) {
        File configFile = new File(projectDir, CONFIG_FILE_NAME);
        return load(configFile);
    }

    private static FormatConfig toFormatConfig(RawConfig raw) {
        FormatConfig defaults = FormatConfig.defaults();

        int maxLineLength = raw.maxLineLength() != null
            ? raw.maxLineLength()
            : defaults.getMaxLineLength();

        List<ImportGroup> importLayout = raw.importLayout() != null
            ? parseImportLayout(raw.importLayout())
            : defaults.getImportLayout();

        return new FormatConfig(maxLineLength, importLayout);
    }

    @SuppressWarnings("unchecked")
    private static List<ImportGroup> parseImportLayout(List<?> entries) {
        List<ImportGroup> groups = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            Object entry = entries.get(i);
            if (entry instanceof String s) {
                groups.add(parseStringEntry(s, i));
            } else if (entry instanceof List<?> list) {
                groups.add(parsePrefixList(list, false, i));
            } else if (entry instanceof Map<?, ?> map) {
                groups.add(parseMapEntry((Map<String, Object>) map, i));
            } else {
                throw new IllegalArgumentException(
                    "importLayout[" + i + "]: expected a string, list, "
                        + "or map, got: "
                        + entry.getClass().getSimpleName());
            }
        }
        return groups;
    }

    private static ImportGroup parseStringEntry(String value, int index) {
        return switch (value) {
        case "catch-all" -> ImportGroup.catchAll();
        case "static-catch-all" -> ImportGroup.staticCatchAll();
        default ->
            throw new IllegalArgumentException(
                "importLayout[" + index + "]: unknown string entry '"
                    + value + "'. Expected 'catch-all' or "
                    + "'static-catch-all'.");
        };
    }

    private static ImportGroup parsePrefixList(
        List<?> list,
        boolean isStatic,
        int index) {
        List<String> prefixes = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof String s) {
                prefixes.add(s);
            } else {
                throw new IllegalArgumentException(
                    "importLayout[" + index
                        + "]: prefix list entries must be strings, "
                        + "got: "
                        + item.getClass().getSimpleName());
            }
        }
        if (prefixes.isEmpty()) {
            throw new IllegalArgumentException(
                "importLayout[" + index
                    + "]: prefix list must not be empty");
        }
        return new ImportGroup(prefixes, isStatic);
    }

    private static ImportGroup parseMapEntry(
        Map<String, Object> map,
        int index) {
        if (map.size() != 1 || !map.containsKey("static")) {
            throw new IllegalArgumentException(
                "importLayout[" + index
                    + "]: map entry must have exactly one key "
                    + "'static', got keys: " + map.keySet());
        }
        Object val = map.get("static");
        if (val instanceof List<?> list) {
            return parsePrefixList(list, true, index);
        }
        throw new IllegalArgumentException(
            "importLayout[" + index
                + "]: 'static' value must be a list of prefixes, "
                + "got: " + val.getClass().getSimpleName());
    }
}
