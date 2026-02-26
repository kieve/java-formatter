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
}
