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
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * An interface which implements nearly all the {@link ImmutableSet}. References in code are no intended to reference
 * this type but always {@link ImmutableSet} making for as simple as possible type references.
 * <br>
 * This is ideal where a concrete Set reference is desired to support additional methods and chaining.
 */
public interface ImmutableSetDefaults<S extends ImmutableSet<E>, E> extends ImmutableSet<E> {

    /**
     * Would be setter, that creates a new instance if the new elements are different otherwise returns true.
     */
    S setElements(final Set<E> elements);

    /**
     * Useful setElements for classes that cannot easily create another instance with the new elements.
     */
    @Override
    default S setElementsFailIfDifferent(final Set<E> elements) {
        Objects.requireNonNull(elements, "elements");

        if (false == this.equals(elements)) {
            throw new UnsupportedOperationException();
        }
        return (S) this;
    }

    /**
     * Returns a new instance of this {@link ImmutableSet} with the element added.
     */
    @Override
    default S concat(final E element) {
        final Set<E> set = this.toSet();
        final boolean added = set.add(element);

        return added ?
            this.setElements(set) :
            (S) this;
    }

    /**
     * Returns a new instance of this {@link ImmutableSet} with the elements added.
     */
    @Override
    default S concatAll(final Collection<E> elements) {
        Objects.requireNonNull(elements, "elements");

        final Set<E> set = this.toSet();
        final boolean added = set.addAll(elements);

        return added ?
            this.setElements(set) :
            (S) this;
    }

    /**
     * Returns a new instance of this {@link ImmutableSet} with the element replaced.
     */
    @Override
    default S replace(final E oldElement,
                      final E newElement) {
        S result = (S) this;

        if (false == Objects.equals(oldElement, newElement)) {
            final Set<E> set = this.toSet();
            if (set.remove(oldElement)) {
                set.add(newElement);

                result = this.setElements(set);
            }
        }

        return result;
    }

    /**
     * Returns an {@link ImmutableSet} without the element
     */
    @Override
    default S delete(final E element) {
        final Set<E> set = this.toSet();
        final boolean removed = set.remove(element);
        return removed ?
            this.setElements(set) :
            (S) this;
    }

    /**
     * Returns a new instance of this {@link ImmutableSet} with the elements removed.
     */
    @Override
    default S deleteAll(final Collection<E> elements) {
        Objects.requireNonNull(elements, "elements");

        final Set<E> set = this.toSet();
        final boolean removed = set.removeAll(elements);

        return removed ?
            this.setElements(set) :
            (S) this;
    }

    /**
     * Returns a new instance of this {@link ImmutableSet} after removing all elements matched by the {@link Predicate}.
     */
    @Override
    default S deleteIf(final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        final Set<E> set = this.toSet();
        final boolean removed = set.removeIf(predicate);

        return removed ?
            this.setElements(set) :
            (S) this;
    }
}
