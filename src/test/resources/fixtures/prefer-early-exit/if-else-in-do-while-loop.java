package com.example;

public class Foo {
    void method(boolean x) {
        do {
            if (x) {
                doA();
                doB();
                doC();
            } else {
                doD();
            }
        } while (x);
    }
}
