/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */
package org.torson.mockito.extras.annoations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mockito.Mock;
import org.mockito.Spy;
import org.torson.mockito.extras.MockSpotter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to annotate a {@link Mock} or {@link Spy} an object stateing that its usage should not be
 * tracked by the {@link MockSpotter}. Typical usage would be:
 * <pre>
 * <code>
 *
 *{@literal @}Unmonitored
 *{@literal @}Spy
 * private RealObject realObject;
 *
 * </code>
 * </pre>
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface Unmonitored {
}
