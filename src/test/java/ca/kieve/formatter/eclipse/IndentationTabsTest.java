package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ca.kieve.formatter.FormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Indentation & Tabs section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Indentation & Tabs"
 */
class IndentationTabsTest {

    @TempDir
    Path testProjectDir;

    // tabulation.char
    @Test
    void tabulationCharConvertsTabsToSpaces() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                \tpublic void method() {
                \t\tSystem.out.println("hello");
                \t}
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    public void method() {
                        System.out.println("hello");
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indentation.size
    @Test
    void indentationSizeUsesFourSpacesPerLevel() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                public void method() {
                if (true) {
                if (true) {
                System.out.println("hello");
                }
                }
                }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    public void method() {
                        if (true) {
                            if (true) {
                                System.out.println("hello");
                            }
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // continuation_indentation
    @Test
    void continuationIndentationIndentsByOneUnit() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int parameterOne, int parameterTwo, int parameterThree, int parameterFour, int paramFive) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(
                        int parameterOne,
                        int parameterTwo,
                        int parameterThree,
                        int parameterFour,
                        int paramFive) {
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // continuation_indentation_for_array_initializer
    @Test
    void continuationIndentationForArrayInitializerIndentsByOneUnit() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        int[] values = new int[] { 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000 };
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        int[] values = new int[] { 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000,
                            8000000 };
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_body_declarations_compare_to_type_header
    @Test
    void indentBodyDeclarationsCompareToTypeHeader() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                int field;
                void method() {
                }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int field;

                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_body_declarations_compare_to_enum_declaration_header
    @Test
    void indentBodyDeclarationsCompareToEnumDeclarationHeader() throws IOException {
        // language=Java
        String input = """
                public enum FormatterTest {
                VALUE_ONE,
                VALUE_TWO;
                int field;
                void method() {
                }
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE,
                    VALUE_TWO;

                    int field;

                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_body_declarations_compare_to_enum_constant_header
    @Test
    void indentBodyDeclarationsCompareToEnumConstantHeader() throws IOException {
        // language=Java
        String input = """
                public enum FormatterTest {
                VALUE_ONE {
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

    // indent_body_declarations_compare_to_annotation_declaration_header
    @Test
    void indentBodyDeclarationsCompareToAnnotationDeclarationHeader() throws IOException {
        // language=Java
        String input = """
                public @interface FormatterTest {
                String value();
                int count() default 0;
                }
                """;

        // language=Java
        String expected = """
                public @interface FormatterTest {
                    String value();

                    int count() default 0;
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_body_declarations_compare_to_record_header
    @Test
    void indentBodyDeclarationsCompareToRecordHeader() throws IOException {
        // language=Java
        String input = """
                public record FormatterTest(int x, int y) {
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

    // indent_statements_compare_to_block
    @Test
    void indentStatementsCompareToBlock() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                int x = 1;
                int y = 2;
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                            int x = 1;
                            int y = 2;
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_statements_compare_to_body
    @Test
    void indentStatementsCompareToBody() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                int x = 1;
                int y = 2;
                System.out.println(x + y);
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        int x = 1;
                        int y = 2;
                        System.out.println(x + y);
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // indent_switchstatements_compare_to_switch
    @Test
    void indentSwitchStatementsCompareToSwitch() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                            case 1:
                                break;
                            default:
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
                        default:
                            break;
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // tabulation.size
    @Test
    void tabulationSizeExpandsTabsToFourSpaces() throws IOException {
        // language=Java
        String input = """
                public class FormatterTest {
                \tpublic void method() {
                \t\tif (true) {
                \t\t\tSystem.out.println("hello");
                \t\t}
                \t}
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    public void method() {
                        if (true) {
                            System.out.println("hello");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(testProjectDir, input));
    }
}
