package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ca.kieve.formatter.FormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Blank Lines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Blank Lines"
 */
class BlankLinesTest {

    @TempDir
    Path testProjectDir;

    // blank_lines_before_package
    @Test
    void blankLinesBeforePackageRemovesLeadingBlanks() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // blank_lines_after_package
    @Test
    void blankLinesAfterPackageEnsuresOneBlankLine() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // blank_lines_before_imports
    @Test
    void blankLinesBeforeImportsEnsuresOneBlankLine() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // blank_lines_after_imports
    @Test
    void blankLinesAfterImportsEnsuresOneBlankLine() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // blank_lines_between_type_declarations
    @Test
    void blankLinesBetweenTypeDeclarationsEnsuresOneBlankLine() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }

    // blank_lines_before_member_type
    @Test
    void blankLinesBeforeMemberTypeEnsuresOneBlankLine() throws IOException {
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

        assertEquals(expected, formatJava(testProjectDir, input));
    }
}
