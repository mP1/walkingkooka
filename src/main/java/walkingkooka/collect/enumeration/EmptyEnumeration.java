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

import walkingkooka.Cast;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An empty {@link Enumeration} that contains no elements.
 */
final class EmptyEnumeration<T> implements Enumeration<T> {

    /**
     * Singleton
     */
    private final static EmptyEnumeration<Object> INSTANCE = new EmptyEnumeration<Object>();

    /**
     * Type safe getter.
     */
    static <T> EmptyEnumeration<T> instance() {
        return Cast.to(EmptyEnumeration.INSTANCE);
    }

    /**
     * Private constructor use singleton
     */
    private EmptyEnumeration() {
        super();
    }

    @Override
    public boolean hasMoreElements() {
        return false;
    }

    @Override
    public T nextElement() {
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return "empty";
    }
}
