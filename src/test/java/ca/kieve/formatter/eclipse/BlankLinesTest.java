package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Blank Lines section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Blank Lines"
 */
class BlankLinesTest extends FormatterTestBase {
    BlankLinesTest() {
        super("blank-lines/", s -> formatJava(s));
    }

    // blank_lines_before_package
    @Test
    void blankLinesBeforePackageRemovesLeadingBlanks() throws IOException {
        test("before-package-input.java", "before-package-expected.java");
    }

    // blank_lines_after_package
    @Test
    void blankLinesAfterPackageEnsuresOneBlankLine() throws IOException {
        test("after-package-input.java", "after-package-expected.java");
    }

    // blank_lines_before_imports
    @Test
    void blankLinesBeforeImportsEnsuresOneBlankLine() throws IOException {
        test("before-imports-input.java", "before-imports-expected.java");
    }

    // blank_lines_after_imports
    @Test
    void blankLinesAfterImportsEnsuresOneBlankLine() throws IOException {
        test("after-imports-input.java", "after-imports-expected.java");
    }

    // blank_lines_between_type_declarations
    @Test
    void blankLinesBetweenTypeDeclarationsEnsuresOneBlankLine() throws IOException {
        test("between-types-input.java", "between-types-expected.java");
    }

    // blank_lines_before_member_type
    @Test
    void blankLinesBeforeMemberTypeEnsuresOneBlankLine() throws IOException {
        test("before-member-type-input.java", "before-member-type-expected.java");
    }

    // blank_lines_before_first_class_body_declaration
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationDoesNotInsertBlank() throws IOException {
        test("first-body-no-blank-input.java", "first-body-no-blank-expected.java");
    }

    // blank_lines_before_first_class_body_declaration — removes existing blank line
    @Test
    void blankLinesBeforeFirstClassBodyDeclarationRemovesExistingBlank() throws IOException {
        test("first-body-removes-blank-input.java", "first-body-removes-blank-expected.java");
    }

    // blank_lines_after_last_class_body_declaration
    @Test
    void blankLinesAfterLastClassBodyDeclarationRemovesTrailingBlank() throws IOException {
        test("last-body-removes-blank-input.java", "last-body-removes-blank-expected.java");
    }

    // blank_lines_before_method
    @Test
    void blankLinesBeforeMethodEnsuresOneBlankLine() throws IOException {
        test("before-method-input.java", "before-method-expected.java");
    }

    // blank_lines_before_field
    @Test
    void blankLinesBeforeFieldDoesNotInsertBlankLine() throws IOException {
        test("before-field-unchanged.java");
    }

    // blank_lines_before_abstract_method
    @Test
    void blankLinesBeforeAbstractMethodDoesNotInsertBlankLine() throws IOException {
        test("before-abstract-method-unchanged.java");
    }

    // blank_lines_before_new_chunk
    @Test
    void blankLinesBeforeNewChunkEnsuresOneBlankLine() throws IOException {
        test("before-new-chunk-input.java", "before-new-chunk-expected.java");
    }

    // blank_lines_between_import_groups
    @Test
    void blankLinesBetweenImportGroupsNormalizesToOneBlankLine() throws IOException {
        test("import-groups-input.java", "import-groups-expected.java");
    }

    // number_of_blank_lines_after_code_block (excluded)
    @Test
    void numberOfBlankLinesAfterCodeBlockDoesNotInsertBlankLines() throws IOException {
        test("after-code-block-unchanged.java");
    }

    // number_of_blank_lines_after_code_block (excluded) — preserves existing blank
    // line
    @Test
    void numberOfBlankLinesAfterCodeBlockPreservesExistingBlankLine() throws IOException {
        test("after-code-block-preserves-unchanged.java");
    }

    // number_of_blank_lines_before_code_block (excluded)
    @Test
    void numberOfBlankLinesBeforeCodeBlockDoesNotInsertBlankLines() throws IOException {
        test("before-code-block-unchanged.java");
    }

    // number_of_blank_lines_before_code_block (excluded) — preserves existing blank line
    @Test
    void numberOfBlankLinesBeforeCodeBlockPreservesExistingBlankLine() throws IOException {
        test("before-code-block-preserves-unchanged.java");
    }

    // number_of_blank_lines_at_beginning_of_code_block
    @Test
    void numberOfBlankLinesAtBeginningOfCodeBlockRemovesBlankLines() throws IOException {
        test("beginning-of-code-block-input.java", "beginning-of-code-block-expected.java");
    }

    // number_of_blank_lines_at_end_of_code_block
    @Test
    void numberOfBlankLinesAtEndOfCodeBlockRemovesBlankLines() throws IOException {
        test("end-of-code-block-input.java", "end-of-code-block-expected.java");
    }

    // number_of_blank_lines_at_beginning_of_method_body
    @Test
    void numberOfBlankLinesAtBeginningOfMethodBodyRemovesBlankLines() throws IOException {
        test("beginning-of-method-body-input.java", "beginning-of-method-body-expected.java");
    }

    // number_of_blank_lines_at_end_of_method_body
    @Test
    void numberOfBlankLinesAtEndOfMethodBodyRemovesBlankLines() throws IOException {
        test("end-of-method-body-input.java", "end-of-method-body-expected.java");
    }

    // blank_lines_between_statement_groups_in_switch
    @Test
    void blankLinesBetweenStatementGroupsInSwitchRemovesBlankLines() throws IOException {
        test("switch-statement-groups-input.java", "switch-statement-groups-expected.java");
    }
}
