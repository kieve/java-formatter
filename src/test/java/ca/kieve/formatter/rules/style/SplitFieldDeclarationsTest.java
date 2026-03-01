package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SplitFieldDeclarationsTest extends FormatterRuleTestBase {
    SplitFieldDeclarationsTest() {
        super("split-field-declarations/", SplitFieldDeclarations::apply);
    }

    @Test
    void splitsSimpleFieldDeclaration() throws IOException {
        test("simple-input.java", "simple-expected.java");
    }

    @Test
    void splitsFieldWithInitializers() throws IOException {
        test("initializers-input.java", "initializers-expected.java");
    }

    @Test
    void preservesModifiers() throws IOException {
        test("modifiers-input.java", "modifiers-expected.java");
    }

    @Test
    void preservesAnnotations() throws IOException {
        test("annotations-input.java", "annotations-expected.java");
    }

    @Test
    void splitsArrayType() throws IOException {
        test("array-type-input.java", "array-type-expected.java");
    }

    @Test
    void splitsGenericType() throws IOException {
        test("generic-type-input.java", "generic-type-expected.java");
    }

    @Test
    void handlesGenericWithCommas() throws IOException {
        test("generic-with-commas-input.java", "generic-with-commas-expected.java");
    }

    @Test
    void preservesIndentation() throws IOException {
        test("indentation-input.java", "indentation-expected.java");
    }

    @Test
    void movesTrailingCommentAbove() throws IOException {
        test("trailing-comment-input.java", "trailing-comment-expected.java");
    }

    @Test
    void splitsLocalVariableDeclaration() throws IOException {
        test("local-variable-input.java", "local-variable-expected.java");
    }

    @Test
    void doesNotSplitForLoopInit() throws IOException {
        test("for-loop-unchanged.java");
    }

    @Test
    void preservesSingleDeclaration() throws IOException {
        test("single-unchanged.java");
    }

    @Test
    void handlesMultipleFieldsInClass() throws IOException {
        test("multiple-fields-input.java", "multiple-fields-expected.java");
    }

    @Test
    void splitsFieldWithComplexInitializer() throws IOException {
        test("complex-initializer-input.java", "complex-initializer-expected.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("split-field-declarations/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }
}
