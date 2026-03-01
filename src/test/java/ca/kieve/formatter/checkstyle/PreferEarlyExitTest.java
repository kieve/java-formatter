package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for the bundled Checkstyle configuration — PreferEarlyExitCheck.
 */
class PreferEarlyExitTest extends CheckstyleTestBase {
    PreferEarlyExitTest() {
        super("prefer-early-exit/", "PreferEarlyExitCheck");
    }

    @Test
    void flagsIfElseInMethod() throws IOException {
        assertViolation("if-else-in-method.java", "early return");
    }

    @Test
    void flagsIfElseInConstructor() throws IOException {
        assertViolation("if-else-in-constructor.java", "early return");
    }

    @Test
    void flagsIfElseInForLoop() throws IOException {
        assertViolation("if-else-in-for-loop.java", "early continue");
    }

    @Test
    void flagsIfElseInWhileLoop() throws IOException {
        assertViolation("if-else-in-while-loop.java", "early continue");
    }

    @Test
    void flagsIfElseInDoWhileLoop() throws IOException {
        assertViolation("if-else-in-do-while-loop.java", "early continue");
    }

    @Test
    void flagsIfElseInLambda() throws IOException {
        assertViolation("if-else-in-lambda.java", "early return");
    }

    @Test
    void flagsIfElseIfChain() throws IOException {
        assertViolation("if-else-if-chain.java", "early return");
    }

    @Test
    void flagsWrappingIfInMethod() throws IOException {
        assertViolation("wrapping-if-in-method.java", "Invert", "early return");
    }

    @Test
    void flagsWrappingIfInLoop() throws IOException {
        assertViolation("wrapping-if-in-loop.java", "early continue");
    }

    @Test
    void flagsIfAsOnlyStatement() throws IOException {
        assertViolation("if-as-only-statement.java", "Invert");
    }

    @Test
    void detectsMultipleViolations() throws IOException {
        assertViolations("multiple-violations.java", 2);
    }

    @Test
    void acceptsGuardReturn() throws IOException {
        assertClean("guard-return.java");
    }

    @Test
    void acceptsGuardThrow() throws IOException {
        assertClean("guard-throw.java");
    }

    @Test
    void acceptsGuardContinue() throws IOException {
        assertClean("guard-continue.java");
    }

    @Test
    void acceptsGuardBreak() throws IOException {
        assertClean("guard-break.java");
    }

    @Test
    void acceptsGuardReturnValue() throws IOException {
        assertClean("guard-return-value.java");
    }

    @Test
    void acceptsIfNotLast() throws IOException {
        assertClean("if-not-last.java");
    }

    @Test
    void acceptsIfElseNotLast() throws IOException {
        assertClean("if-else-not-last.java");
    }

    @Test
    void acceptsIfInNestedBlock() throws IOException {
        assertClean("if-in-nested-block.java");
    }

    @Test
    void acceptsIfInTryBlock() throws IOException {
        assertClean("if-in-try-block.java");
    }

    @Test
    void acceptsCleanMethod() throws IOException {
        assertClean("clean-method.java");
    }

    @Test
    void flagsIfElseInSwitchCase() throws IOException {
        assertViolation("if-else-in-switch-case.java", "early break");
    }
}
