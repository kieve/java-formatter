package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Miscellaneous section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Miscellaneous"
 */
class WhitespaceMiscellaneousTest extends FormatterTestBase {
    WhitespaceMiscellaneousTest() {
        super("whitespace-miscellaneous/", s -> formatJava(s));
    }

    // insert_space_after_at_in_annotation
    // insert_space_after_at_in_annotation_type_declaration
    // insert_space_before_ellipsis
    // insert_space_after_ellipsis
    // insert_space_before_and_in_type_parameter
    // insert_space_after_and_in_type_parameter
    // insert_space_before_question_in_conditional
    // insert_space_after_question_in_conditional
    // insert_space_before_question_in_wildcard
    // insert_space_after_question_in_wildcard
    // insert_space_before_lambda_arrow
    // insert_space_after_lambda_arrow
    @Test
    void miscellaneousSpacing() throws IOException {
        test("miscellaneous-spacing-input.java", "miscellaneous-spacing-expected.java");
    }
}
