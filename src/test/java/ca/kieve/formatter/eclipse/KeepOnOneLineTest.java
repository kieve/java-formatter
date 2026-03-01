package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Keep On One Line section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Keep On One Line"
 */
class KeepOnOneLineTest extends FormatterTestBase {
    KeepOnOneLineTest() {
        super("keep-on-one-line/", s -> formatJava(s));
    }

    // keep_method_body_on_one_line
    @Test
    void keepMethodBodyOnOneLineNeverExpandsBody() throws IOException {
        test("method-body-input.java", "method-body-expected.java");
    }

    // keep_simple_getter_setter_on_one_line
    @Test
    void keepSimpleGetterSetterOnOneLineNeverExpandsGetterSetter() throws IOException {
        test("getter-setter-input.java", "getter-setter-expected.java");
    }

    // keep_code_block_on_one_line
    @Test
    void keepCodeBlockOnOneLineNeverExpandsBlock() throws IOException {
        test("code-block-input.java", "code-block-expected.java");
    }

    // keep_lambda_body_block_on_one_line
    @Test
    void keepLambdaBodyBlockOnOneLineKeepsEmptyButExpandsNonEmpty() throws IOException {
        test("lambda-body-input.java", "lambda-body-expected.java");
    }

    // keep_loop_body_block_on_one_line
    @Test
    void keepLoopBodyBlockOnOneLineNeverExpandsLoop() throws IOException {
        test("loop-body-input.java", "loop-body-expected.java");
    }

    // keep_if_then_body_block_on_one_line
    @Test
    void keepIfThenBodyBlockOnOneLineNeverExpandsIfBody() throws IOException {
        test("if-then-body-input.java", "if-then-body-expected.java");
    }

    // keep_type_declaration_on_one_line
    @Test
    void keepTypeDeclarationOnOneLineNeverExpandsType() throws IOException {
        test("type-declaration-input.java", "type-declaration-expected.java");
    }

    // keep_annotation_declaration_on_one_line
    @Test
    void keepAnnotationDeclarationOnOneLineNeverExpandsAnnotation() throws IOException {
        test("annotation-declaration-input.java", "annotation-declaration-expected.java");
    }

    // keep_anonymous_type_declaration_on_one_line
    @Test
    void keepAnonymousTypeDeclarationOnOneLineNeverExpandsAnonymous() throws IOException {
        test("anonymous-type-input.java", "anonymous-type-expected.java");
    }

    // keep_enum_declaration_on_one_line
    @Test
    void keepEnumDeclarationOnOneLineNeverExpandsEnum() throws IOException {
        test("enum-declaration-input.java", "enum-declaration-expected.java");
    }

    // keep_enum_constant_declaration_on_one_line
    @Test
    void keepEnumConstantDeclarationOnOneLineNeverExpandsEnumConstant() throws IOException {
        test("enum-constant-input.java", "enum-constant-expected.java");
    }

    // keep_record_declaration_on_one_line
    @Test
    void keepRecordDeclarationOnOneLineNeverExpandsRecord() throws IOException {
        test("record-declaration-input.java", "record-declaration-expected.java");
    }

    // keep_record_constructor_on_one_line
    @Test
    void keepRecordConstructorOnOneLineNeverExpandsRecordConstructor() throws IOException {
        test("record-constructor-input.java", "record-constructor-expected.java");
    }
}
