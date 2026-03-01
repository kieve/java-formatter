package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for desired formatting behavior that needs custom formatter steps.
 * All tests in this file are disabled until the corresponding custom rules are implemented.
 */
class TodoTests extends FormatterTestBase {
    TodoTests() {
        super("todo/", s -> formatJava(s));
    }

    // alignment_for_annotations_on_parameter — when parameters wrap, annotations should go on their
    // own lines above each parameter, not inline. See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse keeps annotations inline with wrapped parameters")
    @Test
    void annotationsOnWrappedParametersShouldGoOnOwnLines() throws IOException {
        test("annotations-wrapped-params-input.java", "annotations-wrapped-params-expected.java");
    }

    // wrap_before_binary_operator — when author wraps after an operator, the formatter should move
    // the operator to the beginning of the next line. See TODO.md.
    @Disabled("Needs custom formatter step — Eclipse preserves author's wrap-after-operator style")
    @Test
    void wrapAfterOperatorShouldMoveOperatorBeforeNextLine() throws IOException {
        test("wrap-after-operator-input.java", "wrap-after-operator-expected.java");
    }
}
