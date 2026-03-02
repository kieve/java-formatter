package ca.kieve.formatter.checkstyle;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class UnusedImportsTest extends CheckstyleTestBase {
    UnusedImportsTest() {
        super("unused-imports/", "UnusedImportsCheck");
    }

    @Test
    void detectsUnusedImport() throws IOException {
        assertViolation("unused-import.java");
    }

    @Test
    void acceptsAllUsedImports() throws IOException {
        assertClean("all-used-clean.java");
    }

    @Test
    void detectsMultipleUnusedImports() throws IOException {
        assertViolations("multiple-unused.java", 3);
    }
}
