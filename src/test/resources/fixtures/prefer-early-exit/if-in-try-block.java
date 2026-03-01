package com.example;

public class Foo {
    void method(boolean x) {
        try {
            if (x) {
                doA();
            } else {
                doB();
            }
        } catch (Exception e) {
            doC();
        }
    }
}
