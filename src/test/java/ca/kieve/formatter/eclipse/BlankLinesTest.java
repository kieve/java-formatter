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
        String input = """
                \n\npackage com.example;

                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_after_package
    @Test
    void blankLinesAfterPackageEnsuresOneBlankLine() {
        // language=Java
        String input = """
                package com.example;
                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_imports
    @Test
    void blankLinesBeforeImportsEnsuresOneBlankLine() {
        // language=Java
        String input = """
                package com.example;
                import java.util.List;

                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import java.util.List;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_after_imports
    @Test
    void blankLinesAfterImportsEnsuresOneBlankLine() {
        // language=Java
        String input = """
                package com.example;

                import java.util.List;
                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import java.util.List;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_between_type_declarations
    @Test
    void blankLinesBetweenTypeDeclarationsEnsuresOneBlankLine() {
        // language=Java
        String input = """
                class FormatterTest {
                }
                class AnotherClass {
                }
                """;

        // language=Java
        String expected = """
                class FormatterTest {
                }

                class AnotherClass {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_member_type
    @Test
    void blankLinesBeforeMemberTypeEnsuresOneBlankLine() {
        // language=Java
        String input = """
                public class FormatterTest {
                    int field;
                    static class Inner {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int field;

                    static class Inner {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_first_class_body_declaration
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationDoesNotInsertBlank() {
        // language=Java
        String input = """
                public class FormatterTest {
                int field;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int field;
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_before_first_class_body_declaration — removes existing blank line
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationRemovesExistingBlank() {
        // language=Java
        String input = """
                public class FormatterTest {

                    int field;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int field;
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // blank_lines_after_last_class_body_declaration
    @Test
    void blankLinesAfterLastClassBodyDeclarationRemovesTrailingBlank() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                    }

                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
