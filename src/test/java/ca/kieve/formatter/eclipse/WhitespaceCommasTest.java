package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Commas section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Commas"
 */
class WhitespaceCommasTest extends FormatterTestBase {
    WhitespaceCommasTest() {
        super("whitespace-commas/", s -> formatJava(s));
    }

    // insert_space_before_comma_in_method_invocation_arguments
    // insert_space_after_comma_in_method_invocation_arguments
    // insert_space_before_comma_in_method_declaration_parameters
    // insert_space_after_comma_in_method_declaration_parameters
    // insert_space_before_comma_in_constructor_declaration_parameters
    // insert_space_after_comma_in_constructor_declaration_parameters
    // insert_space_before_comma_in_allocation_expression
    // insert_space_after_comma_in_allocation_expression
    // insert_space_before_comma_in_array_initializer
    // insert_space_after_comma_in_array_initializer
    // insert_space_before_comma_in_enum_constant_arguments
    // insert_space_after_comma_in_enum_constant_arguments
    // insert_space_before_comma_in_enum_declarations
    // insert_space_after_comma_in_enum_declarations
    // insert_space_before_comma_in_for_increments
    // insert_space_after_comma_in_for_increments
    // insert_space_before_comma_in_for_inits
    // insert_space_after_comma_in_for_inits
    // insert_space_before_comma_in_multiple_field_declarations
    // insert_space_after_comma_in_multiple_field_declarations
    // insert_space_before_comma_in_multiple_local_declarations
    // insert_space_after_comma_in_multiple_local_declarations
    // insert_space_before_comma_in_annotation
    // insert_space_after_comma_in_annotation
    // insert_space_before_comma_in_record_components
    // insert_space_after_comma_in_record_components
    // insert_space_before_comma_in_switch_case_expressions
    // insert_space_after_comma_in_switch_case_expressions
    // insert_space_before_comma_in_type_arguments
    // insert_space_after_comma_in_type_arguments
    // insert_space_before_comma_in_type_parameters
    // insert_space_after_comma_in_type_parameters
    // insert_space_before_comma_in_parameterized_type_reference
    // insert_space_after_comma_in_parameterized_type_reference
    @Test
    void commaSpacing() throws IOException {
        test("comma-spacing-input.java", "comma-spacing-expected.java");
    }
}
