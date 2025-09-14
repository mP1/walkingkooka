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

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * An immutable {@link SortedSet}.
 */
public interface ImmutableSortedSet<E> extends ImmutableSet<E>, SortedSet<E> {

    /**
     * Returns a new instance of this {@link ImmutableSet} with the element appended.
     */
    @Override
    ImmutableSortedSet<E> concat(final E element);

    /**
     * Returns a new instance of this {@link ImmutableSet} with the elements appended.
     */
    @Override
    ImmutableSortedSet<E> concatAll(final Collection<E> elements);

    /**
     * Returns an {@link ImmutableSet} without the given element.
     */
    @Override
    ImmutableSortedSet<E> delete(final E element);

    /**
     * Returns an {@link ImmutableSet} without the given elements.
     */
    @Override
    ImmutableSortedSet<E> deleteAll(final Collection<E> elements);

    /**
     * Returns a new instance of this {@link ImmutableSortedSet} after removing all elements matched by the {@link Predicate}.
     */
    @Override
    ImmutableSortedSet<E> deleteIf(final Predicate<? super E> predicate);

    /**
     * Returns a new instance of this {@link ImmutableSet} after replacing.<br>
     * If the oldElement does not exist the original {@link ImmutableSet} will be returned.
     */
    @Override
    ImmutableSortedSet<E> replace(final E oldElement,
                                  final E newElement);

    /**
     * Calls {@link #setElements(SortedSet)} creating a new {@link SortedSet} with the elements if necessary.
     */
    @Override
    default ImmutableSet<E> setElements(final Collection<E> elements) {
        SortedSet<E> sortedSet;

        if(null == elements || elements instanceof SortedSet) {
            sortedSet = (SortedSet<E>) elements;
        } else {
            sortedSet = SortedSets.tree();
            sortedSet.addAll(elements);
        }

        return this.setElements(sortedSet);
    }

    @Override
    default ImmutableSet<E> setElementsFailIfDifferent(final Collection<E> elements) {
        return this.setElementsFailIfDifferent(
            (SortedSet<E>) elements
        );
    }

    /**
     * Returns a mutable {@link SortedSet} with the items in this set. Modifying the given set does not update the elements in this set.
     */
    @Override
    SortedSet<E> toSet();

    // Collector........................................................................................................

    /**
     * A collector that returns an {@link ImmutableSortedSet}.
     */
    static <EE> Collector<EE, ?, ImmutableSortedSet<EE>> collector(final Comparator<? super EE> comparator) {
        return Collectors.collectingAndThen(
            Collectors.toCollection(() -> SortedSets.tree(comparator)),
            SortedSets::immutable
        );
    }
}
