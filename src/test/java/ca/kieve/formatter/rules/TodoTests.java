package ca.kieve.formatter.rules;

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
        String input = """
                public class FormatterTest {
                    void method(@SuppressWarnings("unchecked") @Deprecated String firstParameter, @SuppressWarnings("unchecked") @Deprecated String secondParameter) {
                    }
                }
                """;

        // language=Java
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

        assertEquals(expected, formatJava(input));
    }
}
