package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Operator Wrapping section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Operator Wrapping"
 */
class OperatorWrappingTest {
    // alignment_for_additive_operator (excluded — custom formatter)
    @Test
    void alignmentForAdditiveOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue + secondValue + thirdValue + fourthValue + fifthValue + sixthValue + seventhValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue + secondValue + thirdValue + fourthValue + fifthValue + sixthValue
                            + seventhValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_multiplicative_operator (excluded — custom formatter)
    @Test
    void alignmentForMultiplicativeOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue * secondValue * thirdValue * fourthValue * fifthValue * sixthValue * seventhValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue * secondValue * thirdValue * fourthValue * fifthValue * sixthValue
                            * seventhValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_string_concatenation (excluded — custom formatter)
    @Test
    void alignmentForStringConcatenationWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        String result = "first" + "second" + "third" + "fourth" + "fifth" + "sixth" + "seventh" + "eighth";
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        String result = "first" + "second" + "third" + "fourth" + "fifth" + "sixth" + "seventh"
                            + "eighth";
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_bitwise_operator (excluded — custom formatter)
    @Test
    void alignmentForBitwiseOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue | secondValue | thirdValue | fourthValue | fifthValue | sixthValue | seventhValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue | secondValue | thirdValue | fourthValue | fifthValue | sixthValue
                            | seventhValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_logical_operator (excluded — custom formatter)
    @Test
    void alignmentForLogicalOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        boolean result = firstValue && secondValue && thirdValue && fourthValue && fifthValue && sixthValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        boolean result = firstValue && secondValue && thirdValue && fourthValue && fifthValue
                            && sixthValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_relational_operator (excluded — custom formatter)
    @Test
    void alignmentForRelationalOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        boolean result = veryLongVariableNameFirst == veryLongVariableNameSecond || veryLongVariableNameThird != veryLongVariableNameFourth;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        boolean result = veryLongVariableNameFirst == veryLongVariableNameSecond
                            || veryLongVariableNameThird != veryLongVariableNameFourth;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // alignment_for_shift_operator (excluded — custom formatter)
    @Test
    void alignmentForShiftOperatorWrapsBeforeOperator() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int result = veryLongVariableNameHere << veryLongShiftAmount >> anotherVeryLongVariableNameHereXx >>> finalShiftAmount;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int result = veryLongVariableNameHere << veryLongShiftAmount
                            >> anotherVeryLongVariableNameHereXx >>> finalShiftAmount;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // wrap_before_binary_operator
    @Test
    void wrapBeforeBinaryOperatorPlacesOperatorOnContinuationLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        boolean result = alphaValue + betaValue + gammaValue + deltaValue > epsilonValue + zetaValue + etaValue + thetaValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        boolean result = alphaValue + betaValue + gammaValue + deltaValue > epsilonValue + zetaValue
                            + etaValue + thetaValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // wrap_before_or_operator_multicatch
    @Test
    void wrapBeforeOrOperatorMulticatchPlacesPipeOnContinuationLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        try {
                        } catch (FirstLongerException | SecondLongerException | ThirdLongerException | FourthLongerEx e) {
                        }
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
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
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
