import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

public class FormatterTest {
    @Target(ElementType.TYPE_USE)
    @interface NonNull {
    }

    List<@NonNull String> method() {
        return List.of();
    }
}
