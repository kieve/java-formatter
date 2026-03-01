public class FormatterTest {
    void method(boolean condition) {
        assert condition : "This is a very long assertion message that exceeds the line width limit";
    }
}
