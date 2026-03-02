Styles to enforce:
---
Custom rule for throws clause wrapping. Eclipse JDT always keeps `throws` + first exception on the
same line. Need a custom formatter step to break after `throws` so each exception starts on its own
line.
Disabled tests: `WrapAlignTest.alignmentForThrowsClauseInMethodDeclarationWrapsAllElements`,
`WrapAlignTest.alignmentForThrowsClauseInConstructorDeclarationWrapsAllElements`

---
Fix switch case body indentation. When `case` expressions in a switch case wrap, Eclipse JDT indents
the body (after `->` or `:`) at the same level as the case expressions. The body should be indented
further to distinguish it from the case labels. Not solvable via Eclipse settings (`INDENT_BY_ONE`
adds only 1 space, `INDENT_ON_COLUMN` forces wrapping on all cases). Needs a custom rule.
Disabled tests: `WrapAlignTest.alignmentForExpressionsInSwitchCaseWithArrowWrapsAllElements`,
`WrapAlignTest.alignmentForExpressionsInSwitchCaseWithColonWrapsAllElements`

---
Custom rule for annotations on wrapped parameters. When parameters wrap one-per-line, Eclipse keeps
annotations inline with each parameter. They should go on their own lines above each parameter.
Disabled test: `rules.TodoTests.annotationsOnWrappedParametersShouldGoOnOwnLines`

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
