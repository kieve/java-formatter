package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Eclipse JDT Formatter — Newlines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Newlines"
 */
class NewlinesTest {
    // insert_new_line_at_end_of_file_if_missing
    @Test
    void insertNewLineAtEndOfFileIfMissing() {
        // language=Java — no trailing newline
        String input = "public class FormatterTest {\n}";

        String result = formatJava(input);
        assertTrue(result.endsWith("\n"), "Output should end with a newline");
    }

    // keep_else_statement_on_same_line (excluded — N/A, braces always required)
    @Test
    void keepElseStatementOnSameLineIsNotApplicableWithBraces() {
        // language=Java — else on its own line gets moved up
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        }
                        else {
                            System.out.println("no");
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        } else {
                            System.out.println("no");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_then_statement_on_same_line (excluded — N/A, braces always required)
    @Test
    void keepThenStatementOnSameLineIsNotApplicableWithBraces() {
        // language=Java — with braces, the then body is always on its own line
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_before_else_in_if_statement
    @Test
    void insertNewLineBeforeElseInIfStatementDoesNotInsert() {
        // language=Java — else on its own line gets pulled up to } else {
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        }
                        else {
                            System.out.println("no");
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        } else {
                            System.out.println("no");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_before_catch_in_try_statement
    @Test
    void insertNewLineBeforeCatchInTryStatementDoesNotInsert() {
        // language=Java — catch on its own line gets pulled up to } catch {
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        try {
                            System.out.println("try");
                        }
                        catch (Exception e) {
                            System.out.println("catch");
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
                        try {
                            System.out.println("try");
                        } catch (Exception e) {
                            System.out.println("catch");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_before_finally_in_try_statement
    @Test
    void insertNewLineBeforeFinallyInTryStatementDoesNotInsert() {
        // language=Java — finally on its own line gets pulled up to } finally {
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        try {
                            System.out.println("try");
                        } catch (Exception e) {
                            System.out.println("catch");
                        }
                        finally {
                            System.out.println("finally");
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
                        try {
                            System.out.println("try");
                        } catch (Exception e) {
                            System.out.println("catch");
                        } finally {
                            System.out.println("finally");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_before_while_in_do_statement
    @Test
    void insertNewLineBeforeWhileInDoStatementDoesNotInsert() {
        // language=Java — while on its own line gets pulled up to } while
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        do {
                            System.out.println("loop");
                        }
                        while (false);
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        do {
                            System.out.println("loop");
                        } while (false);
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_opening_brace_in_array_initializer (removed — leave to author)
    // insert_new_line_before_closing_brace_in_array_initializer (removed — leave to author)
    @Test
    void arrayInitializerPreservesSingleLineLayout() {
        // language=Java — single-line layout is preserved
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int[] values = { 1, 2, 3 };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int[] values = { 1, 2, 3 };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_opening_brace_in_array_initializer (removed — leave to author)
    // insert_new_line_before_closing_brace_in_array_initializer (removed — leave to author)
    @Test
    void arrayInitializerPreservesMultiLineLayout() {
        // language=Java — multi-line layout is preserved
        // @formatter:off
        String input = """
                public class FormatterTest {
                    int[] values = {
                        1, 2, 3
                    };
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int[] values = {
                        1, 2, 3
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // insert_new_line_after_label
    @Test
    void insertNewLineAfterLabelInsertsNewLine() {
        // language=Java — statement after label gets moved to its own line
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        outer: for (int i = 0; i < 10; i++) {
                            break outer;
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
                        outer:
                        for (int i = 0; i < 10; i++) {
                            break outer;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // compact_else_if
    @Test
    void compactElseIfTreatsAsOneUnit() {
        // language=Java — split else / if gets compacted to else if
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        if (x == 1) {
                            System.out.println("one");
                        }
                        else
                        if (x == 2) {
                            System.out.println("two");
                        }
                        else {
                            System.out.println("other");
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
                        if (x == 1) {
                            System.out.println("one");
                        } else if (x == 2) {
                            System.out.println("two");
                        } else {
                            System.out.println("other");
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // put_empty_statement_on_new_line
    @Test
    void putEmptyStatementOnNewLineKeepsOnSameLine() {
        // language=Java — empty statement on its own line gets pulled up
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int x = 1;
                        ;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int x = 1;;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
