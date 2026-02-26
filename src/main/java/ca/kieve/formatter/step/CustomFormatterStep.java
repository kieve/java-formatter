package ca.kieve.formatter.step;

import ca.kieve.formatter.FormatConfig;
import com.diffplug.spotless.FormatterFunc;
import com.diffplug.spotless.FormatterStep;

import java.io.Serializable;

/**
 * The main Spotless FormatterStep that applies all custom formatting rules.
 * <p>
 * Individual rules are applied in sequence within a single step to allow
 * them to share the parsed AST and configuration.
 */
public final class CustomFormatterStep {
    private CustomFormatterStep() {}

    public static FormatterStep create() {
        return create(FormatConfig.defaults());
    }

    public static FormatterStep create(FormatConfig config) {
        return FormatterStep.create(
                "customJavaFormatter",
                new State(config),
                State::toFormatter
        );
    }

    private static final class State implements Serializable {
        private static final long serialVersionUID = 1L;
        private final FormatConfig config;

        State(FormatConfig config) {
            this.config = config;
        }

        FormatterFunc toFormatter() {
            return source -> {
                String result = source;
                // Formatting rules will be applied here in sequence.
                // Each rule is a String -> String transformation.
                return result;
            };
        }
    }
}
