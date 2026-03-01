package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Parentheses Preservation section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Parentheses Preservation"
 */
class ParenthesesPreservationTest extends FormatterTestBase {
    ParenthesesPreservationTest() {
        super("parentheses-preservation/", s -> formatJava(s));
    }

    // parentheses_preservation_mode (DO_NOT_PRESERVE_EMPTY)
    @Test
    void parenthesesPreservationModeCollapsesEmptyParentheses() throws IOException {
        test("empty-parens-input.java", "empty-parens-expected.java");
    }
}
