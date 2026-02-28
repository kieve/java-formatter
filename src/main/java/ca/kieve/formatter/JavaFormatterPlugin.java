package ca.kieve.formatter;

import ca.kieve.formatter.step.CustomFormatterStep;
import com.diffplug.gradle.spotless.SpotlessExtension;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Gradle plugin that applies Spotless with an Eclipse baseline formatter
 * and custom formatting rules on top.
 * <p>
 * Consuming projects apply this plugin and get {@code spotlessApply} /
 * {@code spotlessCheck} tasks automatically.
 */
public class JavaFormatterPlugin implements Plugin<Project> {
    private static final String ECLIPSE_CONFIG_RESOURCE = "/eclipse-formatter.xml";

    @Override
    public void apply(Project project) {
        project.getPlugins().apply("com.diffplug.spotless");

        SpotlessExtension spotless = project.getExtensions()
                .getByType(SpotlessExtension.class);

        File eclipseConfig = extractResource(project, ECLIPSE_CONFIG_RESOURCE);
        FormatConfig config = FormatConfigLoader.loadFromDirectory(
                project.getProjectDir());

        spotless.java(java -> {
            java.target("src/*/java/**/*.java");
            java.eclipse().configFile(eclipseConfig);
            java.addStep(CustomFormatterStep.create(config));
        });
    }

    private File extractResource(Project project, String resourcePath) {
        File outputDir = project.getLayout().getBuildDirectory()
                .dir("java-formatter").get().getAsFile();
        outputDir.mkdirs();

        String fileName = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
        File outputFile = new File(outputDir, fileName);

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new GradleException("Resource not found: " + resourcePath);
            }
            Files.copy(is, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new GradleException("Failed to extract " + resourcePath, e);
        }

        return outputFile;
    }
}
