/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.collect.iterator;

import walkingkooka.Cast;
import walkingkooka.build.chain.ChainFactory;

import java.util.Iterator;
import java.util.Objects;

/**
 * A {@link ChainFactory} that creates an {@link IteratorChain}.
 */
final class IteratorChainFactory<T> implements ChainFactory<Iterator<T>> {

    /**
     * Type safe getter.
     */
    static <T> IteratorChainFactory<T> instance() {
        return Cast.to(IteratorChainFactory.INSTANCE);
    }

    /**
     * Singleton
     */
    private final static IteratorChainFactory<Object> INSTANCE = new IteratorChainFactory<Object>();

    /**
     * Private constructor use singleton.
     */
    private IteratorChainFactory() {
        super();
    }

    @Override
    public Iterator<T> create(final Object[] chained) {
        Objects.requireNonNull(chained, "chained");

        final int count = chained.length;
        final Iterator<T>[] iterators = Cast.to(new Iterator[count]);
        for (int i = 0; i < count; i++) {
            iterators[i] = Cast.to(chained[i]);
        }
        return IteratorChain.wrap(iterators);
    }

    @Override
    public String toString() {
        return "IteratorChain";
    }
}
