import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@interface NotNull {
}

@Target(ElementType.TYPE_USE)
@interface Immutable {
}

public class FormatterTest {
    void method() {
        @NotNull @Immutable String value = "";
    }
}
