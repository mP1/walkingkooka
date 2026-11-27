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

import walkingkooka.CanBeEmpty;
import walkingkooka.collect.CanFirstOrEmpty;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A {@link List} that is immutable but also contains a few would be mutator methods that return a new instance if required.
 * The {@link Iterator} and sub-lists will need to be made read only as necessary.
 */
public interface ImmutableList<E> extends List<E>,
    CanBeEmpty,
    CanFirstOrEmpty<E> {

    // CanFirstOrEmpty..................................................................................................

    @Override
    default Optional<E> firstOrEmpty() {
        return Optional.ofNullable(
            this.isEmpty() ?
                null :
                this.get(0)
        );
    }

    // ImmutableList....................................................................................................

    /**
     * Factory method that should return a new list if the given elements are different.
     * A default implementation of all other abstract methods is available by implementing {@link ImmutableListDefaults}.
     * After that is done only this method needs to be implemented.
     */
    ImmutableList<E> setElements(final Collection<E> elements);

    /**
     * Useful setElements for classes that cannot easily create another instance with the new elements.
     */
    ImmutableList<E> setElementsFailIfDifferent(final Collection<E> elements);

    /**
     * Swaps the two elements at the given index.
     */
    ImmutableList<E> swap(final int left,
                          final int right);

    /**
     * Returns a new instance of this {@link ImmutableList} with the element appended.
     */
    ImmutableList<E> concat(final E element);

    /**
     * Returns a new instance of this {@link ImmutableList} with the elements appended.
     */
    ImmutableList<E> concatAll(final Collection<E> elements);

    /**
     * Returns a new instance of this {@link ImmutableList} after replacing the element at the given index with the new.
     */
    ImmutableList<E> replace(final int index,
                             final E element);

    /**
     * Returns an {@link ImmutableList} without the element at index.
     */
    ImmutableList<E> deleteAtIndex(final int index);

    /**
     * Returns an {@link ImmutableList} without the given element.
     */
    ImmutableList<E> delete(final E element);

    /**
     * Returns a new instance of this {@link ImmutableList} with the elements removed.
     */
    ImmutableList<E> deleteAll(final Collection<E> elements);

    /**
     * Returns a new instance of this {@link ImmutableList} after removing all elements matched by the {@link Predicate}.
     */
    ImmutableList<E> deleteIf(final Predicate<? super E> predicate);

    /**
     * Returns a {@link ImmutableList} with the element order reversed.
     */
    default ImmutableList<E> reverse() {
        final List<E> reversed = this.toList();
        Collections.reverse(reversed);
        return this.setElements(reversed);
    }

    /**
     * Helper that may be used by any {@link #setElements(Collection)}, which tests if the elements is the same type,
     * returning that, as well as making a defensive copy and calling a factory if different.
     */
    default ImmutableList<E> setElementsMaybeCreate(final Collection<E> elements,
                                                    final Predicate<Collection<E>> isSame,
                                                    final Consumer<E> elementChecker,
                                                    final Function<List<E>, ImmutableList<E>> factory) {
        final ImmutableList<E> immutableList;

        if (isSame.test(elements)) {
            immutableList = (ImmutableList<E>) elements;
        } else {
            Objects.requireNonNull(elements, "elements");

            final List<E> copy = Lists.array();
            for (final E each : elements) {
                elementChecker.accept(each);
                copy.add(each);
            }

            immutableList = this.equals(copy) ?
                this :
                factory.apply(copy);
        }

        return immutableList;
    }

    /**
     * Returns a sub list view of this {@link ImmutableList}.
     */
    ImmutableList<E> subList(final int from,
                             final int to);

    /**
     * Validates parameters and returns this if the from and to match this list.
     */
    default ImmutableList<E> subListMaybeSame(final int from,
                                              final int to) {
        if (from < 0) {
            throw new IndexOutOfBoundsException("Invalid from index " + from + " < 0");
        }

        if (to < from) {
            throw new IndexOutOfBoundsException("Invalid to index " + to + " < " + from);
        }

        final int size = this.size();
        if (to > size) {
            throw new IndexOutOfBoundsException("Invalid to index " + to + " > " + size);
        }

        if (from == 0 || to == size) {
            return this;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * Returns a mutable {@link List} with the items in this list. Modifying the given list does not update the elements in this list.
     */
    List<E> toList();

    // List read only...................................................................................................

    @Override
    default E set(final int index,
                  final E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void add(final int index,
                     final E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    default E remove(int index) {
        throw new UnsupportedOperationException();
    }

    // Collector........................................................................................................

    /**
     * A collector that returns an {@link ImmutableList}.
     */
    static <EE> Collector<EE, ?, ImmutableList<EE>> collector() {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            Lists::immutable
        );
    }
}
