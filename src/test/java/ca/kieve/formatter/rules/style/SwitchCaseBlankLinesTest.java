package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SwitchCaseBlankLinesTest extends FormatterRuleTestBase {
    SwitchCaseBlankLinesTest() {
        super("switch-case-blank-lines/", SwitchCaseBlankLines::apply);
    }

    @Test
    void removesBlankLineBeforeCase() throws IOException {
        test("before-case-input.java", "case-expected.java");
    }

    @Test
    void removesBlankLineBeforeDefault() throws IOException {
        test("before-default-input.java", "before-default-expected.java");
    }

    @Test
    void removesMultipleBlankLinesBeforeCase() throws IOException {
        test("multiple-blanks-input.java", "case-expected.java");
    }

    @Test
    void preservesNonSwitchBlankLines() throws IOException {
        test("non-switch-unchanged.java");
    }

    @Test
    void preservesSwitchWithNoBlankLines() throws IOException {
        test("no-blank-lines-unchanged.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("switch-case-blank-lines/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
