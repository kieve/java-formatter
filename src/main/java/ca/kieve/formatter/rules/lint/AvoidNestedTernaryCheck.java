package ca.kieve.formatter.rules.lint;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checkstyle check that flags nested ternary operators.
 * <p>
 * A ternary expression whose condition, then-branch, or else-branch contains
 * another ternary expression is reported as a violation. Only the inner
 * (nested) ternary is flagged, not the outer one.
 */
public class AvoidNestedTernaryCheck extends AbstractCheck {
    private static final String MSG = "Nested ternary operators are not allowed.";

    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.QUESTION };
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        DetailAST parent = ast.getParent();
        while (parent != null) {
            if (parent.getType() == TokenTypes.QUESTION) {
                log(ast, MSG);
                return;
            }
            parent = parent.getParent();
        }
    }
}
