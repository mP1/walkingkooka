/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.collect.list;

import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;
import java.util.List;

/**
 * A {@link List} known to be immutable and has no elements.
 */
final class ImmutableListImplEmpty<T> extends ImmutableListImpl<T> {

    /**
     * Singleton instance.
     */
    private final static ImmutableListImplEmpty<?> SINGLETON = new ImmutableListImplEmpty<>();

    /**
     * Returns a {@link List} which is immutable.
     */
    static <T> ImmutableListImplEmpty<T> empty() {
        return Cast.to(SINGLETON);
    }

    /**
     * Private ctor use factory
     */
    private ImmutableListImplEmpty() {
        super();
    }

    @Override
    public boolean contains(final Object other) {
        return false; // always false its empty.
    }

    @Override
    public T get(final int index) {
        throw new IndexOutOfBoundsException("Index " + index + " out of bounds size=1");
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.empty();
    }

    @Override
    public boolean remove(final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "[]";
    }
}
