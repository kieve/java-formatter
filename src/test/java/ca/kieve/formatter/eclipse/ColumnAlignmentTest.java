package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Column Alignment section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Column Alignment"
 */
class ColumnAlignmentTest {

    // align_type_members_on_columns
    @Test
    void alignTypeMembersOnColumnsDisabledRemovesAlignment() {
        // language=Java
        String input = """
                public class FormatterTest {
                    int    x        = 1;
                    String longName = "hello";
                    double d        = 3.14;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int x = 1;
                    String longName = "hello";
                    double d = 3.14;
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // align_variable_declarations_on_columns
    @Test
    void alignVariableDeclarationsOnColumnsDisabledRemovesAlignment() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        int    x        = 1;
                        String longName = "hello";
                        double d        = 3.14;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        int x = 1;
                        String longName = "hello";
                        double d = 3.14;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // align_assignment_statements_on_columns
    @Test
    void alignAssignmentStatementsOnColumnsDisabledRemovesAlignment() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        int x;
                        String longName;
                        double d;
                        x        = 1;
                        longName = "hello";
                        d        = 3.14;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        int x;
                        String longName;
                        double d;
                        x = 1;
                        longName = "hello";
                        d = 3.14;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // align_arrows_in_switch_on_columns
    @Test
    void alignArrowsInSwitchOnColumnsDisabledRemovesAlignment() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1     -> System.out.println("one");
                        case 2     -> System.out.println("two");
                        case 12345 -> System.out.println("large");
                        default    -> System.out.println("other");
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
                        case 12345 -> System.out.println("large");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // align_fields_grouping_blank_lines (excluded — N/A, alignment disabled)
    @Test
    void alignFieldsGroupingBlankLinesIsNotApplicableWithAlignmentDisabled() {
        // language=Java — blank lines between field groups do not trigger alignment
        String input = """
                public class FormatterTest {
                    int x = 1;
                    String longName = "hello";

                    double d = 3.14;
                    float f = 2.0f;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    int x = 1;
                    String longName = "hello";

                    double d = 3.14;
                    float f = 2.0f;
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
