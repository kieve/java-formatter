package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for Eclipse JDT Formatter — Indentation & Tabs section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Indentation & Tabs"
 */
class IndentationTabsTest {
    // tabulation.char
    @Test
    void tabulationCharConvertsTabsToSpaces() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                \tpublic void method() {
                \t\tSystem.out.println("hello");
                \t}
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    public void method() {
                        System.out.println("hello");
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indentation.size
    @Test
    void indentationSizeUsesFourSpacesPerLevel() {
        // language=Java
        // @formatter:off
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
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // continuation_indentation
    @Test
    void continuationIndentationIndentsByOneUnit() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int parameterOne, int parameterTwo, int parameterThree, int parameterFour, int paramFive) {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // continuation_indentation_for_array_initializer
    @Test
    void continuationIndentationForArrayInitializerIndentsByOneUnit() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int[] values = new int[] { 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000 };
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int[] values = new int[] { 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000,
                            8000000 };
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_body_declarations_compare_to_type_header
    @Test
    void indentBodyDeclarationsCompareToTypeHeader() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                int field;
                void method() {
                }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int field;

                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_body_declarations_compare_to_enum_declaration_header
    @Test
    void indentBodyDeclarationsCompareToEnumDeclarationHeader() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest {
                VALUE_ONE,
                VALUE_TWO;
                int field;
                void method() {
                }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE,
                    VALUE_TWO;

                    int field;

                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_body_declarations_compare_to_enum_constant_header
    @Test
    void indentBodyDeclarationsCompareToEnumConstantHeader() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest {
                VALUE_ONE {
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

    // indent_body_declarations_compare_to_annotation_declaration_header
    @Test
    void indentBodyDeclarationsCompareToAnnotationDeclarationHeader() {
        // language=Java
        // @formatter:off
        String input = """
                public @interface FormatterTest {
                String value();
                int count() default 0;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public @interface FormatterTest {
                    String value();

                    int count() default 0;
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_body_declarations_compare_to_record_header
    @Test
    void indentBodyDeclarationsCompareToRecordHeader() {
        // language=Java
        // @formatter:off
        String input = """
                public record FormatterTest(int x, int y) {
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

    // indent_statements_compare_to_block
    @Test
    void indentStatementsCompareToBlock() {
        // language=Java
        // @formatter:off
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
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_statements_compare_to_body
    @Test
    void indentStatementsCompareToBody() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                int x = 1;
                int y = 2;
                System.out.println(x + y);
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int x = 1;
                        int y = 2;
                        System.out.println(x + y);
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_switchstatements_compare_to_switch
    @Test
    void indentSwitchStatementsCompareToSwitch() {
        // language=Java
        // @formatter:off
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
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_switchstatements_compare_to_cases
    @Test
    void indentSwitchStatementsCompareToCases() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1:
                        System.out.println("one");
                        break;
                        default:
                        System.out.println("other");
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
                            System.out.println("one");
                            break;
                        default:
                            System.out.println("other");
                            break;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_breaks_compare_to_cases
    @Test
    void indentBreaksCompareToCases() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1:
                            System.out.println("one");
                        break;
                        case 2:
                            System.out.println("two");
                        break;
                        default:
                            System.out.println("other");
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
                            System.out.println("one");
                            break;
                        case 2:
                            System.out.println("two");
                            break;
                        default:
                            System.out.println("other");
                            break;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // indent_empty_lines
    @Test
    void indentEmptyLinesDoesNotIndentBlankLines() {
        // language=Java — \s preserves trailing spaces on blank lines
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int fieldOne;
                \s\s\s\s
                    int fieldTwo;
                \s\s\s\s
                    void method() {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int fieldOne;

                    int fieldTwo;

                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // use_tabs_only_for_leading_indentation (excluded — N/A with space indentation)
    @Test
    void useTabsOnlyForLeadingIndentationIsNotApplicableWithSpaces() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                \tvoid method() {
                \t\tSystem.out.println("hello");
                \t}
                }
                """;
                // @formatter:on

        String result = formatJava(input);
        assertFalse(
            result.contains("\t"),
            "Output should contain no tab characters with space indentation");
    }

    // tabulation.size
    @Test
    void tabulationSizeExpandsTabsToFourSpaces() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                \tpublic void method() {
                \t\tif (true) {
                \t\t\tSystem.out.println("hello");
                \t\t}
                \t}
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    public void method() {
                        if (true) {
                            System.out.println("hello");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
