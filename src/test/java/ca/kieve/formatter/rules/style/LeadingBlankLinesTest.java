package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadingBlankLinesTest extends FormatterRuleTestBase {
    LeadingBlankLinesTest() {
        super("leading-blank-lines/", LeadingBlankLines::apply);
    }

    @Test
    void stripsMultipleLeadingBlankLines() throws IOException {
        test("multiple-leading-input.java", "leading-expected.java");
    }

    @Test
    void stripsSingleLeadingBlankLine() throws IOException {
        test("single-leading-input.java", "leading-expected.java");
    }

    @Test
    void preservesSourceWithNoLeadingBlankLines() throws IOException {
        test("no-leading-unchanged.java");
    }

    @Test
    void preservesInternalBlankLines() throws IOException {
        test("internal-blanks-input.java", "internal-blanks-expected.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("leading-blank-lines/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
