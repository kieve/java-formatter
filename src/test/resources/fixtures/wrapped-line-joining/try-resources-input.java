public class FormatterTest {
    void test() {
        try (
            InputStream is = ResourceFactory.builder()
                .withParam("value")
                .build()
        ) {
            // body
        }
    }
}
