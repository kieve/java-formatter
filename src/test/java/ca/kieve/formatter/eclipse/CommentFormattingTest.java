package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Comment Formatting section.
 * <p>
 * Verifies that comment formatting is disabled (Javadoc, block, and line
 * comments are not reformatted) while the comment line length limit of 100 is
 * respected.
 */
class CommentFormattingTest {
    // comment.format_javadoc_comments: false — Javadoc not reformatted
    @Test
    void javadocCommentPreservesOriginalFormatting() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    /**
                     * Configuration for the custom formatter.
                     * All formatting rules reference this for their settings.
                     */
                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(input, formatJava(input));
    }

    // comment.format_javadoc_comments: false — multi-paragraph Javadoc preserved
    @Test
    void javadocMultiParagraphPreservesFormatting() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    /**
                     * First paragraph of the Javadoc.
                     * <p>
                     * Second paragraph with more detail.
                     *
                     * @param x the input value
                     * @return the result
                     */
                    int method(int x) {
                        return x;
                    }
                }
                """;
                // @formatter:on

        assertEquals(input, formatJava(input));
    }

    // comment.format_block_comments: false — block comments not reformatted
    @Test
    void blockCommentPreservesOriginalFormatting() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    /*
                     * This is a block comment.
                     * It has multiple lines that should not be joined or reflowed.
                     */
                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(input, formatJava(input));
    }

    // comment.format_line_comments: false — line comments not reformatted
    @Test
    void lineCommentPreservesOriginalFormatting() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    // This is a line comment that should stay exactly as written
                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(input, formatJava(input));
    }

    // Javadoc with short lines should not be joined together
    @Test
    void javadocShortLinesNotJoined() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    /**
                     * Short line one.
                     * Short line two.
                     * Short line three.
                     */
                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(input, formatJava(input));
    }
}
