/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras.junit5;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.torson.mockito.extras.MockSpotter;
import org.torson.mockito.extras.TestInterface1;
import org.torson.mockito.extras.TestInterface2;
import org.torson.mockito.extras.TestInterface3;
import org.torson.mockito.extras.TestObject;
import org.torson.mockito.extras.annoations.FindMocks;
import org.torson.mockito.extras.annoations.Unmonitored;

@ExtendWith(MockFinderExtension.class)
public class MockSpotterExtensionTest {

    @InjectMocks
    private TestObject outerObject;

    @Mock
    private TestInterface1 innerObject1;

    @Mock
    private TestInterface2 innerObject2;

    @Unmonitored
    @Mock
    private TestInterface3 innerObject3;

    @FindMocks
    private MockSpotter mockSpotter;

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
     * Performs a test to verify an interaction with two mocks.
     */
    @Test
    void testTwoMocks() {
        outerObject.doSomethingWithAll();;

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject1).doSomething();

        assertThrows(NoInteractionsWanted.class, () -> mockSpotter.verifyNoMoreInteractions());
        verify(innerObject2).doSomething();

        mockSpotter.verifyNoMoreInteractions();
    }

}
