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

package walkingkooka.predicate.character;

import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that passes each and every char from a {@link CharSequence} to a {@link
 * CharPredicate}. If any char fails the match an exception is thrown.
 */
final class CharPredicateCharSequencePredicate<C extends CharSequence>
        implements Predicate<C>, HashCodeEqualsDefined, Serializable {

    /**
     * Creates a new {@link CharPredicateCharSequencePredicate}
     */
    static <C extends CharSequence> CharPredicateCharSequencePredicate<C> adapt(
            final CharPredicate predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new CharPredicateCharSequencePredicate<C>(predicate);
    }

    /**
     * Private constructor use static factory
     */
    private CharPredicateCharSequencePredicate(final CharPredicate predicate) {
        super();
        this.predicate = predicate;
    }

    // Predicate

    @Override
    public boolean test(final C value) {
        Objects.requireNonNull(value, "value");

        boolean match = false;

        final CharPredicate predicate = this.predicate;
        final int length = value.length();
        for (int i = 0; i < length; i++) {
            final char c = value.charAt(i);
            match = predicate.test(c);
            if (false == match) {
                break;
            }
        }

        return match;
    }

    /**
     * The wrapped {@link CharPredicate}.
     */
    private final CharPredicate predicate;

    // Object

    @Override
    public int hashCode() {
        return this.predicate.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof CharPredicateCharSequencePredicate)
                && this.equals((CharPredicateCharSequencePredicate<?>) other));
    }

    private boolean equals(final CharPredicateCharSequencePredicate<?> other) {
        return this.predicate.equals(other.predicate);
    }

    /**
     * Dumps the wrapped {@link CharPredicate#toString()}.
     */
    @Override
    public String toString() {
        return this.predicate.toString();
    }

    // Serializable

    private static final long serialVersionUID = -5279764474834822861L;
}
