Styles to enforce:
---
Custom rule for wrap-before-operator enforcement. Eclipse's `wrap_before_binary_operator` only
affects wrapping that Eclipse introduces — it does not fix existing wrap-after-operator style.
Need a custom rule to move trailing operators to the beginning of the next line.
Disabled test: `rules.TodoTests.wrapAfterOperatorShouldMoveOperatorBeforeNextLine`

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
