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

package walkingkooka.predicate.character;

import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Accepts char and returns true if they match some condition.
 */
public interface CharPredicate {
    /**
     * Returns true if the given character is matched.
     */
    boolean test(char c);

    default CharPredicate and(final CharPredicate other) {
        return CharPredicates.and(this, other);
    }

    default CharPredicate andNot(final CharPredicate other) {
        return CharPredicates.andNot(this, other);
    }

    default CharPredicate negate() {
        return CharPredicates.not(this);
    }

    default CharPredicate or(final CharPredicate other) {
        return CharPredicates.or(this, other);
    }

    default Predicate<Character> asPredicate() {
        return Predicates.charPredicate(this);
    }

    default CharPredicate setToString(final String toString) {
        return CharPredicates.toString(this, toString);
    }

    // failXXX..........................................................................................................

    /**
     * Fails if the chars are null or any characters fail the {@link CharPredicate} test.
     * It is assumed the {@link CharPredicate} have a meaningful toString as it is included in any exception messages.
     */
    default <T extends CharSequence> T failIfNullOrFalse(final String label,
                                                         final T chars) {
        Objects.requireNonNull(chars, label);
        CharSequences.failIfNullOrEmpty(label, "label");

        this.checkCharacters(chars);

        return chars;
    }

    /**
     * Fails if the chars are null or empty or any characters fail the {@link CharPredicate} test.
     * It is assumed the {@link CharPredicate} have a meaningful toString as it is included in any exception messages.
     */
    default <T extends CharSequence> T failIfNullOrEmptyOrFalse(final String label,
                                                                final T chars) {
        CharSequences.failIfNullOrEmpty(chars, label);
        CharSequences.failIfNullOrEmpty(label, "label");

        this.checkCharacters(chars);

        return chars;
    }

    /**
     * Checks that all characters pass the {@link CharPredicate} test.
     */
    private void checkCharacters(final CharSequence chars) {
        final int length = chars.length();

        for (int i = 0; i < length; i++) {
            final char c = chars.charAt(i);
            if (false == this.test(c)) {
                throw new InvalidCharacterException(chars.toString(), i);
            }
        }
    }
}
