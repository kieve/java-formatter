package ca.kieve.formatter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility methods for formatter tests.
 * <p>
 * Provides helpers for loading test fixture files from resources and
 * normalizing line endings for consistent comparisons.
 */
public final class FormatterTestUtil {
    private FormatterTestUtil() {}

    /**
     * Load a test fixture file from src/test/resources/fixtures/.
     *
     * @param path path relative to the fixtures directory, e.g. "braces/Input.java"
     * @return file contents with Unix line endings
     */
    public static String loadFixture(String path) throws IOException {
        String resourcePath = "/fixtures/" + path;
        try (InputStream is = FormatterTestUtil.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Fixture not found: " + resourcePath);
            }
            return normalizeLineEndings(new String(is.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Normalize line endings to Unix style (\n).
     */
    public static String normalizeLineEndings(String text) {
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }
}
