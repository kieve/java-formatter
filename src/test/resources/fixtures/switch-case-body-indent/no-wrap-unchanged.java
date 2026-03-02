public class FormatterTest {
    void method(String x) {
        switch (x) {
        case "a" -> System.out.println("match");
        case "b":
            System.out.println("match");
            break;
        case "c", "d" -> System.out.println("multi");
        default -> System.out.println("other");
        }
    }
}
