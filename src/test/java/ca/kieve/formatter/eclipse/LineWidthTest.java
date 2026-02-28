package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
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
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        System.out.printf("%s %s %s %s %s", "longValueOne", "longValueTwo", "longValueThree", "longValueFour", "five");
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_empty_lines_to_preserve
    @Test
    void numberOfEmptyLinesToPreserveCollapsesMultipleBlanksToOne() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int fieldOne;



                    int fieldTwo;




                    void method() {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int fieldOne;

                    int fieldTwo;

                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // join_wrapped_lines
    @Test
    void joinWrappedLinesPreservesAuthorLineBreaks() {
        // language=Java — multi-line array initializer is preserved
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int[] values = {
                        1, 2, 3
                    };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int[] values = {
                        1, 2, 3
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
