/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras.junit4;

import org.junit.runner.notification.RunNotifier;
import org.mockito.junit.MockitoJUnitRunner;
import org.torson.mockito.extras.MockCollector;
import org.torson.mockito.extras.MockitoExtraInitializer;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class MockCollectorExtension extends MockitoJUnitRunner {
    public MockCollectorExtension(Class<?> klass) throws InvocationTargetException {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        Object testClass = notifier.context.getRequiredTestInstance();

        Optional<MockCollector> mockCollector = MockitoExtraInitializer.setupCollector(testClass);
        super.run(notifier);
        mockCollector.ifPresent(mc -> MockitoExtraInitializer.removeAnnotatedUnmonitoredMocks(testClass, mc));

        super.run(notifier);
    }

    //    @Override
//    public void beforeEach(final ExtensionContext context) {
//        Object testClass = context.getRequiredTestInstance();
//
//        Optional<MockCollector> mockCollector = MockitoExtraInitializer.setupCollector(testClass);
//        super.beforeEach(context);
//        mockCollector.ifPresent(mc -> MockitoExtraInitializer.removeAnnotatedUnmonitoredMocks(testClass, mc));
//    }
}
