package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Colons & Semicolons section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Colons & Semicolons"
 */
class WhitespaceColonsSemicolonsTest {

    // insert_space_before_colon_in_for
    // insert_space_after_colon_in_for
    // insert_space_before_semicolon
    // insert_space_before_colon_in_assert
    // insert_space_after_colon_in_assert
    // insert_space_before_colon_in_case
    // insert_space_after_colon_in_case
    // insert_space_before_colon_in_default
    // insert_space_after_colon_in_default
    // insert_space_before_colon_in_conditional
    // insert_space_after_colon_in_conditional
    // insert_space_before_colon_in_labeled_statement
    // insert_space_after_colon_in_labeled_statement
    // insert_space_before_semicolon_in_for
    // insert_space_before_semicolon_in_try_with_resources
    @Test
    void colonAndSemicolonSpacing() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        String[] list = { "a", "b" };
                        for (String s:list) {
                        }

                        assert true:"message";

                        int x = 1;
                        switch (x) {
                        case 1 :
                            break;
                        default :
                            break;
                        }

                        int y = true ? 1:2;

                        outer :
                        for (int i = 0; i < 10; i++) {
                            break outer;
                        }

                        int z = 1 ;

                        for (int i = 0 ; i < 10 ; i++) {
                        }

                        try (AutoCloseable r1 = null ; AutoCloseable r2 = null) {
                        } catch (Exception e) {
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        String[] list = { "a", "b" };
                        for (String s : list) {
                        }

                        assert true : "message";

                        int x = 1;
                        switch (x) {
                        case 1:
                            break;
                        default:
                            break;
                        }

                        int y = true ? 1 : 2;

                        outer:
                        for (int i = 0; i < 10; i++) {
                            break outer;
                        }

                        int z = 1;

                        for (int i = 0; i < 10; i++) {
                        }

                        try (AutoCloseable r1 = null; AutoCloseable r2 = null) {
                        } catch (Exception e) {
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
