package ca.kieve.formatter.step;

import com.diffplug.spotless.FormatterFunc;
import com.diffplug.spotless.FormatterStep;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.FormatterTags;
import ca.kieve.formatter.FormatterTags.ProtectedSource;
import ca.kieve.formatter.rules.style.ArrayInitializerWrapping;
import ca.kieve.formatter.rules.style.ClassBodyBlankLines;
import ca.kieve.formatter.rules.style.ClosingBracketNewline;
import ca.kieve.formatter.rules.style.CommentFormatting;
import ca.kieve.formatter.rules.style.FieldOrdering;
import ca.kieve.formatter.rules.style.ImportSorting;
import ca.kieve.formatter.rules.style.InnerTypeOrdering;
import ca.kieve.formatter.rules.style.LeadingBlankLines;
import ca.kieve.formatter.rules.style.SplitFieldDeclarations;
import ca.kieve.formatter.rules.style.SwitchCaseBlankLines;

import java.io.Serializable;

/**
 * The main Spotless FormatterStep that applies all custom formatting rules.
 * <p>
 * Individual rules are applied in sequence within a single step to allow
 * them to share the parsed AST and configuration.
 */
public final class CustomFormatterStep {
    private static final class State implements Serializable {
        private static final long serialVersionUID = 1L;
        private final FormatConfig config;

        State(FormatConfig config) {
            this.config = config;
        }

        FormatterFunc toFormatter() {
            return source -> applyCustomRules(source, config);
        }
    }

    private CustomFormatterStep() {
    }

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

    /**
     * Apply all custom formatting rules to the given source string
     * using default configuration.
     * <p>
     * This is the single source of truth for the custom rule chain.
     * Both the Spotless step and the direct test utility call this method.
     */
    public static String applyCustomRules(String source) {
        return applyCustomRules(source, FormatConfig.defaults());
    }

    /**
     * Apply all custom formatting rules to the given source string
     * using the provided configuration.
     */
    public static String applyCustomRules(String source, FormatConfig config) {
        ProtectedSource ps = FormatterTags.protect(source);
        String result = ps.source();
        result = LeadingBlankLines.apply(result);
        result = ImportSorting.apply(result, config);
        result = CommentFormatting.apply(result, config);
        result = FieldOrdering.apply(result);
        result = InnerTypeOrdering.apply(result);
        result = ClassBodyBlankLines.apply(result);
        result = SwitchCaseBlankLines.apply(result);
        result = SplitFieldDeclarations.apply(result);
        result = ArrayInitializerWrapping.apply(result);
        result = ClosingBracketNewline.apply(result);
        return ps.restore(result);
    }
}
