package ca.kieve.formatter.eclipse;

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
}
