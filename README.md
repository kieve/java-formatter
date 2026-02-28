# java-formatter

A custom Java auto-formatter built as a Gradle convention plugin. It applies an Eclipse JDT
baseline formatter followed by custom formatting rules implemented with JavaParser.

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
```

## Usage

```bash
./gradlew spotlessApply   # auto-format all Java source files
./gradlew spotlessCheck   # verify formatting (fails if files need changes)
```

## Configuration

The plugin reads an optional `kieve-formatter.yaml` file from your project root. All fields are
optional — missing fields use their default values.

### Example

```yaml
maxLineLength: 100
banWildcardImports: true

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
| `banWildcardImports` | boolean | `true` | Whether to expand wildcard imports |
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

```yaml
importLayout:
  - catch-all                # all other non-static imports
  - ["ca.kieve."]            # ca.kieve.* imports
  - ["javax.", "java."]      # javax.* and java.* imports (no blank line between)
  - static-catch-all         # all static imports
```

This produces output like:

```java
import com.example.Foo;
import org.something.Bar;

import ca.kieve.myapp.MyClass;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
```
