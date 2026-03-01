package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.ImportGroup;
import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.IOException;
import java.util.List;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImportSortingTest extends FormatterRuleTestBase {
    private static final FormatConfig CONFIG = FormatConfig.defaults();

    ImportSortingTest() {
        super("import-sorting/", s -> ImportSorting.apply(s, CONFIG));
    }

    @Test
    void sortsImportsIntoGroups() throws IOException {
        test("groups-input.java", "groups-expected.java");
    }

    @Test
    void alreadySortedRemainsUnchanged() throws IOException {
        test("already-sorted-unchanged.java");
    }

    @Test
    void handlesOnlyJavaAndJavaxImports() throws IOException {
        test("java-javax-input.java", "java-javax-expected.java");
    }

    @Test
    void handlesOnlyStaticImports() throws IOException {
        test("static-only-input.java", "static-only-expected.java");
    }

    @Test
    void handlesOnlyOtherImports() throws IOException {
        test("other-only-input.java", "other-only-expected.java");
    }

    @Test
    void removesBlankLinesBetweenImportsInSameGroup() throws IOException {
        test("same-group-blanks-input.java", "same-group-blanks-expected.java");
    }

    @Test
    void deduplicatesImports() throws IOException {
        test("dedup-input.java", "dedup-expected.java");
    }

    @Test
    void noImportsReturnsUnchanged() throws IOException {
        test("no-imports-unchanged.java");
    }

    @Test
    void preservesSurroundingContent() throws IOException {
        test("surrounding-content-input.java", "surrounding-content-expected.java");
    }

    @Test
    void handlesNoPackageStatement() throws IOException {
        test("no-package-input.java", "no-package-expected.java");
    }

    @Override
    void respectsFormatterOffTags() throws IOException {
        String input = loadFixture("import-sorting/formatter-off-unchanged.java");
        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }

    @Test
    void customLayoutReordersGroups() throws IOException {
        // Layout: static catch-all, then java/javax, then regular catch-all
        FormatConfig customConfig = new FormatConfig(
            100,
            List.of(
                ImportGroup.staticCatchAll(),
                ImportGroup.of("java.", "javax."),
                ImportGroup.catchAll()));

        String input = loadFixture("import-sorting/custom-layout-input.java");
        String expected = loadFixture("import-sorting/custom-layout-expected.java");
        assertEquals(expected, ImportSorting.apply(input, customConfig));
    }

    @Test
    void customProjectPrefix() throws IOException {
        FormatConfig customConfig = new FormatConfig(
            100,
            List.of(
                ImportGroup.catchAll(),
                ImportGroup.of("com.mycompany."),
                ImportGroup.of("javax.", "java."),
                ImportGroup.staticCatchAll()));

        String input = loadFixture("import-sorting/custom-project-input.java");
        String expected = loadFixture("import-sorting/custom-project-expected.java");
        assertEquals(expected, ImportSorting.apply(input, customConfig));
    }
}
