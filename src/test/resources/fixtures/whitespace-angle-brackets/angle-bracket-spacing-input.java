public class FormatterTest < T, U > {
    class Container < K, V > {
    }

    < A, B >void genericMethod(A a, B b) {
    }

    void method() {
        Container < String, Integer > c = null;
        this. < String, Integer >genericMethod("a", 1);
    }
}
