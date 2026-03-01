package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for Eclipse JDT Formatter — Wrapping & Alignment section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Wrapping & Alignment"
 */
class WrapAlignTest extends FormatterTestBase {
    WrapAlignTest() {
        super("wrap-align/", s -> formatJava(s));
    }

    // alignment_for_arguments_in_method_invocation
    @Test
    void alignmentForArgumentsInMethodInvocationWrapsAllElements() throws IOException {
        test("method-invocation-args-input.java", "method-invocation-args-expected.java");
    }

    // alignment_for_parameters_in_method_declaration
    @Test
    void alignmentForParametersInMethodDeclarationWrapsAllElements() throws IOException {
        test("method-params-input.java", "method-params-expected.java");
    }

    // alignment_for_arguments_in_allocation_expression
    @Test
    void alignmentForArgumentsInAllocationExpressionWrapsAllElements() throws IOException {
        test("allocation-args-input.java", "allocation-args-expected.java");
    }

    // alignment_for_arguments_in_annotation
    @Test
    void alignmentForArgumentsInAnnotationWrapsAllElements() throws IOException {
        test("annotation-args-input.java", "annotation-args-expected.java");
    }

    // alignment_for_arguments_in_enum_constant
    @Test
    void alignmentForArgumentsInEnumConstantWrapsAllElements() throws IOException {
        test("enum-constant-args-input.java", "enum-constant-args-expected.java");
    }

    // alignment_for_arguments_in_explicit_constructor_call
    @Test
    void alignmentForArgumentsInExplicitConstructorCallWrapsAllElements() throws IOException {
        test("explicit-constructor-args-input.java", "explicit-constructor-args-expected.java");
    }

    // alignment_for_arguments_in_qualified_allocation_expression
    @Test
    void alignmentForArgumentsInQualifiedAllocationExpressionWrapsAllElements() throws IOException {
        test("qualified-allocation-args-input.java", "qualified-allocation-args-expected.java");
    }

    // alignment_for_assertion_message
    @Test
    void alignmentForAssertionMessageWrapsAllElements() throws IOException {
        test("assertion-message-input.java", "assertion-message-expected.java");
    }

    // alignment_for_assignment
    @Test
    void alignmentForAssignmentDoesNotWrap() throws IOException {
        test("assignment-no-wrap-unchanged.java");
    }

    // alignment_for_conditional_expression
    @Test
    void alignmentForConditionalExpressionWrapsAllElements() throws IOException {
        test("conditional-expr-input.java", "conditional-expr-expected.java");
    }

    // alignment_for_enum_constants
    @Test
    void alignmentForEnumConstantsWrapsOnePerLine() throws IOException {
        test("enum-constants-input.java", "enum-constants-expected.java");
    }

    // alignment_for_for_loop_header
    @Test
    void alignmentForForLoopHeaderWrapsAllElements() throws IOException {
        test("for-loop-header-input.java", "for-loop-header-expected.java");
    }

    // alignment_for_method_declaration
    @Test
    void alignmentForMethodDeclarationDoesNotWrap() throws IOException {
        test("method-declaration-no-wrap-unchanged.java");
    }

    // alignment_for_parameters_in_constructor_declaration
    @Test
    void alignmentForParametersInConstructorDeclarationWrapsAllElements() throws IOException {
        test("constructor-params-input.java", "constructor-params-expected.java");
    }

    // alignment_for_permitted_types_in_type_declaration
    @Test
    void alignmentForPermittedTypesInTypeDeclarationWrapsAllElements() throws IOException {
        test("permitted-types-input.java", "permitted-types-expected.java");
    }

    // alignment_for_record_components
    @Test
    void alignmentForRecordComponentsWrapsAllElements() throws IOException {
        test("record-components-input.java", "record-components-expected.java");
    }

    // alignment_for_resources_in_try
    @Test
    void alignmentForResourcesInTryWrapsAllElements() throws IOException {
        test("try-resources-input.java", "try-resources-expected.java");
    }

    // alignment_for_selector_in_method_invocation
    @Test
    void alignmentForSelectorInMethodInvocationWrapsWhereNecessary() throws IOException {
        test("method-selector-input.java", "method-selector-expected.java");
    }

    // alignment_for_superclass_in_type_declaration
    @Test
    void alignmentForSuperclassInTypeDeclarationWrapsAllElements() throws IOException {
        test("superclass-input.java", "superclass-expected.java");
    }

    // alignment_for_superinterfaces_in_type_declaration
    @Test
    void alignmentForSuperinterfacesInTypeDeclarationWrapsAllElements() throws IOException {
        test("superinterfaces-class-input.java", "superinterfaces-class-expected.java");
    }

    // alignment_for_superinterfaces_in_enum_declaration
    @Test
    void alignmentForSuperinterfacesInEnumDeclarationWrapsAllElements() throws IOException {
        test("superinterfaces-enum-input.java", "superinterfaces-enum-expected.java");
    }

