package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ca.kieve.formatter.FormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Brace Positions section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Brace Positions"
 */
class BracePositionsTest {

    @TempDir
    Path testProjectDir;

    // brace_position_for_type_declaration
    @Test
    void bracePositionForTypeDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest
                {
                    int field;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int field;
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_method_declaration
    @Test
    void bracePositionForMethodDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method()
                    {
                        System.out.println("hello");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        System.out.println("hello");
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_constructor_declaration
    @Test
    void bracePositionForConstructorDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest
                {
                    public FormatterTest()
                    {
                        System.out.println("constructed");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    public FormatterTest() {
                        System.out.println("constructed");
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_block
    @Test
    void bracePositionForBlockIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                            System.out.println("hello");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_switch
    @Test
    void bracePositionForSwitchIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_anonymous_type_declaration
    @Test
    void bracePositionForAnonymousTypeDeclarationIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
        String expected = """
                public class FormatterTest {
                    Runnable r = new Runnable() {
                        public void run() {
                        }
                    };
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_array_initializer
    @Test
    void bracePositionForArrayInitializerIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    int[] values = new int[]
                    { 1, 2, 3 };
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int[] values = new int[] { 1, 2, 3 };
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_enum_declaration
    @Test
    void bracePositionForEnumDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public enum FormatterTest
                {
                    VALUE_ONE,
                    VALUE_TWO
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE,
                    VALUE_TWO
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_enum_constant
    @Test
    void bracePositionForEnumConstantIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE {
                        public String toString() {
                            return "one";
                        }
                    };
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_annotation_type_declaration
    @Test
    void bracePositionForAnnotationTypeDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public @interface FormatterTest
                {
                    String value();
                }
                """;

        // language=Java
        String expected = """
                public @interface FormatterTest {
                    String value();
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_record_declaration
    @Test
    void bracePositionForRecordDeclarationIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public record FormatterTest(int x, int y)
                {
                    public int sum() {
                        return x + y;
                    }
                }
                """;

        // language=Java
        String expected = """
                public record FormatterTest(int x, int y) {
                    public int sum() {
                        return x + y;
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_record_constructor
    @Test
    void bracePositionForRecordConstructorIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
        String expected = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest {
                        if (x < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_lambda_body
    @Test
    void bracePositionForLambdaBodyIsEndOfLine() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    Runnable r = () ->
                    {
                        System.out.println("hello");
                    };
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    Runnable r = () -> {
                        System.out.println("hello");
                    };
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_block_in_case
    @Test
    void bracePositionForBlockInCaseIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // brace_position_for_block_in_case_after_arrow
    @Test
    void bracePositionForBlockInCaseAfterArrowIsEndOfLine() throws IOException {
        // language=Java
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

        // language=Java
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }
}
