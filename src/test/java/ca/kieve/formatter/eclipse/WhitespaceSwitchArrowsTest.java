package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Switch Arrows section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Switch Arrows"
 */
class WhitespaceSwitchArrowsTest {

    // insert_space_before_arrow_in_switch_case
    // insert_space_after_arrow_in_switch_case
    // insert_space_before_arrow_in_switch_default
    // insert_space_after_arrow_in_switch_default
    @Test
    void switchArrowSpacing() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1->System.out.println("one");
                        case 2->System.out.println("two");
                        default->System.out.println("other");
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1 -> System.out.println("one");
                        case 2 -> System.out.println("two");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
