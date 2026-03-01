package com.example;

public class Foo {
    Runnable r = () -> {
        if (true) return;
        doA();
    };
}
