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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A {@link List} that is immutable but also contains a few would be mutator methods that return a new instance if required.
 * The {@link Iterator} and sub-lists will need to be made read only as necessary.
 */
public interface ImmutableList<E> extends List<E> {

    // ImmutableList....................................................................................................

    /**
     * Factory method that should return a new list if the given elements are different.
     */
    ImmutableList<E> setElements(final List<E> elements);


    /**
     * Swaps the two elements at the given index.
     */
    default ImmutableList<E> swap(final int left,
                                  final int right) {
        ImmutableList<E> swapped = this;

        if (left != right) {
            final E leftElement = this.get(left);
            final E rightElement = this.get(right);
            if (false == Objects.equals(leftElement, rightElement)) {
                // different need to return a new ImmutableList
                final List<E> list = this.toList();
                list.set(right, leftElement);
                list.set(left, rightElement);

                swapped = this.setElements(list);
            }
        }

        return swapped;

    }

    /**
     * Returns a new instance of this {@link ImmutableList} with the element appended.
     */
    default ImmutableList<E> appendAndNew(final E element) {
        final List<E> list = this.toList();
        list.add(element);

        return this.setElements(list);
    }

    /**
     * Returns a new instance of this {@link ImmutableList} with the element appended.
     */
    default ImmutableList<E> replace(final int index,
                                     final E element) {
        final List<E> list = this.toList();
        final E replaced = list.set(
                index,
                element
        );

        return Objects.equals(
                replaced,
                element
        ) ? this :
                this.setElements(list);
    }

    /**
     * Returns an {@link ImmutableList} without the element at index.
     */
    default ImmutableList<E> removeAndNew(final int index) {
        final List<E> list = this.toList();
        list.remove(index);
        return this.setElements(list);
    }

    /**
     * Returns an {@link ImmutableList} without the element at index.
     */
    default ImmutableList<E> removeElementAndNew(final E element) {
        final List<E> list = this.toList();
        final boolean removed = list.remove(element);
        return removed ?
                this.setElements(list) :
                this;
    }

    /**
     * Returns a mutable {@link List} with the items in this list. Modifying the given list does not update the elements in this list.
     */
    default List<E> toList() {
        return new ArrayList<>(this);
    }

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
