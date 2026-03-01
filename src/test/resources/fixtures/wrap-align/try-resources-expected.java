import java.io.FileInputStream;
import java.io.InputStream;

public class FormatterTest {
    void method() throws Exception {
        try (
            InputStream firstStream = new FileInputStream("a");
            InputStream secondStream = new FileInputStream("b")) {
        }
    }
}
