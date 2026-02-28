package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — On/Off Tags section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "On/Off Tags"
 */
class OnOffTagsTest {
    // use_on_off_tags, enabling_tag, disabling_tag
    @Test
    void formatterOffOnTagsPreserveUnformattedCode() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        // @formatter:off
                        int    x   =   1;
                        int    y   =   2;
                        // @formatter:on
                        int z = 3;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        // @formatter:off
                        int    x   =   1;
                        int    y   =   2;
                        // @formatter:on
                        int z = 3;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
