package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ca.kieve.formatter.FormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Line Width section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Line Width"
 */
class LineWidthTest {

    @TempDir
    Path testProjectDir;

    // lineSplit
    @Test
    void lineSplitWrapsLinesExceedingOneHundredCharacters() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        System.out.printf("%s %s %s %s %s", "longValueOne", "longValueTwo", "longValueThree", "longValueFour", "five");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        System.out.printf(
                            "%s %s %s %s %s",
                            "longValueOne",
                            "longValueTwo",
                            "longValueThree",
                            "longValueFour",
                            "five");
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // number_of_empty_lines_to_preserve
    @Test
    void numberOfEmptyLinesToPreserveCollapsesMultipleBlanksToOne() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    int fieldOne;



                    int fieldTwo;




                    void method() {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int fieldOne;

                    int fieldTwo;

                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }
}
