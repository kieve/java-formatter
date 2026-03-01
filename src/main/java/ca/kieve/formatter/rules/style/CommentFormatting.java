package ca.kieve.formatter.rules.style;

import ca.kieve.formatter.FormatConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enforces comment formatting rules:
 * <ul>
 *     <li>100-char line limit on all comment types</li>
 *     <li>Javadoc tags ({@code @param}, {@code @return}, {@code @throws})
 *         on their own lines</li>
 *     <li>Continuation alignment for javadoc tag text</li>
 * </ul>
 * Description text and non-javadoc comments are only split when overlength
 * (no joining/re-flowing). Tag text is fully re-flowed with correct
 * alignment.
 */
public final class CommentFormatting {
    private static final Pattern PROTECTED_PLACEHOLDER =
        Pattern.compile("^\\s*// __PROTECTED_\\d+__\\s*$");


    private CommentFormatting() {
    }

    public static String apply(String source, FormatConfig config) {
        int maxLen = config.getMaxLineLength();
        String[] lines = source.split("\n", -1);
        List<String> result = new ArrayList<>(lines.length);
        boolean inTextBlock = false;
        boolean inString = false;

        int i = 0;
        while (i < lines.length) {
            String line = lines[i];
            String trimmed = line.trim();

            if (PROTECTED_PLACEHOLDER.matcher(line).matches()) {
                result.add(line);
                i++;
                continue;
            }

            // Track text blocks and strings to skip comment-like content
            // inside them
            if (inTextBlock) {
                result.add(line);
                if (containsTextBlockEnd(line)) {
                    inTextBlock = false;
                }
                i++;
                continue;
            }

            if (inString) {
                result.add(line);
                inString = !lineEndsString(line);
                i++;
                continue;
            }

            if (containsTextBlockStart(trimmed)) {
                inTextBlock = true;
                result.add(line);
                if (containsTextBlockEnd(
                    line.substring(line.indexOf("\"\"\"") + 3))) {
                    inTextBlock = false;
                }
                i++;
                continue;
            }

            // Detect javadoc start: /** ...
            if (trimmed.startsWith("/**") && !trimmed.startsWith("/***")) {
                int blockStart = i;
                if (trimmed.endsWith("*/") && !trimmed.equals("/***/")) {
                    // Single-line javadoc
                    List<String> formatted =
                        formatSingleLineJavadoc(line, maxLen);
                    result.addAll(formatted);
                    i++;
                } else {
                    // Multi-line javadoc — collect all lines
                    List<String> block = new ArrayList<>();
                    block.add(line);
                    i++;
                    while (i < lines.length) {
                        block.add(lines[i]);
                        if (lines[i].trim().endsWith("*/")) {
                            i++;
                            break;
                        }
                        i++;
                    }
                    result.addAll(formatJavadoc(block, maxLen));
                }
                continue;
            }

            // Detect block comment start: /* ...
            if (trimmed.startsWith("/*")) {
                List<String> block = new ArrayList<>();
                block.add(line);
                if (trimmed.endsWith("*/") && trimmed.length() > 2) {
                    // Single-line block comment
                    result.addAll(formatBlockComment(block, maxLen));
                    i++;
                } else {
                    i++;
                    while (i < lines.length) {
                        block.add(lines[i]);
                        if (lines[i].trim().endsWith("*/")) {
                            i++;
                            break;
                        }
                        i++;
                    }
                    result.addAll(formatBlockComment(block, maxLen));
                }
                continue;
            }

            // Detect /// markdown doc comment
            if (trimmed.startsWith("///")) {
                List<String> block = new ArrayList<>();
                block.add(line);
                i++;
                while (i < lines.length
                    && lines[i].trim().startsWith("///")) {
                    block.add(lines[i]);
                    i++;
                }
                result.addAll(formatMarkdownDoc(block, maxLen));
                continue;
            }

            // Detect // line comment (but not ///)
            if (trimmed.startsWith("//") && !trimmed.startsWith("///")) {
                // Check if this is an inline comment (code before //)
                String beforeComment = lineBeforeComment(line);
                if (beforeComment != null) {
                    // Inline comment on a code line — skip
                    result.add(line);
                    i++;
                    continue;
                }
                List<String> block = new ArrayList<>();
                block.add(line);
                i++;
                while (i < lines.length) {
                    String nextTrimmed = lines[i].trim();
                    if (!nextTrimmed.startsWith("//")
                        || nextTrimmed.startsWith("///")
                        || lineBeforeComment(lines[i]) != null) {
                        break;
                    }
                    block.add(lines[i]);
                    i++;
                }
                result.addAll(formatLineComments(block, maxLen));
                continue;
            }

            // Check for string literal that might span (shouldn't in Java,
            // but be safe)
            if (containsUnclosedString(trimmed)) {
                inString = true;
            }

            result.add(line);
            i++;
        }

        return String.join("\n", result);
    }

