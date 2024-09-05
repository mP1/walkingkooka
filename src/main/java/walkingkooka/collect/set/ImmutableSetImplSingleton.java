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
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Set} known to be immutable and holds a copy of any {@link Set} given to it.
 */
final class ImmutableSetImplSingleton<E> extends ImmutableSetImpl<E> {

    /**
     * Returns a {@link Set} which is immutable including copying elements if necessary.
     */
    static <E> ImmutableSetImplSingleton<E> singleton(final E element) {
        return new ImmutableSetImplSingleton<>(element);
    }

    /**
     * Private ctor use factory
     */
    private ImmutableSetImplSingleton(final E element) {
        super();
        this.element = element;
    }

    @Override
    public boolean contains(final Object other) {
        return Objects.equals(this.element, other);
    }

    @Override
    public Iterator<E> iterator() {
        return Iterators.one(this.element);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public String toString() {
        return "[" + this.element + "]";
    }

    private final E element;

    // ImmutableSet.....................................................................................................

    @Override
    public Set<E> toSet() {
        return new HashSet<>(this);
    }
}
