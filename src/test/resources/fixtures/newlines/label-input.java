public class FormatterTest {
    void method() {
        outer: for (int i = 0; i < 10; i++) {
            break outer;
        }
    }
}
