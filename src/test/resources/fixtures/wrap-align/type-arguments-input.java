public class FormatterTest {
    <A, B, C, D> void genericMethod() {
    }

    void caller() {
        this.<FirstLongerTypeXx, SecondLongerTypeXx, ThirdLongerTypeXx, FourthLongerType>genericMethod();
    }
}