    // ── Javadoc formatting ──────────────────────────────────────────────

    private static List<String> formatSingleLineJavadoc(
        String line,
        int maxLen
    ) {
        if (line.length() <= maxLen) {
            return List.of(line);
        }
        // Expand to multi-line and format
        String indent = extractIndent(line);
        String trimmed = line.trim();
        // Strip /** and */
        String content =
            trimmed.substring(3, trimmed.length() - 2).trim();
        List<String> expanded = new ArrayList<>();
        expanded.add(indent + "/**");
        expanded.add(indent + " * " + content);
        expanded.add(indent + " */");
        return formatJavadoc(expanded, maxLen);
    }

    private static List<String> formatJavadoc(
        List<String> lines,
        int maxLen
    ) {
        if (lines.isEmpty()) {
            return lines;
        }

        String indent = extractIndent(lines.get(0));
        String starPrefix = indent + " * ";
        String emptyStarLine = indent + " *";

        // Parse into segments: description lines and tag groups
        List<Segment> segments = parseJavadocSegments(lines, indent);

        // Format each segment
        List<String> result = new ArrayList<>();
        result.add(indent + "/**");

        boolean inVerbatim = false;
        for (Segment seg : segments) {
            if (seg instanceof DescriptionSegment desc) {
                for (String descLine : desc.lines) {
                    if (isVerbatimStart(descLine)) {
                        inVerbatim = true;
                    }
                    if (inVerbatim) {
                        result.add(descLine);
                        if (isVerbatimEnd(descLine)) {
                            inVerbatim = false;
                        }
                        continue;
                    }
                    if (descLine.equals(emptyStarLine)
                        || descLine.trim().equals("*")) {
                        result.add(emptyStarLine);
                        continue;
                    }
                    // Split-only for description lines
                    result.addAll(
                        splitOverlength(descLine, starPrefix, maxLen));
                }
                continue;
            }
            if (!(seg instanceof TagSegment tag)) {
                continue;
            }
            result.addAll(formatTagSegment(tag, starPrefix, maxLen));
        }

        result.add(indent + " */");
        return result;
    }

    private static final Pattern TAG_START_PATTERN =
        Pattern.compile("@(param|return|returns|throws|exception"
            + "|see|since|version|author|deprecated|serial|serialField"
            + "|serialData|hidden|provides|uses)\\b");

    private static List<Segment> parseJavadocSegments(
        List<String> lines,
        String indent
    ) {
        List<Segment> segments = new ArrayList<>();
        String starPrefix = indent + " * ";

        // Skip first line (/**) and last line (*/)
        List<String> rawBodyLines = new ArrayList<>();
        for (int i = 1; i < lines.size() - 1; i++) {
            rawBodyLines.add(lines.get(i));
        }

        // Handle /** on same line as content
        String firstLine = lines.get(0).trim();
        if (firstLine.length() > 3) {
            String afterOpen = firstLine.substring(3).trim();
            if (!afterOpen.isEmpty()) {
                rawBodyLines.add(0, starPrefix + afterOpen);
            }
        }

        // Strip star prefixes and split mid-line tags
        List<String> strippedParts = new ArrayList<>();
        for (String line : rawBodyLines) {
            String stripped = stripStarPrefix(line, indent);
            List<String> split = splitAtTags(stripped);
            strippedParts.addAll(split);
        }

        List<String> currentDescLines = new ArrayList<>();
        TagSegment currentTag = null;

        for (String part : strippedParts) {
            boolean isTag = TAG_START_PATTERN.matcher(part).lookingAt();

            if (isTag) {
                // Flush description
                if (!currentDescLines.isEmpty()) {
                    segments.add(new DescriptionSegment(
                        new ArrayList<>(currentDescLines)));
                    currentDescLines.clear();
                }
                // Flush previous tag
                if (currentTag != null) {
                    segments.add(currentTag);
                }
                currentTag = new TagSegment(part, indent);
                continue;
            }
            if (currentTag != null) {
                // Continuation of current tag
                currentTag.addContinuation(part);
                continue;
            }
            // Description line — reconstruct with star prefix
            if (part.isEmpty()) {
                currentDescLines.add(indent + " *");
                continue;
            }
            currentDescLines.add(starPrefix + part);
        }

        // Flush remaining
        if (!currentDescLines.isEmpty()) {
            segments.add(
                new DescriptionSegment(new ArrayList<>(currentDescLines)));
        }
        if (currentTag != null) {
            segments.add(currentTag);
        }

        return segments;
    }

