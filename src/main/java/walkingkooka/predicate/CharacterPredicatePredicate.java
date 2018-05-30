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

package walkingkooka.predicate;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} adaptor that unwraps the {@link Character} and passes that char to a {@link
 * CharPredicate}.
 */
final class CharacterPredicatePredicate
        implements Predicate<Character>, HashCodeEqualsDefined, Serializable {

    /**
     * Creates a new {@link CharacterPredicatePredicate}
     */
    static CharacterPredicatePredicate adapt(final CharPredicate predicate) {
        Objects.requireNonNull(predicate, "predicate");
        return new CharacterPredicatePredicate(predicate);
    }

    /**
     * Private constructor use static factory
     */
    private CharacterPredicatePredicate(final CharPredicate predicate) {
        super();
        this.predicate = predicate;
    }

    // Predicate

    @Override
    public boolean test(final Character value) {
        Objects.requireNonNull(value, "value");
        return this.predicate.test(value);
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
        return (this == other) || ((other instanceof CharacterPredicatePredicate)
                && this.equals((CharacterPredicatePredicate) other));
    }

    private boolean equals(final CharacterPredicatePredicate other) {
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

    private static final long serialVersionUID = 2265435120794290495L;
}
