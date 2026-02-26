package ca.kieve.formatter.eclipse;

import ca.kieve.formatter.FormatterTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

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
        String input = """
                public class Test {
                \tpublic void method() {
                \t\tSystem.out.println("hello");
                \t}
                }
                """;

        String expected = """
                public class Test {
                    public void method() {
                        System.out.println("hello");
                    }
                }
                """;

        assertEquals(expected, FormatterTestUtil.formatJava(testProjectDir, "Test", input));
    }
}
