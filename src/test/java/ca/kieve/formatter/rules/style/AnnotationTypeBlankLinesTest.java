package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationTypeBlankLinesTest extends FormatterRuleTestBase {
    AnnotationTypeBlankLinesTest() {
        super("annotation-type-blank-lines/", AnnotationTypeBlankLines::apply);
    }

    @Test
    void removesBlankLinesBetweenMembers() throws IOException {
        test("simple-input.java", "simple-expected.java");
    }

    @Test
    void preservesAlreadyCompactAnnotationType() throws IOException {
        test("no-blanks-unchanged.java");
    }

    @Test
    void removesBlankLinesInNestedAnnotationTypes() throws IOException {
        test("nested-input.java", "nested-expected.java");
    }

    @Test
    void preservesBlankLinesInsideNestedClass() throws IOException {
        test("nested-class-input.java", "nested-class-expected.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("annotation-type-blank-lines/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
