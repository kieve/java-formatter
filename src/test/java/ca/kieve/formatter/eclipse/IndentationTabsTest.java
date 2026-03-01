package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for Eclipse JDT Formatter — Indentation & Tabs section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Indentation & Tabs"
 */
class IndentationTabsTest extends FormatterTestBase {
    IndentationTabsTest() {
        super("indentation-tabs/", s -> formatJava(s));
    }

    // tabulation.char
    @Test
    void tabulationCharConvertsTabsToSpaces() throws IOException {
        test("tabs-to-spaces-input.java", "tabs-to-spaces-expected.java");
    }

    // indentation.size
    @Test
    void indentationSizeUsesFourSpacesPerLevel() throws IOException {
        test("indentation-size-input.java", "indentation-size-expected.java");
    }

    // continuation_indentation
    @Test
    void continuationIndentationIndentsByOneUnit() throws IOException {
        test("continuation-indent-input.java", "continuation-indent-expected.java");
    }

    // continuation_indentation_for_array_initializer
    @Test
    void continuationIndentationForArrayInitializerIndentsByOneUnit() throws IOException {
        test("continuation-array-input.java", "continuation-array-expected.java");
    }

    // indent_body_declarations_compare_to_type_header
    @Test
    void indentBodyDeclarationsCompareToTypeHeader() throws IOException {
        test("body-vs-type-header-input.java", "body-vs-type-header-expected.java");
    }

    // indent_body_declarations_compare_to_enum_declaration_header
    @Test
    void indentBodyDeclarationsCompareToEnumDeclarationHeader() throws IOException {
        test("body-vs-enum-header-input.java", "body-vs-enum-header-expected.java");
    }

    // indent_body_declarations_compare_to_enum_constant_header
    @Test
    void indentBodyDeclarationsCompareToEnumConstantHeader() throws IOException {
        test("body-vs-enum-constant-input.java", "body-vs-enum-constant-expected.java");
    }

    // indent_body_declarations_compare_to_annotation_declaration_header
    @Test
    void indentBodyDeclarationsCompareToAnnotationDeclarationHeader() throws IOException {
        test("body-vs-annotation-input.java", "body-vs-annotation-expected.java");
    }

    // indent_body_declarations_compare_to_record_header
    @Test
    void indentBodyDeclarationsCompareToRecordHeader() throws IOException {
        test("body-vs-record-input.java", "body-vs-record-expected.java");
    }

    // indent_statements_compare_to_block
    @Test
    void indentStatementsCompareToBlock() throws IOException {
        test("statements-vs-block-input.java", "statements-vs-block-expected.java");
    }

    // indent_statements_compare_to_body
    @Test
    void indentStatementsCompareToBody() throws IOException {
        test("statements-vs-body-input.java", "statements-vs-body-expected.java");
    }

    // indent_switchstatements_compare_to_switch
    @Test
    void indentSwitchStatementsCompareToSwitch() throws IOException {
        test("switch-vs-switch-input.java", "switch-vs-switch-expected.java");
    }

    // indent_switchstatements_compare_to_cases
    @Test
    void indentSwitchStatementsCompareToCases() throws IOException {
        test("switch-vs-cases-input.java", "switch-vs-cases-expected.java");
    }

    // indent_breaks_compare_to_cases
    @Test
    void indentBreaksCompareToCases() throws IOException {
        test("breaks-vs-cases-input.java", "breaks-vs-cases-expected.java");
    }

    // indent_empty_lines
    @Test
    void indentEmptyLinesDoesNotIndentBlankLines() throws IOException {
        test("indent-empty-lines-input.java", "indent-empty-lines-expected.java");
    }

    // use_tabs_only_for_leading_indentation (excluded — N/A with space indentation)
    @Test
    void useTabsOnlyForLeadingIndentationIsNotApplicableWithSpaces() throws IOException {
        String input = loadFixture("indentation-tabs/tabs-only-leading-input.java");
        String result = formatJava(input);
        assertFalse(
            result.contains("\t"),
            "Output should contain no tab characters with space indentation");
    }

    // tabulation.size
    @Test
    void tabulationSizeExpandsTabsToFourSpaces() throws IOException {
        test("tab-size-input.java", "tab-size-expected.java");
    }
}
