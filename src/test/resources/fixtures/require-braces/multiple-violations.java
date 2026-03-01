package com.example;

public class Foo {
    void method(boolean x, boolean y) {
        if (x)
            doA();
        while (y)
            doB();
        for (int i = 0; i < 10; i++)
            doC();
    }
}
