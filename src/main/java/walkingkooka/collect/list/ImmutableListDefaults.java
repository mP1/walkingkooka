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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An interface which implements nearly all the {@link ImmutableList}. References in code are no intended to reference
 * this type but always {@link ImmutableList} making for as simple as possible type references.
 * <br>
 * This is ideal where a concrete List reference is desired to support additional methods and chaining.
 */
public interface ImmutableListDefaults<T extends ImmutableList<E>, E> extends ImmutableList<E> {

    /**
     * Would be setter, that creates a new instance if the new elements are different otherwise returns true.
     */
    T setElements(final List<E> elements);

    /**
     * Useful setElements for classes that cannot easily create another instance with the new elements.
     */
    @Override
    default T setElementsFailIfDifferent(final List<E> elements) {
        Objects.requireNonNull(elements, "elements");

        if (false == this.equals(elements)) {
            Objects.requireNonNull(elements, "elements");
            throw new UnsupportedOperationException();
        }
        return (T)this;
    }

    /**
     * Swaps the two elements at the given index.
     */
    @Override
    default T swap(final int left,
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

        return (T)swapped;
    }

    /**
     * Returns a new instance of this {@link ImmutableList} with the element appended.
     */
    @Override
    default T concat(final E element) {
        final List<E> list = this.toList();
        list.add(element);

        return this.setElements(list);
    }

    /**
     * Returns a new instance of this {@link ImmutableList} with the elements appended.
     */
    default T concatAll(final Collection<E> elements) {
        Objects.requireNonNull(elements, "elements");

        final List<E> list = this.toList();
        list.addAll(elements);

        return this.setElements(list);
    }

    /**
     * Returns a new instance of this {@link ImmutableList} after replacing the element at the given index with the new.
     */
    @Override
    default T replace(final int index,
                      final E element) {
        final List<E> list = this.toList();
        final E replaced = list.set(
                index,
                element
        );

        return Objects.equals(
                replaced,
                element
        ) ? (T)this :
                this.setElements(list);
    }

    /**
     * Returns an {@link ImmutableList} without the element at index.
     */
    @Override
    default T deleteAtIndex(final int index) {
        final List<E> list = this.toList();
        list.remove(index);
        return this.setElements(list);
    }

    /**
     * Returns an {@link ImmutableList} without the given element.
     */
    @Override
    default T delete(final E element) {
        final List<E> list = this.toList();
        final boolean removed = list.remove(element);
        return removed ?
                this.setElements(list) :
                (T)this;
    }

    /**
     * Returns a new instance of this {@link ImmutableList} after removing all elements matched by the {@link Predicate}.
     */
    default T deleteIf(final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        final List<E> list = this.toList();
        list.removeIf(predicate);

        return this.setElements(list);
    }

    /**
     * Returns a new instance of this {@link ImmutableList} with the elements removed.
     */
    default T deleteAll(final Collection<E> elements) {
        Objects.requireNonNull(elements, "elements");

        final List<E> list = this.toList();
        list.removeAll(elements);

        return this.setElements(list);
    }

    /**
     * Returns a mutable {@link List} with the items in this list. Modifying the given list does not update the elements in this list.
     */
    @Override
    default List<E> toList() {
        return new ArrayList<>(this);
    }
}
