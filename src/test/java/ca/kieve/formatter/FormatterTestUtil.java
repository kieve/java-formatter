package ca.kieve.formatter;

import org.gradle.testkit.runner.GradleRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility methods for formatter tests.
 * <p>
 * Provides helpers for running the full formatting pipeline (Eclipse JDT +
 * custom rules) via Gradle TestKit, as well as loading test fixture files
 * and normalizing line endings.
 */
public final class FormatterTestUtil {
    private static final String BUILD_GRADLE = """
            plugins {
                id 'java'
                id 'com.diffplug.spotless'
            }
            repositories {
                mavenCentral()
            }
            spotless {
                java {
                    target 'src/*/java/**/*.java'
                    eclipse().configFile(file('eclipse-config.xml'))
                }
            }
            """;

    private FormatterTestUtil() {}

    private static final String CLASS_NAME = "FormatterTest";
    private static final String RELATIVE_PATH = "src/main/java/" + CLASS_NAME + ".java";

    /**
     * Format a Java source string through the full Eclipse JDT formatter pipeline.
     * <p>
     * Creates a temporary Gradle project in {@code testProjectDir}, writes the
     * source as {@code src/main/java/FormatterTest.java}, runs {@code spotlessApply},
     * and returns the formatted result.
     *
     * @param testProjectDir a JUnit {@code @TempDir} unique to the calling test
     * @param javaSource     unformatted Java source code
     * @return the formatted source with normalized (Unix) line endings
     */
    public static String formatJava(Path testProjectDir, String javaSource)
            throws IOException {
        writeFile(testProjectDir, "settings.gradle", "");
        writeFile(testProjectDir, "build.gradle", BUILD_GRADLE);
        copyResource("/eclipse-formatter.xml", testProjectDir.resolve("eclipse-config.xml"));

        writeFile(testProjectDir, RELATIVE_PATH, javaSource);

        GradleRunner.create()
                .withProjectDir(testProjectDir.toFile())
                .withPluginClasspath()
                .withArguments("spotlessApply")
                .build();

        return normalizeLineEndings(
                Files.readString(testProjectDir.resolve(RELATIVE_PATH)));
    }

    /**
     * Write a file into the test project directory, creating parent dirs as needed.
     */
    public static void writeFile(Path testProjectDir, String relativePath, String content)
            throws IOException {
        Path file = testProjectDir.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }

    /**
     * Copy a classpath resource to a target path.
     */
    public static void copyResource(String resourcePath, Path target) throws IOException {
        try (InputStream is = FormatterTestUtil.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Files.createDirectories(target.getParent());
            Files.write(target, is.readAllBytes());
        }
    }

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
