/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.torson.mockito.extras.annoations.FindMocks;
import org.torson.mockito.extras.annoations.Unmonitored;

/**
 * Tests the {@link MockitoExtrasAnnotations} class.
 */
class MockitoExtraAnnotationsTest {

    @InjectMocks
    private TestObject outerObject;

    @Mock
    private TestInterface1 innerObject1;

    @Spy
    @Unmonitored
    private TestInterface2 innerObject2;

    @Mock
    private TestInterface3 innerObject3;

    @FindMocks
    private MockSpotter mockSpotter;

    @BeforeEach
    void setup() {
        innerObject2 = new TestInterface2Impl();

        MockitoExtrasAnnotations.initMocks(this);
    }

    /**
     * Performs a test to verify an interaction with one mock.
     */
    @Test
    void testOneMock() {
        outerObject.doSomethingObject1();

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());

        verify(innerObject1).doSomething();
        mockSpotter.verifyNoMoreInteractions();
    }

    /**
     * Performs a test to verify an interaction with two mocks but one is unmonitored.
     */
    @Test
    void testUsingUnmonitored() {
        outerObject.doSomethingWithOneAndTwo();

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject1).doSomething();

                mockSpotter.verifyNoMoreInteractions();
    }

    /**
     * Tests removing a mock from tracked objects.
     */
    @Test
    void testRemoveMock() {
        mockSpotter.removeMock(innerObject2);
        outerObject.doSomethingWithOneAndTwo();

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject1).doSomething();

        mockSpotter.verifyNoMoreInteractions();
    }

}
