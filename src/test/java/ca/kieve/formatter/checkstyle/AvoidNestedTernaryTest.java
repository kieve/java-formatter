package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.CheckstyleTestUtil.Violation;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the bundled Checkstyle configuration — AvoidNestedTernaryCheck.
 */
class AvoidNestedTernaryTest extends CheckstyleTestBase {
    AvoidNestedTernaryTest() {
        super("avoid-nested-ternary/", "AvoidNestedTernaryCheck");
    }

    @Test
    void detectsNestedTernary() throws IOException {
        List<Violation> violations = lint("nested-ternary.java");
        assertEquals(1, violations.size());
        assertEquals("AvoidNestedTernaryCheck", violations.get(0).checkName());
        assertEquals(4, violations.get(0).line());
    }

    @Test
    void detectsNestedTernaryInCondition() throws IOException {
        assertViolation("nested-in-condition.java");
    }

    @Test
    void detectsDeeplyNestedTernary() throws IOException {
        // b?...:3 is nested in a?...:4, and c?1:2 is nested in b?...:3
        assertViolations("deeply-nested.java", 2);
    }

    @Test
    void acceptsSimpleTernary() throws IOException {
        assertClean("simple-ternary-clean.java");
    }

    @Test
    void acceptsMultipleSeparateTernaries() throws IOException {
        assertClean("separate-ternaries-clean.java");
    }
}
