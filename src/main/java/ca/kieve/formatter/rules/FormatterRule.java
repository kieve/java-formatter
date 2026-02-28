package ca.kieve.formatter.rules;

/**
 * A single formatting rule that transforms Java source code.
 */
@FunctionalInterface
public interface FormatterRule {
    String apply(String source);
}
