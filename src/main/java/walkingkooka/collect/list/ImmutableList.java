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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link List} that is immutable but also contains a few would be mutator methods that return a new instance if required.
 * The {@link Iterator} and sub-lists will need to be made read only as necessary.
 */
public interface ImmutableList<E> extends List<E>, CanBeEmpty {

    // ImmutableList....................................................................................................

    /**
     * Factory method that should return a new list if the given elements are different.
     * A default implementation of all other abstract methods is available by implementing {@link ImmutableListDefaults}.
     * After that is done only this method needs to be implemented.
     */
    ImmutableList<E> setElements(final List<E> elements);

    /**
     * Useful setElements for classes that cannot easily create another instance with the new elements.
     */
    ImmutableList<E> setElementsFailIfDifferent(final List<E> elements);

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
}
