package com.example;

public class Foo {
    void method(Object x) {
        if (x == null) throw new IllegalArgumentException();
        doA();
    }
}
