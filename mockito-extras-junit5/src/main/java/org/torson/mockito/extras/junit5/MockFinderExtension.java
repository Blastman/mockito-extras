/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;
import org.torson.mockito.extras.MockSpotter;
import org.torson.mockito.extras.MockitoExtraInitializer;

import java.util.Optional;

public class MockFinderExtension extends MockitoExtension {

    @Override
    public void beforeEach(final ExtensionContext context) {
        Object testClass = context.getRequiredTestInstance();

        Optional<MockSpotter> mockCollector = MockitoExtraInitializer.setupCollector(testClass);
        super.beforeEach(context);
        mockCollector.ifPresent(mc -> MockitoExtraInitializer.removeAnnotatedUnmonitoredMocks(testClass, mc));
    }
}
