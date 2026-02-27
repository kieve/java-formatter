package ca.kieve.formatter.rules;

import java.util.regex.Pattern;

/**
 * Removes blank lines immediately before {@code case} and {@code default} labels
 * in switch statements.
 * <p>
 * Eclipse JDT's {@code blank_lines_between_statement_groups_in_switch: 0} doesn't
 * actively remove existing blank lines when
 * {@code number_of_empty_lines_to_preserve} is set. This rule compensates by
 * collapsing any blank lines that appear before a case or default label.
 */
public final class SwitchCaseBlankLines {
    private SwitchCaseBlankLines() {}

    private static final Pattern BLANK_BEFORE_CASE = Pattern.compile(
            "\\n(\\s*\\n)+(\\s*(?:case\\s|default[:\\s]))");

    public static String apply(String source) {
        return BLANK_BEFORE_CASE.matcher(source).replaceAll("\n$2");
    }
}
