package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Brackets (Array Access) section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Brackets (Array Access)"
 */
class WhitespaceBracketsTest {
    // insert_space_before_opening_bracket_in_array_access
    // insert_space_after_opening_bracket_in_array_access
    // insert_space_before_closing_bracket_in_array_access
    @Test
    void bracketSpacing() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int[] arr = { 1, 2, 3 };
                        int a = arr [ 0 ];
                        arr [ 1 ] = 10;

                        int[][] matrix = { { 1, 2 }, { 3, 4 } };
                        int b = matrix [ 0 ] [ 1 ];
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int[] arr = { 1, 2, 3 };
                        int a = arr[0];
                        arr[1] = 10;

                        int[][] matrix = { { 1, 2 }, { 3, 4 } };
                        int b = matrix[0][1];
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
