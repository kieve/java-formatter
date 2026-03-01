package com.example;

public class Foo {
    // Body is doA() not return/throw, so early-exit exception does not apply
    void method(boolean x) {
        if (x) doA();
        doB();
    }
}
