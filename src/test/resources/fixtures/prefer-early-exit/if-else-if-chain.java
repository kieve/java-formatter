package com.example;

public class Foo {
    void method(int x) {
        if (x > 0) {
            doA();
            doB();
            doC();
        } else if (x < 0) {
            doD();
        } else {
            doE();
        }
    }
}
