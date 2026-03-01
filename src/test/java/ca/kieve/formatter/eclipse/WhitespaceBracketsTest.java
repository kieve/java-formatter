package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Brackets (Array Access) section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Brackets (Array Access)"
 */
class WhitespaceBracketsTest extends FormatterTestBase {
    WhitespaceBracketsTest() {
        super("whitespace-brackets/", s -> formatJava(s));
    }

    // insert_space_before_opening_bracket_in_array_access
    // insert_space_after_opening_bracket_in_array_access
    // insert_space_before_closing_bracket_in_array_access
    @Test
    void bracketSpacing() throws IOException {
        test("bracket-spacing-input.java", "bracket-spacing-expected.java");
    }
}
