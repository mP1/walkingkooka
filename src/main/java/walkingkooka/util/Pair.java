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

package walkingkooka.util;

import walkingkooka.Equality;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Objects;

/**
 * Simple immutable pair that holds two possibly null values.
 */
final public class Pair<T, U> implements HashCodeEqualsDefined, Serializable {

    private static final long serialVersionUID = 841156388447090608L;

    /**
     * Factory that creates a {@link Pair}. Note null values are ok.
     */
    public static <T, U> Pair<T, U> with(final T first, final U second) {
        return new Pair<T, U>(first, second);
    }

    /**
     * Private constructor use static factory.
     */
    private Pair(final T first, final U second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Getter that returns the first value
     */
    public T first() {
        return this.first;
    }

    private final T first;

    /**
     * Getter that returns the second value
     */
    public U second() {
        return this.second;
    }

    private final U second;

    // Object

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof Pair) && this.equals0((Pair<?, ?>) other));
    }

    private boolean equals0(final Pair<?, ?> other) {
        return Equality.safeEquals(this.first, other.first) && Equality.safeEquals(this.second,
                other.second);
    }

    /**
     * Dumps both values adding quotes if either is a {@link CharSequence}.
     */
    @Override
    public String toString() {
        return CharSequences.quoteIfChars(this.first) + " & "
                + CharSequences.quoteIfChars(this.second);
    }
}
