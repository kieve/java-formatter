package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Colons & Semicolons section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Colons & Semicolons"
 */
class WhitespaceColonsSemicolonsTest extends FormatterTestBase {
    WhitespaceColonsSemicolonsTest() {
        super("whitespace-colons-semicolons/", s -> formatJava(s));
    }

    // insert_space_before_colon_in_for
    // insert_space_after_colon_in_for
    // insert_space_before_semicolon
    // insert_space_before_colon_in_assert
    // insert_space_after_colon_in_assert
    // insert_space_before_colon_in_case
    // insert_space_after_colon_in_case
    // insert_space_before_colon_in_default
    // insert_space_after_colon_in_default
    // insert_space_before_colon_in_conditional
    // insert_space_after_colon_in_conditional
    // insert_space_before_colon_in_labeled_statement
    // insert_space_after_colon_in_labeled_statement
    // insert_space_before_semicolon_in_for
    // insert_space_before_semicolon_in_try_with_resources
    @Test
    void colonAndSemicolonSpacing() throws IOException {
        test("colon-semicolon-spacing-input.java", "colon-semicolon-spacing-expected.java");
    }
}
