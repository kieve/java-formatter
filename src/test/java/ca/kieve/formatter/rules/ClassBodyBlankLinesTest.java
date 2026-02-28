package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassBodyBlankLinesTest extends FormatterRuleTestBase {
    @Test
    void removesBlankLineAfterClassBrace() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {

                    int x;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class Foo {
                    int x;
                }
                """;
                // @formatter:on

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesMultipleBlankLinesAfterClassBrace() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {


                    int x;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class Foo {
                    int x;
                }
                """;
                // @formatter:on

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesBlankLineAfterMethodBrace() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                    void bar() {

                        int x = 1;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class Foo {
                    void bar() {
                        int x = 1;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesBlankLineAfterNestedBraces() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {

                    static class Inner {

                        void bar() {

                            int x = 1;
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class Foo {
                    static class Inner {
                        void bar() {
                            int x = 1;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void preservesBlankLinesBetweenMembers() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                    int x;

                    int y;
                }
                """;
                // @formatter:on

        assertEquals(input, ClassBodyBlankLines.apply(input));
    }

    @Test
    void preservesSourceWithNoBlankLinesAfterBrace() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                    int x;
                }
                """;
                // @formatter:on

        assertEquals(input, ClassBodyBlankLines.apply(input));
    }

    @Test
    void handlesIndentedBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                \s\s\s\s
                    int x;
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class Foo {
                    int x;
                }
                """;
                // @formatter:on

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Override
    void respectsFormatterOffTags() {
        // language=Java
        // @formatter:off
        String input = """
                public class Foo {
                    // @formatter:off
                    public class Bar {

                        int x;
                    }
                    // @formatter:on
                }
                """;
                // @formatter:on

        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
