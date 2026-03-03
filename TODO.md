# TODO

## Bug: Formatter destroys text block delimiters in string concatenation

When a `String` field is initialized with concatenation followed by a text block, the formatter
strips the opening `"""` delimiter, turning the text block content into invalid Java code.

**Before formatting (valid Java):**
```java
private static final String CSS =
        EditorTheme.OVERLAY_CSS + """
        .%1$s .list-cell {
            -fx-cell-size: 1.4em;
            -fx-padding: 1 4;
            -fx-cursor: hand;
        }
        .%1$s .list-cell:hover {
            -fx-background-color: -color-neutral-muted;
        }
        """.formatted(EditorTheme.STYLE_OVERLAY);
```

**After formatting (broken — won't compile):**
```java
private static final String CSS = EditorTheme.OVERLAY_CSS
    + .%1$s .list-cell {
        -fx-cell-size: 1.4em;
        -fx-padding: 1 4;
        -fx-cursor: hand;
    }
    .%1$s .list-cell:hover {
        -fx-background-color: -color-neutral-muted;
    }
    """.formatted(EditorTheme.STYLE_OVERLAY);
```

The `+ """` (concatenation operator + text block open) is reduced to just `+`, and the text block
content is emitted as raw tokens, producing ~100 compilation errors (CSS properties parsed as Java
statements).

**Affected files** (all in `editor/src/main/java/ca/kieve/ssss/editor/component/`):
- `SelectedCellOverlay.java`
- `ZLevelOverlay.java`
- `ZoomOverlay.java`

**Pattern:** `CONSTANT + """...""".formatted(...)` — string concatenation where the right operand
is a text block with a chained method call.
