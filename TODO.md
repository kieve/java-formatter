Styles to enforce:
---
Custom comment / doc comment formatter. Eclipse JDT's comment formatting is disabled via
`comment.format_javadoc_comments: false`, `comment.format_block_comments: false`, and
`comment.format_line_comments: false` in the Eclipse config. `comment.line_length` is set to 100
but has no effect while formatting is disabled. Need a custom formatter that handles:
- Enforce 100-character line limit for all comment types (wrap long lines)
- Javadoc comments (`/** */`): expand tags onto separate lines, indent continuation lines,
  enforce boundaries (`/**` and `*/` on own lines)
- Block comments (`/* */`): boundary newlines
- Markdown doc comments (`///`): formatting support
- Line comments (`//`): preserve spacing between code and comment
- Param/return alignment (see below)

---
Write a formatter to align param continuation lines after the space of `@param `
Same for the `@return `

  /**
   *
   * @param foo some long line that wraps
   *        and when it wraps it's aligned after the `@param `
   * @param bar some other param
   */

---
Force braces to be added in all places they can exist
Exception: Early exit if statements

---
Remove blank lines between annotation type members. Eclipse's `blank_lines_before_method` applies
to annotation members the same as regular methods, and `blank_lines_before_abstract_method` doesn't
cover them. Need a custom rule to strip blank lines inside `@interface` bodies.
Test to update: `WrapAlignTest.alignmentForArgumentsInAnnotationWrapsAllElements`

---
Custom rule for assignment wrapping. Eclipse's `alignment_for_assignment` wraps after `=`, but it
interferes with other wrapping (e.g., `new Foo(...)` args get collapsed onto one line after the `=`
wraps instead of wrapping per-argument). Need a custom rule that wraps after `=` only when the RHS
doesn't have its own wrapping.
Test to update: `WrapAlignTest.alignmentForAssignmentDoesNotWrap`

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
