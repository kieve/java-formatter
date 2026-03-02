package com.example;

public class Foo {
    void methodIfElse(boolean x) {
        if (x) {
            doA();
        } else {
            doB();
        }
    }

    void methodWrapping(boolean x) {
        if (x) {
            doA();
            doB();
        }
    }
}
