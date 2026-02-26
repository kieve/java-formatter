Styles to enforce:

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
