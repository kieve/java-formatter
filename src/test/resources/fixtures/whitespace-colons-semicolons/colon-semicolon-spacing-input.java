public class FormatterTest {
    void method() {
        String[] list = { "a", "b" };
        for (String s:list) {
        }

        assert true:"message";

        int x = 1;
        switch (x) {
        case 1 :
            break;
        default :
            break;
        }

        int y = true ? 1:2;

        outer :
        for (int i = 0; i < 10; i++) {
            break outer;
        }

        int z = 1 ;

        for (int i = 0 ; i < 10 ; i++) {
        }

        try (AutoCloseable r1 = null ; AutoCloseable r2 = null) {
        } catch (Exception e) {
        }
    }
}
