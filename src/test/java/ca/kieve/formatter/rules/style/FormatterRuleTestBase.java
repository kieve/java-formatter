package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.rules.FormatterRule;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base class for formatting rule tests. Subclasses must implement
 * {@link #respectsFormatterOffTags()} to verify that content inside
 * {@code // @formatter:off} / {@code // @formatter:on} blocks is preserved.
 */
abstract class FormatterRuleTestBase {
    private final String fixtureDir;
    protected final FormatterRule rule;

    FormatterRuleTestBase() {
        this(null, null);
    }

    FormatterRuleTestBase(FormatterRule rule) {
        this(null, rule);
    }

    FormatterRuleTestBase(String fixtureDir, FormatterRule rule) {
        this.fixtureDir = fixtureDir;
        this.rule = rule;
    }

    /**
     * Load input and expected fixture files, apply the rule, and assert equal.
     */
    protected void test(String inputFile, String expectedFile) throws IOException {
        String input = loadFixture(fixtureDir + inputFile);
        String expected = loadFixture(fixtureDir + expectedFile);
        assertEquals(expected, rule.apply(input));
    }

    /**
     * Load a fixture file and assert the rule leaves it unchanged.
     */
    protected void test(String unchangedFile) throws IOException {
        String input = loadFixture(fixtureDir + unchangedFile);
        assertEquals(input, rule.apply(input));
    }

    @Test
    abstract void respectsFormatterOffTags();
}
