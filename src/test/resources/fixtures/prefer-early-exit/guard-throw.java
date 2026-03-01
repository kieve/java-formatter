package com.example;

public class Foo {
    void method(boolean x) {
        if (x) {
            throw new RuntimeException();
        }
        doA();
    }
}
