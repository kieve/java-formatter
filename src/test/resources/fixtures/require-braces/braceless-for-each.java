package com.example;

import java.util.List;

public class Foo {
    void method(List<String> items) {
        for (String item : items)
            doA(item);
    }
}
