package ca.kieve.formatter.rules;

import ca.kieve.formatter.FormatConfig;
import ca.kieve.formatter.ImportGroup;
import ca.kieve.formatter.step.CustomFormatterStep;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImportSortingTest extends FormatterRuleTestBase {

    private static final FormatConfig CONFIG = FormatConfig.defaults();

    @Test
    void sortsImportsIntoGroups() {
        // language=Java
        String input = """
                package com.example;

                import static org.junit.jupiter.api.Assertions.assertEquals;
                import javax.swing.JFrame;
                import ca.kieve.formatter.FormatConfig;
                import org.slf4j.Logger;
                import java.util.List;
                import static java.util.Collections.emptyList;
                import ca.kieve.formatter.ImportGroup;
                import com.google.common.collect.ImmutableList;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import com.google.common.collect.ImmutableList;
                import org.slf4j.Logger;

                import ca.kieve.formatter.FormatConfig;
                import ca.kieve.formatter.ImportGroup;

                import javax.swing.JFrame;
                import java.util.List;

                import static java.util.Collections.emptyList;
                import static org.junit.jupiter.api.Assertions.assertEquals;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void alreadySortedRemainsUnchanged() {
        // language=Java
        String input = """
                package com.example;

                import com.google.common.collect.ImmutableList;
                import org.slf4j.Logger;

                import ca.kieve.formatter.FormatConfig;

                import javax.swing.JFrame;
                import java.util.List;

                import static org.junit.jupiter.api.Assertions.assertEquals;

                public class Foo {
                }
                """;

        assertEquals(input, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void handlesOnlyJavaAndJavaxImports() {
        // language=Java
        String input = """
                package com.example;

                import java.util.Map;
                import javax.swing.JFrame;
                import java.util.List;
                import javax.annotation.Nullable;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import javax.annotation.Nullable;
                import javax.swing.JFrame;
                import java.util.List;
                import java.util.Map;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void handlesOnlyStaticImports() {
        // language=Java
        String input = """
                package com.example;

                import static org.junit.jupiter.api.Assertions.assertTrue;
                import static org.junit.jupiter.api.Assertions.assertEquals;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import static org.junit.jupiter.api.Assertions.assertEquals;
                import static org.junit.jupiter.api.Assertions.assertTrue;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void handlesOnlyOtherImports() {
        // language=Java
        String input = """
                package com.example;

                import org.slf4j.LoggerFactory;
                import org.slf4j.Logger;
                import com.google.common.collect.ImmutableList;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import com.google.common.collect.ImmutableList;
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void removesBlankLinesBetweenImportsInSameGroup() {
        // language=Java
        String input = """
                package com.example;

                import org.slf4j.Logger;

                import org.slf4j.LoggerFactory;

                import com.google.common.collect.ImmutableList;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import com.google.common.collect.ImmutableList;
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void deduplicatesImports() {
        // language=Java
        String input = """
                package com.example;

                import java.util.List;
                import java.util.List;
                import java.util.Map;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import java.util.List;
                import java.util.Map;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void noImportsReturnsUnchanged() {
        // language=Java
        String input = """
                package com.example;

                public class Foo {
                }
                """;

        assertEquals(input, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void wildcardImportThrows() {
        // language=Java
        String input = """
                package com.example;

                import java.util.*;

                public class Foo {
                }
                """;

        assertThrows(IllegalStateException.class,
                () -> ImportSorting.apply(input, CONFIG));
    }

    @Test
    void staticWildcardImportThrows() {
        // language=Java
        String input = """
                package com.example;

                import static org.junit.jupiter.api.Assertions.*;

                public class Foo {
                }
                """;

        assertThrows(IllegalStateException.class,
                () -> ImportSorting.apply(input, CONFIG));
    }

    @Test
    void preservesSurroundingContent() {
        // language=Java
        String input = """
                package com.example;

                import java.util.Map;
                import org.slf4j.Logger;

                /**
                 * A sample class.
                 */
                public class Foo {
                    private int x = 42;
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import org.slf4j.Logger;

                import java.util.Map;

                /**
                 * A sample class.
                 */
                public class Foo {
                    private int x = 42;
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Test
    void handlesNoPackageStatement() {
        // language=Java
        String input = """
                import java.util.Map;
                import org.slf4j.Logger;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                import org.slf4j.Logger;

                import java.util.Map;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, CONFIG));
    }

    @Override
    void respectsFormatterOffTags() {
        // language=Java
        String input = """
                package com.example;

                // @formatter:off
                import java.util.Map;
                import org.slf4j.Logger;
                // @formatter:on

                public class Foo {
                }
                """;

        assertEquals(input, CustomFormatterStep.applyCustomRules(input));
    }

    @Test
    void customLayoutReordersGroups() {
        // Layout: static catch-all, then java/javax, then regular catch-all
        FormatConfig customConfig = new FormatConfig(100, List.of(
                ImportGroup.staticCatchAll(),
                ImportGroup.of("java.", "javax."),
                ImportGroup.catchAll()
        ), true);

        // language=Java
        String input = """
                package com.example;

                import org.slf4j.Logger;
                import java.util.List;
                import static org.junit.jupiter.api.Assertions.assertEquals;
                import javax.swing.JFrame;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.example;

                import static org.junit.jupiter.api.Assertions.assertEquals;

                import java.util.List;
                import javax.swing.JFrame;

                import org.slf4j.Logger;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, customConfig));
    }

    @Test
    void customProjectPrefix() {
        FormatConfig customConfig = new FormatConfig(100, List.of(
                ImportGroup.catchAll(),
                ImportGroup.of("com.mycompany."),
                ImportGroup.of("javax.", "java."),
                ImportGroup.staticCatchAll()
        ), true);

        // language=Java
        String input = """
                package com.mycompany.app;

                import java.util.List;
                import com.mycompany.core.Engine;
                import org.slf4j.Logger;
                import com.mycompany.util.Helper;

                public class Foo {
                }
                """;

        // language=Java
        String expected = """
                package com.mycompany.app;

                import org.slf4j.Logger;

                import com.mycompany.core.Engine;
                import com.mycompany.util.Helper;

                import java.util.List;

                public class Foo {
                }
                """;

        assertEquals(expected, ImportSorting.apply(input, customConfig));
    }
}
