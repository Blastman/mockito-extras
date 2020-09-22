/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.torson.mockito.extras.annoations.FindMocks;
import org.torson.mockito.extras.annoations.Unmonitored;

import java.lang.reflect.Field;
import java.util.Optional;

public class MockitoExtraInitializer {

    public static Optional<MockSpotter> setupCollector(Object testClass){
        if (testClass == null) {
            throw new MockitoException("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class");
        }

        MockSpotter mockSpotter = null;
        Field[] fields = testClass.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(FindMocks.class)) {
                    if (MockSpotter.class == field.getType()) {
                        field.setAccessible(true);
                        Object existingCollector = field.get(testClass);
                        if (existingCollector == null) {
                            mockSpotter = new MockSpotter();
                            FieldSetter.setField(testClass, field, mockSpotter);
                        } else {
                            // User manually created collector. Continue on.
                            mockSpotter = (MockSpotter) existingCollector;
                        }
                        break;
                    } else {
                        throw new MockitoException(FindMocks.class.getSimpleName() + " annotation can only be used on " + MockSpotter.class.getSimpleName() + " objects.");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new MockitoException("Unable to access MockCollector object", e);
        }

        return Optional.ofNullable(mockSpotter);
    }

    public static void  removeAnnotatedUnmonitoredMocks(Object testClass, MockSpotter mockSpotter){
        if (mockSpotter != null) {
            try {
                for (Field field : testClass.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Unmonitored.class)) {
                        field.setAccessible(true);
                        mockSpotter.removeMock(field.get(testClass));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new MockitoException("Unable to access object marked with @" + Unmonitored.class.getSimpleName(), e);
            }
        }
    }
}
