Styles to enforce:
---
Support
// @formatter:off
// @formatter:on 

---
Can we enforce these on code inside JavaDoc comments (incl Markdown ones)

---
Import sorting

---
All Inner types must be declared at the top, before any members.
Might have to edit the setting `blank_lines_before_member_type`

---
distinguish between public vs public final vs private fields within the same static/instance group
Example

public static final String A = "";
public static final String B = "";

public static ...
public static ...

static ...
static ...

private ...
private ...

etc...

---
Can we enforce invert + early exit if statements:

---
Enforce if a () is wrapped, the ) goes on it's own line.
Example

public void main(
    ... etc ...
) {
}

---
In the following case, we need a custom formatter to for the `);` onto a new line.
Same for `) {`
Same for type argument `>`

// Exceeds line width — wraps with first break + one per line
result = someObject.doSomething(
    firstArg,
    secondArg,
    thirdArg,
    fourthArg);

---
We need a complete custom formatter for array initializers

3. alignment_for_expressions_in_array_initializer

  Controls how array initializer elements wrap. Currently set to 16 in YAML.

  // 16 — wrap where necessary (current)
  int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9,
          10, 11, 12};

  // 80 — wrap next per line
  int[] values = {
          1, 2, 3, 4, 5, 6, 7, 8, 9,
          10, 11, 12};

  // 48 — wrap all elements (one per line)
  int[] values = {1,
          2,
          3,

---
Custom comment / doc comment formatter. Eclipse JDT's comment formatter doesn't properly handle
tag separation (e.g., expanding single-line javadocs with `@param`/`@return` onto separate lines,
even with `insert_new_line_before_root_tags` set). All `comment.*` settings have been removed from
the Eclipse config and `F_INCLUDE_COMMENTS` disabled. Need a custom formatter that handles:
- Javadoc comments (`/** */`): expand tags onto separate lines, wrap long descriptions, indent
  continuation lines, enforce boundaries (`/**` and `*/` on own lines)
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
Linting:
- Find a way to effectively "ban" nested ternary operators
- Ban multiple field declarations in one line. ex. `int x, y, z;`
