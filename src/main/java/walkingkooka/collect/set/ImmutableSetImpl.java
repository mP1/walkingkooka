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

import java.util.AbstractSet;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Set} known to be immutable and holds a copy of any {@link Set} given to it.
 */
abstract class ImmutableSetImpl<E> extends AbstractSet<E> implements ImmutableSetDefaults<ImmutableSet<E>, E> {

    /**
     * Returns a {@link ImmutableSet} which is immutable including copying elements if necessary.
     */
    static <E> ImmutableSet<E> with(final Set<E> set) {
        Objects.requireNonNull(set, "set");

        return set instanceof ImmutableSet ?
            Cast.to(set) :
            prepare(set.toArray());
    }

    /**
     * Takes a defensive copy of the list elements and returns a immutable list.
     */
    static <E> ImmutableSetImpl<E> prepare(final Object[] elements) {
        final ImmutableSetImpl<E> immutable;

        switch (elements.length) {
            case 0:
                immutable = ImmutableSetImplEmpty.empty();
                break;
            case 1:
                immutable = ImmutableSetImplSingleton.singleton(
                    Cast.to(elements[0])
                );
                break;
            default:
                immutable = ImmutableSetImplNonSingleton.nonSingleton(elements);
                break;
        }

        return immutable;
    }

    /**
     * Package private to limit sub classing.
     */
    ImmutableSetImpl() {
        super();
    }

    // Set..............................................................................................................

    @Override
    abstract public boolean contains(final Object other);

    @Override
    abstract public String toString();

    // ImmutableSet.....................................................................................................

    @Override
    public final void elementCheck(final E element) {
        // allow nulls
    }

    @Override
    public final ImmutableSet<E> setElements(final Set<E> elements) {
        Objects.requireNonNull(elements, "elements");

        final ImmutableSetImpl<E> copy = prepare(elements.toArray());
        return this.equals(copy) ?
            this :
            copy;
    }
}
