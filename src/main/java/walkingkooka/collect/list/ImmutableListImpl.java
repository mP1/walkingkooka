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

import walkingkooka.Cast;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all immutable {@link List} returned by {@link Lists}. Note unlike the immutable lists returned by List,
 * null elements are allowed.
 */
abstract class ImmutableListImpl<T> extends AbstractList<T> implements ImmutableListDefaults<ImmutableList<T>, T> {

    /**
     * Returns a {@link ImmutableList} which is immutable including copying elements if necessary.
     */
    static <T> ImmutableList<T> with(final Collection<T> collection) {
        Objects.requireNonNull(collection, "collection");

        return collection instanceof ImmutableList ?
            Cast.to(collection) :
            prepare(collection.toArray());
    }

    /**
     * Takes a defensive copy of the list elements and returns a immutable list.
     */
    static <T> ImmutableListImpl<T> prepare(final Object[] elements) {
        final ImmutableListImpl<T> immutable;

        switch (elements.length) {
            case 0:
                immutable = empty();
                break;
            case 1:
                immutable = singleton(
                    Cast.to(elements[0])
                );
                break;
            default:
                immutable = nonSingleton(elements);
                break;
        }

        return immutable;
    }


    /**
     * {@see ImmutableListImplEmpty}.
     */
    static <T> ImmutableListImpl<T> empty() {
        return ImmutableListImplEmpty.empty();
    }

    /**
     * {@see ImmutableListSingleton}.
     */
    static <T> ImmutableListImpl<T> singleton(final T element) {
        return ImmutableListImplSingleton.withElement(element);
    }

    /**
     * Creates a {@link ImmutableListImplNonSingleton} with the given {@link List} which is not defensively copied.
     */
    private static <T> ImmutableListImpl<T> nonSingleton(final Object[] wrap) {
        return ImmutableListImplNonSingleton.with(wrap);
    }

    @Override
    public final void elementCheck(final T element) {
        // nulls are allowed.
    }

    @Override
    public final ImmutableList<T> setElements(final Collection<T> elements) {
        Objects.requireNonNull(elements, "elements");

        final ImmutableListImpl<T> copy = prepare(elements.toArray());
        return this.equals(copy) ?
            this :
            copy;
    }
}
