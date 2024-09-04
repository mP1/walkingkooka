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

import java.util.Set;

/**
 * A {@link Set} that is immutable but also contains a few would be mutator methods that return a new instance if required.
 * Note the {@link java.util.Iterator} returned will also need to be made read-only.
 */
public interface ImmutableSet<E> extends Set<E> {

    // ImmutableSet....................................................................................................

    /**
     * Factory method that should return a new set if the given elements are different.
     * A default implementation of all other abstract methods is available by implementing {@link ImmutableSetDefaults}.
     * After that is done only this method needs to be implemented.
     */
    ImmutableSet<E> setElements(final Set<E> elements);

    /**
     * Useful setElements for classes that cannot easily create another instance with the new elements.
     */
    ImmutableSet<E> setElementsFailIfDifferent(final Set<E> elements);

    /**
     * Returns a new instance of this {@link ImmutableSet} with the element appended.
     */
    ImmutableSet<E> concat(final E element);

    /**
     * Returns an {@link ImmutableSet} without the given element.
     */
    ImmutableSet<E> delete(final E element);

    /**
     * Returns a new instance of this {@link ImmutableSet} after replacing.
     */
    ImmutableSet<E> replace(final E oldElement,
                            final E newElement);

    /**
     * Returns a mutable {@link Set} with the items in this set. Modifying the given set does not update the elements in this set.
     */
    Set<E> toSet();

    // Set read only...................................................................................................

    @Override
    default boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException();
    }
}
