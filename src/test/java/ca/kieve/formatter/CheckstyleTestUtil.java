package ca.kieve.formatter;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Test utility that runs Checkstyle linting in-process against a Java source
 * string using the bundled {@code checkstyle.xml} configuration.
 * <p>
 * Similar to {@link DirectFormatterTestUtil} for Eclipse JDT formatting tests.
 */
public final class CheckstyleTestUtil {
	public record Violation(int line, String message, String checkName) {
	}

	private CheckstyleTestUtil() {
	}

	/**
	 * Lint a Java source string through Checkstyle using the bundled config.
	 *
	 * @param javaSource Java source code to lint
	 * @return list of violations found (empty if source is clean)
	 */
	public static List<Violation> lint(String javaSource) {
		try {
			Path tempFile = Files.createTempFile("checkstyle-test", ".java");
			try {
				Files.writeString(tempFile, javaSource);
				return runChecker(tempFile);
			} finally {
				Files.deleteIfExists(tempFile);
			}
		} catch (CheckstyleException e) {
			throw new RuntimeException("Checkstyle lint failed", e);
		} catch (Exception e) {
			throw new RuntimeException("Checkstyle lint failed", e);
		}
	}

	private static List<Violation> runChecker(Path sourceFile)
			throws Exception {
		try (
			InputStream configStream = CheckstyleTestUtil.class
				.getResourceAsStream("/checkstyle.xml")) {
			if (configStream == null) {
				throw new IllegalStateException(
					"checkstyle.xml not found on classpath.");
			}

			Configuration config = ConfigurationLoader.loadConfiguration(
				new InputSource(configStream),
				new PropertiesExpander(new Properties()),
				ConfigurationLoader.IgnoredModulesOptions.OMIT);

			Checker checker = new Checker();
			checker.setModuleClassLoader(Checker.class.getClassLoader());
			checker.configure(config);

			List<Violation> violations = new ArrayList<>();
			checker.addListener(new AuditListener() {
				@Override
				public void auditStarted(AuditEvent event) {
				}

				@Override
				public void auditFinished(AuditEvent event) {
				}

				@Override
				public void fileStarted(AuditEvent event) {
				}

				@Override
				public void fileFinished(AuditEvent event) {
				}

				@Override
				public void addError(AuditEvent event) {
					String sourceName = event.getSourceName();
					String checkName = sourceName.substring(
						sourceName.lastIndexOf('.') + 1);
					violations.add(new Violation(
						event.getLine(),
						event.getMessage(),
						checkName));
				}

				@Override
				public void addException(
						AuditEvent event,
						Throwable throwable) {
					throw new RuntimeException(
						"Checkstyle exception", throwable);
				}
			});

			checker.process(List.of(sourceFile.toFile()));
			checker.destroy();

			return violations;
		}
	}
}
