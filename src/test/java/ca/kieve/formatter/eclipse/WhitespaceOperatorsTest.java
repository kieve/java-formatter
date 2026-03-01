package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Operators section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Operators"
 */
class WhitespaceOperatorsTest extends FormatterTestBase {
    WhitespaceOperatorsTest() {
        super("whitespace-operators/", s -> formatJava(s));
    }

    // insert_space_before_assignment_operator
    // insert_space_after_assignment_operator
    // insert_space_before_additive_operator
    // insert_space_after_additive_operator
    // insert_space_before_multiplicative_operator
    // insert_space_after_multiplicative_operator
    // insert_space_before_bitwise_operator
    // insert_space_after_bitwise_operator
    // insert_space_before_logical_operator
    // insert_space_after_logical_operator
    // insert_space_before_relational_operator
    // insert_space_after_relational_operator
    // insert_space_before_shift_operator
    // insert_space_after_shift_operator
    // insert_space_before_string_concatenation
    // insert_space_after_string_concatenation
    // insert_space_before_unary_operator
    // insert_space_after_unary_operator
    // insert_space_before_prefix_operator
    // insert_space_after_prefix_operator
    // insert_space_before_postfix_operator
    // insert_space_after_postfix_operator
    // insert_space_after_not_operator
    @Test
    void operatorSpacing() throws IOException {
        test("operator-spacing-input.java", "operator-spacing-expected.java");
    }
}
