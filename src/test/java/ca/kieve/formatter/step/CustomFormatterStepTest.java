package ca.kieve.formatter.step;

import com.diffplug.spotless.FormatterStep;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomFormatterStepTest {
    @Test
    void stepCreatesSuccessfully() {
        FormatterStep step = CustomFormatterStep.create();
        assertNotNull(step);
        assertEquals("customJavaFormatter", step.getName());
    }

    @Test
    void identityFormattingPreservesInput() throws Exception {
        FormatterStep step = CustomFormatterStep.create();
        // @formatter:off
        String input = """
                public class Hello {
                    public static void main(String[] args) {
                        System.out.println("Hello");
                    }
                }
                """;
                // @formatter:on
        String result = step.format(input, new java.io.File("Hello.java"));
        assertEquals(input, result);
    }
}
