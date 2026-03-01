package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldOrderingTest extends FormatterRuleTestBase {
    FieldOrderingTest() {
        super("field-ordering/", FieldOrdering::apply);
    }

    @Test
    void sortsStaticBeforeInstance() throws IOException {
        test(
            "static-before-instance-input.java",
            "static-before-instance-expected.java");
    }

    @Test
    void sortsPublicBeforeNonPublic() throws IOException {
        test(
            "public-before-nonpublic-input.java",
            "public-before-nonpublic-expected.java");
    }

    @Test
    void sortsFinalBeforeNonFinal() throws IOException {
        test(
            "final-before-nonfinal-input.java",
            "final-before-nonfinal-expected.java");
    }

    @Test
    void sortsFullGroupOrdering() throws IOException {
        test(
            "full-group-ordering-input.java",
            "full-group-ordering-expected.java");
    }

    @Test
    void movesFieldsBeforeConstructor() throws IOException {
        test(
            "fields-before-constructor-input.java",
            "fields-before-constructor-expected.java");
    }

    @Test
    void preservesIntraGroupOrder() throws IOException {
        test("intra-group-order-unchanged.java");
    }

    @Test
    void preservesAlreadyOrdered() throws IOException {
        test("already-ordered-unchanged.java");
    }

    @Test
    void handlesEnumWithConstants() throws IOException {
        test(
            "enum-with-constants-input.java",
            "enum-with-constants-expected.java");
    }

    @Test
    void carriesJavadocWithField() throws IOException {
        test("javadoc-input.java", "javadoc-expected.java");
    }

    @Test
    void carriesAnnotationsWithField() throws IOException {
        test("annotations-input.java", "annotations-expected.java");
    }

    @Test
    @Override
    void respectsFormatterOffTags() {
        try {
            String input = loadFixture(
                "field-ordering/formatter-off-unchanged.java");
            assertEquals(
                input,
                CustomFormatterStep.applyCustomRules(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
