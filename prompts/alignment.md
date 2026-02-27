# Eclipse JDT Alignment Bitmask Reference

The `alignment_for_*` settings in Eclipse JDT use bitmask integers produced by
`DefaultCodeFormatterConstants.createAlignmentValue(boolean forceSplit, int wrapStyle, int indentStyle)`.

Source: [DefaultCodeFormatterConstants.java](https://github.com/eclipse-jdt/eclipse.jdt.core/blob/master/org.eclipse.jdt.core/formatter/org/eclipse/jdt/core/formatter/DefaultCodeFormatterConstants.java)

---

## Input Constants

These are the named constants passed to `createAlignmentValue`:

### Wrap Style (`wrapStyle` parameter)

| Constant                   | Value |
|----------------------------|-------|
| `WRAP_NO_SPLIT`            | 0     |
| `WRAP_COMPACT`             | 1     |
| `WRAP_COMPACT_FIRST_BREAK` | 2     |
| `WRAP_ONE_PER_LINE`        | 3     |
| `WRAP_NEXT_SHIFTED`        | 4     |
| `WRAP_NEXT_PER_LINE`       | 5     |

### Indent Style (`indentStyle` parameter)

| Constant            | Value |
|---------------------|-------|
| `INDENT_DEFAULT`    | 0     |
| `INDENT_ON_COLUMN`  | 1     |
| `INDENT_BY_ONE`     | 2     |
| `INDENT_PRESERVE`   | 3     |

---

## Output Bitmask Layout

`createAlignmentValue` maps the input constants to internal `Alignment.M_*` bitmask
values. The resulting integer has this bit layout:

| Bits | Meaning         | Values                                                     |
|------|-----------------|------------------------------------------------------------|
| 0–1  | Indent style    | 0 = default, 1 = on column, 2 = by one                    |
| 4–5  | Wrap style      | 0 = none, 1 = compact, 2 = compact-first-break, 3 = one-per-line |
| 6    | Force split     | 0 = off, 1 = force                                        |

### Common Bitmask Values

| Value | Meaning                                          | `createAlignmentValue` equivalent                      |
|-------|--------------------------------------------------|--------------------------------------------------------|
| 0     | No wrapping                                      | `(false, WRAP_NO_SPLIT, INDENT_DEFAULT)`               |
| 16    | Wrap where necessary                             | `(false, WRAP_COMPACT, INDENT_DEFAULT)`                |
| 32    | Wrap first element                               | `(false, WRAP_COMPACT_FIRST_BREAK, INDENT_DEFAULT)`    |
| 48    | Wrap all elements (one per line)                 | `(false, WRAP_ONE_PER_LINE, INDENT_DEFAULT)`           |
| 49    | Wrap all elements + indent on column             | `(false, WRAP_ONE_PER_LINE, INDENT_ON_COLUMN)`         |
| 80    | Wrap where necessary + force                     | `(true, WRAP_COMPACT, INDENT_DEFAULT)`                 |
| 112   | Wrap all elements + force                        | `(true, WRAP_ONE_PER_LINE, INDENT_DEFAULT)`            |

---

## Practical Findings

### Force split (`+64`) doesn't always work

In testing with Spotless 8.2.1 and its bundled Eclipse JDT formatter, the force
split bit (`+64`) was **ignored** for `alignment_for_enum_constants`. Value `112`
(one-per-line + force) did not wrap short enum constant lists.

### Indent on column (`+1`) triggers wrapping

Adding `INDENT_ON_COLUMN` (`+1`) to a wrap style **does** cause wrapping even for
short lines. Value `49` (one-per-line + indent on column) successfully forces each
enum constant onto its own line regardless of line length.

Working values from real projects:
- `49` — used by [Eclipse SmartHome](https://github.com/eclipse-archived/smarthome/pull/211/files)
- `17` — reported working on [Stack Overflow](https://stackoverflow.com/questions/6676305/how-to-stop-eclipse-formatter-from-placing-all-enums-on-one-line)

### `alignment_for_for_loop_header` is the wrong setting ID

The YAML key `alignment_for_for_loop_header` generates a valid-looking XML setting, but Eclipse JDT
ignores it entirely. The correct setting ID is `alignment_for_expressions_in_for_loop_header`, which
matches the constant name `FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_FOR_LOOP_HEADER`. The constant's
*value* string is `"org.eclipse.jdt.core.formatter.alignment_for_for_loop_header"` according to the
docs, but in practice only `alignment_for_expressions_in_for_loop_header` works.

### `alignment_for_assignment` interferes with other wrapping

Setting `alignment_for_assignment` to any non-zero value (tested `16` and `48`) causes Eclipse to
wrap after `=` **before** applying other wrapping rules (e.g., `alignment_for_arguments_in_allocation_expression`).
This means `Object obj = new Foo(longArg1, longArg2, longArg3)` wraps as:

```java
Object obj =
    new Foo(longArg1, longArg2, longArg3);  // args no longer wrap individually
```

instead of the desired:

```java
Object obj = new Foo(
    longArg1,
    longArg2,
    longArg3);
```

The assignment wrap "absorbs" the line length so the RHS fits on one line, preventing per-argument
wrapping. This needs a custom rule that only wraps after `=` when the RHS doesn't have its own
wrapping (e.g., a plain string literal or expression, not a method/constructor call).

### `join_wrapped_lines` preserves author line breaks

The global setting `org.eclipse.jdt.core.formatter.join_wrapped_lines` (`true`/`false`)
controls whether the formatter joins lines the author has already wrapped. Setting it to
`false` prevents Eclipse from collapsing multi-line constructs (like array initializers)
back to single lines. This is the correct way to make Eclipse "hands off" about wrapping
decisions the author has already made — alignment bitmask values alone cannot achieve this.

References:
- [VS Code Java issue #2181](https://github.com/redhat-developer/vscode-java/issues/2181)
- [Eclipse bug #454865](https://bugs.eclipse.org/bugs/show_bug.cgi?id=454865)
