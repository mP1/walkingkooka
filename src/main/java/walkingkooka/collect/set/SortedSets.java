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

import walkingkooka.reflect.PublicStaticHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A collection of factory methods to get various {@link SortedSet} and {@link ImmutableSortedSet}.
 */
final public class SortedSets implements PublicStaticHelper {

    /**
     * An empty {@link SortedSet}
     */
    public static <E> SortedSet<E> empty() {
        return (SortedSet<E>)EMPTY;
    }

    private final static SortedSet<?> EMPTY = ImmutableSortedSetImpl.with(new TreeSet<>());

    /**
     * Returns a {@link SortedSet} that is immutable, making a defensive copy if necessary.
     */
    public static <E> ImmutableSortedSet<E> immutable(final SortedSet<E> sortedSet) {
        return ImmutableSortedSetImpl.with(sortedSet);
    }

    /**
     * A {@link SortedSet} with a single item.
     */
    public static <E> SortedSet<E> of(final E item) {
        Objects.requireNonNull(item, "item");

        final SortedSet<E> sortedSet = new TreeSet<>();
        sortedSet.add(item);

        return ImmutableSortedSetImpl.with(sortedSet);
    }

    /**
     * Convenience method that creates a SortedSet from an array of elements. This method only exists of a
     * convenience because Lists.of() does not have a {@link SortedSet} equivalent.
     */
    @SafeVarargs
    public static <E> SortedSet<E> of(final E... elements) {
        Objects.requireNonNull(elements, "elements");

        final SortedSet<E> sortedSet = new TreeSet<>();
        Collections.addAll(sortedSet, elements);

        return ImmutableSortedSetImpl.with(sortedSet);
    }

    /**
     * {@see TreeSet}
     */
    public static <E> SortedSet<E> tree() {
        return new TreeSet<>();
    }

    /**
     * {@see TreeSet}
     */
    public static <E> SortedSet<E> tree(final Comparator<? super E> comparator) {
        return new TreeSet<>(comparator);
    }

    /**
     * Stop creation
     */
    private SortedSets() {
        throw new UnsupportedOperationException();
    }
}
