package ca.kieve.formatter;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatConfigLoaderTest {

    private static FormatConfig loadYaml(String yaml) {
        InputStream in = new ByteArrayInputStream(
                yaml.getBytes(StandardCharsets.UTF_8));
        return FormatConfigLoader.load(in);
    }

    @Test
    void loadsAllFields() {
        String yaml = """
                maxLineLength: 120
                banWildcardImports: false
                importLayout:
                  - catch-all
                  - ["org.example."]
                  - static-catch-all
                """;
        FormatConfig config = loadYaml(yaml);
        assertEquals(120, config.getMaxLineLength());
        assertFalse(config.isBanWildcardImports());
        assertEquals(3, config.getImportLayout().size());

        ImportGroup first = config.getImportLayout().get(0);
        assertTrue(first.isCatchAll());
        assertFalse(first.isStatic());

        ImportGroup second = config.getImportLayout().get(1);
        assertEquals(List.of("org.example."), second.prefixes());
        assertFalse(second.isStatic());

        ImportGroup third = config.getImportLayout().get(2);
        assertTrue(third.isCatchAll());
        assertTrue(third.isStatic());
    }

    @Test
    void missingFieldsUseDefaults() {
        FormatConfig config = loadYaml("");
        FormatConfig defaults = FormatConfig.defaults();
        assertEquals(defaults.getMaxLineLength(), config.getMaxLineLength());
        assertEquals(defaults.isBanWildcardImports(),
                config.isBanWildcardImports());
        assertEquals(defaults.getImportLayout(), config.getImportLayout());
    }

    @Test
    void noFileReturnsDefaults() {
        FormatConfig config = FormatConfigLoader.load((File) null);
        assertEquals(FormatConfig.defaults().getMaxLineLength(),
                config.getMaxLineLength());

        File nonExistent = new File("/does/not/exist/kieve-formatter.yaml");
        config = FormatConfigLoader.load(nonExistent);
        assertEquals(FormatConfig.defaults().getMaxLineLength(),
                config.getMaxLineLength());
    }

    @Test
    void parsesStaticGroupWithPrefixes() {
        String yaml = """
                importLayout:
                  - catch-all
                  - static:
                    - "org.mockito."
                    - "org.junit."
                  - static-catch-all
                """;
        FormatConfig config = loadYaml(yaml);
        assertEquals(3, config.getImportLayout().size());

        ImportGroup staticGroup = config.getImportLayout().get(1);
        assertTrue(staticGroup.isStatic());
        assertEquals(List.of("org.mockito.", "org.junit."),
                staticGroup.prefixes());
    }

    @Test
    void parsesCatchAllStrings() {
        String yaml = """
                importLayout:
                  - catch-all
                  - static-catch-all
                """;
        FormatConfig config = loadYaml(yaml);
        assertEquals(2, config.getImportLayout().size());

        assertTrue(config.getImportLayout().get(0).isCatchAll());
        assertFalse(config.getImportLayout().get(0).isStatic());

        assertTrue(config.getImportLayout().get(1).isCatchAll());
        assertTrue(config.getImportLayout().get(1).isStatic());
    }

    @Test
    void parsesListOfPrefixes() {
        String yaml = """
                importLayout:
                  - ["javax.", "java."]
                """;
        FormatConfig config = loadYaml(yaml);
        assertEquals(1, config.getImportLayout().size());

        ImportGroup group = config.getImportLayout().get(0);
        assertFalse(group.isStatic());
        assertEquals(List.of("javax.", "java."), group.prefixes());
    }

    @Test
    void invalidLayoutEntryThrows() {
        String yaml = """
                importLayout:
                  - 42
                """;
        assertThrows(IllegalArgumentException.class, () -> loadYaml(yaml));
    }

    @Test
    void unknownKeyThrows() {
        String yaml = """
                maxLineLength: 100
                unknownOption: true
                """;
        assertThrows(IllegalArgumentException.class, () -> loadYaml(yaml));
    }

    @Test
    void invalidStringEntryThrows() {
        String yaml = """
                importLayout:
                  - "not-a-valid-entry"
                """;
        assertThrows(IllegalArgumentException.class, () -> loadYaml(yaml));
    }

    @Test
    void badTypeForMaxLineLengthThrows() {
        String yaml = """
                maxLineLength: "not a number"
                """;
        assertThrows(IllegalArgumentException.class, () -> loadYaml(yaml));
    }

    @Test
    void badTypeForBanWildcardImportsThrows() {
        String yaml = """
                banWildcardImports: "not a boolean"
                """;
        assertThrows(IllegalArgumentException.class, () -> loadYaml(yaml));
    }

    @Test
    void partialConfigMergesWithDefaults() {
        String yaml = """
                maxLineLength: 80
                """;
        FormatConfig config = loadYaml(yaml);
        assertEquals(80, config.getMaxLineLength());
        // Other fields should use defaults
        FormatConfig defaults = FormatConfig.defaults();
        assertEquals(defaults.isBanWildcardImports(),
                config.isBanWildcardImports());
        assertEquals(defaults.getImportLayout(), config.getImportLayout());
    }
}
