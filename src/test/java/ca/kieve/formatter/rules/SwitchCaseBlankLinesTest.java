package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwitchCaseBlankLinesTest {

    @Test
    void removesBlankLineBeforeCase() {
        // language=Java
        String input = """
                switch (x) {
                case 1:
                    break;

                case 2:
                    break;
                }
                """;

        // language=Java
        String expected = """
                switch (x) {
                case 1:
                    break;
                case 2:
                    break;
                }
                """;

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void removesBlankLineBeforeDefault() {
        // language=Java
        String input = """
                switch (x) {
                case 1:
                    break;

                default:
                    break;
                }
                """;

        // language=Java
        String expected = """
                switch (x) {
                case 1:
                    break;
                default:
                    break;
                }
                """;

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void removesMultipleBlankLinesBeforeCase() {
        // language=Java
        String input = """
                switch (x) {
                case 1:
                    break;


                case 2:
                    break;
                }
                """;

        // language=Java
        String expected = """
                switch (x) {
                case 1:
                    break;
                case 2:
                    break;
                }
                """;

        assertEquals(expected, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void preservesNonSwitchBlankLines() {
        // language=Java
        String input = """
                public class Foo {
                    int x;

                    int y;
                }
                """;

        assertEquals(input, SwitchCaseBlankLines.apply(input));
    }

    @Test
    void preservesSwitchWithNoBlankLines() {
        // language=Java
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

        assertEquals(input, SwitchCaseBlankLines.apply(input));
    }
}
