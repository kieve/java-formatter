package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassBodyBlankLinesTest extends FormatterRuleTestBase {
    ClassBodyBlankLinesTest() {
        super("class-body-blank-lines/", ClassBodyBlankLines::apply);
    }

    @Test
    void removesBlankLineAfterClassBrace() throws IOException {
        test("class-brace-input.java", "class-brace-expected.java");
    }

    @Test
    void removesMultipleBlankLinesAfterClassBrace() throws IOException {
        test("multiple-blank-lines-input.java", "multiple-blank-lines-expected.java");
    }

    @Test
    void removesBlankLineAfterMethodBrace() throws IOException {
        test("method-brace-input.java", "method-brace-expected.java");
    }

    @Test
    void removesBlankLineAfterNestedBraces() throws IOException {
        test("nested-braces-input.java", "nested-braces-expected.java");
    }

    @Test
    void preservesBlankLinesBetweenMembers() throws IOException {
        test("between-members-unchanged.java");
    }

    @Test
    void preservesSourceWithNoBlankLinesAfterBrace() throws IOException {
        test("no-blank-lines-unchanged.java");
    }

    @Test
    void handlesIndentedBlankLines() throws IOException {
        test("indented-blank-lines-input.java", "indented-blank-lines-expected.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("class-body-blank-lines/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
