package ca.kieve.formatter.rules.style;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.rules.FormatterRule;
import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

/**
 * Base class for custom formatting rule tests. Extends {@link FormatterTestBase}
 * with the additional requirement that subclasses implement
 * {@link #respectsFormatterOffTags()} to verify that content inside
 * {@code // @formatter:off} / {@code // @formatter:on} blocks is preserved.
 */
abstract class FormatterRuleTestBase extends FormatterTestBase {
    FormatterRuleTestBase(String fixtureDir, FormatterRule rule) {
        super(fixtureDir, rule);
    }

    @Test
    abstract void respectsFormatterOffTags() throws IOException;
}
