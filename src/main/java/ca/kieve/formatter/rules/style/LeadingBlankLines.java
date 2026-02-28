package ca.kieve.formatter.rules.style;

/**
 * Strips all leading blank lines from source code.
 * <p>
 * Eclipse JDT's {@code blank_lines_before_package: 0} doesn't fully remove
 * leading blank lines when {@code number_of_empty_lines_to_preserve} is set,
 * so this rule compensates.
 */
public final class LeadingBlankLines {
    private LeadingBlankLines() {
    }

    public static String apply(String source) {
        return source.stripLeading();
    }
}
