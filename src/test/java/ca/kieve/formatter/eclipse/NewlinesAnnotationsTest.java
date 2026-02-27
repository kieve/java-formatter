package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Newlines / Annotations on Newlines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Annotations on Newlines"
 */
class NewlinesAnnotationsTest {

    // insert_new_line_after_annotation_on_type
    @Test
    void insertNewLineAfterAnnotationOnTypeInsertsNewLine() {
        // language=Java — annotation on same line as type gets moved to its own line
        String input = """
                @SuppressWarnings("unchecked") public class FormatterTest {
                    void method() {
                    }
                }
                """;

        // language=Java
        String expected = """
                @SuppressWarnings("unchecked")
                public class FormatterTest {
                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_method
    @Test
    void insertNewLineAfterAnnotationOnMethodInsertsNewLine() {
        // language=Java — annotation on same line as method gets moved to its own line
        String input = """
                public class FormatterTest {
                    @Override public String toString() {
                        return "test";
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    @Override
                    public String toString() {
                        return "test";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_field
    @Test
    void insertNewLineAfterAnnotationOnFieldInsertsNewLine() {
        // language=Java — annotation on same line as field gets moved to its own line
        String input = """
                public class FormatterTest {
                    @Deprecated private int value = 0;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    @Deprecated
                    private int value = 0;
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_local_variable
    @Test
    void insertNewLineAfterAnnotationOnLocalVariableInsertsNewLine() {
        // language=Java — annotation on same line as local variable gets moved to its own line
        String input = """
                public class FormatterTest {
                    void method() {
                        @SuppressWarnings("unchecked") int value = 0;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        @SuppressWarnings("unchecked")
                        int value = 0;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_parameter
    @Test
    void insertNewLineAfterAnnotationOnParameterDoesNotInsert() {
        // language=Java — annotation on parameter stays on the same line
        String input = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") int value) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") int value) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_package
    @Test
    void insertNewLineAfterAnnotationOnPackageInsertsNewLine() {
        // language=Java — annotation on same line as package gets moved to its own line
        String input = """
                @SuppressWarnings("unchecked") package com.example;

                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                @SuppressWarnings("unchecked")
                package com.example;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_annotation_on_enum_constant
    @Test
    void insertNewLineAfterAnnotationOnEnumConstantInsertsNewLine() {
        // language=Java — annotation on same line as enum constant gets moved to its own line
        String input = """
                public enum FormatterTest {
                    @Deprecated VALUE_ONE,
                    @Deprecated VALUE_TWO
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    @Deprecated
                    VALUE_ONE,
                    @Deprecated
                    VALUE_TWO
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_type_annotation
    @Test
    void insertNewLineAfterTypeAnnotationDoesNotInsert() {
        // language=Java — type annotation in a type-use position stays inline
        String input = """
                import java.lang.annotation.ElementType;
                import java.lang.annotation.Target;
                import java.util.List;

                public class FormatterTest {
                    @Target(ElementType.TYPE_USE)
                    @interface NonNull {
                    }

                    List<@NonNull String> method() {
                        return List.of();
                    }
                }
                """;

        // language=Java
        String expected = """
                import java.lang.annotation.ElementType;
                import java.lang.annotation.Target;
                import java.util.List;

                public class FormatterTest {
                    @Target(ElementType.TYPE_USE)
                    @interface NonNull {
                    }

                    List<@NonNull String> method() {
                        return List.of();
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
