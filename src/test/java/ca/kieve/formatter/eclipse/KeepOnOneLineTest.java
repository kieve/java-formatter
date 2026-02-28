package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Keep On One Line section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Keep On One Line"
 */
class KeepOnOneLineTest {
    // keep_method_body_on_one_line
    @Test
    void keepMethodBodyOnOneLineNeverExpandsBody() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() { return; }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        return;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_simple_getter_setter_on_one_line
    @Test
    void keepSimpleGetterSetterOnOneLineNeverExpandsGetterSetter() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    private int value;

                    int getValue() { return value; }

                    void setValue(int value) { this.value = value; }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    private int value;

                    int getValue() {
                        return value;
                    }

                    void setValue(int value) {
                        this.value = value;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_code_block_on_one_line
    @Test
    void keepCodeBlockOnOneLineNeverExpandsBlock() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) { System.out.println("yes"); }
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

    // keep_lambda_body_block_on_one_line
    @Test
    void keepLambdaBodyBlockOnOneLineKeepsEmptyButExpandsNonEmpty() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        Runnable empty = () -> {};
                        Runnable nonEmpty = () -> { System.out.println("hello"); };
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        Runnable empty = () -> {};
                        Runnable nonEmpty = () -> {
                            System.out.println("hello");
                        };
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_loop_body_block_on_one_line
    @Test
    void keepLoopBodyBlockOnOneLineNeverExpandsLoop() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        for (int i = 0; i < 10; i++) { System.out.println(i); }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(i);
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_if_then_body_block_on_one_line
    @Test
    void keepIfThenBodyBlockOnOneLineNeverExpandsIfBody() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        if (x > 0) { x++; } else { x--; }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(int x) {
                        if (x > 0) {
                            x++;
                        } else {
                            x--;
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_type_declaration_on_one_line
    @Test
    void keepTypeDeclarationOnOneLineNeverExpandsType() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    static class Inner { int field; }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    static class Inner {
                        int field;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_annotation_declaration_on_one_line
    @Test
    void keepAnnotationDeclarationOnOneLineNeverExpandsAnnotation() {
        // language=Java
        // @formatter:off
        String input = """
                public @interface FormatterTest { String value(); }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public @interface FormatterTest {
                    String value();
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_anonymous_type_declaration_on_one_line
    @Test
    void keepAnonymousTypeDeclarationOnOneLineNeverExpandsAnonymous() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        Runnable r = new Runnable() { public void run() { System.out.println("hello"); } };
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        Runnable r = new Runnable() {
                            public void run() {
                                System.out.println("hello");
                            }
                        };
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_enum_declaration_on_one_line
    @Test
    void keepEnumDeclarationOnOneLineNeverExpandsEnum() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest { ONE, TWO, THREE }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public enum FormatterTest {
                    ONE,
                    TWO,
                    THREE
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_enum_constant_declaration_on_one_line
    @Test
    void keepEnumConstantDeclarationOnOneLineNeverExpandsEnumConstant() {
        // language=Java
        // @formatter:off
        String input = """
                public enum FormatterTest {
                    ONE { public String toString() { return "one"; } }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public enum FormatterTest {
                    ONE {
                        public String toString() {
                            return "one";
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_record_declaration_on_one_line
    @Test
    void keepRecordDeclarationOnOneLineNeverExpandsRecord() {
        // language=Java
        // @formatter:off
        String input = """
                public record FormatterTest(int x, int y) { public int sum() { return x + y; } }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public record FormatterTest(int x, int y) {
                    public int sum() {
                        return x + y;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // keep_record_constructor_on_one_line
    @Test
    void keepRecordConstructorOnOneLineNeverExpandsRecordConstructor() {
        // language=Java
        // @formatter:off
        String input = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest { if (x < 0) { throw new IllegalArgumentException(); } }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest {
                        if (x < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
