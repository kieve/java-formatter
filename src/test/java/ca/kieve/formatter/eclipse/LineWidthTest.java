package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Line Width section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Line Width"
 */
class LineWidthTest extends FormatterTestBase {
    LineWidthTest() {
        super("line-width/", s -> formatJava(s));
    }

    // lineSplit
    @Test
    void lineSplitWrapsLinesExceedingOneHundredCharacters() throws IOException {
        test("line-split-input.java", "line-split-expected.java");
    }

    // number_of_empty_lines_to_preserve
    @Test
    void numberOfEmptyLinesToPreserveCollapsesMultipleBlanksToOne() throws IOException {
        test("empty-lines-input.java", "empty-lines-expected.java");
    }

    // join_wrapped_lines
    @Test
    void joinWrappedLinesPreservesAuthorLineBreaks() throws IOException {
        test("join-wrapped-lines-unchanged.java");
    }
}
