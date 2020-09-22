/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.torson.mockito.extras.annoations.FindMocks;
import org.torson.mockito.extras.annoations.Unmonitored;

import java.util.Optional;

/**
 * This class is used to initialize fields annotation with Mockiti annoations and also additional
 * annotations used to know about created mocks (See {@link MockSpotter}.
 *
 * Example usage:
 * <pre>
 * <code>
 *
 *{@literal @}Mock
 * private MockClass1 someMock1;
 *
 *{@literal @}Mock
 * private MockClass2 someMock2;
 *
 *{@literal @}FindMocks
 * private MockSpotter mocks;
 *
 *{@literal @}InjectMocks
 * private SomeService someService;
 *
 *{@literal @}Before
 * public void setup(){
 *     MockitoExtrasAnnotations.initMocks(this);
 * }
 *
 *{@literal @}Test
 * public void testDoSomething(){
 *     someService.doSomething();
 *
 *     verify(someMock1).method1();
 *
 *     mocks.verifyNoMoreInteractions();  // Verify no other mock interactions happened
 * }
 *
 * </code>
 * </pre>
 */
public class MockitoExtrasAnnotations {

    /**
     * Initializes objects annotated with Mockito annotations and Mockito-extra annoations for given
     * testClass:
     * <ul>
     *  <li>{@literal @}{@link Mock}</li>
     *  <li>{@literal @}{@link Spy}</li>
     *  <li>{@literal @}{@link Captor}</li>
     *  <li>{@literal @}{@link InjectMocks}</li>
     *  <li>{@literal @}{@link Unmonitored}</li>
     *  <li>{@literal @}{@link FindMocks}</li>
     * </ul>
     *
     * See examples in javadoc for {@link MockitoExtrasAnnotations} class.
     */
    public static void initMocks(Object testClass) {
        Optional<MockSpotter> mockCollector = MockitoExtraInitializer.setupCollector(testClass);
        MockitoAnnotations.initMocks(testClass);
        mockCollector.ifPresent(mc -> MockitoExtraInitializer.removeAnnotatedUnmonitoredMocks(testClass, mc));
    }
}
