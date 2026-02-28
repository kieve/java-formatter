package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Angle Brackets (Generics) section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Angle Brackets (Generics)"
 */
class WhitespaceAngleBracketsTest {
    // insert_space_before_opening_angle_bracket_in_type_arguments
    // insert_space_after_opening_angle_bracket_in_type_arguments
    // insert_space_before_closing_angle_bracket_in_type_arguments
    // insert_space_after_closing_angle_bracket_in_type_arguments
    // insert_space_before_opening_angle_bracket_in_type_parameters
    // insert_space_after_opening_angle_bracket_in_type_parameters
    // insert_space_before_closing_angle_bracket_in_type_parameters
    // insert_space_after_closing_angle_bracket_in_type_parameters
    @Test
    void angleBracketSpacing() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest < T, U > {
                    class Container < K, V > {
                    }

                    < A, B >void genericMethod(A a, B b) {
                    }

                    void method() {
                        Container < String, Integer > c = null;
                        this. < String, Integer >genericMethod("a", 1);
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest<T, U> {
                    class Container<K, V> {
                    }

                    <A, B> void genericMethod(A a, B b) {
                    }

                    void method() {
                        Container<String, Integer> c = null;
                        this.<String, Integer>genericMethod("a", 1);
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
