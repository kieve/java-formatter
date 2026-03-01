package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Column Alignment section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Column Alignment"
 */
class ColumnAlignmentTest extends FormatterTestBase {
    ColumnAlignmentTest() {
        super("column-alignment/", s -> formatJava(s));
    }

    // align_type_members_on_columns
    @Test
    void alignTypeMembersOnColumnsDisabledRemovesAlignment() throws IOException {
        test("type-members-input.java", "type-members-expected.java");
    }

    // align_variable_declarations_on_columns
    @Test
    void alignVariableDeclarationsOnColumnsDisabledRemovesAlignment() throws IOException {
        test("variable-declarations-input.java", "variable-declarations-expected.java");
    }

    // align_assignment_statements_on_columns
    @Test
    void alignAssignmentStatementsOnColumnsDisabledRemovesAlignment() throws IOException {
        test("assignment-statements-input.java", "assignment-statements-expected.java");
    }

    // align_arrows_in_switch_on_columns
    @Test
    void alignArrowsInSwitchOnColumnsDisabledRemovesAlignment() throws IOException {
        test("switch-arrows-input.java", "switch-arrows-expected.java");
    }

    // align_fields_grouping_blank_lines (excluded — N/A, alignment disabled)
    @Test
    void alignFieldsGroupingBlankLinesIsNotApplicableWithAlignmentDisabled() throws IOException {
        test("fields-grouping-unchanged.java");
    }
}
