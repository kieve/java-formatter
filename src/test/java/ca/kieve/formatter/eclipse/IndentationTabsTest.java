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
