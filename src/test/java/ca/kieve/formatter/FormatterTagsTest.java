package ca.kieve.formatter;

import org.junit.jupiter.api.Test;

import ca.kieve.formatter.FormatterTags.ProtectedSource;

import java.io.IOException;

import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatterTagsTest {
    private static final String FIXTURE_DIR = "formatter-tags/";

    @Test
    void singleProtectedBlock() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "single-protected-block.java");

        ProtectedSource ps = FormatterTags.protect(input);

        // The protected block should be replaced with a placeholder
        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        // Original content should not appear in protected source
        assertTrue(!ps.source().contains("protected content"));
        // Restoring should give back the original
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void multipleProtectedBlocks() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "multiple-protected-blocks.java");

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(ps.source().contains("// __PROTECTED_1__"));
        assertTrue(ps.source().contains("middle"));
        assertEquals(2, ps.protectedBlocks().size());
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void unclosedFormatterOff() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "unclosed-formatter-off.java");

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(!ps.source().contains("protected to end"));
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void noFormatterTags() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "no-formatter-tags.java");

        ProtectedSource ps = FormatterTags.protect(input);

        assertEquals(input, ps.source());
        assertTrue(ps.protectedBlocks().isEmpty());
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void nestedFormatterOffInsideBlock() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "nested-formatter-off.java");

        ProtectedSource ps = FormatterTags.protect(input);

        // The nested off tag should be absorbed into the single block
        assertEquals(1, ps.protectedBlocks().size());
        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void indentedFormatterTags() throws IOException {
        String input = loadFixture(FIXTURE_DIR + "indented-formatter-tags.java");

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(!ps.source().contains("int   x   =   1;"));
        assertEquals(input, ps.restore(ps.source()));
    }
}
