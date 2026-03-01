package com.example;

public class Foo {
    void method() {
        for (int i = 0; i < 10; i++) {
            if (i > 5) {
                doA();
            } else {
                doB();
            }
        }
    }
}
