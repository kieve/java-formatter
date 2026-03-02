package ca.kieve.formatter.rules.lint;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checkstyle check that flags control statements ({@code if}, {@code else},
 * {@code while}, {@code for}, {@code do-while}) that lack braces.
 * <p>
 * <b>Exception:</b> braceless early-exit {@code if} statements at the top of a
 * method, constructor, or lambda body are allowed. An early exit is
 * {@code if (cond) return;} / {@code throw} / {@code continue} / {@code break}
 * with no {@code else} clause. The exception applies only when all preceding
 * statements in the body are also braceless early-exit ifs.
 */
public class RequireBracesCheck extends AbstractCheck {
    private static final String MSG = "''%s'' statement must use braces.";

    @Override
    public int[] getDefaultTokens() {
        return new int[] {
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_ELSE,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_DO,
        };
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
        switch (ast.getType()) {
        case TokenTypes.LITERAL_IF -> visitIf(ast);
        case TokenTypes.LITERAL_ELSE -> visitElse(ast);
        case TokenTypes.LITERAL_WHILE -> visitWhile(ast);
        case TokenTypes.LITERAL_FOR -> visitFor(ast);
        case TokenTypes.LITERAL_DO -> visitDo(ast);
        default -> {
        }
        }
    }

    private void visitIf(DetailAST ast) {
        DetailAST thenBlock = getBlockAfterRparen(ast);
        if (thenBlock == null || thenBlock.getType() == TokenTypes.SLIST) {
            return;
        }
        if (isAllowedEarlyExit(ast)) {
            return;
        }
        log(ast, String.format(MSG, "if"));
    }

    private void visitElse(DetailAST ast) {
        DetailAST child = ast.getFirstChild();
        if (child == null) {
            return;
        }
        // else-if: let visitIf handle the inner if
        if (child.getType() == TokenTypes.LITERAL_IF) {
            return;
        }
        if (child.getType() == TokenTypes.SLIST) {
            return;
        }
        log(ast, String.format(MSG, "else"));
    }

    private void visitWhile(DetailAST ast) {
        // while inside do-while: let visitDo handle it
        if (ast.getParent().getType() == TokenTypes.LITERAL_DO) {
            return;
        }
        DetailAST body = getBlockAfterRparen(ast);
        if (body == null || body.getType() == TokenTypes.SLIST) {
            return;
        }
        log(ast, String.format(MSG, "while"));
    }

    private void visitFor(DetailAST ast) {
        DetailAST body = getBlockAfterRparen(ast);
        if (body == null || body.getType() == TokenTypes.SLIST) {
            return;
        }
        log(ast, String.format(MSG, "for"));
    }

    private void visitDo(DetailAST ast) {
        DetailAST body = ast.getFirstChild();
        if (body == null || body.getType() == TokenTypes.SLIST) {
            return;
        }
        log(ast, String.format(MSG, "do"));
    }

    /**
     * Checks whether a braceless if is an allowed early-exit at the top of a
     * method, constructor, or lambda body. The exception requires:
     * <ol>
     *   <li>The body is a single exit statement (return/throw/continue/break)</li>
     *   <li>There is no else clause</li>
     *   <li>The parent is an SLIST of a method/constructor/lambda (not a loop)</li>
     *   <li>All preceding siblings are also braceless early-exit ifs</li>
     * </ol>
     */
    private boolean isAllowedEarlyExit(DetailAST ifAst) {
        // Must have no else
        if (ifAst.findFirstToken(TokenTypes.LITERAL_ELSE) != null) {
            return false;
        }

        // Body must be a single exit statement
        DetailAST body = getBlockAfterRparen(ifAst);
        if (body == null || !isExitStatement(body.getType())) {
            return false;
        }

        // Parent must be SLIST
        DetailAST parent = ifAst.getParent();
        if (parent == null || parent.getType() != TokenTypes.SLIST) {
            return false;
        }

        // Grandparent must be method/constructor/lambda (not a loop)
        DetailAST grandparent = parent.getParent();
        if (grandparent == null || !isMethodLike(grandparent.getType())) {
            return false;
        }

        // All preceding siblings must also be braceless early-exit ifs
        DetailAST sibling = parent.getFirstChild();
        while (sibling != null && sibling != ifAst) {
            if (!isBracelessEarlyExitIf(sibling)) {
                return false;
            }
            sibling = sibling.getNextSibling();
        }

        return true;
    }

    /**
     * Checks whether a sibling AST node is a braceless early-exit if
     * (no else, body is an exit statement, no braces).
     */
    private boolean isBracelessEarlyExitIf(DetailAST ast) {
        if (ast.getType() != TokenTypes.LITERAL_IF) {
            return false;
        }
        if (ast.findFirstToken(TokenTypes.LITERAL_ELSE) != null) {
            return false;
        }
        DetailAST body = getBlockAfterRparen(ast);
        return body != null
            && body.getType() != TokenTypes.SLIST
            && isExitStatement(body.getType());
    }

    private boolean isExitStatement(int tokenType) {
        return tokenType == TokenTypes.LITERAL_RETURN
            || tokenType == TokenTypes.LITERAL_THROW
            || tokenType == TokenTypes.LITERAL_CONTINUE
            || tokenType == TokenTypes.LITERAL_BREAK;
    }

    private boolean isMethodLike(int tokenType) {
        return tokenType == TokenTypes.METHOD_DEF
            || tokenType == TokenTypes.CTOR_DEF
            || tokenType == TokenTypes.COMPACT_CTOR_DEF
            || tokenType == TokenTypes.LAMBDA;
    }

    /**
     * Returns the first child after RPAREN, which is the body of an if/while/for.
     */
    private DetailAST getBlockAfterRparen(DetailAST ast) {
        DetailAST child = ast.getFirstChild();
        while (child != null) {
            if (child.getType() == TokenTypes.RPAREN) {
                return child.getNextSibling();
            }
            child = child.getNextSibling();
        }
        return null;
    }
}
