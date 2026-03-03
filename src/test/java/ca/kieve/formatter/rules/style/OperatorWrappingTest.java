package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorWrappingTest extends FormatterRuleTestBase {
    OperatorWrappingTest() {
        super("operator-wrapping/", OperatorWrapping::apply);
    }

    @Test
    void logicalOperatorMoves() throws IOException {
        test("logical-operator-input.java", "logical-operator-expected.java");
    }

    @Test
    void stringConcatMoves() throws IOException {
        test("string-concat-input.java", "string-concat-expected.java");
    }

    @Test
    void alreadyCorrectUnchanged() throws IOException {
        test("already-correct-unchanged.java");
    }

    @Test
    void singleLineUnchanged() throws IOException {
        test("single-line-unchanged.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("operator-wrapping/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
