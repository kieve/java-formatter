public class FormatterTest {
    @interface Config {
        String value();
    }

    <T extends Comparable & Cloneable> void bounded(T t) {
    }

    @Override
    public String toString() {
        return "test";
    }

    void method(String... args) {
        int a = true ? 1 : 2;

        Class<?> c = null;

        Runnable r = () -> System.out.println("hello");
    }
}
