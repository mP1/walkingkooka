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

package walkingkooka.collect.iterable;

import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * An {@link Iterable} that applies a mapping {@link Function} to the values returned by the wrapped {@link Iterable}.
 */
final class IterableMapping<S, D> implements Iterable<D> {

    static <S, D> IterableMapping<S, D> with(final Iterable<S> iterable,
                                             final Function<S, D> mapper) {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(mapper, "mapper");

        return new IterableMapping<>(
            iterable,
            mapper
        );
    }

    private IterableMapping(final Iterable<S> iterable,
                            final Function<S, D> mapper) {
        super();

        this.iterable = iterable;
        this.mapper = mapper;
    }

    // Iterable.........................................................................................................

    @Override
    public Iterator<D> iterator() {
        return Iterators.mapping(
            this.iterable.iterator(),
            this.mapper
        );
    }

    private final Iterable<S> iterable;

    /**
     * Mapping function to each returned value.
     */
    private final Function<S, D> mapper;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.iterable + " " + this.mapper;
    }
}
