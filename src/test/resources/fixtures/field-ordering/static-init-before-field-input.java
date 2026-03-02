public class Foo {
    int instanceField;

    Foo() {
    }

    static {
        System.setProperty("key", "value");
    }

    private static final String CONFIG = System.getProperty("key");
}
