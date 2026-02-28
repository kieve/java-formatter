package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Miscellaneous section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Miscellaneous"
 */
class WhitespaceMiscellaneousTest {

    // insert_space_after_at_in_annotation
    // insert_space_after_at_in_annotation_type_declaration
    // insert_space_before_ellipsis
    // insert_space_after_ellipsis
    // insert_space_before_and_in_type_parameter
    // insert_space_after_and_in_type_parameter
    // insert_space_before_question_in_conditional
    // insert_space_after_question_in_conditional
    // insert_space_before_question_in_wildcard
    // insert_space_after_question_in_wildcard
    // insert_space_before_lambda_arrow
    // insert_space_after_lambda_arrow
    @Test
    void miscellaneousSpacing() {
        // language=Java
        String input = """
                public class FormatterTest {
                    @ interface Config {
                        String value();
                    }

                    <T extends Comparable&Cloneable> void bounded(T t) {
                    }

                    @ Override
                    public String toString() {
                        return "test";
                    }

                    void method(String ...args) {
                        int a = true?1 : 2;

                        Class< ? > c = null;

                        Runnable r = ()->System.out.println("hello");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    @interface Config {
                        String value();
                    }

                    <T extends Comparable & Cloneable> void bounded(T t) {
                    }

                    @Override
                    public String toString() {
                        return "test";
                    }

                    void method(String... args) {
                        int a = true ? 1 : 2;

                        Class<?> c = null;

                        Runnable r = () -> System.out.println("hello");
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
