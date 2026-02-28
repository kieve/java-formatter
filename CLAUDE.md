# java-formatter

A custom Java auto-formatter and linter built as a Gradle convention plugin. It applies an Eclipse
JDT baseline formatter followed by custom formatting rules implemented with JavaParser, plus
Checkstyle for linting rules like wildcard import detection.

## How It Works

This project is a **Gradle plugin** (`ca.kieve.java-formatter`). When applied to a project, it:

1. Runs **pre-format rules** via `PreFormatTask` (e.g., qualified import resolution)
2. Applies the Spotless Gradle plugin
3. Configures Eclipse JDT formatter as the baseline (config generated from `src/main/formatter/eclipse-formatter.yaml`)
4. Applies custom formatting rules on top via `CustomFormatterStep`
5. Applies the Checkstyle plugin with bundled linting rules (e.g., `AvoidStarImport`)

Pre-format rules run before Spotless so their output (e.g., new imports) flows through Eclipse JDT
and the custom rule chain. Spotless provides the `spotlessApply` (auto-format) and `spotlessCheck`
(CI validation) tasks. Checkstyle provides the `checkstyleMain` and `checkstyleTest` tasks for linting.

## Usage in Consuming Projects

This project is included as a **git submodule**, then wired in via Gradle's composite build:

```gradle
// settings.gradle
pluginManagement {
    includeBuild('java-formatter')  // path to the git submodule
}
```

```gradle
// build.gradle
plugins {
    id 'java'
    id 'ca.kieve.java-formatter'
}
```

Then run:
- `./gradlew format` — auto-format all Java source files
- `./gradlew formatCheck` — verify formatting (fails if files need changes)
- `./gradlew lint` — lint main source files
- `./gradlew lintTest` — lint test source files

## Project Structure

```
src/main/java/ca/kieve/formatter/
    JavaFormatterPlugin.java        — Gradle plugin entry point
    FormatConfig.java               — Configuration (line length, etc.)
    step/
        CustomFormatterStep.java    — Main Spotless FormatterStep (chains all custom rules)
    task/
        PreFormatTask.java          — Gradle task for pre-format rules (runs before Spotless)
    printer/                        — Custom pretty printer visitors (future)
    rules/                          — Individual formatting rules
src/main/resources/
    checkstyle.xml                  — Checkstyle linting config (bundled in JAR)
src/main/formatter/
    eclipse-formatter.yaml          — Eclipse JDT config (human-editable source of truth)
src/test/java/ca/kieve/formatter/
    util/                           — Test utilities
        FormatterTestUtil.java      — Fixture loading, line ending normalization
        DirectFormatterTestUtil.java — In-process Eclipse JDT + custom rules formatting
        CheckstyleTestUtil.java     — In-process Checkstyle linting
    step/
        CustomFormatterStepTest.java
    rules/                          — Rule-level unit tests
src/test/resources/fixtures/        — Input/expected Java files for comparison tests
```

## Architecture

- **Pre-format rules** run as a separate Gradle task (`PreFormatTask`) before Spotless, for
  transformations that must happen before Eclipse JDT (e.g., qualified import resolution adds
  imports that Eclipse then formats)
- **Spotless** handles file discovery, caching, and Gradle task integration
- **Eclipse JDT** provides baseline formatting (indentation, spacing, braces)
- **JavaParser** provides AST parsing for custom rules that need structural understanding
- **Custom rules** are `String -> String` transformations chained inside `CustomFormatterStep`
- **Checkstyle** provides linting rules (e.g., `AvoidStarImport`) via `checkstyleMain`/`checkstyleTest` tasks

The formatting pipeline runs in this order:

```
preFormat → Eclipse JDT (via Spotless) → CustomFormatterStep rules
```

Each rule (pre-format or custom) should be independently testable with simple input/output string pairs.

## Eclipse Formatter Configuration

The Eclipse JDT formatter config is maintained as a **YAML file** (`src/main/formatter/eclipse-formatter.yaml`)
and converted to Eclipse's XML format at build time by the `generateEclipseConfig` Gradle task.

**How it works:** YAML keys are joined with `.` separators to produce flat Eclipse setting IDs.
For example:

```yaml
org:
  eclipse:
    jdt:
      core:
        formatter:
          tabulation:
            char: tab
```

