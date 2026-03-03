package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ThrowsWrappingTest extends FormatterRuleTestBase {
    private static final FormatConfig CONFIG = FormatConfig.defaults();

    ThrowsWrappingTest() {
        super("throws-wrapping/", s -> ThrowsWrapping.apply(s, CONFIG));
    }

    @Test
    void wrapsThrowsClauseInMethodDeclaration() throws IOException {
        test("method-input.java", "method-expected.java");
    }

    @Test
    void wrapsThrowsClauseInConstructorDeclaration() throws IOException {
        test("constructor-input.java", "constructor-expected.java");
    }

    @Test
    void preservesSingleLineThrowsClause() throws IOException {
        test("single-line-unchanged.java");
    }

    @Test
    void wrapsThrowsClauseWithWrappedParameters() throws IOException {
        test("wrapped-params-input.java", "wrapped-params-expected.java");
    }

    @Test
    void wrapsThrowsClauseInAbstractMethod() throws IOException {
        test("abstract-input.java", "abstract-expected.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("throws-wrapping/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
