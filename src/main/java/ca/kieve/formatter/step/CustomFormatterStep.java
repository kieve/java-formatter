package ca.kieve.formatter.step;

import com.diffplug.spotless.FormatterFunc;
import com.diffplug.spotless.FormatterStep;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.FormatterTags;
import ca.kieve.formatter.FormatterTags.ProtectedSource;
import ca.kieve.formatter.rules.style.AnnotationTypeBlankLines;
import ca.kieve.formatter.rules.style.ArrayInitializerWrapping;
import ca.kieve.formatter.rules.style.AssignmentWrapping;
import ca.kieve.formatter.rules.style.ClassBodyBlankLines;
import ca.kieve.formatter.rules.style.ClosingBracketNewline;
import ca.kieve.formatter.rules.style.CommentFormatting;
import ca.kieve.formatter.rules.style.FieldOrdering;
import ca.kieve.formatter.rules.style.ImportSorting;
import ca.kieve.formatter.rules.style.InnerTypeOrdering;
import ca.kieve.formatter.rules.style.LeadingBlankLines;
import ca.kieve.formatter.rules.style.OperatorWrapping;
import ca.kieve.formatter.rules.style.ParameterAnnotationWrapping;
import ca.kieve.formatter.rules.style.SplitFieldDeclarations;
import ca.kieve.formatter.rules.style.SwitchCaseBlankLines;
import ca.kieve.formatter.rules.style.SwitchCaseBodyIndentation;
import ca.kieve.formatter.rules.style.ThrowsWrapping;
import ca.kieve.formatter.rules.style.WrappedLineJoining;

import java.io.Serializable;
import java.util.function.UnaryOperator;

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

    private static volatile boolean debugRules = false;

    public static void setDebugRules(boolean enabled) {
        debugRules = enabled;
    }

    private CustomFormatterStep() {
    }

    public static FormatterStep create() {
        return create(FormatConfig.defaults());
    }

    public static FormatterStep create(FormatConfig config) {
        return FormatterStep.create("customJavaFormatter", new State(config), State::toFormatter);
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
        result = debugApply("LeadingBlankLines", result, LeadingBlankLines::apply);
        result = debugApply("ImportSorting", result, r -> ImportSorting.apply(r, config));
        result = debugApply("CommentFormatting", result, r -> CommentFormatting.apply(r, config));
        result = debugApply("FieldOrdering", result, FieldOrdering::apply);
        result = debugApply("InnerTypeOrdering", result, InnerTypeOrdering::apply);
        result = debugApply("ClassBodyBlankLines", result, ClassBodyBlankLines::apply);
        result = debugApply("SwitchCaseBlankLines", result, SwitchCaseBlankLines::apply);
        result = debugApply("SwitchCaseBodyIndentation", result, SwitchCaseBodyIndentation::apply);
        result = debugApply("AnnotationTypeBlankLines", result, AnnotationTypeBlankLines::apply);
        result = debugApply("SplitFieldDeclarations", result, SplitFieldDeclarations::apply);
        result = debugApply("ArrayInitializerWrapping", result, ArrayInitializerWrapping::apply);
        result = debugApply("OperatorWrapping", result, OperatorWrapping::apply);
        result = debugApply("AssignmentWrapping", result, r -> AssignmentWrapping.apply(r, config));
        result = debugApply("ClosingBracketNewline", result, ClosingBracketNewline::apply);
        result = debugApply(
            "ParameterAnnotationWrapping",
            result,
            ParameterAnnotationWrapping::apply
        );
        result = debugApply("ThrowsWrapping", result, r -> ThrowsWrapping.apply(r, config));
        result = debugApply("WrappedLineJoining", result, r -> WrappedLineJoining.apply(r, config));
        return ps.restore(result);
    }

    private static String debugApply(String ruleName, String input, UnaryOperator<String> rule) {
        String output = rule.apply(input);
        if (debugRules && !output.equals(input)) {
            System.err.println("=== " + ruleName + " BEFORE ===");
            System.err.println(input);
            System.err.println("=== " + ruleName + " AFTER ===");
            System.err.println(output);
            System.err.println("=== END " + ruleName + " ===");
        }
        return output;
    }
}
