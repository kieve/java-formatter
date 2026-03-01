public class FormatterTest {
    void method(String x) {
        switch (x) {
        case "firstLongerValueXx", "secondLongerValueXx", "thirdLongerValueXx", "fourthLongerValueXx" -> System.out.println("match");
        default -> System.out.println("other");
        }
    }
}
