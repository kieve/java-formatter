package com.example;

public class Foo {
    Foo(boolean x) {
        if (x) {
            doA();
            doB();
            doC();
        } else {
            doD();
        }
    }
}
