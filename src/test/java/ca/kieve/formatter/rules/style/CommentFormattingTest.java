package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentFormattingTest extends FormatterRuleTestBase {
    private static final FormatConfig CONFIG = FormatConfig.defaults();

    CommentFormattingTest() {
        super(
            "comment-formatting-rule/",
            s -> CommentFormatting.apply(s, CONFIG)
        );
    }

    @Test
    void javadocLongLineWraps() throws IOException {
        test("javadoc-long-line-input.java", "javadoc-long-line-expected.java");
    }

    @Test
    void javadocTagSplit() throws IOException {
        test("javadoc-tag-split-input.java", "javadoc-tag-split-expected.java");
    }

    @Test
    void paramContinuationAlign() throws IOException {
        test(
            "param-continuation-input.java",
            "param-continuation-expected.java"
        );
    }

    @Test
    void returnContinuationAlign() throws IOException {
        test(
            "return-continuation-input.java",
            "return-continuation-expected.java"
        );
    }

    @Test
    void throwsContinuationAlign() throws IOException {
        test(
            "throws-continuation-input.java",
            "throws-continuation-expected.java"
        );
    }

    @Test
    void blockCommentWraps() throws IOException {
        test(
            "block-comment-wrap-input.java",
            "block-comment-wrap-expected.java"
        );
    }

    @Test
    void lineCommentWraps() throws IOException {
        test(
            "line-comment-wrap-input.java",
            "line-comment-wrap-expected.java"
        );
    }

    @Test
    void markdownDocWraps() throws IOException {
        test(
            "markdown-doc-wrap-input.java",
            "markdown-doc-wrap-expected.java"
        );
    }

    @Test
    void preBlockPreserved() throws IOException {
        test("pre-block-unchanged.java");
    }

    @Test
    void inlineTagAtomic() throws IOException {
        test("inline-tag-input.java", "inline-tag-expected.java");
    }

    @Test
    void shortCommentPreserved() throws IOException {
        test("short-comment-unchanged.java");
    }

    @Test
    void singleLineJavadocPreserved() throws IOException {
        test("single-line-javadoc-unchanged.java");
    }

    @Test
    void deeplyNestedIndent() throws IOException {
        test("deeply-nested-input.java", "deeply-nested-expected.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture(
            "comment-formatting-rule/formatter-off-unchanged.java"
        );
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
