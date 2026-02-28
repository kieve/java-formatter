package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Parentheses Preservation section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Parentheses Preservation"
 */
class ParenthesesPreservationTest {
    // parentheses_preservation_mode (DO_NOT_PRESERVE_EMPTY)
    @Test
    void parenthesesPreservationModeCollapsesEmptyParentheses() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(
                    ) {
                    }

                    void other(
                            int a,
                            int b
                    ) {
                    }
                }
                """;
                // @formatter:on

        // Empty parens are collapsed; non-empty parens preserve their positioning
        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                    }

                    void other(
                        int a,
                        int b) {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
