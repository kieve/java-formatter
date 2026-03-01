# Converting Inline Tests to Fixture-Based Tests

Guide for converting test classes that define Java source inline (text blocks) to use external
fixture files loaded from `src/test/resources/fixtures/`.

## Why Fixtures

- Keeps test classes short and focused on test logic
- Fixture files get proper syntax highlighting and editor support
- Easier to review and update test data independently
- Consistent pattern across the codebase

## Directory Structure

```
src/test/resources/fixtures/<rule-name>/
    <case>-input.java          # input for transformation tests
    <case>-expected.java       # expected output after rule applies
    <case>-unchanged.java      # input that the rule should not modify
    formatter-off-unchanged.java  # verifies @formatter:off protection
```

## Naming Conventions

- **Directory name**: kebab-case matching the rule (e.g., `class-body-blank-lines/`)
- **Transformation tests**: `<case>-input.java` + `<case>-expected.java`
- **Preservation tests**: `<case>-unchanged.java`
- **Formatter-off test**: always `formatter-off-unchanged.java`

## Step-by-Step Conversion

### 1. Create the fixture directory

```
src/test/resources/fixtures/<rule-name>/
```

### 2. Extract inline strings to fixture files

For each test method, take the text block content and write it to a `.java` file. Text blocks
strip leading whitespace based on the closing `"""` position — the fixture file should contain
the resulting string (no extra leading indentation).

**Example** — this text block:
```java
String input = """
        public class Foo {

            int x;
        }
        """;
```
becomes a fixture file containing:
```java
public class Foo {

    int x;
}
```
(with a trailing newline)

**Watch out for `\s` escapes** — text blocks use `\s` to preserve trailing whitespace. In a
fixture file, write literal spaces instead. Use `printf` or similar to write files that need
trailing whitespace on blank lines:
```bash
printf 'public class Foo {\n    \n    int x;\n}\n' > fixture.java
```

### 3. Extend the appropriate base class and wire the fixture directory

Every converted test **must** extend the base class for its category. The base classes provide
`test(input, expected)` and `test(unchanged)` helpers that load fixtures and assert results.

`FormatterTestBase` (in `ca.kieve.formatter.util`) is the shared base — it takes a fixture
directory and a `FormatterRule` (`String -> String`). Custom rule tests extend
`FormatterRuleTestBase` (which adds the `respectsFormatterOffTags()` requirement). Eclipse
formatter tests extend `FormatterTestBase` directly. Checkstyle tests extend `CheckstyleTestBase`.

For custom formatter rules (extending `FormatterRuleTestBase`):
```java
class MyRuleTest extends FormatterRuleTestBase {
    MyRuleTest() {
        super("<rule-name>/", MyRule::apply);
    }
```

For checkstyle lint rules (extending `CheckstyleTestBase`):
```java
class MyCheckTest extends CheckstyleTestBase {
    MyCheckTest() {
        super("<rule-name>/", "MyCheckName");
    }
```

For Eclipse formatter tests (extending `FormatterTestBase` directly):
```java
class MyEclipseTest extends FormatterTestBase {
    MyEclipseTest() {
        super("<rule-name>/", s -> formatJava(s));
    }
```

### 4. Convert test methods

**Before** (inline):
```java
@Test
void removesBlankLine() {
    String input = """
            public class Foo {

                int x;
            }
            """;
    String expected = """
            public class Foo {
                int x;
            }
            """;
    assertEquals(expected, MyRule.apply(input));
}
```

**After** (fixture):
```java
@Test
void removesBlankLine() throws IOException {
    test("blank-line-input.java", "blank-line-expected.java");
}
```

For unchanged/preservation tests:
```java
@Test
void preservesValidCode() throws IOException {
    test("valid-unchanged.java");
}
```

### 5. Convert the `respectsFormatterOffTags()` test

This test must use `CustomFormatterStep.applyCustomRules()` (not the individual rule), and the
fixture should contain `// @formatter:off` / `// @formatter:on` blocks wrapping content that
would otherwise be modified:

```java
@Override
void respectsFormatterOffTags() throws IOException {
    String input = loadFixture("<rule-name>/formatter-off-unchanged.java");
    assertEquals(input, CustomFormatterStep.applyCustomRules(input));
}
```

### 6. Required imports

```java
import org.junit.jupiter.api.Test;
import ca.kieve.formatter.step.CustomFormatterStep;
import java.io.IOException;
import static ca.kieve.formatter.util.FormatterTestUtil.loadFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
```

### 7. Run the tests

```bash
./gradlew test --tests "ca.kieve.formatter.rules.style.MyRuleTest"
```

---

## Conversion Checklist

Tests still using inline Java text blocks that should be converted to fixtures.

### Custom Formatter Rules (`rules/style/`)

- [x] `ClassBodyBlankLinesTest`
- [x] `LeadingBlankLinesTest`
- [x] `SwitchCaseBlankLinesTest`
- [x] `ImportSortingTest`

### Checkstyle Lint Rules (`checkstyle/`)

- [x] `AvoidStarImportTest`
- [x] `AvoidNestedTernaryTest`

### Eclipse Formatter Tests (`eclipse/`)

- [x] `BracePositionsTest`
- [x] `BlankLinesTest`
- [x] `ColumnAlignmentTest`
- [ ] `CommentFormattingTest`
- [ ] `IndentationTabsTest`
- [ ] `KeepOnOneLineTest`
- [ ] `LineWidthTest`
- [ ] `NewlinesAnnotationsTest`
- [ ] `NewlinesTest`
- [ ] `OnOffTagsTest`
- [ ] `OperatorWrappingTest`
- [ ] `ParenthesesPreservationTest`
- [ ] `WhitespaceAngleBracketsTest`
- [ ] `WhitespaceBracesTest`
- [ ] `WhitespaceBracketsTest`
- [ ] `WhitespaceColonsSemicolonsTest`
- [ ] `WhitespaceCommasTest`
- [ ] `WhitespaceMiscellaneousTest`
- [ ] `WhitespaceOperatorsTest`
- [ ] `WhitespaceParenthesesTest`
- [ ] `WhitespaceSwitchArrowsTest`
- [ ] `WrapAlignTest`

### Other

- [ ] `CustomFormatterStepTest`
- [ ] `TodoTests`
