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

package walkingkooka.build.chain;

import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.set.Sets;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link ChainBuilder} that creates using a {@link ChainFactory}.
 */
final class ChainFactoryChainBuilder<T> implements ChainBuilder<T> {

    /**
     * Creates a new {@link ChainFactoryChainBuilder}
     */
    static <T> ChainFactoryChainBuilder<T> with(final ChainType type, final ChainFactory<T> factory) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(factory, "factory");

        return new ChainFactoryChainBuilder<T>(type, factory);
    }

    /**
     * Private constructor use factory
     */
    private ChainFactoryChainBuilder(final ChainType type, final ChainFactory<T> factory) {
        super();
        this.type = type;
        this.factory = factory;
    }

    /**
     * Adds a new item ignoring it if it is a duplicate.
     */
    @Override
    public ChainBuilder<T> add(final T add) {
        Objects.requireNonNull(add, "add");

        final Set<T> all = this.all;

        for (; ; ) {
            if (add instanceof Chained) {
                final Chained<T> chained = Cast.to(add);
                if (this.type.equals(chained.chainType())) {

                    int additions = 0;
                    int duplicates = 0;
                    for (final T adding : chained.chained()) {
                        duplicates++;
                        if (false == all.contains(adding)) {
                            all.add(adding);
                            additions++;
                            duplicates--;
                        }
                    }
                    final int size = all.size();

                    // only replace if first was a Chain
                    if (size == duplicates) {
                        if (false == (this.first instanceof Chained)) {
                            this.first = add;
                        }
                        break;
                    }
                    // new addition contains all previous additions
                    if (size == (duplicates + additions)) {
                        this.first = add;
                        break;
                    }
                    // some new additions clear $first
                    if (additions > 0) {
                        this.first = null;
                    }
                    break;
                }
            }
            // new addition clear $first
            if (false == all.contains(add)) {
                all.add(add);
                this.first = null;
                break;
            }
            break;
        }

        return this;
    }

    /**
     * Calls the {@link ChainFactory} when not empty.
     */
    @Override
    public T build() throws BuilderException {
        final Set<T> all = this.all;
        final int size = all.size();
        T built = this.first;
        if (null == built) {
            switch (size) {
                case 0:
                    throw new BuilderException("cannot build when empty");
                case 1:
                    built = all.iterator().next();
                    break;
                default:
                    final Object[] array = new Object[size];
                    int i = 0;
                    for (final T item : all) {
                        array[i] = item;
                        i++;
                    }
                    built = this.factory.create(array);
                    break;
            }
        }
        return built;
    }

    /**
     * Holds all the unique added items.
     */
    final Set<T> all = Sets.ordered();

    /**
     * The {@link ChainFactory} that will create the chained.
     */
    final ChainFactory<T> factory;

    /**
     * Adds the individual items of any {@link Chained} with a matching {@link ChainType}.
     */
    final ChainType type;

    /**
     * A copy of the first item.
     */
    private T first;

    /**
     * Dumps all added items.
     */
    @Override
    public String toString() {
        return this.all.toString();
    }
}
