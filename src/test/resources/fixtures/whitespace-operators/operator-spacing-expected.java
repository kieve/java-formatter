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
