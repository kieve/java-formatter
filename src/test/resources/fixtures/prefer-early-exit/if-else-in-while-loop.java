package com.example;

public class Foo {
    void method(boolean x) {
        while (x) {
            if (x) {
                doA();
                doB();
                doC();
            } else {
                doD();
            }
        }
    }
}
