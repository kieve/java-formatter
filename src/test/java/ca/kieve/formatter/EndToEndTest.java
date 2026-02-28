package ca.kieve.formatter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ca.kieve.formatter.FormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * End-to-end test that runs the full Gradle pipeline (Spotless + Eclipse JDT +
 * custom rules) via TestKit.
 * <p>
 * This is intentionally a single test with one comprehensive input to keep
 * Gradle invocations minimal. Individual formatter settings are tested faster
 * via {@link DirectFormatterTestUtil} in the eclipse/ package tests.
 */
class EndToEndTest {
    @TempDir
    Path testProjectDir;

    @Disabled("Slow — uses Gradle TestKit. Run manually to validate the full pipeline.")
    @Test
    void fullPipelineFormatsComprehensiveInput() throws IOException {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest
                {
                \tint fieldOne;



                \tint fieldTwo;
                    static class Inner
                    {
                    int x;
                    }
                    void method(int parameterOne, int parameterTwo, int parameterThree, int parameterFour, int paramFive)
                    {
                        if (true)
                        {
                System.out.println("hello");
                        }

                        switch (parameterOne)
                        {
                            case 1:
                                System.out.println("one");
                                break;
                            default:
                                break;
                        }
                    }

                    Runnable r = () ->
                    {
                        System.out.println("lambda");
                    };

                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    int fieldOne;

                    int fieldTwo;

                    static class Inner {
                        int x;
                    }

                    void method(
                        int parameterOne,
                        int parameterTwo,
                        int parameterThree,
                        int parameterFour,
                        int paramFive) {
                        if (true) {
                            System.out.println("hello");
                        }

                        switch (parameterOne) {
                        case 1:
                            System.out.println("one");
                            break;
                        default:
                            break;
                        }
                    }

                    Runnable r = () -> {
                        System.out.println("lambda");
                    };
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(testProjectDir, input));
    }
}
