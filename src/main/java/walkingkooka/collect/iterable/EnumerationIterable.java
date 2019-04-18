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

package walkingkooka.collect.iterable;

import walkingkooka.collect.iterator.Iterators;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Iterable} that returns an {@link Enumeration} once. It is intended to be used with for
 * each loops.
 */
final class EnumerationIterable<T> implements Iterable<T> {

    /**
     * Creates a new {@link EnumerationIterable}.
     */
    static <T> EnumerationIterable<T> with(final Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration, "enumeration");

        return new EnumerationIterable<T>(enumeration);
    }

    /**
     * Private constructor use factory.
     */
    private EnumerationIterable(final Enumeration<T> enumeration) {
        super();
        this.enumeration = enumeration;
    }

    @Override
    public Iterator<T> iterator() {
        if (this.given) {
            throw new IllegalStateException("Enumeration can only be taken once");
        }
        final Enumeration<T> enumeration = this.enumeration;
        this.given = true;
        return Iterators.enumeration(enumeration);
    }

    private final Enumeration<T> enumeration;

    private boolean given;

    /**
     * Dumps the {@link Enumeration#toString()}.
     */
    @Override
    public String toString() {
        return this.enumeration.toString();
    }
}
