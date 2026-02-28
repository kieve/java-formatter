# java-formatter

A custom Java auto-formatter built as a Gradle convention plugin. It applies an Eclipse JDT
baseline formatter followed by custom formatting rules implemented with JavaParser.

## How It Works

This project is a **Gradle plugin** (`ca.kieve.java-formatter`). When applied to a project, it:

1. Applies the Spotless Gradle plugin
2. Configures Eclipse JDT formatter as the baseline (config generated from `src/main/formatter/eclipse-formatter.yaml`)
3. Applies custom formatting rules on top via `CustomFormatterStep`

Spotless provides the `spotlessApply` (auto-format) and `spotlessCheck` (CI validation) tasks.

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
- `./gradlew spotlessApply` — auto-format all Java source files
- `./gradlew spotlessCheck` — verify formatting (fails if files need changes)

## Project Structure

```
src/main/java/ca/kieve/formatter/
    JavaFormatterPlugin.java        — Gradle plugin entry point
    FormatConfig.java               — Configuration (line length, etc.)
    step/
        CustomFormatterStep.java    — Main Spotless FormatterStep (chains all custom rules)
    printer/                        — Custom pretty printer visitors (future)
    rules/                          — Individual formatting rules (future)
src/main/formatter/
    eclipse-formatter.yaml          — Eclipse JDT config (human-editable source of truth)
src/test/java/ca/kieve/formatter/
    FormatterTestUtil.java          — Test helpers (fixture loading, line ending normalization)
    step/
        CustomFormatterStepTest.java
    rules/                          — Rule-level unit tests (future)
src/test/resources/fixtures/        — Input/expected Java files for comparison tests
```

## Architecture

- **Spotless** handles file discovery, caching, and Gradle task integration
- **Eclipse JDT** provides baseline formatting (indentation, spacing, braces)
- **JavaParser** provides AST parsing for custom rules that need structural understanding
- **Custom rules** are `String -> String` transformations chained inside `CustomFormatterStep`

Each custom rule should be independently testable with simple input/output string pairs.

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

## Adding a New Formatting Rule

1. Create the rule class in `ca.kieve.formatter.rules` — a static method taking `String` and returning `String`
2. Wire it into `CustomFormatterStep.applyCustomRules()` in the rule chain
3. Add unit tests in `ca.kieve.formatter.rules` with input/expected string pairs
4. Include a `respectsFormatterOffTags()` test that verifies content inside `// @formatter:off` / `// @formatter:on` blocks is preserved (run through `CustomFormatterStep.applyCustomRules()`)
5. Optionally add fixture files in `src/test/resources/fixtures/` for larger test cases

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
- **JUnit 5** for testing