becomes `<setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="tab"/>`.

- **Source of truth:** `src/main/formatter/eclipse-formatter.yaml` — edit this file
- **Generated output:** `build/generated-resources/eclipse-formatter.xml` — do not edit
- The generated XML is included in the JAR automatically via `sourceSets.main.resources`
- Values can be unquoted in YAML; the generator calls `.toString()` on all values
- Settings are sorted alphabetically in the generated XML (TreeMap)

## Build Commands

```
./gradlew build          # compile + test
./gradlew test           # run tests only
./gradlew clean build    # clean rebuild
```

## Self-Formatting

This project formats and lints its own code using a previously published version of itself
from mavenLocal. Bootstrap on first clone or after rule changes:

```
./gradlew publishToMavenLocal   # publish the plugin to ~/.m2/repository
./gradlew format                # now uses the published plugin to format this project
```

The build detects whether the plugin is available in mavenLocal automatically. If not found,
`./gradlew build` works normally without self-formatting, and a log message reminds you to
publish.

**Updating rules:** after changing formatting or linting rules, the new rules do not apply to
this project until you re-publish:

```
./gradlew publishToMavenLocal   # publish updated rules
./gradlew format                # now applies updated rules to this project
```

## Checkstyle Configuration

Checkstyle linting rules are defined in `src/main/resources/checkstyle.xml`. This file is bundled
in the plugin JAR and extracted at runtime (like the Eclipse formatter config). The plugin sets
`toolVersion` to a specific Checkstyle release for reproducibility.

To add a new Checkstyle rule, add a `<module>` entry inside the `<module name="TreeWalker">` block
in `checkstyle.xml`. See the [Checkstyle documentation](https://checkstyle.org/checks.html) for
available checks.

## Adding a New Linting Rule

1. Add the Checkstyle module to `src/main/resources/checkstyle.xml` inside the `TreeWalker` block
2. Test in a consuming project with `./gradlew lint` to verify the rule fires correctly

## Adding a New Formatting Rule

1. Create the rule class in `ca.kieve.formatter.rules` — a static method taking `String` and returning `String`
2. Wire it into `CustomFormatterStep.applyCustomRules()` in the rule chain
3. Add unit tests in `ca.kieve.formatter.rules` with fixture files in `src/test/resources/fixtures/`
4. Include a `respectsFormatterOffTags()` test that verifies content inside `// @formatter:off` / `// @formatter:on` blocks is preserved (run through `CustomFormatterStep.applyCustomRules()`)

## Adding a New Pre-Format Rule

Pre-format rules run before Eclipse JDT. Use these when the rule produces output that needs
further formatting by Eclipse (e.g., adding import statements that Eclipse will indent/sort).

1. Create the rule class in `ca.kieve.formatter.rules` — a static method taking `String` and returning `String`
2. Wire it into `PreFormatTask.applyPreFormatRules()`
3. Add unit tests in `ca.kieve.formatter.rules` with fixture files in `src/test/resources/fixtures/`
4. Include a `respectsFormatterOffTags()` test (run through `CustomFormatterStep.applyCustomRules()`)

Pre-format rules must call `FormatterTags.protect()` / `restore()` themselves since they run
outside `CustomFormatterStep`.

**Formatter tag protection:** `FormatterTags.protect()` replaces `// @formatter:off` ... `// @formatter:on` blocks with placeholder comments (`// __PROTECTED_N__`) before any rule runs, then `restore()` puts the original content back. This is handled centrally in `applyCustomRules()` — individual rules do not need to implement it. Comment-formatting rules must not modify lines matching `// __PROTECTED_\d+__`.

## Testing Tips

Java text blocks strip trailing whitespace from each line at compile time. When a test needs
blank lines that contain whitespace (e.g., to test `indent_empty_lines`), use the `\s` escape
sequence to preserve trailing spaces:

```java
String input = """
        public class Foo {
        \s\s\s\s
        }
        """;
```

The `\s` escape (Java 15+) produces a literal space that survives text block processing.

## Tech Stack

- **Java 25**
- **Gradle 9.3.1** (wrapper included)
- **Spotless Plugin for Gradle 8.2.1**
- **JavaParser 3.28.0**
- **Checkstyle 10.22.0** for linting
- **JUnit 5** for testing
