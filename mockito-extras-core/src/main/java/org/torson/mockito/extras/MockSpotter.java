/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

import org.mockito.Mockito;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * A utility class which automatically knows what mock objects you created and can be used to verify
 * no more interactions.  Example usage:
 * <pre>
 * <code>
 *
 * {@literal @}Mock
 * private MockClass1 someMock1;
 *
 * {@literal @}Mock
 * private MockClass2 someMock2;
 *
 * private MockCollector mocks;
 *
 * {@literal @}InjectMocks
 * private SomeService someService;
 *
 * {@literal @}Before
 * public void setup(){
 *     MockitoExtrasAnnotations.initMocks(this);
 * }
 *
 * {@literal @}Test
 * public void testDoSomething(){
 *     someService.doSomething();
 *     verify(someMock1).method1();
 *     mocks.verifyNoMoreInteractions();  // Verify no other mock interactions happened
 * }
 *
 * </code>
 * </pre>
 */
public class MockSpotter {

    private final CreatedMocksListener createdMocksListener;

    public MockSpotter() {
        MockingProgress progress = ThreadSafeMockingProgress.mockingProgress();

        createdMocksListener = CreatedMocksListener.getCachedInstance();
        createdMocksListener.clearRegisteredMocks();

        // Don't know if this is the first test or not. Always remove it and add it (trying to add same listener twice will throw exception).
        progress.removeListener(createdMocksListener);
        progress.addListener(createdMocksListener);
    }

    /**
     * Returns all the mocks that are known.
     */
    public Object[] getMocks() {
        return createdMocksListener.getMocks().toArray();
    }

    /**
     * Verifies that all mocks and spies created have no unverified interactions.
     *
     * @see Mockito#verifyNoMoreInteractions(Object...)
     */
    public void verifyNoMoreInteractions() {
        if (!createdMocksListener.getMocks().isEmpty()) {
            Mockito.verifyNoMoreInteractions(getMocks());
        }
    }

    /**
     * Removes a mock object from the tracked collection.  Useful for when you spy an object and it
     * calls too many internal methods.
     *
     * @param object
     *         The mock to no longer track.
     * @return {@code true} if this list contained the specified element
     */
    public boolean removeMock(Object object) {
        return createdMocksListener.getMocks().remove(object);
    }

}
