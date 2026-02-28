package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Blank Lines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Blank Lines"
 */
class BlankLinesTest {
    // blank_lines_before_package
    @Test
    void blankLinesBeforePackageRemovesLeadingBlanks() {
        // language=Java
        // @formatter:off
        String input = """
                \n\npackage com.example;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_after_package
    @Test
    void blankLinesAfterPackageEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                package com.example;
                public class FormatterTest {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_imports
    @Test
    void blankLinesBeforeImportsEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                package com.example;
                import java.util.List;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                import java.util.List;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_after_imports
    @Test
    void blankLinesAfterImportsEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                package com.example;

                import java.util.List;
                public class FormatterTest {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                import java.util.List;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_between_type_declarations
    @Test
    void blankLinesBetweenTypeDeclarationsEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                class FormatterTest {
                }
                class AnotherClass {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                class FormatterTest {
                }

                class AnotherClass {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_member_type
    @Test
    void blankLinesBeforeMemberTypeEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int field;
                    static class Inner {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int field;

                    static class Inner {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_first_class_body_declaration
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationDoesNotInsertBlank() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
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

    // blank_lines_before_first_class_body_declaration — removes existing blank line
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationRemovesExistingBlank() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {

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

    // blank_lines_after_last_class_body_declaration
    @Test
    void blankLinesAfterLastClassBodyDeclarationRemovesTrailingBlank() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                    }

                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_method
    @Test
    void blankLinesBeforeMethodEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void methodOne() {
                    }
                    void methodTwo() {
                    }
                    void methodThree() {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void methodOne() {
                    }

                    void methodTwo() {
                    }

                    void methodThree() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_field
    @Test
    void blankLinesBeforeFieldDoesNotInsertBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int fieldOne;
                    int fieldTwo;
                    String fieldThree;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int fieldOne;
                    int fieldTwo;
                    String fieldThree;
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_abstract_method
    @Test
    void blankLinesBeforeAbstractMethodDoesNotInsertBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public abstract class FormatterTest {
                    abstract void methodOne();
                    abstract void methodTwo();
                    abstract void methodThree();
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public abstract class FormatterTest {
                    abstract void methodOne();
                    abstract void methodTwo();
                    abstract void methodThree();
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_new_chunk
    @Test
    void blankLinesBeforeNewChunkEnsuresOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int fieldOne;
                    int fieldTwo;
                    void methodOne() {
                    }
                    void methodTwo() {
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

                    void methodOne() {
                    }

                    void methodTwo() {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_between_import_groups
    @Test
    void blankLinesBetweenImportGroupsNormalizesToOneBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                package com.example;

                import java.util.List;
                import java.util.Map;



                import javax.swing.JFrame;
                import javax.swing.JPanel;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        // language=Java — custom import sorter groups javax/java together, javax first
        // @formatter:off
        String expected = """
                package com.example;

                import javax.swing.JFrame;
                import javax.swing.JPanel;
                import java.util.List;
                import java.util.Map;

                public class FormatterTest {
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_blank_lines_after_code_block (excluded)
    @Test
    void numberOfBlankLinesAfterCodeBlockDoesNotInsertBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                            System.out.println("inside if");
                        }
                        System.out.println("after if");
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
                            System.out.println("inside if");
                        }
                        System.out.println("after if");
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_blank_lines_after_code_block (excluded) — preserves existing blank
    // line
    @Test
    void numberOfBlankLinesAfterCodeBlockPreservesExistingBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                            System.out.println("inside if");
                        }

                        System.out.println("after if");
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
                            System.out.println("inside if");
                        }

                        System.out.println("after if");
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_blank_lines_before_code_block (excluded)
    @Test
    void numberOfBlankLinesBeforeCodeBlockDoesNotInsertBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        System.out.println("before if");
                        if (true) {
                            System.out.println("inside if");
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
                        System.out.println("before if");
                        if (true) {
                            System.out.println("inside if");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_blank_lines_before_code_block (excluded) — preserves existing blank line
    @Test
    void numberOfBlankLinesBeforeCodeBlockPreservesExistingBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        System.out.println("before if");

                        if (true) {
                            System.out.println("inside if");
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
                        System.out.println("before if");

                        if (true) {
                            System.out.println("inside if");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // number_of_blank_lines_at_beginning_of_code_block
    @Test
    void numberOfBlankLinesAtBeginningOfCodeBlockRemovesBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true) {

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

    // number_of_blank_lines_at_end_of_code_block
    @Test
    void numberOfBlankLinesAtEndOfCodeBlockRemovesBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        if (true) {
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

    // number_of_blank_lines_at_beginning_of_method_body
    @Test
    void numberOfBlankLinesAtBeginningOfMethodBodyRemovesBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {

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

    // number_of_blank_lines_at_end_of_method_body
    @Test
    void numberOfBlankLinesAtEndOfMethodBodyRemovesBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
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

    // blank_lines_between_statement_groups_in_switch
    @Test
    void blankLinesBetweenStatementGroupsInSwitchRemovesBlankLines() {
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
}
