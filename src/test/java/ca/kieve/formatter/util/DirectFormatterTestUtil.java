package ca.kieve.formatter.util;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import ca.kieve.formatter.step.CustomFormatterStep;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test utility that formats Java source in-process using Eclipse JDT
 * CodeFormatter + custom rules.
 * <p>
 * Much faster than the Gradle TestKit approach since it avoids spinning
 * up a full Gradle build for each test. Each thread gets its own
 * {@link CodeFormatter} instance via {@link ThreadLocal} since the
 * Eclipse JDT formatter is not thread-safe.
 */
public final class DirectFormatterTestUtil {
    private static final Map<String, String> FORMATTER_OPTIONS;

    static {
        Map<String, String> options = loadFormatterOptions();
        // Ensure Java compiler settings are present — the formatter requires these
        // to parse source correctly.
        options.putIfAbsent(JavaCore.COMPILER_SOURCE, "25");
        options.putIfAbsent(JavaCore.COMPILER_COMPLIANCE, "25");
        options.putIfAbsent(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, "25");
        FORMATTER_OPTIONS = Collections.unmodifiableMap(options);
    }

    private static final ThreadLocal<CodeFormatter> CODE_FORMATTER = ThreadLocal.withInitial(
        () -> ToolFactory.createCodeFormatter(
            FORMATTER_OPTIONS,
            ToolFactory.M_FORMAT_EXISTING));

    private DirectFormatterTestUtil() {
    }

    /**
     * Format a Java source string through Eclipse JDT + custom rules, in-process.
     *
     * @param javaSource unformatted Java source code
     * @return the formatted source with Unix line endings
     */
    public static String formatJava(String javaSource) {
        try {
            // Normalize to Unix line endings before formatting — the Eclipse JDT
            // formatter computes TextEdit offsets based on the source length, so
            // \r\n vs \n mismatches cause StringIndexOutOfBoundsException.
            String source = FormatterTestUtil.normalizeLineEndings(javaSource);

            // Eclipse JDT formatting
            TextEdit edit = CODE_FORMATTER.get().format(
                CodeFormatter.K_COMPILATION_UNIT,
                source,
                0,
                source.length(),
                0,
                "\n");
            if (edit != null) {
                Document document = new Document(source);
                edit.apply(document);
                source = document.get();
            }

            // Custom rules
            source = CustomFormatterStep.applyCustomRules(source);

            return FormatterTestUtil.normalizeLineEndings(source);
        } catch (Exception e) {
            throw new RuntimeException("Direct formatting failed", e);
        }
    }

    private static Map<String, String> loadFormatterOptions() {
        try (
            InputStream is = DirectFormatterTestUtil.class
                .getResourceAsStream("/eclipse-formatter.xml")) {
            if (is == null) {
                throw new IllegalStateException(
                    "eclipse-formatter.xml not found on classpath. "
                        + "Run ./gradlew generateEclipseConfig first.");
            }

            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(is);

            var settings = doc.getElementsByTagName("setting");
            Map<String, String> options = new HashMap<>();
            for (int i = 0; i < settings.getLength(); i++) {
                var element = settings.item(i);
                String id = element.getAttributes().getNamedItem("id").getNodeValue();
                String value = element.getAttributes().getNamedItem("value").getNodeValue();
                options.put(id, value);
            }
            return options;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load eclipse-formatter.xml", e);
        }
    }
}
