# Eclipse JDT Formatter Settings Test Checklist

Track test coverage for each Eclipse JDT formatter setting. Check off items as tests are written and passing.

Reference: [DefaultCodeFormatterConstants (Eclipse JDT API)](https://help.eclipse.org/latest/topic/org.eclipse.jdt.doc.isv/reference/api/org/eclipse/jdt/core/formatter/DefaultCodeFormatterConstants.html)

All setting IDs are relative to `org.eclipse.jdt.core.formatter.` unless otherwise noted.

---

## Indentation & Tabs

- [x] `tabulation.char` — tab vs space vs mixed
- [x] `tabulation.size` — tab display width
- [x] `indentation.size` — indentation width
- [x] `continuation_indentation` — extra indent for continuation lines (default: `2` units)
- [x] `continuation_indentation_for_array_initializer` — continuation indent for array initializers (default: `2`)
- [x] `indent_body_declarations_compare_to_type_header` — indent class body relative to class decl
- [x] `indent_body_declarations_compare_to_enum_declaration_header` — indent enum body
- [x] `indent_body_declarations_compare_to_enum_constant_header` — indent enum constant body
- [x] `indent_body_declarations_compare_to_annotation_declaration_header` — indent annotation body
- [x] `indent_body_declarations_compare_to_record_header` — indent record body
- [x] `indent_statements_compare_to_block` — indent statements inside blocks
- [x] `indent_statements_compare_to_body` — indent statements inside method body
- [x] `indent_switchstatements_compare_to_switch` — indent case labels relative to switch
- [x] `indent_switchstatements_compare_to_cases` — indent statements relative to case
- [x] `indent_breaks_compare_to_cases` — indent break/yield relative to case
- [x] `indent_empty_lines` — whether to indent blank lines
- [x] `use_tabs_only_for_leading_indentation` — tabs only for leading indent, spaces for alignment *(excluded — N/A with space indentation)*

---

## Line Width

- [x] `lineSplit` — max line width before wrapping
- [x] `number_of_empty_lines_to_preserve` — max consecutive blank lines to keep

---

## Brace Positions

Values: `end_of_line`, `next_line`, `next_line_shifted`, `next_line_on_wrap`

- [x] `brace_position_for_type_declaration`
- [x] `brace_position_for_method_declaration`
- [x] `brace_position_for_constructor_declaration`
- [x] `brace_position_for_block`
- [x] `brace_position_for_switch`
- [x] `brace_position_for_anonymous_type_declaration`
- [x] `brace_position_for_array_initializer`
- [x] `brace_position_for_enum_declaration`
- [x] `brace_position_for_enum_constant`
- [x] `brace_position_for_annotation_type_declaration`
- [x] `brace_position_for_record_declaration`
- [x] `brace_position_for_record_constructor`
- [x] `brace_position_for_lambda_body`
- [x] `brace_position_for_block_in_case` — brace position for block inside case
- [x] `brace_position_for_block_in_case_after_arrow` — brace position for block inside arrow case

---

## Blank Lines

- [x] `blank_lines_before_package`
- [x] `blank_lines_after_package`
- [x] `blank_lines_before_imports`
- [x] `blank_lines_after_imports`
- [x] `blank_lines_between_type_declarations`
- [x] `blank_lines_before_member_type`
- [ ] `blank_lines_before_first_class_body_declaration`
- [ ] `blank_lines_after_last_class_body_declaration`
- [ ] `blank_lines_before_method`
- [ ] `blank_lines_before_field`
- [ ] `blank_lines_before_abstract_method` — blank lines before abstract methods
- [ ] `blank_lines_before_new_chunk` — blank lines before a new "chunk" of declarations
- [ ] `blank_lines_between_import_groups` — blank lines between import groups
- [ ] `blank_lines_between_statement_groups_in_switch` — blank lines between switch case groups
- [ ] `number_of_blank_lines_after_code_block` — blank lines after a code block *(excluded)*
- [ ] `number_of_blank_lines_before_code_block` — blank lines before a code block *(excluded)*
- [ ] `number_of_blank_lines_at_beginning_of_code_block` — blank lines at start of code block
- [ ] `number_of_blank_lines_at_end_of_code_block` — blank lines at end of code block
- [ ] `number_of_blank_lines_at_beginning_of_method_body` — blank lines at start of method body
- [ ] `number_of_blank_lines_at_end_of_method_body` — blank lines at end of method body

---

## Newlines

- [ ] `insert_new_line_at_end_of_file_if_missing`
- [ ] `keep_else_statement_on_same_line` *(excluded — N/A, braces always required)*
- [ ] `keep_then_statement_on_same_line` *(excluded — N/A, braces always required)*
- [ ] `put_empty_statement_on_new_line`
- [ ] `insert_new_line_before_else_in_if_statement` — newline before `else`
- [ ] `insert_new_line_before_catch_in_try_statement` — newline before `catch`
- [ ] `insert_new_line_before_finally_in_try_statement` — newline before `finally`
- [ ] `insert_new_line_before_while_in_do_statement` — newline before `while` in do-while
- [ ] `insert_new_line_after_opening_brace_in_array_initializer` — newline after `{` in array init
- [ ] `insert_new_line_before_closing_brace_in_array_initializer` — newline before `}` in array init
- [ ] `insert_new_line_after_label` — newline after a label
- [ ] `compact_else_if` — treat `else if` as a single unit

### Annotations on Newlines

- [ ] `insert_new_line_after_annotation_on_type` — newline after annotation on type
- [ ] `insert_new_line_after_annotation_on_method` — newline after annotation on method
- [ ] `insert_new_line_after_annotation_on_field` — newline after annotation on field
- [ ] `insert_new_line_after_annotation_on_local_variable` — newline after annotation on local var
- [ ] `insert_new_line_after_annotation_on_parameter` — newline after annotation on parameter
- [ ] `insert_new_line_after_annotation_on_package` — newline after annotation on package
- [ ] `insert_new_line_after_annotation_on_enum_constant` — newline after annotation on enum constant
- [ ] `insert_new_line_after_type_annotation` — newline after type annotation

---

## Keep On One Line

Values: `one_line_never`, `one_line_if_empty`, `one_line_if_single_item`, `one_line_always`, `one_line_preserve`

- [ ] `keep_method_body_on_one_line`
- [ ] `keep_simple_getter_setter_on_one_line`
- [ ] `keep_code_block_on_one_line`
- [ ] `keep_lambda_body_block_on_one_line`
- [ ] `keep_loop_body_block_on_one_line`
- [ ] `keep_if_then_body_block_on_one_line`
- [ ] `keep_type_declaration_on_one_line`
- [ ] `keep_annotation_declaration_on_one_line`
- [ ] `keep_anonymous_type_declaration_on_one_line`
- [ ] `keep_enum_declaration_on_one_line`
- [ ] `keep_enum_constant_declaration_on_one_line`
- [ ] `keep_record_declaration_on_one_line`
- [ ] `keep_record_constructor_on_one_line`

---

## Wrapping & Alignment

Alignment values are bitmask integers. Key bits: `0` = no wrap, `16` = wrap where necessary,
`32` = wrap first element, `48` = wrap all elements, `+64` = force split, `+16` = indent on column.

- [ ] `alignment_for_arguments_in_method_invocation`
- [ ] `alignment_for_parameters_in_method_declaration`
- [ ] `alignment_for_expressions_in_array_initializer` *(excluded — custom formatter rule needed)*
- [ ] `alignment_for_arguments_in_allocation_expression` — args in `new Foo(...)` calls
- [ ] `alignment_for_arguments_in_annotation` — args in annotations
- [ ] `alignment_for_arguments_in_enum_constant` — args in enum constants
- [ ] `alignment_for_arguments_in_explicit_constructor_call` — args in `this()`/`super()` calls
- [ ] `alignment_for_arguments_in_qualified_allocation_expression` — args in qualified `new`
- [ ] `alignment_for_assertion_message` — assertion message wrapping
- [ ] `alignment_for_assignment` — wrapping around `=`
- [ ] `alignment_for_compact_if` — compact if statement alignment *(excluded — braces always required)*
- [ ] `alignment_for_compact_loops` — compact loop alignment *(excluded — braces always required)*
- [ ] `alignment_for_conditional_expression` — ternary `? :` wrapping
- [ ] `alignment_for_conditional_expression_chain` — chained ternaries *(excluded — will be banned via linting)*
- [ ] `alignment_for_enum_constants` — enum constant list wrapping
- [ ] `alignment_for_for_loop_header` — for loop header expressions
- [ ] `alignment_for_method_declaration` — method declaration wrapping
- [ ] `alignment_for_module_statements` — module-info statements *(excluded)*
- [ ] `alignment_for_multiple_fields` — multiple field declarations *(excluded)*
- [ ] `alignment_for_parameters_in_constructor_declaration` — constructor params
- [ ] `alignment_for_permitted_types_in_type_declaration` — `permits` clause
- [ ] `alignment_for_record_components` — record component list
- [ ] `alignment_for_resources_in_try` — try-with-resources
- [ ] `alignment_for_selector_in_method_invocation` — method chain `.foo().bar()`
- [ ] `alignment_for_superclass_in_type_declaration` — `extends` clause
- [ ] `alignment_for_superinterfaces_in_type_declaration` — `implements` clause
- [ ] `alignment_for_superinterfaces_in_enum_declaration` — enum `implements`
- [ ] `alignment_for_superinterfaces_in_record_declaration` — record `implements`
- [ ] `alignment_for_throws_clause_in_method_declaration` — method `throws`
- [ ] `alignment_for_throws_clause_in_constructor_declaration` — constructor `throws`
- [ ] `alignment_for_type_arguments` — type arguments `<A, B>`
- [ ] `alignment_for_type_parameters` — type parameters `<T extends ...>`
- [ ] `alignment_for_parameterized_type_references` — parameterized type refs
- [ ] `alignment_for_union_type_in_multicatch` — multi-catch `|` types
- [ ] `alignment_for_switch_case_with_arrow` — switch case arrow alignment
- [ ] `alignment_for_expressions_in_switch_case_with_arrow` — expressions in arrow case
- [ ] `alignment_for_expressions_in_switch_case_with_colon` — expressions in colon case
- [ ] `alignment_for_annotations_on_type` — annotation wrapping on types
- [ ] `alignment_for_annotations_on_method` — annotation wrapping on methods
- [ ] `alignment_for_annotations_on_field` — annotation wrapping on fields
- [ ] `alignment_for_annotations_on_local_variable` — annotation wrapping on locals
- [ ] `alignment_for_annotations_on_parameter` — annotation wrapping on params
- [ ] `alignment_for_annotations_on_package` — annotation wrapping on package
- [ ] `alignment_for_annotations_on_enum_constant` — annotation wrapping on enum constants
- [ ] `alignment_for_type_annotations` — type annotation wrapping

### Operator Wrapping

- [ ] `alignment_for_additive_operator` — `+` `-` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_multiplicative_operator` — `*` `/` `%` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_string_concatenation` — string `+` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_bitwise_operator` — `&` `|` `^` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_logical_operator` — `&&` `||` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_relational_operator` — `<` `>` `<=` `>=` `==` `!=` wrapping *(excluded — custom formatter)*
- [ ] `alignment_for_shift_operator` — `<<` `>>` `>>>` wrapping *(excluded — custom formatter)*
- [ ] `wrap_before_binary_operator` — wrap before or after binary operators
- [ ] `wrap_before_or_operator_multicatch` — wrap before `|` in multi-catch

### Column Alignment

- [ ] `align_type_members_on_columns` — align fields/methods on columns
- [ ] `align_variable_declarations_on_columns` — align variable declarations
- [ ] `align_assignment_statements_on_columns` — align assignment `=` on columns
- [ ] `align_arrows_in_switch_on_columns` — align switch case arrows
- [ ] `align_fields_grouping_blank_lines` — blank line grouping for field alignment *(excluded — N/A, alignment disabled)*
- [ ] `align_with_spaces` — use spaces for alignment *(excluded — N/A, alignment disabled)*

---

## Whitespace — Operators

- [ ] `insert_space_before_assignment_operator`
- [ ] `insert_space_after_assignment_operator`
- [ ] `insert_space_before_additive_operator`
- [ ] `insert_space_after_additive_operator`
- [ ] `insert_space_before_multiplicative_operator`
- [ ] `insert_space_after_multiplicative_operator`
- [ ] `insert_space_before_bitwise_operator`
- [ ] `insert_space_after_bitwise_operator`
- [ ] `insert_space_before_logical_operator`
- [ ] `insert_space_after_logical_operator`
- [ ] `insert_space_before_relational_operator`
- [ ] `insert_space_after_relational_operator`
- [ ] `insert_space_before_shift_operator`
- [ ] `insert_space_after_shift_operator`
- [ ] `insert_space_before_string_concatenation`
- [ ] `insert_space_after_string_concatenation`
- [ ] `insert_space_before_unary_operator`
- [ ] `insert_space_after_unary_operator`
- [ ] `insert_space_before_prefix_operator`
- [ ] `insert_space_after_prefix_operator`
- [ ] `insert_space_before_postfix_operator`
- [ ] `insert_space_after_postfix_operator`
- [ ] `insert_space_after_not_operator`

---

## Whitespace — Parentheses (Opening)

- [ ] `insert_space_before_opening_paren_in_method_declaration`
- [ ] `insert_space_before_opening_paren_in_method_invocation`
- [ ] `insert_space_before_opening_paren_in_if`
- [ ] `insert_space_before_opening_paren_in_for`
- [ ] `insert_space_before_opening_paren_in_while`
- [ ] `insert_space_before_opening_paren_in_switch`
- [ ] `insert_space_before_opening_paren_in_catch`
- [ ] `insert_space_before_opening_paren_in_constructor_declaration`
- [ ] `insert_space_before_opening_paren_in_annotation`
- [ ] `insert_space_before_opening_paren_in_enum_constant`
- [ ] `insert_space_before_opening_paren_in_record_declaration`
- [ ] `insert_space_before_opening_paren_in_synchronized`

---

## Whitespace — Parentheses (After Opening / Before Closing)

- [ ] `insert_space_after_opening_paren_in_method_declaration`
- [ ] `insert_space_after_opening_paren_in_method_invocation`
- [ ] `insert_space_after_opening_paren_in_constructor_declaration`
- [ ] `insert_space_after_opening_paren_in_if`
- [ ] `insert_space_after_opening_paren_in_for`
- [ ] `insert_space_after_opening_paren_in_while`
- [ ] `insert_space_after_opening_paren_in_switch`
- [ ] `insert_space_after_opening_paren_in_catch`
- [ ] `insert_space_after_opening_paren_in_synchronized`
- [ ] `insert_space_after_opening_paren_in_cast`
- [ ] `insert_space_after_opening_paren_in_annotation`
- [ ] `insert_space_after_opening_paren_in_enum_constant`
- [ ] `insert_space_after_opening_paren_in_record_declaration`
- [ ] `insert_space_before_closing_paren_in_method_declaration`
- [ ] `insert_space_before_closing_paren_in_method_invocation`
- [ ] `insert_space_before_closing_paren_in_constructor_declaration`
- [ ] `insert_space_before_closing_paren_in_if`
- [ ] `insert_space_before_closing_paren_in_for`
- [ ] `insert_space_before_closing_paren_in_while`
- [ ] `insert_space_before_closing_paren_in_switch`
- [ ] `insert_space_before_closing_paren_in_catch`
- [ ] `insert_space_before_closing_paren_in_synchronized`
- [ ] `insert_space_before_closing_paren_in_cast`
- [ ] `insert_space_before_closing_paren_in_annotation`
- [ ] `insert_space_before_closing_paren_in_enum_constant`
- [ ] `insert_space_before_closing_paren_in_record_declaration`
- [ ] `insert_space_after_closing_paren_in_cast` — space after `)` in cast

---

## Whitespace — Braces

- [ ] `insert_space_before_opening_brace_in_type_declaration`
- [ ] `insert_space_before_opening_brace_in_method_declaration`
- [ ] `insert_space_before_opening_brace_in_block`
- [ ] `insert_space_before_opening_brace_in_array_initializer`
- [ ] `insert_space_after_opening_brace_in_array_initializer`
- [ ] `insert_space_before_closing_brace_in_array_initializer`
- [ ] `insert_space_after_closing_brace_in_block`

---

## Whitespace — Commas

- [ ] `insert_space_before_comma_in_method_invocation_arguments`
- [ ] `insert_space_after_comma_in_method_invocation_arguments`
- [ ] `insert_space_before_comma_in_method_declaration_parameters`
- [ ] `insert_space_after_comma_in_method_declaration_parameters`
- [ ] `insert_space_before_comma_in_constructor_declaration_parameters`
- [ ] `insert_space_after_comma_in_constructor_declaration_parameters`
- [ ] `insert_space_before_comma_in_allocation_expression`
- [ ] `insert_space_after_comma_in_allocation_expression`
- [ ] `insert_space_before_comma_in_array_initializer`
- [ ] `insert_space_after_comma_in_array_initializer`
- [ ] `insert_space_before_comma_in_enum_constant_arguments`
- [ ] `insert_space_after_comma_in_enum_constant_arguments`
- [ ] `insert_space_before_comma_in_enum_declarations`
- [ ] `insert_space_after_comma_in_enum_declarations`
- [ ] `insert_space_before_comma_in_for_increments`
- [ ] `insert_space_after_comma_in_for_increments`
- [ ] `insert_space_before_comma_in_for_inits`
- [ ] `insert_space_after_comma_in_for_inits`
- [ ] `insert_space_before_comma_in_multiple_field_declarations`
- [ ] `insert_space_after_comma_in_multiple_field_declarations`
- [ ] `insert_space_before_comma_in_multiple_local_declarations`
- [ ] `insert_space_after_comma_in_multiple_local_declarations`
- [ ] `insert_space_before_comma_in_annotation`
- [ ] `insert_space_after_comma_in_annotation`
- [ ] `insert_space_before_comma_in_record_components`
- [ ] `insert_space_after_comma_in_record_components`
- [ ] `insert_space_before_comma_in_switch_case_expressions`
- [ ] `insert_space_after_comma_in_switch_case_expressions`
- [ ] `insert_space_before_comma_in_type_arguments`
- [ ] `insert_space_after_comma_in_type_arguments`
- [ ] `insert_space_before_comma_in_type_parameters`
- [ ] `insert_space_after_comma_in_type_parameters`
- [ ] `insert_space_before_comma_in_parameterized_type_reference`
- [ ] `insert_space_after_comma_in_parameterized_type_reference`

---

## Whitespace — Colons & Semicolons

- [ ] `insert_space_before_colon_in_for`
- [ ] `insert_space_after_colon_in_for`
- [ ] `insert_space_before_semicolon`
- [ ] `insert_space_before_colon_in_assert`
- [ ] `insert_space_after_colon_in_assert`
- [ ] `insert_space_before_colon_in_case`
- [ ] `insert_space_after_colon_in_case`
- [ ] `insert_space_before_colon_in_default` — space before `:` in default
- [ ] `insert_space_after_colon_in_default` — space after `:` in default
- [ ] `insert_space_before_colon_in_conditional`
- [ ] `insert_space_after_colon_in_conditional`
- [ ] `insert_space_before_colon_in_labeled_statement`
- [ ] `insert_space_after_colon_in_labeled_statement`
- [ ] `insert_space_before_semicolon_in_for`
- [ ] `insert_space_before_semicolon_in_try_with_resources`

---

## Whitespace — Angle Brackets (Generics)

- [ ] `insert_space_before_opening_angle_bracket_in_type_arguments`
- [ ] `insert_space_after_opening_angle_bracket_in_type_arguments`
- [ ] `insert_space_before_closing_angle_bracket_in_type_arguments`
- [ ] `insert_space_after_closing_angle_bracket_in_type_arguments`
- [ ] `insert_space_before_opening_angle_bracket_in_type_parameters`
- [ ] `insert_space_after_opening_angle_bracket_in_type_parameters`
- [ ] `insert_space_before_closing_angle_bracket_in_type_parameters`
- [ ] `insert_space_after_closing_angle_bracket_in_type_parameters`

---

## Whitespace — Brackets (Array Access)

- [ ] `insert_space_before_opening_bracket_in_array_access`
- [ ] `insert_space_after_opening_bracket_in_array_access`
- [ ] `insert_space_before_closing_bracket_in_array_access`

---

## Whitespace — Switch Arrows

- [ ] `insert_space_before_arrow_in_switch_case`
- [ ] `insert_space_after_arrow_in_switch_case`
- [ ] `insert_space_before_arrow_in_switch_default`
- [ ] `insert_space_after_arrow_in_switch_default`

---

## Whitespace — Miscellaneous

- [ ] `insert_space_after_at_in_annotation`
- [ ] `insert_space_after_at_in_annotation_type_declaration`
- [ ] `insert_space_before_ellipsis` — space before varargs `...`
- [ ] `insert_space_after_ellipsis` — space after varargs `...`
- [ ] `insert_space_before_and_in_type_parameter` — space before `&` in `T extends A & B`
- [ ] `insert_space_after_and_in_type_parameter` — space after `&` in `T extends A & B`
- [ ] `insert_space_before_question_in_conditional` — space before `?` in ternary
- [ ] `insert_space_after_question_in_conditional` — space after `?` in ternary
- [ ] `insert_space_before_question_in_wildcard` — space before `?` in `<?>` wildcard
- [ ] `insert_space_after_question_in_wildcard` — space after `?` in `<?>` wildcard
- [ ] `insert_space_after_lambda_arrow` — space after `->` in lambda
- [ ] `insert_space_before_lambda_arrow` — space before `->` in lambda

---

## Comments

- [ ] `comment.format_javadoc_comments`
- [ ] `comment.format_block_comments`
- [ ] `comment.format_line_comments`
- [ ] `comment.line_length`
- [ ] `comment.format_markdown_comments` — format markdown-style doc comments
- [ ] `comment.format_header` — format file header comments
- [ ] `comment.format_source_code` — format source code inside comments
- [ ] `comment.clear_blank_lines_in_block_comment` — remove blank lines in block comments
- [ ] `comment.clear_blank_lines_in_javadoc_comment` — remove blank lines in javadoc
- [ ] `comment.count_line_length_from_starting_position` — count line length from comment start
- [ ] `comment.indent_parameter_description` — indent `@param` description *(will be replaced by custom rule for alignment after param name)*
- [ ] `comment.indent_return_description` — indent `@return` description
- [ ] `comment.insert_new_line_before_root_tags` — blank line before `@param`, `@return`, etc.
- [ ] `comment.insert_new_line_between_different_tags` — blank line between different tag types
- [ ] `comment.insert_new_line_for_parameter` — each `@param` on its own line
- [ ] `comment.new_lines_at_block_boundaries` — newlines after `/*` and before `*/`
- [ ] `comment.new_lines_at_javadoc_boundaries` — newlines after `/**` and before `*/`
- [ ] `comment.preserve_white_space_between_code_and_line_comments` — keep space before `//`
- [ ] `comment.javadoc_paragraphs_tags_with_content` — javadoc paragraph tag handling
- [ ] `format_line_comment_starting_on_first_column` — format `//` comments starting at column 0

---

## Parentheses Preservation

- [ ] `parentheses_preservation_mode` — `PRESERVE`, `DO_NOT_PRESERVE_EMPTY`, `REMOVE_UNNECESSARY`

---

## On/Off Tags

- [ ] `use_on_off_tags` — enable `// @formatter:off` / `// @formatter:on`
- [ ] `enabling_tag` — custom enabling tag text
- [ ] `disabling_tag` — custom disabling tag text
