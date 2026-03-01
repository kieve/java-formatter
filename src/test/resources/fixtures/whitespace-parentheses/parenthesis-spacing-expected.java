@SuppressWarnings("unchecked")
public class FormatterTest {
    enum Color {
        RED("red");

        Color(String s) {
        }
    }

    record Point(int x, int y) {
    }

    FormatterTest(int x) {
    }

    void method(int a) {
        System.out.println("hello");

        if (true) {
        }

        for (int i = 0; i < 10; i++) {
        }

        while (true) {
            break;
        }

        switch (a) {
        case 1:
            break;
        }

        try {
        } catch (Exception e) {
        }

        synchronized (this) {
        }

        Object obj = "hello";
        String s = (String) obj;
    }
}