    private static List<String> splitAtTags(String stripped) {
        List<Integer> tagPositions = new ArrayList<>();
        Matcher m = TAG_START_PATTERN.matcher(stripped);

        while (m.find()) {
            int pos = m.start();
            // Check not inside braces (would be an inline tag like {@link})
            int braceDepth = 0;
            for (int i = 0; i < pos; i++) {
                if (stripped.charAt(i) == '{') {
                    braceDepth++;
                    continue;
                }
                if (stripped.charAt(i) != '}') {
                    continue;
                }
                braceDepth--;
            }
            if (braceDepth != 0) {
                continue;
            }
            tagPositions.add(pos);
        }

        if (tagPositions.isEmpty() || tagPositions.get(0) == 0) {
            if (tagPositions.size() <= 1) {
                return List.of(stripped);
            }
        }

        List<String> parts = new ArrayList<>();
        int lastStart = 0;
        for (int pos : tagPositions) {
            if (pos > lastStart) {
                String before = stripped.substring(lastStart, pos).trim();
                if (!before.isEmpty()) {
                    parts.add(before);
                }
            }
            lastStart = pos;
        }
        String last = stripped.substring(lastStart).trim();
        if (!last.isEmpty()) {
            parts.add(last);
        }

        return parts.isEmpty() ? List.of(stripped) : parts;
    }

    private static String stripStarPrefix(String line, String indent) {
        String trimmed = line.trim();
        if (trimmed.startsWith("* ")) {
            return trimmed.substring(2);
        }
        if (trimmed.equals("*")) {
            return "";
        }
        return trimmed;
    }

    private static List<String> formatTagSegment(
        TagSegment tag,
        String starPrefix,
        int maxLen
    ) {
        String fullText = tag.getFullText();
        String contIndentStr = tag.getContinuationIndent();
        String contPrefix = starPrefix + contIndentStr;

        // Tokenize and wrap
        List<String> tokens = tokenize(fullText);
        if (tokens.isEmpty()) {
            return List.of(starPrefix + tag.tagLine);
        }

        return wrapLine(tokens, starPrefix, contPrefix, maxLen);
    }

    // ── Block comment formatting ────────────────────────────────────────

    private static List<String> formatBlockComment(
        List<String> lines,
        int maxLen
    ) {
        List<String> result = new ArrayList<>();
        boolean inVerbatim = false;

        for (String line : lines) {
            if (isVerbatimStart(line)) {
                inVerbatim = true;
            }
            if (inVerbatim) {
                result.add(line);
                if (isVerbatimEnd(line)) {
                    inVerbatim = false;
                }
                continue;
            }

            if (line.length() <= maxLen) {
                result.add(line);
                continue;
            }

            String trimmed = line.trim();

            // Determine continuation prefix from actual line structure
            String contPrefix;
            if (trimmed.startsWith("/* ")) {
                contPrefix = extractIndent(line) + " * ";
            } else {
                int starIdx = line.indexOf('*');
                contPrefix = line.substring(0, starIdx) + "* ";
            }

            result.addAll(splitOverlength(line, contPrefix, maxLen));
        }

        return result;
    }

    // ── Line comment formatting ─────────────────────────────────────────

