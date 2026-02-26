# java-formatter

A custom Java auto-formatter built as a Gradle convention plugin. It applies an Eclipse JDT
baseline formatter followed by custom formatting rules implemented with JavaParser.

## How It Works

This project is a **Gradle plugin** (`ca.kieve.java-formatter`). When applied to a project, it:

1. Applies the Spotless Gradle plugin
2. Configures Eclipse JDT formatter as the baseline (config bundled in `src/main/resources/eclipse-formatter.xml`)
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
src/main/resources/
    eclipse-formatter.xml           — Eclipse JDT baseline formatter configuration
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

## Build Commands

```
./gradlew build          # compile + test
./gradlew test           # run tests only
./gradlew clean build    # clean rebuild
```

## Adding a New Formatting Rule

1. Create the rule class in `ca.kieve.formatter.rules` — a static method taking `String` and returning `String`
2. Wire it into `CustomFormatterStep.State.toFormatter()` in the rule chain
3. Add unit tests in `ca.kieve.formatter.rules` with input/expected string pairs
4. Optionally add fixture files in `src/test/resources/fixtures/` for larger test cases

## Tech Stack

- **Java 25**
- **Gradle 9.3.1** (wrapper included)
- **Spotless Plugin for Gradle 8.2.1**
- **JavaParser 3.28.0**
- **JUnit 5** for testing
