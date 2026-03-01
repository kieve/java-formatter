package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Brace Positions section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Brace Positions"
 */
class BracePositionsTest extends FormatterTestBase {
    BracePositionsTest() {
        super("brace-positions/", s -> formatJava(s));
    }

    // brace_position_for_type_declaration
    @Test
    void bracePositionForTypeDeclarationIsEndOfLine() throws IOException {
        test("type-declaration-input.java", "type-declaration-expected.java");
    }

    // brace_position_for_method_declaration
    @Test
    void bracePositionForMethodDeclarationIsEndOfLine() throws IOException {
        test("method-declaration-input.java", "method-declaration-expected.java");
    }

    // brace_position_for_constructor_declaration
    @Test
    void bracePositionForConstructorDeclarationIsEndOfLine() throws IOException {
        test("constructor-declaration-input.java", "constructor-declaration-expected.java");
    }

    // brace_position_for_block
    @Test
    void bracePositionForBlockIsEndOfLine() throws IOException {
        test("block-input.java", "block-expected.java");
    }

    // brace_position_for_switch
    @Test
    void bracePositionForSwitchIsEndOfLine() throws IOException {
        test("switch-input.java", "switch-expected.java");
    }

    // brace_position_for_anonymous_type_declaration
    @Test
    void bracePositionForAnonymousTypeDeclarationIsEndOfLine() throws IOException {
        test("anonymous-type-input.java", "anonymous-type-expected.java");
    }

    // brace_position_for_array_initializer
    @Test
    void bracePositionForArrayInitializerIsEndOfLine() throws IOException {
        test("array-initializer-input.java", "array-initializer-expected.java");
    }

    // brace_position_for_enum_declaration
    @Test
    void bracePositionForEnumDeclarationIsEndOfLine() throws IOException {
        test("enum-declaration-input.java", "enum-declaration-expected.java");
    }

    // brace_position_for_enum_constant
    @Test
    void bracePositionForEnumConstantIsEndOfLine() throws IOException {
        test("enum-constant-input.java", "enum-constant-expected.java");
    }

    // brace_position_for_annotation_type_declaration
    @Test
    void bracePositionForAnnotationTypeDeclarationIsEndOfLine() throws IOException {
        test("annotation-type-input.java", "annotation-type-expected.java");
    }

    // brace_position_for_record_declaration
    @Test
    void bracePositionForRecordDeclarationIsEndOfLine() throws IOException {
        test("record-declaration-input.java", "record-declaration-expected.java");
    }

    // brace_position_for_record_constructor
    @Test
    void bracePositionForRecordConstructorIsEndOfLine() throws IOException {
        test("record-constructor-input.java", "record-constructor-expected.java");
    }

    // brace_position_for_lambda_body
    @Test
    void bracePositionForLambdaBodyIsEndOfLine() throws IOException {
        test("lambda-body-input.java", "lambda-body-expected.java");
    }

    // brace_position_for_block_in_case
    @Test
    void bracePositionForBlockInCaseIsEndOfLine() throws IOException {
        test("block-in-case-input.java", "block-in-case-expected.java");
    }

    // brace_position_for_block_in_case_after_arrow
    @Test
    void bracePositionForBlockInCaseAfterArrowIsEndOfLine() throws IOException {
        test("block-in-case-arrow-input.java", "block-in-case-arrow-expected.java");
    }
}
