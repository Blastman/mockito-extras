/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;

/**
 * Tests the {@link MockSpotter} class.
 */
class MockSpotterTest {

    @InjectMocks
    private TestObject outerObject;

    @Mock
    private TestInterface1 innerObject1;

    @Mock
    private TestInterface2 innerObject2;

    private MockSpotter mockSpotter;

    @BeforeEach
    void setup() {
        mockSpotter = new MockSpotter();
        initMocks(this);
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
     * Performs a test to verify an interaction with two mock.
     */
    @Test
    void testTwoMocks() {
        outerObject.doSomethingWithOneAndTwo();;

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject1).doSomething();

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject2).doSomething();

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
