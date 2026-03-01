public class FormatterTest {
    void method(int x) {
        switch (x) {
        case 1 -> {
            System.out.println("one");
        }
        default -> {
            System.out.println("other");
        }
        }
    }
}
