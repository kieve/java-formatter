package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Line Width section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Line Width"
 */
class LineWidthTest {

    // lineSplit
    @Test
    void lineSplitWrapsLinesExceedingOneHundredCharacters() {
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

        assertEquals(expected, formatJava(input));
    }

    // number_of_empty_lines_to_preserve
    @Test
    void numberOfEmptyLinesToPreserveCollapsesMultipleBlanksToOne() {
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

        assertEquals(expected, formatJava(input));
    }
}
