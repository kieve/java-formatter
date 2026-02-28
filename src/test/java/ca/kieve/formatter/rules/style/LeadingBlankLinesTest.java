package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadingBlankLinesTest extends FormatterRuleTestBase {
    @Test
    void stripsMultipleLeadingBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                \n\n\npackage com.example;

                public class Foo {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                public class Foo {
                }
                """;
                // @formatter:on

        assertEquals(expected, LeadingBlankLines.apply(input));
    }

    @Test
    void stripsSingleLeadingBlankLine() {
        // language=Java
        // @formatter:off
        String input = """
                \npackage com.example;

                public class Foo {
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                public class Foo {
                }
                """;
                // @formatter:on

        assertEquals(expected, LeadingBlankLines.apply(input));
    }

    @Test
    void preservesSourceWithNoLeadingBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                package com.example;

                public class Foo {
                }
                """;
                // @formatter:on

        assertEquals(input, LeadingBlankLines.apply(input));
    }

    @Test
    void preservesInternalBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                \n\npackage com.example;

                public class Foo {

                    int x;

                    int y;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                package com.example;

                public class Foo {

                    int x;

                    int y;
                }
                """;
                // @formatter:on

        assertEquals(expected, LeadingBlankLines.apply(input));
    }

    @Override
    void respectsFormatterOffTags() {
        // language=Java
        // @formatter:off
        String input = """
                // @formatter:off

                package com.example;
                // @formatter:on

                public class Foo {
                }
                """;
                // @formatter:on

        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
