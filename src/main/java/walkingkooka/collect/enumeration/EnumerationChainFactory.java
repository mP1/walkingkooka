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

package walkingkooka.collect.enumeration;

import walkingkooka.Cast;
import walkingkooka.build.chain.ChainFactory;

import java.util.Enumeration;
import java.util.Objects;

/**
 * A {@link ChainFactory} that creates an {@link EnumerationChain}.
 */
final class EnumerationChainFactory<T> implements ChainFactory<Enumeration<T>> {

    /**
     * Type safe getter.
     */
    static <T> EnumerationChainFactory<T> instance() {
        return Cast.to(EnumerationChainFactory.INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnumerationChainFactory<Object> INSTANCE
            = new EnumerationChainFactory<Object>();

    /**
     * Private constructor use singleton.
     */
    private EnumerationChainFactory() {
        super();
    }

    @Override
    public Enumeration<T> create(final Object[] chained) {
        Objects.requireNonNull(chained, "chained");

        final int count = chained.length;
        final Enumeration<T>[] enumerations = Cast.to(new Enumeration[count]);
        for (int i = 0; i < count; i++) {
            enumerations[i] = Cast.to(chained[i]);
        }
        return EnumerationChain.wrap(enumerations);
    }

    @Override
    public String toString() {
        return "EnumerationChain";
    }
}
