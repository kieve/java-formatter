package ca.kieve.formatter;

import com.diffplug.gradle.spotless.SpotlessExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.quality.Checkstyle;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskProvider;

import ca.kieve.formatter.step.CustomFormatterStep;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Gradle plugin that applies Spotless with an Eclipse baseline formatter
 * and custom formatting rules on top, plus Checkstyle for linting.
 * <p>
 * Consuming projects apply this plugin and get {@code spotlessApply} /
 * {@code spotlessCheck} tasks for formatting and {@code checkstyleMain} /
 * {@code checkstyleTest} tasks for linting automatically.
 */
public class JavaFormatterPlugin implements Plugin<Project> {
    private static final String ECLIPSE_CONFIG_RESOURCE = "/eclipse-formatter.xml";
    private static final String CHECKSTYLE_CONFIG_RESOURCE = "/checkstyle.xml";

    @Override
    public void apply(Project project) {
        File outputDir = project.getLayout().getBuildDirectory()
            .dir("java-formatter").get().getAsFile();
        File eclipseConfig = new File(outputDir, "eclipse-formatter.xml");
        File checkstyleConfig = new File(outputDir, "checkstyle.xml");

        TaskProvider<ExtractConfigTask> extractTask = project.getTasks()
            .register("extractFormatterConfig", ExtractConfigTask.class, task -> {
                task.setGroup("formatting");
                task.setDescription(
                    "Extract formatter and linter config files from the plugin JAR");
                task.setOutputDir(outputDir);
            });

        project.getPlugins().apply("com.diffplug.spotless");

        SpotlessExtension spotless = project.getExtensions()
            .getByType(SpotlessExtension.class);

        FormatConfig config = FormatConfigLoader.loadFromDirectory(
            project.getProjectDir());

        spotless.java(java -> {
            java.target("src/*/java/**/*.java");
            java.eclipse().configFile(eclipseConfig);
            java.addStep(CustomFormatterStep.create(config));
        });

        project.getPlugins().apply("checkstyle");
        CheckstyleExtension checkstyle = project.getExtensions()
            .getByType(CheckstyleExtension.class);
        checkstyle.setConfigFile(checkstyleConfig);
        checkstyle.setToolVersion("10.22.0");

        // Add our plugin classes to each Checkstyle task's tool classpath so
        // custom checks are discoverable at runtime.
        try {
            File pluginClasses = new File(
                JavaFormatterPlugin.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            project.getTasks().withType(Checkstyle.class).configureEach(
                task -> task.setCheckstyleClasspath(
                    task.getCheckstyleClasspath()
                        .plus(project.files(pluginClasses))));
        } catch (URISyntaxException e) {
            throw new GradleException(
                "Failed to locate plugin classes for Checkstyle classpath",
                e);
        }

        // Wire extraction to run before any spotless or checkstyle task
        project.getTasks().configureEach(task -> {
            String name = task.getName();
            if (name.startsWith("spotless") || name.startsWith("checkstyle")) {
                task.dependsOn(extractTask);
            }
        });

        project.getTasks().register("format", task -> {
            task.dependsOn("spotlessApply");
            task.setGroup("formatting");
            task.setDescription("Auto-format all Java source files");
        });
        project.getTasks().register("formatCheck", task -> {
            task.dependsOn("spotlessCheck");
            task.setGroup("formatting");
            task.setDescription("Verify formatting (fails if files need changes)");
        });
        project.getTasks().register("lint", task -> {
            task.dependsOn("checkstyleMain");
            task.setGroup("verification");
            task.setDescription("Lint main source files");
        });
        project.getTasks().register("lintTest", task -> {
            task.dependsOn("checkstyleTest");
            task.setGroup("verification");
            task.setDescription("Lint test source files");
        });
    }

    public static abstract class ExtractConfigTask extends DefaultTask {
        private File outputDir;

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        @TaskAction
        public void extract() {
            outputDir.mkdirs();

            extractResource(ECLIPSE_CONFIG_RESOURCE, outputDir);
            extractResource(CHECKSTYLE_CONFIG_RESOURCE, outputDir);
        }

        private void extractResource(String resourcePath, File outputDir) {
            String fileName = resourcePath.substring(
                resourcePath.lastIndexOf('/') + 1);
            File outputFile = new File(outputDir, fileName);

            try (
                InputStream is = JavaFormatterPlugin.class
                    .getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new GradleException(
                        "Resource not found: " + resourcePath);
                }
                Files.copy(
                    is,
                    outputFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new GradleException(
                    "Failed to extract " + resourcePath,
                    e);
            }
        }
    }
}
