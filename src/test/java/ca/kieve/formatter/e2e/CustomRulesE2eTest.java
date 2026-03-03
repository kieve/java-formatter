package ca.kieve.formatter.e2e;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;
import ca.kieve.formatter.util.FormatterTestUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * End-to-end tests that run the full custom rules chain via
 * {@link CustomFormatterStep#applyCustomRules(String)} to verify that
 * rules interact correctly when combined.
 */
class CustomRulesE2eTest {
    private static final String FIXTURE_DIR = "e2e/";

    private void test(String inputFile, String expectedFile) throws IOException {
        String input = FormatterTestUtil.loadFixture(FIXTURE_DIR + inputFile);
        String expected = FormatterTestUtil.loadFixture(FIXTURE_DIR + expectedFile);
        assertEquals(expected, CustomFormatterStep.applyCustomRules(input));
    }

    private void test(String unchangedFile) throws IOException {
        String input = FormatterTestUtil.loadFixture(FIXTURE_DIR + unchangedFile);
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }

    @Test
    void textBlockConcatenationPreserved() throws IOException {
        test("text-block-concatenation-unchanged.java");
    }

    @Test
    void annotationTypeInnerTypeReorderingAndBlankLines() throws IOException {
        test("annotation-type-reordering-input.java", "annotation-type-reordering-expected.java");
    }
}
