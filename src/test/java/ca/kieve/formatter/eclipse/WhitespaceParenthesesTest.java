package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Parentheses sections.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Parentheses (Opening)"
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Parentheses (After Opening / Before Closing)"
 */
class WhitespaceParenthesesTest {

    // insert_space_before_opening_paren_in_method_declaration
    // insert_space_before_opening_paren_in_method_invocation
    // insert_space_before_opening_paren_in_if
    // insert_space_before_opening_paren_in_for
    // insert_space_before_opening_paren_in_while
    // insert_space_before_opening_paren_in_switch
    // insert_space_before_opening_paren_in_catch
    // insert_space_before_opening_paren_in_constructor_declaration
    // insert_space_before_opening_paren_in_annotation
    // insert_space_before_opening_paren_in_enum_constant
    // insert_space_before_opening_paren_in_record_declaration
    // insert_space_before_opening_paren_in_synchronized
    // insert_space_after_opening_paren_in_method_declaration
    // insert_space_after_opening_paren_in_method_invocation
    // insert_space_after_opening_paren_in_constructor_declaration
    // insert_space_after_opening_paren_in_if
    // insert_space_after_opening_paren_in_for
    // insert_space_after_opening_paren_in_while
    // insert_space_after_opening_paren_in_switch
    // insert_space_after_opening_paren_in_catch
    // insert_space_after_opening_paren_in_synchronized
    // insert_space_after_opening_paren_in_cast
    // insert_space_after_opening_paren_in_annotation
    // insert_space_after_opening_paren_in_enum_constant
    // insert_space_after_opening_paren_in_record_declaration
    // insert_space_before_closing_paren_in_method_declaration
    // insert_space_before_closing_paren_in_method_invocation
    // insert_space_before_closing_paren_in_constructor_declaration
    // insert_space_before_closing_paren_in_if
    // insert_space_before_closing_paren_in_for
    // insert_space_before_closing_paren_in_while
    // insert_space_before_closing_paren_in_switch
    // insert_space_before_closing_paren_in_catch
    // insert_space_before_closing_paren_in_synchronized
    // insert_space_before_closing_paren_in_cast
    // insert_space_before_closing_paren_in_annotation
    // insert_space_before_closing_paren_in_enum_constant
    // insert_space_before_closing_paren_in_record_declaration
    // insert_space_after_closing_paren_in_cast
    @Test
    void parenthesisSpacing() {
        // language=Java
        String input = """
                @SuppressWarnings ( "unchecked" )
                public class FormatterTest {
                    enum Color {
                        RED ( "red" );

                        Color ( String s ) {
                        }
                    }

                    record Point ( int x, int y ) {
                    }

                    FormatterTest ( int x ) {
                    }

                    void method ( int a ) {
                        System.out.println ( "hello" );

                        if( true ) {
                        }

                        for( int i = 0; i < 10; i++ ) {
                        }

                        while( true ) {
                            break;
                        }

                        switch( a ) {
                        case 1:
                            break;
                        }

                        try {
                        } catch( Exception e ) {
                        }

                        synchronized( this ) {
                        }

                        Object obj = "hello";
                        String s = ( String )obj;
                    }
                }
                """;

        // language=Java
        String expected = """
                @SuppressWarnings("unchecked")
                public class FormatterTest {
                    enum Color {
                        RED("red");

                        Color(String s) {
                        }
                    }

                    record Point(int x, int y) {
                    }

                    FormatterTest(int x) {
                    }

                    void method(int a) {
                        System.out.println("hello");

                        if (true) {
                        }

                        for (int i = 0; i < 10; i++) {
                        }

                        while (true) {
                            break;
                        }

                        switch (a) {
                        case 1:
                            break;
                        }

                        try {
                        } catch (Exception e) {
                        }

                        synchronized (this) {
                        }

                        Object obj = "hello";
                        String s = (String) obj;
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
