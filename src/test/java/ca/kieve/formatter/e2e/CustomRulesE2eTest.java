package ca.kieve.formatter.e2e;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.step.CustomFormatterStep;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * End-to-end tests that run the full custom rules chain via
 * {@link CustomFormatterStep#applyCustomRules(String)} to verify that
 * rules interact correctly when combined.
 */
class CustomRulesE2eTest {
    @Test
    void annotationTypeInnerTypeReorderingAndBlankLines() {
        // language=Java
        // @formatter:off
        String input = """
                @interface Outer {
                    String value() default "";
                    @interface Inner {
                        String name() default "";
                        int count() default 0;
                    }
                    String other() default "";
                }
                """;
        // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                @interface Outer {
                    @interface Inner {
                        String name() default "";
                        int count() default 0;
                    }

                    String value() default "";
                    String other() default "";
                }
                """;
        // @formatter:on

        assertEquals(expected, CustomFormatterStep.applyCustomRules(input));
    }
}
