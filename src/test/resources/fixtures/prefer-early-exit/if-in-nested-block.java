package com.example;

public class Foo {
    void method(boolean x, boolean y) {
        if (x) {
            if (y) {
                doA();
            } else {
                doB();
            }
        }
        doC();
    }
}
