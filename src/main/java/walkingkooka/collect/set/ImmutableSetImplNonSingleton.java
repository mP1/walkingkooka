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

import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Set} presents a read only view of a defensively copied {@link Set} given to it.
 */
final class ImmutableSetImplNonSingleton<E> extends ImmutableSetImpl<E> {

    /**
     * Returns a {@link Set} which is immutable including copying elements if necessary.
     */
    static <T> ImmutableSetImplNonSingleton<T> nonSingleton(final Object[] notCopied) {
        return new ImmutableSetImplNonSingleton<>(notCopied);
    }

    private ImmutableSetImplNonSingleton(final Object[] elements) {
        super();
        this.elements = elements;
    }

    @Override
    public boolean contains(final Object other) {
        boolean contains = false;

        for (final Object element : this.elements) {
            contains = Objects.equals(element, other);
            if (contains) {
                break;
            }
        }

        return contains;
    }

    @Override
    public Iterator<E> iterator() {
        return Cast.to(
            Iterators.array(this.elements)
        );
    }

    @Override
    public int size() {
        return this.elements.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private final Object[] elements;

    @Override
    public String toString() {
        return Arrays.asList(this.elements)
            .toString();
    }

    // ImmutableSet.....................................................................................................

    @Override
    public Set<E> toSet() {
        return Cast.to(
            new HashSet<>(
                Arrays.asList(this.elements)
            )
        );
    }
}
