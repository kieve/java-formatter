package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Braces section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Braces"
 */
class WhitespaceBracesTest extends FormatterTestBase {
    WhitespaceBracesTest() {
        super("whitespace-braces/", s -> formatJava(s));
    }

    // insert_space_before_opening_brace_in_type_declaration
    // insert_space_before_opening_brace_in_method_declaration
    // insert_space_before_opening_brace_in_block
    // insert_space_before_opening_brace_in_array_initializer
    // insert_space_after_opening_brace_in_array_initializer
    // insert_space_before_closing_brace_in_array_initializer
    // insert_space_after_closing_brace_in_block
    @Test
    void braceSpacing() throws IOException {
        test("brace-spacing-input.java", "brace-spacing-expected.java");
    }
}
