package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — On/Off Tags section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "On/Off Tags"
 */
class OnOffTagsTest extends FormatterTestBase {
    OnOffTagsTest() {
        super("on-off-tags/", s -> formatJava(s));
    }

    // use_on_off_tags, enabling_tag, disabling_tag
    @Test
    void formatterOffOnTagsPreserveUnformattedCode() throws IOException {
        test("formatter-off-on-tags-unchanged.java");
    }
}
