package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for the bundled Checkstyle configuration — RequireBracesCheck.
 */
class RequireBracesTest extends CheckstyleTestBase {
    RequireBracesTest() {
        super("require-braces/", "RequireBracesCheck");
    }

    // --- Violation cases ---

    @Test
    void flagsBracelessIf() throws IOException {
        assertViolation("braceless-if.java", "'if'");
    }

    @Test
    void flagsBracelessElse() throws IOException {
        assertViolation("braceless-else.java", "'else'");
    }

    @Test
    void flagsBracelessWhile() throws IOException {
        assertViolation("braceless-while.java", "'while'");
    }

    @Test
    void flagsBracelessFor() throws IOException {
        assertViolation("braceless-for.java", "'for'");
    }

    @Test
    void flagsBracelessForEach() throws IOException {
        assertViolation("braceless-for-each.java", "'for'");
    }

    @Test
    void flagsBracelessDoWhile() throws IOException {
        assertViolation("braceless-do-while.java", "'do'");
    }

    @Test
    void flagsBracelessElseIf() throws IOException {
        assertViolation("braceless-else-if.java", "'if'");
    }

    @Test
    void flagsEarlyExitAfterNonExitStatement() throws IOException {
        assertViolation("early-exit-after-non-exit.java", "'if'");
    }

    @Test
    void flagsEarlyExitAfterBracedIf() throws IOException {
        assertViolation("early-exit-after-braced-if.java", "'if'");
    }

    @Test
    void flagsEarlyExitWithElse() throws IOException {
        // Both the if and else are braceless
        assertViolations("early-exit-with-else.java", 2);
    }

    @Test
    void flagsNonExitBodyAtTop() throws IOException {
        assertViolation("non-exit-body-at-top.java", "'if'");
    }

    @Test
    void flagsEarlyExitInLoop() throws IOException {
        assertViolation("early-exit-in-loop.java", "'if'");
    }

    @Test
    void detectsMultipleViolations() throws IOException {
        assertViolations("multiple-violations.java", 3);
    }

    // --- Clean cases ---

    @Test
    void acceptsBracedIfElse() throws IOException {
        assertClean("braced-if-else-clean.java");
    }

    @Test
    void acceptsBracedWhile() throws IOException {
        assertClean("braced-while-clean.java");
    }

    @Test
    void acceptsBracedFor() throws IOException {
        assertClean("braced-for-clean.java");
    }

    @Test
    void acceptsBracedDoWhile() throws IOException {
        assertClean("braced-do-while-clean.java");
    }

    @Test
    void acceptsBracedElseIf() throws IOException {
        assertClean("braced-else-if-clean.java");
    }

    @Test
    void acceptsEarlyExitReturn() throws IOException {
        assertClean("early-exit-return-clean.java");
    }

    @Test
    void acceptsEarlyExitThrow() throws IOException {
        assertClean("early-exit-throw-clean.java");
    }

    @Test
    void acceptsMultipleEarlyExits() throws IOException {
        assertClean("multiple-early-exits-clean.java");
    }

    @Test
    void acceptsEarlyExitInConstructor() throws IOException {
        assertClean("early-exit-in-constructor-clean.java");
    }

    @Test
    void acceptsEarlyExitInLambda() throws IOException {
        assertClean("early-exit-in-lambda-clean.java");
    }

    @Test
    void acceptsEmptyMethod() throws IOException {
        assertClean("empty-method-clean.java");
    }
}
