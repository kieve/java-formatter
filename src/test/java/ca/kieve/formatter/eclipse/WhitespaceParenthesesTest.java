package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Parentheses sections.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Parentheses (Opening)"
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Parentheses (After Opening / Before
 *      Closing)"
 */
class WhitespaceParenthesesTest extends FormatterTestBase {
    WhitespaceParenthesesTest() {
        super("whitespace-parentheses/", s -> formatJava(s));
    }

    // insert_space_before_opening_paren_in_method_declaration
    // insert_space_before_opening_paren_in_method_invocation
    // insert_space_before_opening_paren_in_if
    // insert_space_before_opening_paren_in_for
    // insert_space_before_opening_paren_in_while
    // insert_space_before_opening_paren_in_switch
    // insert_space_before_opening_paren_in_catch
    // insert_space_before_opening_paren_in_constructor_declaration
    // insert_space_before_opening_paren_in_annotation
    // insert_space_before_opening_paren_in_enum_constant
    // insert_space_before_opening_paren_in_record_declaration
    // insert_space_before_opening_paren_in_synchronized
    // insert_space_after_opening_paren_in_method_declaration
    // insert_space_after_opening_paren_in_method_invocation
    // insert_space_after_opening_paren_in_constructor_declaration
    // insert_space_after_opening_paren_in_if
    // insert_space_after_opening_paren_in_for
    // insert_space_after_opening_paren_in_while
    // insert_space_after_opening_paren_in_switch
    // insert_space_after_opening_paren_in_catch
    // insert_space_after_opening_paren_in_synchronized
    // insert_space_after_opening_paren_in_cast
    // insert_space_after_opening_paren_in_annotation
    // insert_space_after_opening_paren_in_enum_constant
    // insert_space_after_opening_paren_in_record_declaration
    // insert_space_before_closing_paren_in_method_declaration
    // insert_space_before_closing_paren_in_method_invocation
    // insert_space_before_closing_paren_in_constructor_declaration
    // insert_space_before_closing_paren_in_if
    // insert_space_before_closing_paren_in_for
    // insert_space_before_closing_paren_in_while
    // insert_space_before_closing_paren_in_switch
    // insert_space_before_closing_paren_in_catch
    // insert_space_before_closing_paren_in_synchronized
    // insert_space_before_closing_paren_in_cast
    // insert_space_before_closing_paren_in_annotation
    // insert_space_before_closing_paren_in_enum_constant
    // insert_space_before_closing_paren_in_record_declaration
    // insert_space_after_closing_paren_in_cast
    @Test
    void parenthesisSpacing() throws IOException {
        test("parenthesis-spacing-input.java", "parenthesis-spacing-expected.java");
    }
}
