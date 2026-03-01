package ca.kieve.formatter.rules.lint;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checkstyle check that flags if/if-else statements that could be replaced
 * with guard clauses using early return/continue/break.
 * <p>
 * Detects two patterns in exitable blocks (methods, constructors, lambdas,
 * loops, switch cases):
 * <ul>
 *   <li>if-else as the last statement — suggests early exit to eliminate else</li>
 *   <li>wrapping if (no else) as the last statement — suggests inverting the
 *       condition and using early exit (unless the body is already a guard clause)</li>
 * </ul>
 */
public class PreferEarlyExitCheck extends AbstractCheck {
    private static final String MSG_ELSE = "Use early %s to eliminate the else branch.";
    private static final String MSG_WRAP = "Invert the condition and use early %s to reduce nesting.";

    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.LITERAL_IF };
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
        // Skip inner else-if: only visit the outermost if in a chain
        if (ast.getParent().getType() == TokenTypes.LITERAL_ELSE) {
            return;
        }

        DetailAST parent = ast.getParent();
        int parentType = parent.getType();

        // Must be inside a statement list or directly in a case group
        if (parentType != TokenTypes.SLIST
            && parentType != TokenTypes.CASE_GROUP) {
            return;
        }

        // Must be last statement in the block
        if (!isLastStatement(ast, parent)) {
            return;
        }

        // Determine exit keyword from enclosing context
        String exitKeyword = findExitKeyword(parent);
        if (exitKeyword == null) {
            return;
        }

        DetailAST elseClause = ast.findFirstToken(TokenTypes.LITERAL_ELSE);
        if (elseClause != null) {
            log(ast, String.format(MSG_ELSE, exitKeyword));
        } else if (!isGuardClause(ast)) {
            log(ast, String.format(MSG_WRAP, exitKeyword));
        }
    }

    private boolean isLastStatement(DetailAST ast, DetailAST parent) {
        if (parent.getType() == TokenTypes.SLIST) {
            DetailAST next = ast.getNextSibling();
            if (next == null) {
                return true;
            }
            return next.getType() == TokenTypes.RCURLY;
        }
        // CASE_GROUP: last if no more siblings
        return ast.getNextSibling() == null;
    }

    private String findExitKeyword(DetailAST parent) {
        DetailAST context;
        if (parent.getType() == TokenTypes.SLIST) {
            context = parent.getParent();
        } else {
            // Direct child of CASE_GROUP
            context = parent;
        }

        return switch (context.getType()) {
        case
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.COMPACT_CTOR_DEF,
            TokenTypes.LAMBDA ->
            "return";
        case
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO ->
            "continue";
        case TokenTypes.CASE_GROUP -> "break";
        default -> null;
        };
    }

    private boolean isGuardClause(DetailAST ifAst) {
        DetailAST thenBlock = getThenBlock(ifAst);
        if (thenBlock == null) {
            return false;
        }

        if (thenBlock.getType() == TokenTypes.SLIST) {
            return hasSingleExitStatement(thenBlock);
        }
        // Braceless form
        return isExitStatement(thenBlock.getType());
    }

    private DetailAST getThenBlock(DetailAST ifAst) {
        DetailAST child = ifAst.getFirstChild();
        while (child != null) {
            if (child.getType() == TokenTypes.RPAREN) {
                return child.getNextSibling();
            }
            child = child.getNextSibling();
        }
        return null;
    }

    private boolean hasSingleExitStatement(DetailAST slist) {
        int count = 0;
        DetailAST candidate = null;
        DetailAST child = slist.getFirstChild();
        while (child != null) {
            if (child.getType() != TokenTypes.RCURLY) {
                count++;
                candidate = child;
            }
            child = child.getNextSibling();
        }
        return count == 1
            && candidate != null
            && isExitStatement(candidate.getType());
    }

    private boolean isExitStatement(int tokenType) {
        return tokenType == TokenTypes.LITERAL_RETURN
            || tokenType == TokenTypes.LITERAL_THROW
            || tokenType == TokenTypes.LITERAL_CONTINUE
            || tokenType == TokenTypes.LITERAL_BREAK;
    }
}
