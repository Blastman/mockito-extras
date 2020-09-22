/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

public class TestObject {
    private TestInterface1 innerObject1;
    private TestInterface2 innerObject2;
    private TestInterface3 innerObject3;

    public void doSomethingObject1() {
        innerObject1.doSomething();
    }

    public void doSomethingWithOneAndTwo() {
        innerObject1.doSomething();
        innerObject2.doSomething();
    }

    public void doSomethingWithAll() {
        innerObject1.doSomething();
        innerObject2.doSomething();
        innerObject3.doSomething();
    }

}
