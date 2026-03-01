package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Operator Wrapping section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Operator Wrapping"
 */
class OperatorWrappingTest extends FormatterTestBase {
    OperatorWrappingTest() {
        super("operator-wrapping/", s -> formatJava(s));
    }

    // alignment_for_additive_operator (excluded — custom formatter)
    @Test
    void alignmentForAdditiveOperatorWrapsBeforeOperator() throws IOException {
        test("additive-operator-input.java", "additive-operator-expected.java");
    }

    // alignment_for_multiplicative_operator (excluded — custom formatter)
    @Test
    void alignmentForMultiplicativeOperatorWrapsBeforeOperator() throws IOException {
        test("multiplicative-operator-input.java", "multiplicative-operator-expected.java");
    }

    // alignment_for_string_concatenation (excluded — custom formatter)
    @Test
    void alignmentForStringConcatenationWrapsBeforeOperator() throws IOException {
        test("string-concatenation-input.java", "string-concatenation-expected.java");
    }

    // alignment_for_bitwise_operator (excluded — custom formatter)
    @Test
    void alignmentForBitwiseOperatorWrapsBeforeOperator() throws IOException {
        test("bitwise-operator-input.java", "bitwise-operator-expected.java");
    }

    // alignment_for_logical_operator (excluded — custom formatter)
    @Test
    void alignmentForLogicalOperatorWrapsBeforeOperator() throws IOException {
        test("logical-operator-input.java", "logical-operator-expected.java");
    }

    // alignment_for_relational_operator (excluded — custom formatter)
    @Test
    void alignmentForRelationalOperatorWrapsBeforeOperator() throws IOException {
        test("relational-operator-input.java", "relational-operator-expected.java");
    }

    // alignment_for_shift_operator (excluded — custom formatter)
    @Test
    void alignmentForShiftOperatorWrapsBeforeOperator() throws IOException {
        test("shift-operator-input.java", "shift-operator-expected.java");
    }

    // wrap_before_binary_operator
    @Test
    void wrapBeforeBinaryOperatorPlacesOperatorOnContinuationLine() throws IOException {
        test("binary-operator-input.java", "binary-operator-expected.java");
    }

    // wrap_before_or_operator_multicatch
    @Test
    void wrapBeforeOrOperatorMulticatchPlacesPipeOnContinuationLine() throws IOException {
        test("multicatch-input.java", "multicatch-expected.java");
    }
}
