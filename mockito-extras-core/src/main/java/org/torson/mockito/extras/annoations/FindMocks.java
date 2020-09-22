/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras.annoations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.torson.mockito.extras.MockSpotter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Used to annotate a {@link MockSpotter} to tell the annotation processor to create the {@link
 * MockSpotter} before creating the mocks so that they can be tracked. Typical usage would be:
 * <pre>
 * <code>
 *
 *{@literal @}FindMocks
 * private MockSpotter mocks;
 *
 * </code>
 * </pre>
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface FindMocks {
}
