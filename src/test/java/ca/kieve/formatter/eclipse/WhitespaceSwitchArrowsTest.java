package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Switch Arrows section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Switch Arrows"
 */
class WhitespaceSwitchArrowsTest extends FormatterTestBase {
    WhitespaceSwitchArrowsTest() {
        super("whitespace-switch-arrows/", s -> formatJava(s));
    }

    // insert_space_before_arrow_in_switch_case
    // insert_space_after_arrow_in_switch_case
    // insert_space_before_arrow_in_switch_default
    // insert_space_after_arrow_in_switch_default
    @Test
    void switchArrowSpacing() throws IOException {
        test("switch-arrow-spacing-input.java", "switch-arrow-spacing-expected.java");
    }
}
