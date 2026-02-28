package ca.kieve.formatter.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import ca.kieve.formatter.rules.style.QualifiedImportResolution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Gradle task that applies pre-format rules to Java source files.
 * These rules must run before Eclipse JDT and all other formatting steps.
 * <p>
 * When {@code checkOnly} is true, the task reports files that would change
 * and fails the build instead of writing them.
 */
public abstract class PreFormatTask extends DefaultTask {
    private boolean checkOnly;

    @Input
    public boolean isCheckOnly() {
        return checkOnly;
    }

    public void setCheckOnly(boolean checkOnly) {
        this.checkOnly = checkOnly;
    }

    @TaskAction
    public void run() {
        File projectDir = getProject().getProjectDir();
        List<Path> javaFiles = findJavaFiles(projectDir);
        List<Path> changedFiles = new ArrayList<>();

        for (Path file : javaFiles) {
            try {
                String original = Files.readString(file, StandardCharsets.UTF_8);
                String formatted = applyPreFormatRules(original);

                if (!formatted.equals(original)) {
                    if (checkOnly) {
                        changedFiles.add(file);
                    } else {
                        Files.writeString(
                            file,
                            formatted,
                            StandardCharsets.UTF_8);
                        getLogger().lifecycle(
                            "Pre-formatted: {}",
                            projectDir.toPath()
                                .relativize(file));
                    }
                }
            } catch (IOException e) {
                throw new GradleException(
                    "Failed to process " + file,
                    e);
            }
        }

        if (checkOnly && !changedFiles.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("The following files need pre-formatting:\n");
            for (Path file : changedFiles) {
                msg.append("  ")
                    .append(projectDir.toPath().relativize(file))
                    .append("\n");
            }
            msg.append("Run './gradlew format' to fix.");
            throw new GradleException(msg.toString());
        }
    }

    private static String applyPreFormatRules(String source) {
        return QualifiedImportResolution.apply(source);
    }

    private static List<Path> findJavaFiles(File projectDir) {
        List<Path> result = new ArrayList<>();
        Path srcDir = projectDir.toPath().resolve("src");
        if (!Files.isDirectory(srcDir)) {
            return result;
        }

        try {
            Files.walkFileTree(srcDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(
                    Path file,
                    BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        // Only include files under src/*/java/
                        Path relative = srcDir.relativize(file);
                        if (relative.getNameCount() >= 3
                            && relative.getName(1).toString()
                                .equals("java")) {
                            result.add(file);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new GradleException(
                "Failed to scan source files",
                e);
        }

        return result;
    }
}
