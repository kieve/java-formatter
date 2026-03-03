package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SwitchCaseBodyIndentationTest extends FormatterRuleTestBase {
    SwitchCaseBodyIndentationTest() {
        super("switch-case-body-indent/", SwitchCaseBodyIndentation::apply);
    }

    @Test
    void indentsArrowBodyAfterWrappedCaseExpressions() throws IOException {
        test("arrow-input.java", "arrow-expected.java");
    }

    @Test
    void indentsColonBodyAfterWrappedCaseExpressions() throws IOException {
        test("colon-input.java", "colon-expected.java");
    }

    @Test
    void preservesNonWrappedCases() throws IOException {
        test("no-wrap-unchanged.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("switch-case-body-indent/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
