# java-formatter

A custom Java auto-formatter and linter built as a Gradle convention plugin. It applies an Eclipse
JDT baseline formatter followed by custom formatting rules implemented with JavaParser, plus
Checkstyle for linting rules like wildcard import detection.

## Setup

Include this project as a **git submodule**, then wire it into your build via Gradle composite builds:

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

group = 'com.example'  // required: used as the default project-specific import group
```

## Usage

```bash
./gradlew format        # auto-format all Java source files
./gradlew formatCheck   # verify formatting (fails if files need changes)
./gradlew lint          # lint main source files
./gradlew lintTest      # lint test source files
```

## Configuration

The plugin reads an optional `kieve-formatter.yaml` file from your project root. All fields are
optional — missing fields use their default values.

### Example

```yaml
maxLineLength: 100

importLayout:
  - catch-all
  - ["ca.kieve."]
  - ["javax.", "java."]
  - static-catch-all
```

### Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `maxLineLength` | integer | `100` | Maximum line length for wrapping rules |
| `importLayout` | list | *(see below)* | Import grouping and ordering |

### Import Layout

The `importLayout` field defines how imports are grouped and ordered. Each entry is separated by
a blank line in the output. Entries are one of four types:

| Entry type | YAML syntax | Description |
|------------|-------------|-------------|
| Non-static catch-all | `catch-all` | Matches all non-static imports not matched by another group |
| Static catch-all | `static-catch-all` | Matches all static imports not matched by another group |
| Non-static prefix group | `["javax.", "java."]` | Matches non-static imports starting with any listed prefix |
| Static prefix group | `static: ["org.mockito."]` | Matches static imports starting with any listed prefix |

#### Default Import Layout

When no `importLayout` is specified, the plugin uses the project's Gradle `group` property to
build the default layout. For example, a project with `group = 'com.example'` gets:

```yaml
importLayout:
  - catch-all                # all other non-static imports
  - ["com.example."]         # project-specific imports (derived from group)
  - ["javax.", "java."]      # javax.* and java.* imports (no blank line between)
  - static-catch-all         # all static imports
```

This produces output like:

```java
import org.slf4j.Logger;
import org.something.Bar;

import com.example.myapp.MyClass;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
```

If `group` is not set, the project-specific group is omitted from the default layout.

## Self-Formatting (Development)

This project uses a published version of itself from mavenLocal to format and lint its own code.

```bash
./gradlew publishToMavenLocal   # bootstrap: publish the plugin locally
./gradlew format                # format this project using the published plugin
```

After changing formatting or linting rules, re-publish before running `format` to apply the
updated rules to this project.

## Checkstyle Rules

The plugin applies [Checkstyle](https://checkstyle.org/) for linting. Rules are bundled in the
plugin and run automatically via `checkstyleMain` / `checkstyleTest`.

| Rule | Description |
|------|-------------|
| [`AvoidStarImport`](https://checkstyle.org/checks/imports/avoidstarimport.html) | Bans wildcard imports (`import java.util.*`) |
