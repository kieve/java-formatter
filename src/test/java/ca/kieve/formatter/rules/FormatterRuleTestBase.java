package ca.kieve.formatter.rules;

import org.junit.jupiter.api.Test;

/**
 * Base class for formatting rule tests. Subclasses must implement
 * {@link #respectsFormatterOffTags()} to verify that content inside
 * {@code // @formatter:off} / {@code // @formatter:on} blocks is preserved.
 */
abstract class FormatterRuleTestBase {

    @Test
    abstract void respectsFormatterOffTags();
}
