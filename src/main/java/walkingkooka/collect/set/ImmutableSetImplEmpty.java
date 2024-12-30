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

package walkingkooka.collect.set;

import walkingkooka.collect.iterator.Iterators;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A {@link Set} known to be immutable without any elements.
 */
final class ImmutableSetImplEmpty<E> extends ImmutableSetImpl<E> {

    /**
     * Returns a {@link Set} which is immutable including copying elements if necessary.
     */
    static <E> ImmutableSetImplEmpty<E> empty() {
        return (ImmutableSetImplEmpty<E>)INSTANCE;
    }

    private final static ImmutableSetImplEmpty<Object> INSTANCE = new ImmutableSetImplEmpty<>();

    /**
     * Private ctor use factory
     */
    private ImmutableSetImplEmpty() {
        super();
    }

    @Override
    public boolean contains(final Object other) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return Iterators.empty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return "[]";
    }

    // ImmutableSet.....................................................................................................

    @Override
    public Set<E> toSet() {
        return new HashSet<>();
    }
}
