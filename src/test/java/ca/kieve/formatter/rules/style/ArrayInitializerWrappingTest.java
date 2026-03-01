package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayInitializerWrappingTest extends FormatterRuleTestBase {
    ArrayInitializerWrappingTest() {
        super("array-initializer-wrapping/", ArrayInitializerWrapping::apply);
    }

    @Test
    void wrapsContentOffBraceLine() throws IOException {
        test("wrap-elements-input.java", "wrap-elements-expected.java");
    }

    @Test
    void singleLineUnchanged() throws IOException {
        test("single-line-unchanged.java");
    }

    @Test
    void alreadyOnOwnLineUnchanged() throws IOException {
        test("already-own-line-unchanged.java");
    }

    @Test
    void nestedArrayWraps() throws IOException {
        test("nested-input.java", "nested-expected.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture(
            "array-initializer-wrapping/formatter-off-unchanged.java"
        );
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
