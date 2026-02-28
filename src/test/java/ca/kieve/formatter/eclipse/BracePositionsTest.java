package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Brace Positions section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Brace Positions"
 */
class BracePositionsTest {
    // brace_position_for_type_declaration
    @Test
    void bracePositionForTypeDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest
                {
                    int field;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int field;
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_method_declaration
    @Test
    void bracePositionForMethodDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method()
                    {
                        System.out.println("hello");
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        System.out.println("hello");
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_constructor_declaration
    @Test
    void bracePositionForConstructorDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest
                {
                    public FormatterTest()
                    {
                        System.out.println("constructed");
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    public FormatterTest() {
                        System.out.println("constructed");
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_block
    @Test
    void bracePositionForBlockIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true)
                        {
                            System.out.println("hello");
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                            System.out.println("hello");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_switch
    @Test
    void bracePositionForSwitchIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x)
                        {
                        case 1:
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
                    void method(int x) {
                        switch (x) {
                        case 1:
                            break;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_anonymous_type_declaration
    @Test
    void bracePositionForAnonymousTypeDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    Runnable r = new Runnable()
                    {
                        public void run()
                        {
                        }
                    };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    Runnable r = new Runnable() {
                        public void run() {
                        }
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_array_initializer
    @Test
    void bracePositionForArrayInitializerIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int[] values = new int[]
                    { 1, 2, 3 };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int[] values = new int[] { 1, 2, 3 };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_enum_declaration
    @Test
    void bracePositionForEnumDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest
                {
                    VALUE_ONE,
                    VALUE_TWO
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE,
                    VALUE_TWO
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_enum_constant
    @Test
    void bracePositionForEnumConstantIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest {
                    VALUE_ONE
                    {
                        public String toString() {
                            return "one";
                        }
                    };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE {
                        public String toString() {
                            return "one";
                        }
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_annotation_type_declaration
    @Test
    void bracePositionForAnnotationTypeDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public @interface FormatterTest
                {
                    String value();
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public @interface FormatterTest {
                    String value();
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_record_declaration
    @Test
    void bracePositionForRecordDeclarationIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public record FormatterTest(int x, int y)
                {
                    public int sum() {
                        return x + y;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public record FormatterTest(int x, int y) {
                    public int sum() {
                        return x + y;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_record_constructor
    @Test
    void bracePositionForRecordConstructorIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public record FormatterTest(int x, int y)
                {
                    public FormatterTest
                    {
                        if (x < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest {
                        if (x < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_lambda_body
    @Test
    void bracePositionForLambdaBodyIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    Runnable r = () ->
                    {
                        System.out.println("hello");
                    };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    Runnable r = () -> {
                        System.out.println("hello");
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_block_in_case
    @Test
    void bracePositionForBlockInCaseIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1:
                        {
                            System.out.println("one");
                            break;
                        }
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1: {
                            System.out.println("one");
                            break;
                        }
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // brace_position_for_block_in_case_after_arrow
    @Test
    void bracePositionForBlockInCaseAfterArrowIsEndOfLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1 ->
                        {
                            System.out.println("one");
                        }
                        default ->
                        {
                            System.out.println("other");
                        }
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1 -> {
                            System.out.println("one");
                        }
                        default -> {
                            System.out.println("other");
                        }
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
