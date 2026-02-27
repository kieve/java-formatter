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
        String input = """
                public class FormatterTest {
                    void method() { return; }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        return;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_simple_getter_setter_on_one_line
    @Test
    void keepSimpleGetterSetterOnOneLineNeverExpandsGetterSetter() {
        // language=Java
        String input = """
                public class FormatterTest {
                    private int value;

                    int getValue() { return value; }

                    void setValue(int value) { this.value = value; }
                }
                """;

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }

    // keep_code_block_on_one_line
    @Test
    void keepCodeBlockOnOneLineNeverExpandsBlock() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) { System.out.println("yes"); }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(boolean flag) {
                        if (flag) {
                            System.out.println("yes");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_lambda_body_block_on_one_line
    @Test
    void keepLambdaBodyBlockOnOneLineKeepsEmptyButExpandsNonEmpty() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        Runnable empty = () -> {};
                        Runnable nonEmpty = () -> { System.out.println("hello"); };
                    }
                }
                """;

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }

    // keep_loop_body_block_on_one_line
    @Test
    void keepLoopBodyBlockOnOneLineNeverExpandsLoop() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        for (int i = 0; i < 10; i++) { System.out.println(i); }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(i);
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_if_then_body_block_on_one_line
    @Test
    void keepIfThenBodyBlockOnOneLineNeverExpandsIfBody() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        if (x > 0) { x++; } else { x--; }
                    }
                }
                """;

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }

    // keep_type_declaration_on_one_line
    @Test
    void keepTypeDeclarationOnOneLineNeverExpandsType() {
        // language=Java
        String input = """
                public class FormatterTest {
                    static class Inner { int field; }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    static class Inner {
                        int field;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_annotation_declaration_on_one_line
    @Test
    void keepAnnotationDeclarationOnOneLineNeverExpandsAnnotation() {
        // language=Java
        String input = """
                public @interface FormatterTest { String value(); }
                """;

        // language=Java
        String expected = """
                public @interface FormatterTest {
                    String value();
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_anonymous_type_declaration_on_one_line
    @Test
    void keepAnonymousTypeDeclarationOnOneLineNeverExpandsAnonymous() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        Runnable r = new Runnable() { public void run() { System.out.println("hello"); } };
                    }
                }
                """;

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }

    // keep_enum_declaration_on_one_line
    @Test
    void keepEnumDeclarationOnOneLineNeverExpandsEnum() {
        // language=Java
        String input = """
                public enum FormatterTest { ONE, TWO, THREE }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    ONE,
                    TWO,
                    THREE
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_enum_constant_declaration_on_one_line
    @Test
    void keepEnumConstantDeclarationOnOneLineNeverExpandsEnumConstant() {
        // language=Java
        String input = """
                public enum FormatterTest {
                    ONE { public String toString() { return "one"; } }
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    ONE {
                        public String toString() {
                            return "one";
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_record_declaration_on_one_line
    @Test
    void keepRecordDeclarationOnOneLineNeverExpandsRecord() {
        // language=Java
        String input = """
                public record FormatterTest(int x, int y) { public int sum() { return x + y; } }
                """;

        // language=Java
        String expected = """
                public record FormatterTest(int x, int y) {
                    public int sum() {
                        return x + y;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // keep_record_constructor_on_one_line
    @Test
    void keepRecordConstructorOnOneLineNeverExpandsRecordConstructor() {
        // language=Java
        String input = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest { if (x < 0) { throw new IllegalArgumentException(); } }
                }
                """;

        // language=Java
        String expected = """
                public record FormatterTest(int x, int y) {
                    public FormatterTest {
                        if (x < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
