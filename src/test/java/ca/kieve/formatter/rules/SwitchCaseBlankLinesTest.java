package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwitchCaseBlankLinesTest extends FormatterRuleTestBase {
    @Test
    void removesBlankLineBeforeCase() {
        // language=Java
        // @formatter:off
        String input = """
                switch (x) {
                case 1:
                    break;

                case 2:
                    break;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                switch (x) {
                case 1:
                    break;
                case 2:
                    break;
                }
                """;
                // @formatter:on

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void removesBlankLineBeforeDefault() {
        // language=Java
        // @formatter:off
        String input = """
                switch (x) {
                case 1:
                    break;

                default:
                    break;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                switch (x) {
                case 1:
                    break;
                default:
                    break;
                }
                """;
                // @formatter:on

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void removesMultipleBlankLinesBeforeCase() {
        // language=Java
        // @formatter:off
        String input = """
                switch (x) {
                case 1:
                    break;


                case 2:
                    break;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                switch (x) {
                case 1:
                    break;
                case 2:
                    break;
                }
                """;
                // @formatter:on

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void preservesNonSwitchBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                    int x;

                    int y;
                }
                """;
                // @formatter:on

        assertEquals(input, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void preservesSwitchWithNoBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                switch (x) {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
                }
                """;
                // @formatter:on

        assertEquals(input, SwitchCaseBlankLines.apply(input));
    }

    @Override
    void respectsFormatterOffTags() {
        // language=Java
        // @formatter:off
        String input = """
                // @formatter:off
                switch (x) {
                case 1:
                    break;

                case 2:
                    break;
                }
                // @formatter:on
                """;
                // @formatter:on

        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
