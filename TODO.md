Styles to enforce:
---
Support
// @formatter:off
// @formatter:on 

---
Can we enforce these on code inside JavaDoc comments (incl Markdown ones)

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
Write a formatter to align param continuation lines after the space of `@param `
Same for the `@return `

  /**
   *
   * @param foo some long line that wraps
   *        and when it wraps it's aligned after the `@param `
   * @param bar some other param
   */

---


---
Linting:
- Find a way to effectively "ban" nested ternary operators
- Ban multiple field declarations in one line. ex. `int x, y, z;`
