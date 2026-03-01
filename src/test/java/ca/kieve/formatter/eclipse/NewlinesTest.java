package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Eclipse JDT Formatter — Newlines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Newlines"
 */
class NewlinesTest extends FormatterTestBase {
    NewlinesTest() {
        super("newlines/", s -> formatJava(s));
    }

    // insert_new_line_at_end_of_file_if_missing
    @Test
    void insertNewLineAtEndOfFileIfMissing() throws IOException {
        String input = loadFixture("newlines/missing-eof-newline-input.java");
        String result = formatJava(input);
        assertTrue(result.endsWith("\n"), "Output should end with a newline");
    }

    // keep_else_statement_on_same_line (excluded — N/A, braces always required)
    @Test
    void keepElseStatementOnSameLineIsNotApplicableWithBraces() throws IOException {
        test("else-on-same-line-input.java", "else-on-same-line-expected.java");
    }

    // keep_then_statement_on_same_line (excluded — N/A, braces always required)
    @Test
    void keepThenStatementOnSameLineIsNotApplicableWithBraces() throws IOException {
        test("then-with-braces-unchanged.java");
    }

    // insert_new_line_before_else_in_if_statement
    @Test
    void insertNewLineBeforeElseInIfStatementDoesNotInsert() throws IOException {
        test("else-on-same-line-input.java", "else-on-same-line-expected.java");
    }

    // insert_new_line_before_catch_in_try_statement
    @Test
    void insertNewLineBeforeCatchInTryStatementDoesNotInsert() throws IOException {
        test("catch-on-same-line-input.java", "catch-on-same-line-expected.java");
    }

    // insert_new_line_before_finally_in_try_statement
    @Test
    void insertNewLineBeforeFinallyInTryStatementDoesNotInsert() throws IOException {
        test("finally-on-same-line-input.java", "finally-on-same-line-expected.java");
    }

    // insert_new_line_before_while_in_do_statement
    @Test
    void insertNewLineBeforeWhileInDoStatementDoesNotInsert() throws IOException {
        test("do-while-on-same-line-input.java", "do-while-on-same-line-expected.java");
    }

    // insert_new_line_after_opening_brace_in_array_initializer (removed — leave to author)
    // insert_new_line_before_closing_brace_in_array_initializer (removed — leave to author)
    @Test
    void arrayInitializerPreservesSingleLineLayout() throws IOException {
        test("array-single-line-unchanged.java");
    }

    // insert_new_line_after_opening_brace_in_array_initializer (removed — leave to author)
    // insert_new_line_before_closing_brace_in_array_initializer (removed — leave to author)
    @Test
    void arrayInitializerPreservesMultiLineLayout() throws IOException {
        test("array-multi-line-unchanged.java");
    }

    // insert_new_line_after_label
    @Test
    void insertNewLineAfterLabelInsertsNewLine() throws IOException {
        test("label-input.java", "label-expected.java");
    }

    // compact_else_if
    @Test
    void compactElseIfTreatsAsOneUnit() throws IOException {
        test("compact-else-if-input.java", "compact-else-if-expected.java");
    }

    // put_empty_statement_on_new_line
    @Test
    void putEmptyStatementOnNewLineKeepsOnSameLine() throws IOException {
        test("empty-statement-input.java", "empty-statement-expected.java");
    }
}
