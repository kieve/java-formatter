public class FormatterTest {
    void method() {
        Runnable empty = () -> {};
        Runnable nonEmpty = () -> {
            System.out.println("hello");
        };
    }
}
