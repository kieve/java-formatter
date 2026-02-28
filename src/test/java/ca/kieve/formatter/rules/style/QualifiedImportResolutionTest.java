package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QualifiedImportResolutionTest extends FormatterRuleTestBase {
    QualifiedImportResolutionTest() {
        super("qualified-import-resolution/", QualifiedImportResolution::apply);
    }

    @Test
    void singleQualifiedTypeAddsImportAndSimplifies() throws IOException {
        test("single-type-input.java", "single-type-expected.java");
    }

    @Test
    void multipleQualifiedTypesResolvesAll() throws IOException {
        test("multiple-types-input.java", "multiple-types-expected.java");
    }

    @Test
    void alreadyImportedJustSimplifies() throws IOException {
        test("already-imported-input.java", "already-imported-expected.java");
    }

    @Test
    void javaLangSimplifiesWithoutImport() throws IOException {
        test("java-lang-input.java", "java-lang-expected.java");
    }

    @Test
    void samePackageSimplifiesWithoutImport() throws IOException {
        test("same-package-input.java", "same-package-expected.java");
    }

    @Test
    void collisionTwoFqcnsSameSimpleNameLeavesBothQualified()
        throws IOException {
        test("collision-two-fqcns-unchanged.java");
    }

    @Test
    void collisionWithExistingImportLeavesQualified() throws IOException {
        test("collision-existing-import-unchanged.java");
    }

    @Test
    void wildcardImportCoversPackage() throws IOException {
        test("wildcard-import-input.java", "wildcard-import-expected.java");
    }

    @Test
    void innerClassImportsOuterAndSimplifies() throws IOException {
        test("inner-class-input.java", "inner-class-expected.java");
    }

    @Test
    void qualifiedAnnotationSimplifies() throws IOException {
        test("annotation-input.java", "annotation-expected.java");
    }

    @Test
    void staticMethodAccessSimplifies() throws IOException {
        test("static-method-input.java", "static-method-expected.java");
    }

    @Test
    void staticFieldAccessSimplifies() throws IOException {
        test("static-field-input.java", "static-field-expected.java");
    }

    @Test
    void nestedGenericsResolvesAll() throws IOException {
        test("nested-generics-input.java", "nested-generics-expected.java");
    }

    @Test
    void extendsClauseSimplifies() throws IOException {
        test("extends-clause-input.java", "extends-clause-expected.java");
    }

    @Test
    void noQualifiedRefsReturnsUnchanged() throws IOException {
        test("no-qualified-refs-unchanged.java");
    }

    @Test
    void unparseableSourceReturnsUnchanged() throws IOException {
        test("unparseable-unchanged.txt");
    }

    @Override
    @Test
    void respectsFormatterOffTags() {
        try {
            String input = loadFixture(
                "qualified-import-resolution/formatter-off-unchanged.java");
            assertEquals(input, CustomFormatterStep.applyCustomRules(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
