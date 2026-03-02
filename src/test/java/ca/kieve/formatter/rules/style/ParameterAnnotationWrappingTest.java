package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterAnnotationWrappingTest extends FormatterRuleTestBase {
    ParameterAnnotationWrappingTest() {
        super(
            "parameter-annotation-wrapping/",
            ParameterAnnotationWrapping::apply
        );
    }

    @Test
    void wrapsAnnotationsOnMethodParameters() throws IOException {
        test("method-input.java", "method-expected.java");
    }

    @Test
    void wrapsAnnotationsOnConstructorParameters() throws IOException {
        test("constructor-input.java", "constructor-expected.java");
    }

    @Test
    void wrapsSingleAnnotation() throws IOException {
        test("single-annotation-input.java", "single-annotation-expected.java");
    }

    @Test
    void wrapsAnnotationsOnMixedParameters() throws IOException {
        test("mixed-params-input.java", "mixed-params-expected.java");
    }

    @Test
    void preservesNotWrappedParameters() throws IOException {
        test("not-wrapped-unchanged.java");
    }

    @Test
    void preservesAlreadyCorrectAnnotations() throws IOException {
        test("already-correct-unchanged.java");
    }

    @Test
    void preservesParametersWithNoAnnotations() throws IOException {
        test("no-annotations-unchanged.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture(
            "parameter-annotation-wrapping/formatter-off-unchanged.java"
        );
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
