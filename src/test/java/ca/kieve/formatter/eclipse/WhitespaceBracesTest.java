package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Braces section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Braces"
 */
class WhitespaceBracesTest {

    // insert_space_before_opening_brace_in_type_declaration
    // insert_space_before_opening_brace_in_method_declaration
    // insert_space_before_opening_brace_in_block
    // insert_space_before_opening_brace_in_array_initializer
    // insert_space_after_opening_brace_in_array_initializer
    // insert_space_before_closing_brace_in_array_initializer
    // insert_space_after_closing_brace_in_block
    @Test
    void braceSpacing() {
        // language=Java
        String input = """
                public class FormatterTest{
                    void method(){
                        if (true){
                        }else{
                        }

                        for (int i = 0; i < 10; i++){
                        }

                        while (true){
                            break;
                        }

                        do{
                        }while (true);

                        try{
                        }catch (Exception e){
                        }finally{
                        }

                        synchronized (this){
                        }

                        int[] a = new int[]{1, 2, 3};
                    }
                }
                """;

        // language=Java
        String expected = """
                public class FormatterTest {
                    void method() {
                        if (true) {
                        } else {
                        }

                        for (int i = 0; i < 10; i++) {
                        }

                        while (true) {
                            break;
                        }

                        do {
                        } while (true);

                        try {
                        } catch (Exception e) {
                        } finally {
                        }

                        synchronized (this) {
                        }

                        int[] a = new int[] { 1, 2, 3 };
                    }
                }
                """;

        assertEquals(expected, formatJava(input));
    }
}
