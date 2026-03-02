package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssignmentWrappingTest extends FormatterRuleTestBase {
    private static final FormatConfig CONFIG = FormatConfig.defaults();

    AssignmentWrappingTest() {
        super(
            "assignment-wrapping/",
            s -> AssignmentWrapping.apply(s, CONFIG)
        );
    }

    @Test
    void simpleLocalVariableWraps() throws IOException {
        test("simple-input.java", "simple-expected.java");
    }

    @Test
    void fieldDeclarationWraps() throws IOException {
        test("field-input.java", "field-expected.java");
    }

    @Test
    void compoundOperatorWraps() throws IOException {
        test("compound-input.java", "compound-expected.java");
    }

    @Test
    void multilineRhsUnchanged() throws IOException {
        test("multiline-rhs-unchanged.java");
    }

    @Test
    void withinLimitUnchanged() throws IOException {
        test("within-limit-unchanged.java");
    }

    @Test
    void forLoopUnchanged() throws IOException {
        test("for-loop-unchanged.java");
    }

    @Test
    void annotationUnchanged() throws IOException {
        test("annotation-unchanged.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture(
            "assignment-wrapping/formatter-off-unchanged.java"
        );
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