    private static List<String> formatLineComments(
        List<String> lines,
        int maxLen
    ) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            if (line.length() <= maxLen) {
                result.add(line);
                continue;
            }
            String indent = extractIndent(line);
            String prefix = indent + "// ";
            result.addAll(splitOverlength(line, prefix, maxLen));
        }
        return result;
    }

    // ── Markdown doc comment formatting ─────────────────────────────────

    private static List<String> formatMarkdownDoc(
        List<String> lines,
        int maxLen
    ) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            if (line.length() <= maxLen) {
                result.add(line);
                continue;
            }
            String indent = extractIndent(line);
            String prefix = indent + "/// ";
            result.addAll(splitOverlength(line, prefix, maxLen));
        }
        return result;
    }

    // ── Shared helpers ──────────────────────────────────────────────────

    /**
     * Split an overlength line at word boundaries. Only splits — never
     * joins with adjacent lines. The first output line keeps the original
     * text up to the last word boundary that fits; continuation lines use
     * the given prefix.
     */
    private static List<String> splitOverlength(
        String line,
        String contPrefix,
        int maxLen
    ) {
        if (line.length() <= maxLen) {
            return List.of(line);
        }

        String indent = extractIndent(line);
        String trimmed = line.trim();

        // Figure out what the "text" portion is (after comment marker)
        String text;
        String firstPrefix;
        if (trimmed.startsWith("/// ")) {
            firstPrefix = indent + "/// ";
            text = trimmed.substring(4);
        } else if (trimmed.startsWith("// ")) {
            firstPrefix = indent + "// ";
            text = trimmed.substring(3);
        } else if (trimmed.startsWith("* ")) {
            firstPrefix = indent + " * ";
            // But preserve the original indent structure
            int starIdx = line.indexOf('*');
            if (starIdx >= 0) {
                firstPrefix = line.substring(0, starIdx) + "* ";
            }
            text = trimmed.substring(2);
        } else if (trimmed.startsWith("/* ")) {
            firstPrefix = indent + "/* ";
            text = trimmed.substring(3);
            // After split, continuation uses " * " not "/* "
        } else {
            // Can't parse the comment structure, leave as-is
            return List.of(line);
        }

        List<String> tokens = tokenize(text);
        if (tokens.isEmpty()) {
            return List.of(line);
        }

        return wrapLine(tokens, firstPrefix, contPrefix, maxLen);
    }

    static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            // Skip whitespace
            if (Character.isWhitespace(text.charAt(i))) {
                i++;
                continue;
            }

            // Check for inline tag: {@link ...} or {@code ...}
            if (text.charAt(i) == '{' && i + 1 < text.length()
                && text.charAt(i + 1) == '@') {
                int depth = 1;
                int j = i + 2;
                while (j < text.length() && depth > 0) {
                    if (text.charAt(j) == '{') {
                        depth++;
                    } else if (text.charAt(j) == '}') {
                        depth--;
                    }
                    j++;
                }
                tokens.add(text.substring(i, j));
                i = j;
                continue;
            }

            // Check for URL
            if ((text.charAt(i) == 'h')
                && text.substring(i).startsWith("http://")
                || text.substring(i).startsWith("https://")) {
                int j = i;
                while (j < text.length()
                    && !Character.isWhitespace(text.charAt(j))) {
                    j++;
                }
                tokens.add(text.substring(i, j));
                i = j;
                continue;
            }

            // Regular word
            int j = i;
            while (j < text.length()
                && !Character.isWhitespace(text.charAt(j))) {
                j++;
            }
            tokens.add(text.substring(i, j));
            i = j;
        }
        return tokens;
    }

    private static List<String> wrapLine(
        List<String> tokens,
        String firstPrefix,
        String contPrefix,
        int maxLen
    ) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder(firstPrefix);
        boolean first = true;
        boolean onFirstLine = true;

        for (String token : tokens) {
            String prefix = onFirstLine ? firstPrefix : contPrefix;
            int projected = current.length()
                + (first ? 0 : 1)
                + token.length();

            if (!first && projected > maxLen) {
                result.add(current.toString());
                current = new StringBuilder(contPrefix);
                current.append(token);
                onFirstLine = false;
                first = false;
                continue;
            }
            if (!first) {
                current.append(' ');
            }
            current.append(token);
            first = false;
        }

        if (!current.toString().equals(contPrefix)
            && !current.toString().equals(firstPrefix)) {
            result.add(current.toString());
        } else if (result.isEmpty()) {
            result.add(current.toString());
        }

        return result;
    }

    private static String extractIndent(String line) {
        int i = 0;
        while (i < line.length()
            && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(0, i);
    }

    private static boolean containsTextBlockStart(String trimmed) {
        int idx = trimmed.indexOf("\"\"\"");
        if (idx < 0) {
            return false;
        }
        // Make sure it's not inside a line comment
        int commentIdx = trimmed.indexOf("//");
        return commentIdx < 0 || idx < commentIdx;
    }

    private static boolean containsTextBlockEnd(String line) {
        // Look for closing """ that's not the opening one
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\\' && i + 1 < line.length()) {
                i++;
                continue;
            }
            if (line.charAt(i) != '"' || i + 2 >= line.length()
                || line.charAt(i + 1) != '"'
                || line.charAt(i + 2) != '"') {
                continue;
            }
            count++;
            i += 2;
        }
        // If we find closing """, the text block ends
        return count > 0;
    }

    private static boolean containsUnclosedString(String trimmed) {
        // Simplified check — not needed for correctness since Java strings
        // don't span lines, but kept for safety
        return false;
    }

    private static boolean lineEndsString(String line) {
        return true;
    }

    /**
     * Returns the code portion before a line comment, or null if the line
     * is a standalone comment (only whitespace before //).
     */
    private static String lineBeforeComment(String line) {
        String indent = extractIndent(line);
        String afterIndent = line.substring(indent.length());
        if (afterIndent.startsWith("//")) {
            return null;
        }
        return indent;
    }

    private static boolean isVerbatimStart(String line) {
        String trimmed = line.trim();
        String stripped = trimmed;
        if (stripped.startsWith("* ")) {
            stripped = stripped.substring(2).trim();
        } else if (stripped.startsWith("*")) {
            stripped = stripped.substring(1).trim();
        }
        return stripped.equals("<pre>")
            || stripped.startsWith("<pre>")
            || stripped.equals("```")
            || stripped.startsWith("```")
            || stripped.equals("{@code")
            || isMultiLineCodeTag(stripped);
    }

    private static boolean isVerbatimEnd(String line) {
        String trimmed = line.trim();
        String stripped = trimmed;
        if (stripped.startsWith("* ")) {
            stripped = stripped.substring(2).trim();
        } else if (stripped.startsWith("*")) {
            stripped = stripped.substring(1).trim();
        }
        return stripped.equals("</pre>")
            || stripped.endsWith("</pre>")
            || stripped.equals("```")
            || stripped.equals("}");
    }

    private static boolean isMultiLineCodeTag(String stripped) {
        // {@code that doesn't close on the same line
        if (!stripped.startsWith("{@code")) {
            return false;
        }
        int braceDepth = 0;
        for (int i = 0; i < stripped.length(); i++) {
            if (stripped.charAt(i) == '{') {
                braceDepth++;
                continue;
            }
            if (stripped.charAt(i) != '}') {
                continue;
            }
            braceDepth--;
            if (braceDepth == 0) {
                return false; // Closes on same line
            }
        }
        return braceDepth > 0;
    }

    // ── Segment types for javadoc parsing ───────────────────────────────

    private sealed interface Segment {
    }

    private record DescriptionSegment(List<String> lines) implements Segment {
    }

    private static final class TagSegment implements Segment {
        final String tagLine;
        final String indent;
        private final List<String> continuationLines = new ArrayList<>();

        TagSegment(String tagLine, String indent) {
            this.tagLine = tagLine;
            this.indent = indent;
        }

        void addContinuation(String line) {
            if (line.isEmpty()) {
                return;
            }
            continuationLines.add(line);
        }

        String getFullText() {
            StringBuilder sb = new StringBuilder(tagLine);
            for (String cont : continuationLines) {
                sb.append(' ').append(cont);
            }
            return sb.toString();
        }

        String getContinuationIndent() {
            // @param name text → align under text
            // @return text → align under text
            // @throws Type text → align under text
            String text = tagLine;
            if (text.startsWith("@param ")) {
                String rest = text.substring("@param ".length());
                // Find the parameter name
                int spaceIdx = rest.indexOf(' ');
                if (spaceIdx >= 0) {
                    return " ".repeat("@param ".length() + spaceIdx + 1);
                }
                return " ".repeat("@param ".length());
            }
            if (text.startsWith("@return ") || text.startsWith("@returns ")) {
                String tag = text.startsWith("@returns ")
                    ? "@returns " : "@return ";
                return " ".repeat(tag.length());
            }
            if (text.startsWith("@throws ")
                || text.startsWith("@exception ")) {
                String tag = text.startsWith("@exception ")
                    ? "@exception " : "@throws ";
                String rest = text.substring(tag.length());
                int spaceIdx = rest.indexOf(' ');
                if (spaceIdx >= 0) {
                    return " ".repeat(tag.length() + spaceIdx + 1);
                }
                return " ".repeat(tag.length());
            }
            // Other tags: just indent past @tag
            int spaceIdx = text.indexOf(' ');
            if (spaceIdx >= 0) {
                return " ".repeat(spaceIdx + 1);
            }
            return "    ";
        }
    }
}
