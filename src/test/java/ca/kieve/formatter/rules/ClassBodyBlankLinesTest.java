package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassBodyBlankLinesTest {

    @Test
    void removesBlankLineAfterClassBrace() {
        // language=Java
        String input = """
                public class Foo {

                    int x;
                }
                """;

        // language=Java
        String expected = """
                public class Foo {
                    int x;
                }
                """;

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesMultipleBlankLinesAfterClassBrace() {
        // language=Java
        String input = """
                public class Foo {


                    int x;
                }
                """;

        // language=Java
        String expected = """
                public class Foo {
                    int x;
                }
                """;

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesBlankLineAfterMethodBrace() {
        // language=Java
        String input = """
                public class Foo {
                    void bar() {

                        int x = 1;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class Foo {
                    void bar() {
                        int x = 1;
                    }
                }
                """;

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void removesBlankLineAfterNestedBraces() {
        // language=Java
        String input = """
                public class Foo {

                    static class Inner {

                        void bar() {

                            int x = 1;
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class Foo {
                    static class Inner {
                        void bar() {
                            int x = 1;
                        }
                    }
                }
                """;

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }

    @Test
    void preservesBlankLinesBetweenMembers() {
        // language=Java
        String input = """
                public class Foo {
                    int x;

                    int y;
                }
                """;

        assertEquals(input, ClassBodyBlankLines.apply(input));
    }

    @Test
    void preservesSourceWithNoBlankLinesAfterBrace() {
        // language=Java
        String input = """
                public class Foo {
                    int x;
                }
                """;

        assertEquals(input, ClassBodyBlankLines.apply(input));
    }

    @Test
    void handlesIndentedBlankLines() {
        // language=Java
        String input = """
                public class Foo {
                \s\s\s\s
                    int x;
                }
                """;

        // language=Java
        String expected = """
                public class Foo {
                    int x;
                }
                """;

        assertEquals(expected, ClassBodyBlankLines.apply(input));
    }
}
