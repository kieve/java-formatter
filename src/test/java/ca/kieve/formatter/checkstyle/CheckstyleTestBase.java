package ca.kieve.formatter.checkstyle;

import ca.kieve.formatter.util.CheckstyleTestUtil;
import ca.kieve.formatter.util.CheckstyleTestUtil.Violation;
import ca.kieve.formatter.util.FormatterTestUtil;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base class for Checkstyle lint tests that use fixture files.
 */
abstract class CheckstyleTestBase {
    private final String fixtureDir;
    private final String checkName;

    CheckstyleTestBase(String fixtureDir, String checkName) {
        this.fixtureDir = fixtureDir;
        this.checkName = checkName;
    }

    protected List<Violation> lint(String fixture) throws IOException {
        return CheckstyleTestUtil.lint(
            FormatterTestUtil.loadFixture(fixtureDir + fixture));
    }

    /**
     * Assert exactly one violation with the expected check name. Optionally
     * verify the message contains each of the given substrings.
     */
    protected void assertViolation(String fixture, String... messageContains)
        throws IOException {
        List<Violation> violations = lint(fixture);
        assertEquals(1, violations.size());
        assertEquals(checkName, violations.get(0).checkName());
        for (String expected : messageContains) {
            assertTrue(
                violations.get(0).message().contains(expected),
                "Expected message containing '" + expected
                    + "' but got: " + violations.get(0).message());
        }
    }

    protected void assertViolations(String fixture, int count)
        throws IOException {
        assertEquals(count, lint(fixture).size());
    }

    protected void assertClean(String fixture) throws IOException {
        assertTrue(lint(fixture).isEmpty());
    }
}
