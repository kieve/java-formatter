public class FormatterTest {
    class Inner {
        Inner(String first, String second, String third, String fourth) {
        }
    }

    void method() {
        FormatterTest outer = new FormatterTest();
        Inner obj = outer.new Inner(
            "firstLongArg",
            "secondLongArg",
            "thirdLongArgument",
            "fourthArg");
    }
}
