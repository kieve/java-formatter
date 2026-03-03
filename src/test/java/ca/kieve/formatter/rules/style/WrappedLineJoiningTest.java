package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WrappedLineJoiningTest extends FormatterRuleTestBase {
    private static final FormatConfig CONFIG = FormatConfig.defaults();

    WrappedLineJoiningTest() {
        super("wrapped-line-joining/", s -> WrappedLineJoining.apply(s, CONFIG));
    }

    @Test
    void joinsMethodParams() throws IOException {
        test("method-params-input.java", "method-params-expected.java");
    }

    @Test
    void joinsConstructorParams() throws IOException {
        test("constructor-params-input.java", "constructor-params-expected.java");
    }

    @Test
    void joinsMethodArgs() throws IOException {
        test("method-args-input.java", "method-args-expected.java");
    }

    @Test
    void joinsTryResources() throws IOException {
        test("try-resources-input.java", "try-resources-expected.java");
    }

    @Test
    void preservesTooLong() throws IOException {
        test("too-long-unchanged.java");
    }

    @Test
    void preservesArrayArg() throws IOException {
        test("array-arg-unchanged.java");
    }

    @Test
    void preservesLambdaArg() throws IOException {
        test("lambda-arg-unchanged.java");
    }

    @Test
    void preservesAnnotatedParams() throws IOException {
        test("annotated-params-unchanged.java");
    }

    @Test
    void joinsNestedCalls() throws IOException {
        test("nested-calls-input.java", "nested-calls-expected.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("wrapped-line-joining/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
