package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Newlines / Annotations on Newlines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Annotations on Newlines"
 */
class NewlinesAnnotationsTest extends FormatterTestBase {
    NewlinesAnnotationsTest() {
        super("newlines-annotations/", s -> formatJava(s));
    }

    // insert_new_line_after_annotation_on_type
    @Test
    void insertNewLineAfterAnnotationOnTypeInsertsNewLine() throws IOException {
        test("on-type-input.java", "on-type-expected.java");
    }

    // insert_new_line_after_annotation_on_method
    @Test
    void insertNewLineAfterAnnotationOnMethodInsertsNewLine() throws IOException {
        test("on-method-input.java", "on-method-expected.java");
    }

    // insert_new_line_after_annotation_on_field
    @Test
    void insertNewLineAfterAnnotationOnFieldInsertsNewLine() throws IOException {
        test("on-field-input.java", "on-field-expected.java");
    }

    // insert_new_line_after_annotation_on_local_variable
    @Test
    void insertNewLineAfterAnnotationOnLocalVariableInsertsNewLine() throws IOException {
        test("on-local-variable-input.java", "on-local-variable-expected.java");
    }

    // insert_new_line_after_annotation_on_parameter
    @Test
    void insertNewLineAfterAnnotationOnParameterDoesNotInsert() throws IOException {
        test("on-parameter-unchanged.java");
    }

    // insert_new_line_after_annotation_on_package
    @Test
    void insertNewLineAfterAnnotationOnPackageInsertsNewLine() throws IOException {
        test("on-package-input.java", "on-package-expected.java");
    }

    // insert_new_line_after_annotation_on_enum_constant
    @Test
    void insertNewLineAfterAnnotationOnEnumConstantInsertsNewLine() throws IOException {
        test("on-enum-constant-input.java", "on-enum-constant-expected.java");
    }

    // insert_new_line_after_type_annotation
    @Test
    void insertNewLineAfterTypeAnnotationDoesNotInsert() throws IOException {
        test("type-annotation-unchanged.java");
    }
}
