package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Commas section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Commas"
 */
class WhitespaceCommasTest {
    // insert_space_before_comma_in_method_invocation_arguments
    // insert_space_after_comma_in_method_invocation_arguments
    // insert_space_before_comma_in_method_declaration_parameters
    // insert_space_after_comma_in_method_declaration_parameters
    // insert_space_before_comma_in_constructor_declaration_parameters
    // insert_space_after_comma_in_constructor_declaration_parameters
    // insert_space_before_comma_in_allocation_expression
    // insert_space_after_comma_in_allocation_expression
    // insert_space_before_comma_in_array_initializer
    // insert_space_after_comma_in_array_initializer
    // insert_space_before_comma_in_enum_constant_arguments
    // insert_space_after_comma_in_enum_constant_arguments
    // insert_space_before_comma_in_enum_declarations
    // insert_space_after_comma_in_enum_declarations
    // insert_space_before_comma_in_for_increments
    // insert_space_after_comma_in_for_increments
    // insert_space_before_comma_in_for_inits
    // insert_space_after_comma_in_for_inits
    // insert_space_before_comma_in_multiple_field_declarations
    // insert_space_after_comma_in_multiple_field_declarations
    // insert_space_before_comma_in_multiple_local_declarations
    // insert_space_after_comma_in_multiple_local_declarations
    // insert_space_before_comma_in_annotation
    // insert_space_after_comma_in_annotation
    // insert_space_before_comma_in_record_components
    // insert_space_after_comma_in_record_components
    // insert_space_before_comma_in_switch_case_expressions
    // insert_space_after_comma_in_switch_case_expressions
    // insert_space_before_comma_in_type_arguments
    // insert_space_after_comma_in_type_arguments
    // insert_space_before_comma_in_type_parameters
    // insert_space_after_comma_in_type_parameters
    // insert_space_before_comma_in_parameterized_type_reference
    // insert_space_after_comma_in_parameterized_type_reference
    @Test
    void commaSpacing() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    @interface Config {
                        String name();

                        int value();
                    }

                    class Pair<A ,B> {
                    }

                    enum Color {
                        RED(1 ,2) ,
                        GREEN(3 ,4) ,
                        BLUE(5 ,6);

                        Color(int a ,int b) {
                        }
                    }

                    record Point(int x ,int y) {
                    }

                    int a ,b ,c;

                    FormatterTest(int x ,int y) {
                    }

                    <T ,U> void genericMethod(T a ,U b) {
                    }

                    @Config(name = "test" ,value = 1)
                    void method(int x ,int y) {
                        int d ,e ,f;
                        System.out.printf("%d %d" ,x ,y);

                        Object obj = new FormatterTest(1 ,2);

                        int[] arr = { 1 ,2 ,3 };

                        Pair<String ,Integer> pair = null;
                        this.<String ,Integer>genericMethod("a" ,1);

                        for (int i = 0 ,j = 0; i < 10; i++ ,j++) {
                        }

                        switch (x) {
                        case 1 ,2 ,3:
                            break;
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    @interface Config {
                        String name();

                        int value();
                    }

                    class Pair<A, B> {
                    }

                    enum Color {
                        RED(1, 2),
                        GREEN(3, 4),
                        BLUE(5, 6);

                        Color(int a, int b) {
                        }
                    }

                    record Point(int x, int y) {
                    }

                    int a, b, c;

                    FormatterTest(int x, int y) {
                    }

                    <T, U> void genericMethod(T a, U b) {
                    }

                    @Config(name = "test", value = 1)
                    void method(int x, int y) {
                        int d, e, f;
                        System.out.printf("%d %d", x, y);

                        Object obj = new FormatterTest(1, 2);

                        int[] arr = { 1, 2, 3 };

                        Pair<String, Integer> pair = null;
                        this.<String, Integer>genericMethod("a", 1);

                        for (int i = 0, j = 0; i < 10; i++, j++) {
                        }

                        switch (x) {
                        case 1, 2, 3:
                            break;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