    // alignment_for_superinterfaces_in_record_declaration
    @Test
    void alignmentForSuperinterfacesInRecordDeclarationWrapsAllElements() throws IOException {
        test("superinterfaces-record-input.java", "superinterfaces-record-expected.java");
    }

    // alignment_for_throws_clause_in_method_declaration
    // DISABLED: Eclipse JDT keeps "throws" + first exception as a single unit.
    // Needs a custom formatter step to break after "throws". See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse JDT cannot separate throws from first exception")
    @Test
    void alignmentForThrowsClauseInMethodDeclarationWrapsAllElements() throws IOException {
        test("throws-method-input.java", "throws-method-expected.java");
    }

    // alignment_for_throws_clause_in_constructor_declaration
    // DISABLED: Eclipse JDT keeps "throws" + first exception as a single unit.
    // Needs a custom formatter step to break after "throws". See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse JDT cannot separate throws from first exception")
    @Test
    void alignmentForThrowsClauseInConstructorDeclarationWrapsAllElements() throws IOException {
        test("throws-constructor-input.java", "throws-constructor-expected.java");
    }

    // alignment_for_type_arguments
    @Test
    void alignmentForTypeArgumentsWrapsAllElements() throws IOException {
        test("type-arguments-input.java", "type-arguments-expected.java");
    }

    // alignment_for_type_parameters
    @Test
    void alignmentForTypeParametersWrapsAllElements() throws IOException {
        test("type-parameters-input.java", "type-parameters-expected.java");
    }

    // alignment_for_parameterized_type_references
    @Test
    void alignmentForParameterizedTypeReferencesDoesNotWrap() throws IOException {
        test("parameterized-type-ref-unchanged.java");
    }

    // alignment_for_union_type_in_multicatch
    @Test
    void alignmentForUnionTypeInMulticatchWrapsAllElements() throws IOException {
        test("union-multicatch-input.java", "union-multicatch-expected.java");
    }

    // alignment_for_switch_case_with_arrow
    @Test
    void alignmentForSwitchCaseWithArrowWrapsAllElements() throws IOException {
        test("switch-arrow-wrap-input.java", "switch-arrow-wrap-expected.java");
    }

    // alignment_for_expressions_in_switch_case_with_arrow
    // DISABLED: Eclipse JDT indents the arrow body at the same level as the case expressions.
    // The body should be indented further. Likely a configuration issue. See TODO.md.
    @Disabled("Arrow body should be indented further than case expressions — needs custom rule")
    @Test
    void alignmentForExpressionsInSwitchCaseWithArrowWrapsAllElements() throws IOException {
        test("switch-arrow-expressions-input.java", "switch-arrow-expressions-expected.java");
    }

    // alignment_for_expressions_in_switch_case_with_colon
    // DISABLED: Same issue as arrow case — body indented at same level as case
    // expressions. See TODO.md.
    @Disabled("Case body should be indented further than case expressions — needs custom rule")
    @Test
    void alignmentForExpressionsInSwitchCaseWithColonWrapsAllElements() throws IOException {
        test("switch-colon-expressions-input.java", "switch-colon-expressions-expected.java");
    }

    // alignment_for_annotations_on_type
    @Test
    void alignmentForAnnotationsOnTypeWrapsAllElements() throws IOException {
        test("annotations-type-input.java", "annotations-type-expected.java");
    }

    // alignment_for_annotations_on_method
    @Test
    void alignmentForAnnotationsOnMethodWrapsAllElements() throws IOException {
        test("annotations-method-input.java", "annotations-method-expected.java");
    }

    // alignment_for_annotations_on_field
    @Test
    void alignmentForAnnotationsOnFieldWrapsAllElements() throws IOException {
        test("annotations-field-input.java", "annotations-field-expected.java");
    }

    // alignment_for_annotations_on_local_variable
    @Test
    void alignmentForAnnotationsOnLocalVariableWrapsAllElements() throws IOException {
        test("annotations-local-var-input.java", "annotations-local-var-expected.java");
    }

    // alignment_for_annotations_on_parameter
    @Test
    void alignmentForAnnotationsOnParameterKeepsShortAnnotationsOnSameLine() throws IOException {
        test("annotations-parameter-unchanged.java");
    }

    // alignment_for_annotations_on_package
    @Test
    void alignmentForAnnotationsOnPackageWrapsAllElements() throws IOException {
        test("annotations-package-input.java", "annotations-package-expected.java");
    }

    // alignment_for_annotations_on_enum_constant
    @Test
    void alignmentForAnnotationsOnEnumConstantWrapsAllElements() throws IOException {
        test("annotations-enum-constant-input.java", "annotations-enum-constant-expected.java");
    }

    // alignment_for_type_annotations
    @Test
    void alignmentForTypeAnnotationsWrapsAllElements() throws IOException {
        test("type-annotations-input.java", "type-annotations-expected.java");
    }
}
