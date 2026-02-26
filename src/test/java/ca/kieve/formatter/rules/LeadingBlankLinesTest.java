package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadingBlankLinesTest {

    @Test
    void stripsMultipleLeadingBlankLines() {
        // language=Java
        String input = """
                \n\n\npackage com.example;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                public class Foo {
                }
                """;

        assertEquals(expected, LeadingBlankLines.apply(input));
    }

    @Test
    void stripsSingleLeadingBlankLine() {
        // language=Java
        String input = """
                \npackage com.example;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                public class Foo {
                }
                """;

        assertEquals(expected, LeadingBlankLines.apply(input));
    }

    @Test
    void preservesSourceWithNoLeadingBlankLines() {
        // language=Java
        String input = """
                package com.example;

                public class Foo {
                }
                """;

        assertEquals(input, LeadingBlankLines.apply(input));
    }

    @Test
    void preservesInternalBlankLines() {
        // language=Java
        String input = """
                \n\npackage com.example;

                public class Foo {

                    int x;

                    int y;
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                public class Foo {

                    int x;

                    int y;
                }
                """;

        assertEquals(expected, LeadingBlankLines.apply(input));
    }
}
