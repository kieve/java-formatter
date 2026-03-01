public class FormatterTest {
    @interface Config {
        String name();

        int value();
    }

    class Pair<A ,B> {
    }

    enum Color {
        RED(1 ,2) ,
        GREEN(3 ,4) ,
        BLUE(5 ,6);

        Color(int a ,int b) {
        }
    }

    record Point(int x ,int y) {
    }

    int a ,b ,c;

    FormatterTest(int x ,int y) {
    }

    <T ,U> void genericMethod(T a ,U b) {
    }

    @Config(name = "test" ,value = 1)
    void method(int x ,int y) {
        int d ,e ,f;
        System.out.printf("%d %d" ,x ,y);

        Object obj = new FormatterTest(1 ,2);

        int[] arr = { 1 ,2 ,3 };

        Pair<String ,Integer> pair = null;
        this.<String ,Integer>genericMethod("a" ,1);

        for (int i = 0 ,j = 0; i < 10; i++ ,j++) {
        }

        switch (x) {
        case 1 ,2 ,3:
            break;
        }
    }
}
