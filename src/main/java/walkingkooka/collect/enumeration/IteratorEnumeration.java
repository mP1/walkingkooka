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

package walkingkooka.collect.enumeration;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Enumeration} view of an {@link Iterator}.
 */
final class IteratorEnumeration<E> implements Enumeration<E> {

    static <E> IteratorEnumeration<E> with(final Iterator<E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        return new IteratorEnumeration<>(iterator);
    }

    /**
     * Private constructor use static factory
     */
    private IteratorEnumeration(final Iterator<E> iterator) {
        super();
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return this.iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return this.iterator.next();
    }

    /**
     * The wrapped adapted {@link Iterator}.
     */
    private final Iterator<E> iterator;

    @Override
    public String toString() {
        return this.iterator.toString();
    }
}
