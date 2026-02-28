package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.CheckstyleTestUtil;
import ca.kieve.formatter.CheckstyleTestUtil.Violation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the bundled Checkstyle configuration — AvoidStarImport rule.
 */
class AvoidStarImportTest {
	@Test
	void detectsWildcardImport() {
		// language=Java
		// @formatter:off
		String input = """
				package com.example;

				import java.util.*;

				public class Foo {
				}
				""";
		// @formatter:on

		List<Violation> violations = CheckstyleTestUtil.lint(input);
		assertEquals(1, violations.size());
		assertEquals("AvoidStarImportCheck", violations.get(0).checkName());
		assertEquals(3, violations.get(0).line());
	}

	@Test
	void detectsStaticWildcardImport() {
		// language=Java
		// @formatter:off
		String input = """
				package com.example;

				import static org.junit.jupiter.api.Assertions.*;

				public class Foo {
				}
				""";
		// @formatter:on

		List<Violation> violations = CheckstyleTestUtil.lint(input);
		assertEquals(1, violations.size());
		assertEquals("AvoidStarImportCheck", violations.get(0).checkName());
	}

	@Test
	void acceptsExplicitImport() {
		// language=Java
		// @formatter:off
		String input = """
				package com.example;

				import java.util.List;

				public class Foo {
				}
				""";
		// @formatter:on

		List<Violation> violations = CheckstyleTestUtil.lint(input);
		assertTrue(violations.isEmpty());
	}

	@Test
	void detectsMultipleWildcardImports() {
		// language=Java
		// @formatter:off
		String input = """
				package com.example;

				import java.util.*;
				import java.io.*;

				public class Foo {
				}
				""";
		// @formatter:on

		List<Violation> violations = CheckstyleTestUtil.lint(input);
		assertEquals(2, violations.size());
	}
}
