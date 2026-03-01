package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.CheckstyleTestUtil.Violation;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the bundled Checkstyle configuration — AvoidStarImport rule.
 */
class AvoidStarImportTest extends CheckstyleTestBase {
    AvoidStarImportTest() {
        super("avoid-star-import/", "AvoidStarImportCheck");
    }

    @Test
    void detectsWildcardImport() throws IOException {
        List<Violation> violations = lint("wildcard-import.java");
        assertEquals(1, violations.size());
        assertEquals("AvoidStarImportCheck", violations.get(0).checkName());
        assertEquals(3, violations.get(0).line());
    }

    @Test
    void detectsStaticWildcardImport() throws IOException {
        assertViolation("static-wildcard-import.java");
    }

    @Test
    void acceptsExplicitImport() throws IOException {
        assertClean("explicit-import-clean.java");
    }

    @Test
    void detectsMultipleWildcardImports() throws IOException {
        assertViolations("multiple-wildcards.java", 2);
    }
}
