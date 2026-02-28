package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
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

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }
}
