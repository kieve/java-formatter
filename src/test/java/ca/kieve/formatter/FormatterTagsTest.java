package ca.kieve.formatter;

import ca.kieve.formatter.FormatterTags.ProtectedSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatterTagsTest {

    @Test
    void singleProtectedBlock() {
        String input = """
                before
                // @formatter:off
                protected content
                // @formatter:on
                after
                """;

        ProtectedSource ps = FormatterTags.protect(input);

        // The protected block should be replaced with a placeholder
        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        // Original content should not appear in protected source
        assertTrue(!ps.source().contains("protected content"));
        // Restoring should give back the original
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void multipleProtectedBlocks() {
        String input = """
                before
                // @formatter:off
                block one
                // @formatter:on
                middle
                // @formatter:off
                block two
                // @formatter:on
                after
                """;

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(ps.source().contains("// __PROTECTED_1__"));
        assertTrue(ps.source().contains("middle"));
        assertEquals(2, ps.protectedBlocks().size());
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void unclosedFormatterOff() {
        String input = """
                before
                // @formatter:off
                protected to end
                more content""";

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(!ps.source().contains("protected to end"));
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void noFormatterTags() {
        String input = """
                public class Foo {
                    int x;
                }
                """;

        ProtectedSource ps = FormatterTags.protect(input);

        assertEquals(input, ps.source());
        assertTrue(ps.protectedBlocks().isEmpty());
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void nestedFormatterOffInsideBlock() {
        String input = """
                before
                // @formatter:off
                first off
                // @formatter:off
                still off
                // @formatter:on
                after
                """;

        ProtectedSource ps = FormatterTags.protect(input);

        // The nested off tag should be absorbed into the single block
        assertEquals(1, ps.protectedBlocks().size());
        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertEquals(input, ps.restore(ps.source()));
    }

    @Test
    void indentedFormatterTags() {
        String input = """
                public class Foo {
                    // @formatter:off
                    int   x   =   1;
                    // @formatter:on
                }
                """;

        ProtectedSource ps = FormatterTags.protect(input);

        assertTrue(ps.source().contains("// __PROTECTED_0__"));
        assertTrue(!ps.source().contains("int   x   =   1;"));
        assertEquals(input, ps.restore(ps.source()));
    }
}
