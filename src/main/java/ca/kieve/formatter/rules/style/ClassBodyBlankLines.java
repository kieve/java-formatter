package ca.kieve.formatter.rules.style;

import java.util.regex.Pattern;

/**
 * Removes blank lines immediately after an opening brace and before a closing brace.
 * <p>
 * Eclipse JDT's {@code blank_lines_before_first_class_body_declaration} and
 * {@code blank_lines_after_last_class_body_declaration} only control insertion —
 * they cannot remove existing blank lines when
 * {@code number_of_empty_lines_to_preserve} is set. This rule compensates by
 * collapsing any blank lines that follow an opening brace or precede a closing brace.
 */
public final class ClassBodyBlankLines {
    private ClassBodyBlankLines() {
    }

    private static final Pattern BLANK_AFTER_BRACE = Pattern.compile(
        "(\\{[^\\S\\n]*\\n)(\\s*\\n)+");

    private static final Pattern BLANK_BEFORE_BRACE = Pattern.compile(
        "\\n(\\s*\\n)+(\\s*})");

    public static String apply(String source) {
        String result = BLANK_AFTER_BRACE.matcher(source).replaceAll("$1");
        return BLANK_BEFORE_BRACE.matcher(result).replaceAll("\n$2");
    }
}
