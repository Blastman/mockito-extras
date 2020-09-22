/*
 * Copyright (c) 2020 Jeff Torson
 * This program is made available under the terms of the MIT License.
 */

package org.torson.mockito.extras;

import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link MockCreationListener} which is used to keep track of the created mocks. Note that since Mockito now uses a {@link
 * ThreadSafeMockingProgress} in a {@link ThreadLocal}, the registered mocks need to be cleared before each test.
 */
class CreatedMocksListener implements MockCreationListener {

    private static final ThreadLocal<CreatedMocksListener> CREATED_MOCKS_LISTENER_PROVIDER = ThreadLocal.withInitial(CreatedMocksListener::new);

    private final List<Object> mocks;

    private CreatedMocksListener() {
        this.mocks = new ArrayList<>();
    }

    /**
     * Returns the {@link CreatedMocksListener} for the current thread.  Note that for new tests, this will need to be cleared.
     *
     * @return A {@link CreatedMocksListener} for current thread.  Never will be null.
     */
    public static CreatedMocksListener getCachedInstance() {
        return CREATED_MOCKS_LISTENER_PROVIDER.get();
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }

    public List<Object> getMocks() {
        return mocks;
    }

    public void clearRegisteredMocks() {
        mocks.clear();
    }
}