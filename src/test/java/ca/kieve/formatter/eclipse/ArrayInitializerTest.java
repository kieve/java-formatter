package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.util.FormatterTestBase;

import java.io.IOException;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;

/**
 * Tests for array initializer formatting behavior.
 *
 * Verifies:
 * - Short arrays stay on one line
 * - Long arrays wrap when exceeding 100-char limit
 * - When wrapped, closing brace goes on its own line
 * - Author multiline choices are preserved
 */
class ArrayInitializerTest extends FormatterTestBase {
    ArrayInitializerTest() {
        super("array-initializer/", s -> formatJava(s));
    }

    @Test
    void shortArrayFitsOnOneLine() throws IOException {
        test("one-line-unchanged.java");
    }

    @Test
    void longArrayWraps() throws IOException {
        test("wrap-elements-input.java", "wrap-elements-expected.java");
    }

    @Test
    void authorMultilinePreserved() throws IOException {
        test("author-multiline-unchanged.java");
    }

    @Test
    void nestedArrayInitializers() throws IOException {
        test("nested-unchanged.java");
    }
}
