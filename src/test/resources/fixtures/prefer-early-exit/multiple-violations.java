package com.example;

public class Foo {
    void method1(boolean x) {
        if (x) {
            doA();
            doB();
            doC();
        } else {
            doD();
        }
    }

    void method2(boolean x) {
        if (x) {
            doE();
            doF();
            doG();
        } else {
            doH();
        }
    }
}
