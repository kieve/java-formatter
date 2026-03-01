package com.example;

public class Foo {
    void method(boolean x) {
        do {
            if (x) {
                doA();
            } else {
                doB();
            }
        } while (x);
    }
}
