public class Foo {
    static {
        System.setProperty("key", "value");
    }

    private static final String CONFIG = System.getProperty("key");

    int instanceField;

    Foo() {
    }
}
