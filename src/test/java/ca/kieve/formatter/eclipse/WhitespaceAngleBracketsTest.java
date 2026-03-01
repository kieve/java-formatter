package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Angle Brackets (Generics) section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Angle Brackets (Generics)"
 */
class WhitespaceAngleBracketsTest extends FormatterTestBase {
    WhitespaceAngleBracketsTest() {
        super("whitespace-angle-brackets/", s -> formatJava(s));
    }

    // insert_space_before_opening_angle_bracket_in_type_arguments
    // insert_space_after_opening_angle_bracket_in_type_arguments
    // insert_space_before_closing_angle_bracket_in_type_arguments
    // insert_space_after_closing_angle_bracket_in_type_arguments
    // insert_space_before_opening_angle_bracket_in_type_parameters
    // insert_space_after_opening_angle_bracket_in_type_parameters
    // insert_space_before_closing_angle_bracket_in_type_parameters
    // insert_space_after_closing_angle_bracket_in_type_parameters
    @Test
    void angleBracketSpacing() throws IOException {
        test("angle-bracket-spacing-input.java", "angle-bracket-spacing-expected.java");
    }
}
