package ca.kieve.formatter.util;

import ca.kieve.formatter.rules.FormatterRule;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Shared base class for fixture-based formatter tests. Subclasses pass a
 * fixture directory and a formatting function ({@link FormatterRule}) to the
 * constructor, then call {@link #test(String, String)} or
 * {@link #test(String)} in each test method.
 *
 * <p>Used directly by Eclipse formatter tests and extended by
 * {@code FormatterRuleTestBase} (which adds the
 * {@code respectsFormatterOffTags()} requirement) for custom rule tests.
 */
public abstract class FormatterTestBase {
    private final String fixtureDir;
    protected final FormatterRule rule;

    protected FormatterTestBase(String fixtureDir, FormatterRule rule) {
        this.fixtureDir = fixtureDir;
        this.rule = rule;
    }

    /**
     * Load input and expected fixture files, apply the rule, and assert equal.
     */
    protected void test(String inputFile, String expectedFile) throws IOException {
        String input = FormatterTestUtil.loadFixture(fixtureDir + inputFile);
        String expected = FormatterTestUtil.loadFixture(fixtureDir + expectedFile);
        assertEquals(expected, rule.apply(input));
    }

    /**
     * Load a fixture file and assert the rule leaves it unchanged.
     */
    protected void test(String unchangedFile) throws IOException {
        String input = FormatterTestUtil.loadFixture(fixtureDir + unchangedFile);
        assertEquals(input, rule.apply(input));
    }
}
