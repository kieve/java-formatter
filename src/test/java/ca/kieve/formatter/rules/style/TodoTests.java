package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for desired formatting behavior that needs custom formatter steps.
 * All tests in this file are disabled until the corresponding custom rules are implemented.
 */
class TodoTests {
    // alignment_for_annotations_on_parameter — when parameters wrap, annotations should go on their
    // own lines above each parameter, not inline. See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse keeps annotations inline with wrapped parameters")
    @Test
    void annotationsOnWrappedParametersShouldGoOnOwnLines() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") @Deprecated String firstParameter, @SuppressWarnings("unchecked") @Deprecated String secondParameter) {
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(
                        @SuppressWarnings("unchecked")
                        @Deprecated
                        String firstParameter,
                        @SuppressWarnings("unchecked")
                        @Deprecated
                        String secondParameter) {
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }

    // wrap_before_binary_operator — when author wraps after an operator, the formatter should move
    // the operator to the beginning of the next line. See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse preserves author's wrap-after-operator style")
    @Test
    void wrapAfterOperatorShouldMoveOperatorBeforeNextLine() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue +
                            secondValue +
                            thirdValue;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method() {
                        int result = firstValue
                            + secondValue
                            + thirdValue;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
