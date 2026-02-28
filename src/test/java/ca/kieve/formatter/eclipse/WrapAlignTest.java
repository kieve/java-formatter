package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for Eclipse JDT Formatter — Wrapping & Alignment section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Wrapping & Alignment"
 */
class WrapAlignTest {

    // alignment_for_arguments_in_method_invocation
    @Test
    void alignmentForArgumentsInMethodInvocationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void caller() {
                        someMethod(firstArgument, secondArgument, thirdArgument, fourthArgument, fifthArgument, sixthArg);
                    }

                    void someMethod(int firstArgument, int secondArgument, int thirdArgument, int fourthArgument, int fifthArgument, int sixthArg) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void caller() {
                        someMethod(
                            firstArgument,
                            secondArgument,
                            thirdArgument,
                            fourthArgument,
                            fifthArgument,
                            sixthArg);
                    }

                    void someMethod(
                        int firstArgument,
                        int secondArgument,
                        int thirdArgument,
                        int fourthArgument,
                        int fifthArgument,
                        int sixthArg) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_parameters_in_method_declaration
    @Test
    void alignmentForParametersInMethodDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void longMethodName(String firstParam, String secondParam, String thirdParam, String fourthParam) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void longMethodName(
                        String firstParam,
                        String secondParam,
                        String thirdParam,
                        String fourthParam) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_arguments_in_allocation_expression
    @Test
    void alignmentForArgumentsInAllocationExpressionWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        Object obj = new StringBuilder(firstArgument, secondArgument, thirdArgument, fourthArgument);
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        Object obj = new StringBuilder(
                            firstArgument,
                            secondArgument,
                            thirdArgument,
                            fourthArgument);
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_arguments_in_annotation
    @Test
    void alignmentForArgumentsInAnnotationWrapsAllElements() {
        // language=Java
        String input = """
                @interface MyAnnotation {
                    String first() default "";
                    String second() default "";
                    String third() default "";
                    String fourth() default "";
                }

                @MyAnnotation(first = "valueOneXx", second = "valueTwoXx", third = "valueThreeX", fourth = "valueFour")
                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                @interface MyAnnotation {
                    String first() default "";

                    String second() default "";

                    String third() default "";

                    String fourth() default "";
                }

                @MyAnnotation(
                    first = "valueOneXx",
                    second = "valueTwoXx",
                    third = "valueThreeX",
                    fourth = "valueFour")
                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_arguments_in_enum_constant
    @Test
    void alignmentForArgumentsInEnumConstantWrapsAllElements() {
        // language=Java
        String input = """
                public enum FormatterTest {
                    VALUE_ONE("firstArgument", "secondArgument", "thirdArgument", "fourthArgument", "fifthArgument");

                    FormatterTest(String a, String b, String c, String d, String e) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE(
                        "firstArgument",
                        "secondArgument",
                        "thirdArgument",
                        "fourthArgument",
                        "fifthArgument");

                    FormatterTest(String a, String b, String c, String d, String e) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_arguments_in_explicit_constructor_call
    @Test
    void alignmentForArgumentsInExplicitConstructorCallWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    FormatterTest(String first, String second, String third, String fourth) {
                    }

                    FormatterTest() {
                        this("firstLongArgument", "secondLongArgument", "thirdLongArgument", "fourthLongArgumentXx");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    FormatterTest(String first, String second, String third, String fourth) {
                    }

                    FormatterTest() {
                        this(
                            "firstLongArgument",
                            "secondLongArgument",
                            "thirdLongArgument",
                            "fourthLongArgumentXx");
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_arguments_in_qualified_allocation_expression
    @Test
    void alignmentForArgumentsInQualifiedAllocationExpressionWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    class Inner {
                        Inner(String first, String second, String third, String fourth) {
                        }
                    }

                    void method() {
                        FormatterTest outer = new FormatterTest();
                        Inner obj = outer.new Inner("firstLongArg", "secondLongArg", "thirdLongArgument", "fourthArg");
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    class Inner {
                        Inner(String first, String second, String third, String fourth) {
                        }
                    }

                    void method() {
                        FormatterTest outer = new FormatterTest();
                        Inner obj = outer.new Inner(
                            "firstLongArg",
                            "secondLongArg",
                            "thirdLongArgument",
                            "fourthArg");
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_assertion_message
    @Test
    void alignmentForAssertionMessageWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(boolean condition) {
                        assert condition : "This is a very long assertion message that exceeds the line width limit";
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(boolean condition) {
                        assert condition
                            : "This is a very long assertion message that exceeds the line width limit";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_assignment
    @Test
    void alignmentForAssignmentDoesNotWrap() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        String someVariable = "a very long string value that combined with the variable name exceeds limit";
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        String someVariable = "a very long string value that combined with the variable name exceeds limit";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_conditional_expression
    @Test
    void alignmentForConditionalExpressionWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(boolean condition) {
                        String result = condition ? "the value when condition is true" : "the value when condition is false";
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(boolean condition) {
                        String result = condition
                            ? "the value when condition is true"
                            : "the value when condition is false";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_enum_constants
    @Test
    void alignmentForEnumConstantsWrapsOnePerLine() {
        // language=Java
        String input = """
                public enum FormatterTest {
                    VALUE_ONE, VALUE_TWO, VALUE_THREE
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    VALUE_ONE,
                    VALUE_TWO,
                    VALUE_THREE
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_for_loop_header
    @Test
    void alignmentForForLoopHeaderWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        for (int veryLongVariableNameHere = 0; veryLongVariableNameHere < someUpperBoundValue; veryLongVariableNameHere++) {
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        for (
                            int veryLongVariableNameHere = 0;
                            veryLongVariableNameHere < someUpperBoundValue;
                            veryLongVariableNameHere++) {
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_method_declaration
    @Test
    void alignmentForMethodDeclarationDoesNotWrap() {
        // language=Java
        String input = """
                public class FormatterTest {
                    public static synchronized String veryLongMethodNameThatExceedsTheLineWidthLimit() {
                        return "";
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    public static synchronized String veryLongMethodNameThatExceedsTheLineWidthLimit() {
                        return "";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_parameters_in_constructor_declaration
    @Test
    void alignmentForParametersInConstructorDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    FormatterTest(String firstParameter, String secondParameter, String thirdParameter, String fourth) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    FormatterTest(
                        String firstParameter,
                        String secondParameter,
                        String thirdParameter,
                        String fourth) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_permitted_types_in_type_declaration
    @Test
    void alignmentForPermittedTypesInTypeDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public sealed class FormatterTest permits SubclassAlpha, SubclassBeta, SubclassGamma, SubclassDelta {
                }

                final class SubclassAlpha extends FormatterTest {
                }

                final class SubclassBeta extends FormatterTest {
                }

                final class SubclassGamma extends FormatterTest {
                }

                final class SubclassDelta extends FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                public sealed class FormatterTest
                    permits
                    SubclassAlpha,
                    SubclassBeta,
                    SubclassGamma,
                    SubclassDelta {
                }

                final class SubclassAlpha extends FormatterTest {
                }

                final class SubclassBeta extends FormatterTest {
                }

                final class SubclassGamma extends FormatterTest {
                }

                final class SubclassDelta extends FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_record_components
    @Test
    void alignmentForRecordComponentsWrapsAllElements() {
        // language=Java
        String input = """
                public record FormatterTest(String firstComponent, String secondComponent, String thirdComponentXx) {
                }
                """;

        // language=Java
        String expected = """
                public record FormatterTest(
                    String firstComponent,
                    String secondComponent,
                    String thirdComponentXx) {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_resources_in_try
    @Test
    void alignmentForResourcesInTryWrapsAllElements() {
        // language=Java
        String input = """
                import java.io.*;

                public class FormatterTest {
                    void method() throws Exception {
                        try (InputStream firstStream = new FileInputStream("a"); InputStream secondStream = new FileInputStream("b")) {
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                import java.io.*;

                public class FormatterTest {
                    void method() throws Exception {
                        try (
                            InputStream firstStream = new FileInputStream("a");
                            InputStream secondStream = new FileInputStream("b")) {
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_selector_in_method_invocation
    @Test
    void alignmentForSelectorInMethodInvocationWrapsWhereNecessary() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        String result = someObject.firstMethod().secondMethod().thirdMethod().fourthMethod().toString();
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        String result = someObject.firstMethod().secondMethod().thirdMethod().fourthMethod()
                            .toString();
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_superclass_in_type_declaration
    @Test
    void alignmentForSuperclassInTypeDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class VeryLongClassNameFormatterTest extends VeryLongBaseClassNameThatExceedsTheLineWidthLimitXx {
                }
                """;

        // language=Java
        String expected = """
                public class VeryLongClassNameFormatterTest
                    extends
                    VeryLongBaseClassNameThatExceedsTheLineWidthLimitXx {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_superinterfaces_in_type_declaration
    @Test
    void alignmentForSuperinterfacesInTypeDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest implements FirstInterface, SecondInterface, ThirdInterface, FourthInterface {
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest
                    implements
                    FirstInterface,
                    SecondInterface,
                    ThirdInterface,
                    FourthInterface {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_superinterfaces_in_enum_declaration
    @Test
    void alignmentForSuperinterfacesInEnumDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public enum FormatterTest implements FirstInterface, SecondInterface, ThirdInterface, FourthInterface {
                    VALUE
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest
                    implements
                    FirstInterface,
                    SecondInterface,
                    ThirdInterface,
                    FourthInterface {
                    VALUE
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_superinterfaces_in_record_declaration
    @Test
    void alignmentForSuperinterfacesInRecordDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public record FormatterTest(int x) implements FirstLongerInterface, SecondLongerInterface, ThirdLongerInterface, FourthIface {
                }
                """;

        // language=Java
        String expected = """
                public record FormatterTest(int x)
                    implements
                    FirstLongerInterface,
                    SecondLongerInterface,
                    ThirdLongerInterface,
                    FourthIface {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_throws_clause_in_method_declaration
    // DISABLED: Eclipse JDT keeps "throws" + first exception as a single unit.
    // Needs a custom formatter step to break after "throws". See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse JDT cannot separate throws from first exception")
    @Test
    void alignmentForThrowsClauseInMethodDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() throws FirstLongerException, SecondLongerException, ThirdLongerException, FourthLongerException {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method()
                        throws
                        FirstLongerException,
                        SecondLongerException,
                        ThirdLongerException,
                        FourthLongerException {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_throws_clause_in_constructor_declaration
    // DISABLED: Eclipse JDT keeps "throws" + first exception as a single unit.
    // Needs a custom formatter step to break after "throws". See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse JDT cannot separate throws from first exception")
    @Test
    void alignmentForThrowsClauseInConstructorDeclarationWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    FormatterTest() throws FirstLongerException, SecondLongerException, ThirdLongerException, FourthLongerException {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    FormatterTest()
                        throws
                        FirstLongerException,
                        SecondLongerException,
                        ThirdLongerException,
                        FourthLongerException {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_type_arguments
    @Test
    void alignmentForTypeArgumentsWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    <A, B, C, D> void genericMethod() {
                    }

                    void caller() {
                        this.<FirstLongerTypeXx, SecondLongerTypeXx, ThirdLongerTypeXx, FourthLongerType>genericMethod();
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    <A, B, C, D> void genericMethod() {
                    }

                    void caller() {
                        this.<
                            FirstLongerTypeXx,
                            SecondLongerTypeXx,
                            ThirdLongerTypeXx,
                            FourthLongerType>genericMethod();
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_type_parameters
    @Test
    void alignmentForTypeParametersWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    <FirstLongerParam, SecondLongerParam, ThirdLongerParam, FourthLongerParam> void genericMethod() {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    <
                        FirstLongerParam,
                        SecondLongerParam,
                        ThirdLongerParam,
                        FourthLongerParam> void genericMethod() {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_parameterized_type_references
    @Test
    void alignmentForParameterizedTypeReferencesDoesNotWrap() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        SomeLongerGenericType<FirstLongerTypeXx, SecondLongerTypeXx, ThirdLongerTypeXx> variable = null;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        SomeLongerGenericType<FirstLongerTypeXx, SecondLongerTypeXx, ThirdLongerTypeXx> variable = null;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_union_type_in_multicatch
    @Test
    void alignmentForUnionTypeInMulticatchWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method() {
                        try {
                        } catch (FirstLongerException | SecondLongerException | ThirdLongerException | FourthLongerEx e) {
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        try {
                        } catch (
                            FirstLongerException
                            | SecondLongerException
                            | ThirdLongerException
                            | FourthLongerEx e) {
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_switch_case_with_arrow
    @Test
    void alignmentForSwitchCaseWithArrowWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1 -> System.out.println("short");
                        case 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 -> System.out.println("many");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(int x) {
                        switch (x) {
                        case 1 -> System.out.println("short");
                        case 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 ->
                            System.out.println("many");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_expressions_in_switch_case_with_arrow
    // DISABLED: Eclipse JDT indents the arrow body at the same level as the case expressions.
    // The body should be indented further. Likely a configuration issue. See TODO.md.
    @Disabled("Arrow body should be indented further than case expressions — needs custom rule")
    @Test
    void alignmentForExpressionsInSwitchCaseWithArrowWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(String x) {
                        switch (x) {
                        case "firstLongerValueXx", "secondLongerValueXx", "thirdLongerValueXx", "fourthLongerValueXx" -> System.out.println("match");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(String x) {
                        switch (x) {
                        case
                            "firstLongerValueXx",
                            "secondLongerValueXx",
                            "thirdLongerValueXx",
                            "fourthLongerValueXx" ->
                                System.out.println("match");
                        default -> System.out.println("other");
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_expressions_in_switch_case_with_colon
    // DISABLED: Same issue as arrow case — body indented at same level as case expressions. See TODO.md.
    @Disabled("Case body should be indented further than case expressions — needs custom rule")
    @Test
    void alignmentForExpressionsInSwitchCaseWithColonWrapsAllElements() {
        // language=Java
        String input = """
                public class FormatterTest {
                    void method(String x) {
                        switch (x) {
                        case "firstLongerValueXx", "secondLongerValueXx", "thirdLongerValueXx", "fourthLongerValueXx":
                            System.out.println("match");
                            break;
                        default:
                            System.out.println("other");
                            break;
                        }
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(String x) {
                        switch (x) {
                        case
                            "firstLongerValueXx",
                            "secondLongerValueXx",
                            "thirdLongerValueXx",
                            "fourthLongerValueXx":
                                System.out.println("match");
                                break;
                        default:
                            System.out.println("other");
                            break;
                        }
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_type
    @Test
    void alignmentForAnnotationsOnTypeWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                @SuppressWarnings("unchecked") @Deprecated public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                @SuppressWarnings("unchecked")
                @Deprecated
                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_method
    @Test
    void alignmentForAnnotationsOnMethodWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                public class FormatterTest {
                    @SuppressWarnings("unchecked") @Deprecated void method() {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    @SuppressWarnings("unchecked")
                    @Deprecated
                    void method() {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_field
    @Test
    void alignmentForAnnotationsOnFieldWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                public class FormatterTest {
                    @SuppressWarnings("unchecked") @Deprecated int field;
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    @SuppressWarnings("unchecked")
                    @Deprecated
                    int field;
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_local_variable
    @Test
    void alignmentForAnnotationsOnLocalVariableWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                public class FormatterTest {
                    void method() {
                        @SuppressWarnings("unchecked") @Deprecated int local = 0;
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        @SuppressWarnings("unchecked")
                        @Deprecated
                        int local = 0;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_parameter
    @Test
    void alignmentForAnnotationsOnParameterKeepsShortAnnotationsOnSameLine() {
        // language=Java — short annotations on parameters stay on the same line
        String input = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") @Deprecated int param) {
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") @Deprecated int param) {
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_package
    @Test
    void alignmentForAnnotationsOnPackageWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                @SuppressWarnings("unchecked") @Deprecated package com.example;

                public class FormatterTest {
                }
                """;

        // language=Java
        String expected = """
                @SuppressWarnings("unchecked")
                @Deprecated
                package com.example;

                public class FormatterTest {
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_annotations_on_enum_constant
    @Test
    void alignmentForAnnotationsOnEnumConstantWrapsAllElements() {
        // language=Java — two short annotations that fit on one line should still split
        String input = """
                public enum FormatterTest {
                    @SuppressWarnings("unchecked") @Deprecated VALUE_ONE,
                    VALUE_TWO
                }
                """;

        // language=Java
        String expected = """
                public enum FormatterTest {
                    @SuppressWarnings("unchecked")
                    @Deprecated
                    VALUE_ONE,
                    VALUE_TWO
                }
                """;

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_type_annotations
    @Test
    void alignmentForTypeAnnotationsWrapsAllElements() {
        // language=Java — two short type annotations that fit on one line should still split
        String input = """
                import java.lang.annotation.*;

                @Target(ElementType.TYPE_USE)
                @interface NotNull {
                }

                @Target(ElementType.TYPE_USE)
                @interface Immutable {
                }

                public class FormatterTest {
                    void method() {
                        @NotNull @Immutable String value = "";
                    }
                }
                """;

        // language=Java
        String expected = """
                import java.lang.annotation.*;

                @Target(ElementType.TYPE_USE)
                @interface NotNull {
                }

                @Target(ElementType.TYPE_USE)
                @interface Immutable {
                }

                public class FormatterTest {
                    void method() {
                        @NotNull
                        @Immutable
                        String value = "";
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
