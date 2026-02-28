package ca.kieve.formatter.eclipse;

import org.junit.jupiter.api.Test;

import static ca.kieve.formatter.util.DirectFormatterTestUtil.formatJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Eclipse JDT Formatter — Whitespace Operators section.
 *
 * @see prompts/eclipseFormatterTestChecklist.md — "Whitespace — Operators"
 */
class WhitespaceOperatorsTest {
    // insert_space_before_assignment_operator
    // insert_space_after_assignment_operator
    // insert_space_before_additive_operator
    // insert_space_after_additive_operator
    // insert_space_before_multiplicative_operator
    // insert_space_after_multiplicative_operator
    // insert_space_before_bitwise_operator
    // insert_space_after_bitwise_operator
    // insert_space_before_logical_operator
    // insert_space_after_logical_operator
    // insert_space_before_relational_operator
    // insert_space_after_relational_operator
    // insert_space_before_shift_operator
    // insert_space_after_shift_operator
    // insert_space_before_string_concatenation
    // insert_space_after_string_concatenation
    // insert_space_before_unary_operator
    // insert_space_after_unary_operator
    // insert_space_before_prefix_operator
    // insert_space_after_prefix_operator
    // insert_space_before_postfix_operator
    // insert_space_after_postfix_operator
    // insert_space_after_not_operator
    @Test
    void operatorSpacing() {
        // language=Java
        // @formatter:off
        String input = """
                public class FormatterTest {
                    void method(int a) {
                        int b=1;
                        b+=2;
                        b-=1;
                        b*=3;
                        b/=2;
                        b%=2;
                        b&=0xFF;
                        b|=0x01;
                        b^=0x10;
                        b>>=1;
                        b<<=1;
                        b>>>=1;

                        int c=a+b;
                        int d=a-b;

                        int e=a*b;
                        int f=a/b;
                        int g=a%b;

                        int h=a&0xFF;
                        int i=a|0x01;
                        int j=a^0x10;

                        boolean k=true&&false;
                        boolean l=true||false;

                        boolean m=a<10;
                        boolean n=a>5;
                        boolean o=a<=10;
                        boolean p=a>=5;
                        boolean q=a==1;
                        boolean r=a!=1;

                        int s=a<<2;
                        int t=a>>2;
                        int u=a>>>2;

                        String v="hello"+"world";

                        int w = - a;
                        int x = + a;

                        ++ b;
                        -- b;

                        b ++;
                        b --;

                        boolean y = ! true;
                        int z = ~ a;
                    }
                }
                """;
                // @formatter:on

        // language=Java
        // @formatter:off
        String expected = """
                public class FormatterTest {
                    void method(int a) {
                        int b = 1;
                        b += 2;
                        b -= 1;
                        b *= 3;
                        b /= 2;
                        b %= 2;
                        b &= 0xFF;
                        b |= 0x01;
                        b ^= 0x10;
                        b >>= 1;
                        b <<= 1;
                        b >>>= 1;

                        int c = a + b;
                        int d = a - b;

                        int e = a * b;
                        int f = a / b;
                        int g = a % b;

                        int h = a & 0xFF;
                        int i = a | 0x01;
                        int j = a ^ 0x10;

                        boolean k = true && false;
                        boolean l = true || false;

                        boolean m = a < 10;
                        boolean n = a > 5;
                        boolean o = a <= 10;
                        boolean p = a >= 5;
                        boolean q = a == 1;
                        boolean r = a != 1;

                        int s = a << 2;
                        int t = a >> 2;
                        int u = a >>> 2;

                        String v = "hello" + "world";

                        int w = -a;
                        int x = +a;

                        ++b;
                        --b;

                        b++;
                        b--;

                        boolean y = !true;
                        int z = ~a;
                    }
                }
                """;
                // @formatter:on

        assertEquals(expected, formatJava(input));
    }
}
