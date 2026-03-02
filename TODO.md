Styles to enforce:
---
Custom rule to join wrapped lines that fit on one line. Eclipse's `join_wrapped_lines` is too
aggressive — it also collapses array initializers and other intentionally wrapped constructs. Need
a custom rule that joins wrapped content back onto the previous line when the result fits within
100 characters. This applies to method parameters, constructor parameters, method arguments,
and try-with-resources. Examples:

```
// Input — unnecessarily wrapped method parameters
private static ImportGroup parsePrefixList(
    List<?> list, boolean isStatic, int index) {
}

// Expected — fits on one line, should be joined
private static ImportGroup parsePrefixList(List<?> list, boolean isStatic, int index) {
}

// Input — unnecessarily wrapped try-with-resources
try (
    InputStream is = JavaFormatterPlugin.class
        .getResourceAsStream(resourcePath)) {
}

// Expected — fits on one line, should be joined
try (InputStream is = JavaFormatterPlugin.class.getResourceAsStream(resourcePath)) {
}
```
