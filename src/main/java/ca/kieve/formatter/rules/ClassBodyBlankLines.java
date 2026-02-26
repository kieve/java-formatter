package ca.kieve.formatter.rules;

import java.util.regex.Pattern;

/**
 * Removes blank lines immediately after an opening brace.
 * <p>
 * Eclipse JDT's {@code blank_lines_before_first_class_body_declaration: 0} only
 * controls insertion — it cannot remove existing blank lines when
 * {@code number_of_empty_lines_to_preserve} is set. This rule compensates by
 * collapsing any blank lines that follow an opening brace.
 */
public final class ClassBodyBlankLines {
    private ClassBodyBlankLines() {}

    private static final Pattern BLANK_AFTER_BRACE = Pattern.compile(
            "(\\{[^\\S\\n]*\\n)(\\s*\\n)+");

    public static String apply(String source) {
        return BLANK_AFTER_BRACE.matcher(source).replaceAll("$1");
    }
}
