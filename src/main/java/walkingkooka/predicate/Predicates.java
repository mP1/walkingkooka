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

import walkingkooka.compare.ComparisonRelation;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.type.PublicStaticHelper;

import java.util.Objects;
import java.util.function.Predicate;

final public class Predicates implements PublicStaticHelper {
    /**
     * {@see AlwaysPredicate}.
     */
    public static <T> Predicate<T> always() {
        return AlwaysPredicate.instance();
    }

    /**
     * {@see CharacterPredicatePredicate}
     */
    public static Predicate<Character> charPredicate(final CharPredicate predicate) {
        return CharacterPredicatePredicate.adapt(predicate);
    }

    /**
     * {@see CharPredicateCharSequencePredicate}
     */
    public static Predicate<CharSequence> charSequence(final CharPredicate predicate) {
        return CharPredicateCharSequencePredicate.with(predicate);
    }

    /**
     * {@see ComparableComparisonRelationPredicate}
     */
    public static <C extends Comparable<C>> Predicate<C> comparisonRelation(final C left,
                                                                            final ComparisonRelation relation) {
        return ComparableComparisonRelationPredicate.with(left, relation);
    }

    /**
     * {@see CustomToStringPredicate}
     */
    public static <T> Predicate<T> customToString(final Predicate<T> predicate, final String toString) {
        return CustomToStringPredicate.wrap(predicate, toString);
    }

    /**
     * If a value is null or fails the {@link Predicate}, a {@link NullPointerException} or {@link IllegalArgumentException}
     * will be thrown.
     */
    public static <T> void failIfNullOrFalse(final T value, final Predicate<T> predicate, final String formatMessage) {
        if(null==value) {
            Objects.requireNonNull(value, String.format(formatMessage, value));
        }
        if(!predicate.test(value)){
            throw new IllegalArgumentException(String.format(formatMessage, value) + " must be " + predicate);
        }
    }

    /**
     * {@see FakePredicate}.
     */
    public static <T> Predicate<T> fake() {
        return FakePredicate.create();
    }

    /**
     * {@see InitialAndPartCharPredicateCharSequencePredicate}
     */
    public static Predicate<CharSequence> initialAndPart(final CharPredicate initial, final CharPredicate remaining) {
        return InitialAndPartCharPredicateCharSequencePredicate.with(initial, remaining);
    }

    /**
     * {@see ObjectEqualityMatcher}
     */
    public static <T> Predicate<T> is(final T value) {
        return ObjectEqualityPredicate.with(value);
    }

    /**
     * Matches java identifiers using {@link Character#isJavaIdentifierStart(char)} and
     * {@link Character#isJavaIdentifierPart(char)}.
     */
    public static Predicate<CharSequence> javaIdentifier() {
        return JAVA_IDENTIFIER;
    }

    private final static Predicate<CharSequence> JAVA_IDENTIFIER = Predicates.initialAndPart(
            (c) -> Character.isJavaIdentifierStart(c),
            (c) -> Character.isJavaIdentifierPart(c));

    /**
     * {@see NeverPredicate}.
     */
    public static <T> Predicate<T> never() {
        return NeverPredicate.instance();
    }

    /**
     * {@see NotPredicate}.
     */
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return NotPredicate.wrap(predicate);
    }

    /**
     * Stop creation
     */
    private Predicates() {
        throw new UnsupportedOperationException();
    }
}
