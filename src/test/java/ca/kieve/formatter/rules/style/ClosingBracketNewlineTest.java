package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClosingBracketNewlineTest extends FormatterRuleTestBase {
    ClosingBracketNewlineTest() {
        super("closing-bracket-newline/", ClosingBracketNewline::apply);
    }

    @Test
    void movesClosingParenForMethodParams() throws IOException {
        test("method-params-input.java", "method-params-expected.java");
    }

    @Test
    void movesClosingParenForMethodArgs() throws IOException {
        test("method-args-input.java", "method-args-expected.java");
    }

    @Test
    void movesClosingParenForConstructorParams() throws IOException {
        test("constructor-params-input.java", "constructor-params-expected.java");
    }

    @Test
    void movesClosingAngleBracketForTypeArguments() throws IOException {
        test("type-arguments-input.java", "type-arguments-expected.java");
    }

    @Test
    void movesClosingAngleBracketForTypeParameters() throws IOException {
        test("type-parameters-input.java", "type-parameters-expected.java");
    }

    @Test
    void movesClosingParenForAnnotationArgs() throws IOException {
        test("annotation-args-input.java", "annotation-args-expected.java");
    }

    @Test
    void movesClosingParenForForLoopHeader() throws IOException {
        test("for-loop-input.java", "for-loop-expected.java");
    }

    @Test
    void movesClosingParenForTryResources() throws IOException {
        test("try-resources-input.java", "try-resources-expected.java");
    }

    @Test
    void movesClosingParenForRecordComponents() throws IOException {
        test("record-components-input.java", "record-components-expected.java");
    }

    @Test
    void movesClosingParenForAllocationArgs() throws IOException {
        test("allocation-args-input.java", "allocation-args-expected.java");
    }

    @Test
    void movesClosingParenForEnumConstantArgs() throws IOException {
        test("enum-constant-args-input.java", "enum-constant-args-expected.java");
    }

    @Test
    void movesClosingParenForMultiCatch() throws IOException {
        test("multi-catch-input.java", "multi-catch-expected.java");
    }

    @Test
    void handlesNestedWrapping() throws IOException {
        test("nested-wrapping-input.java", "nested-wrapping-expected.java");
    }

    @Test
    void preservesNotWrapped() throws IOException {
        test("not-wrapped-unchanged.java");
    }

    @Test
    void preservesAlreadyOnOwnLine() throws IOException {
        test("already-own-line-unchanged.java");
    }

    @Test
    void preservesBracketsInStrings() throws IOException {
        test("brackets-in-strings-unchanged.java");
    }

    @Override
    @Test
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture(
            "closing-bracket-newline/formatter-off-unchanged.java"
        );
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
