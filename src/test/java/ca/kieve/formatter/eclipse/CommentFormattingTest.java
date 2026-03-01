package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Comment Formatting section.
 * <p>
 * Verifies that comment formatting is disabled (Javadoc, block, and line
 * comments are not reformatted) while the comment line length limit of 100 is
 * respected.
 */
class CommentFormattingTest extends FormatterTestBase {
    CommentFormattingTest() {
        super("comment-formatting/", s -> formatJava(s));
    }

    // comment.format_javadoc_comments: false — Javadoc not reformatted
    @Test
    void javadocCommentPreservesOriginalFormatting() throws IOException {
        test("javadoc-unchanged.java");
    }

    // comment.format_javadoc_comments: false — multi-paragraph Javadoc preserved
    @Test
    void javadocMultiParagraphPreservesFormatting() throws IOException {
        test("javadoc-multi-paragraph-unchanged.java");
    }

    // comment.format_block_comments: false — block comments not reformatted
    @Test
    void blockCommentPreservesOriginalFormatting() throws IOException {
        test("block-comment-unchanged.java");
    }

    // comment.format_line_comments: false — line comments not reformatted
    @Test
    void lineCommentPreservesOriginalFormatting() throws IOException {
        test("line-comment-unchanged.java");
    }

    // Javadoc with short lines should not be joined together
    @Test
    void javadocShortLinesNotJoined() throws IOException {
        test("javadoc-short-lines-unchanged.java");
    }
}
