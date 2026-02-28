package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InnerTypeOrderingTest extends FormatterRuleTestBase {
    InnerTypeOrderingTest() {
        super("inner-type-ordering/", InnerTypeOrdering::apply);
    }

    @Test
    void movesInnerClassToTop() throws IOException {
        test("basic-class-input.java", "basic-class-expected.java");
    }

    @Test
    void movesInnerInterfaceToTop() throws IOException {
        test("inner-interface-input.java", "inner-interface-expected.java");
    }

    @Test
    void movesInnerEnumToTop() throws IOException {
        test("inner-enum-input.java", "inner-enum-expected.java");
    }

    @Test
    void movesInnerRecordToTop() throws IOException {
        test("inner-record-input.java", "inner-record-expected.java");
    }

    @Test
    void movesInnerAnnotationToTop() throws IOException {
        test("inner-annotation-input.java", "inner-annotation-expected.java");
    }

    @Test
    void movesMultipleInnerTypes() throws IOException {
        test("multiple-inner-types-input.java", "multiple-inner-types-expected.java");
    }

    @Test
    void handlesNestedInnerTypes() throws IOException {
        test("nested-inner-types-input.java", "nested-inner-types-expected.java");
    }

    @Test
    void carriesJavadocWithMember() throws IOException {
        test("javadoc-input.java", "javadoc-expected.java");
    }

    @Test
    void carriesAnnotationsWithMember() throws IOException {
        test("annotations-input.java", "annotations-expected.java");
    }

    @Test
    void handlesEnumWithConstants() throws IOException {
        test("enum-with-constants-input.java", "enum-with-constants-expected.java");
    }

    @Test
    void preservesAlreadyOrdered() throws IOException {
        test("already-ordered-unchanged.java");
    }

    @Test
    void preservesNoInnerTypes() throws IOException {
        test("no-inner-types-unchanged.java");
    }

    @Test
    @Override
    void respectsFormatterOffTags() {
        try {
            String input = loadFixture("inner-type-ordering/formatter-off-unchanged.java");
            assertEquals(input, CustomFormatterStep.applyCustomRules(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
