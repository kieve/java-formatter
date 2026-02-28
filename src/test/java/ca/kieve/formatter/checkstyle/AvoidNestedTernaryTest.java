package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.CheckstyleTestUtil;
import ca.kieve.formatter.util.CheckstyleTestUtil.Violation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the bundled Checkstyle configuration — AvoidNestedTernaryCheck.
 */
class AvoidNestedTernaryTest {
    @Test
    void detectsNestedTernary() {
        // language=Java
        // @formatter:off
		String input = """
				package com.example;

				public class Foo {
				    int x = (a == b) ? -1 : (c == d) ? 0 : 1;
				}
				""";
		// @formatter:on

        List<Violation> violations = CheckstyleTestUtil.lint(input);
        assertEquals(1, violations.size());
        assertEquals(
            "AvoidNestedTernaryCheck",
            violations.get(0).checkName());
        assertEquals(4, violations.get(0).line());
    }

    @Test
    void detectsNestedTernaryInCondition() {
        // language=Java
        // @formatter:off
		String input = """
				package com.example;

				public class Foo {
				    int x = (a > 0 ? true : false) ? 1 : 2;
				}
				""";
		// @formatter:on

        List<Violation> violations = CheckstyleTestUtil.lint(input);
        assertEquals(1, violations.size());
        assertEquals(
            "AvoidNestedTernaryCheck",
            violations.get(0).checkName());
    }

    @Test
    void detectsDeeplyNestedTernary() {
        // language=Java
        // @formatter:off
		String input = """
				package com.example;

				public class Foo {
				    int x = a ? b ? c ? 1 : 2 : 3 : 4;
				}
				""";
		// @formatter:on

        List<Violation> violations = CheckstyleTestUtil.lint(input);
        // b?...:3 is nested in a?...:4, and c?1:2 is nested in b?...:3
        assertEquals(2, violations.size());
    }

    @Test
    void acceptsSimpleTernary() {
        // language=Java
        // @formatter:off
		String input = """
				package com.example;

				public class Foo {
				    int x = (a == b) ? 1 : 2;
				}
				""";
		// @formatter:on

        List<Violation> violations = CheckstyleTestUtil.lint(input);
        assertTrue(violations.isEmpty());
    }

    @Test
    void acceptsMultipleSeparateTernaries() {
        // language=Java
        // @formatter:off
		String input = """
				package com.example;

				public class Foo {
				    int x = a ? 1 : 2;
				    int y = b ? 3 : 4;
				}
				""";
		// @formatter:on

        List<Violation> violations = CheckstyleTestUtil.lint(input);
        assertTrue(violations.isEmpty());
    }
}
