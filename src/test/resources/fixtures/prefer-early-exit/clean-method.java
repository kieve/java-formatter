package com.example;

public class Foo {
    void method(boolean x, boolean y) {
        if (!x) {
            return;
        }
        if (!y) {
            return;
        }
        doA();
        doB();
    }
}
