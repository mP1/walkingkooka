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

package walkingkooka.predicate;

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.compare.CompareResult;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final public class Predicates implements PublicStaticHelper {

    /**
     * /**
     * {@see PredicateAlways}.
     */
    public static <T> Predicate<T> always() {
        return PredicateAlways.instance();
    }

    /**
     * /**
     * {@see PredicateAny}.
     */
    public static <T> Predicate<T> any(final Collection<Predicate<T>> predicates) {
        return PredicateAny.with(predicates);
    }

    /**
     * {@see PredicateCharPredicate}
     */
    public static Predicate<Character> charPredicate(final CharPredicate predicate) {
        return PredicateCharPredicate.adapt(predicate);
    }

    /**
     * {@see PredicateCharPredicateCharSequence}
     */
    public static Predicate<CharSequence> charSequence(final CharPredicate predicate) {
        return PredicateCharPredicateCharSequence.with(predicate);
    }

    /**
     * {@see CaseSensitivity#predicateEndsWith}
     */
    public static <C extends CharSequence> Predicate<C> charSequenceEndsWith(final CaseSensitivity caseSensitivity,
                                                                             final C endsWith) {
        return caseSensitivity.predicateEndsWith(endsWith);
    }

    /**
     * {@see CaseSensitivity#predicateContains}
     */
    public static <C extends CharSequence> Predicate<C> charSequenceContains(final CaseSensitivity caseSensitivity,
                                                                             final C contains) {
        return caseSensitivity.predicateContains(contains);
    }

    /**
     * {@see CaseSensitivity#predicateStartsWith}
     */
    public static <C extends CharSequence> Predicate<C> charSequenceStartsWith(final CaseSensitivity caseSensitivity,
                                                                               final C startsWith) {
        return caseSensitivity.predicateStartsWith(startsWith);
    }

    /**
     * {@see PredicateComparableCompareResult}
     */
    public static <C extends Comparable<C>> Predicate<C> compareResult(final CompareResult compareResult,
                                                                       final C right) {
        return PredicateComparableCompareResult.with(
            compareResult,
            right
        );
    }

    /**
     * {@see PredicateCustomToString}
     */
    public static <T> Predicate<T> customToString(final Predicate<T> predicate, final String toString) {
        return PredicateCustomToString.wrap(predicate, toString);
    }

    /**
     * {@see FakePredicate}.
     */
    public static <T> Predicate<T> fake() {
        return FakePredicate.create();
    }

    /**
     * Returns a {@link Predicate} that is composed of multiple glob expressions, with one for each token within the expression.
     * The predicate returns true if any glob pattern is matched.
     * <br>
     * Note the {@link Object#toString()} returned will return the original expression verbatim.
     */
    public static Predicate<CharSequence> globPatterns(final String expression,
                                                       final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(expression, "expression");

        return Predicates.customToString(
            Predicates.any(
                Arrays.stream(
                        expression.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(caseSensitivity::globPattern)
                    .distinct()
                    .collect(Collectors.<Predicate<CharSequence>>toList())
            ),
            expression
        );
    }

    /**
     * {@see PredicateInitialAndPartCharPredicateCharSequence}
     */
    public static Predicate<CharSequence> initialAndPart(final CharPredicate initial, final CharPredicate remaining) {
        return PredicateInitialAndPartCharPredicateCharSequence.with(initial, remaining);
    }

    /**
     * {@see ObjectEqualityMatcher}
     */
    public static <T> Predicate<T> is(final T value) {
        return PredicateObjectEquality.with(value);
    }

    /**
     * Matches java identifiers using {@link Character#isJavaIdentifierStart(char)} and
     * {@link Character#isJavaIdentifierPart(char)}.
     */
    @GwtIncompatible
    public static Predicate<CharSequence> javaIdentifier() {
        return JAVA_IDENTIFIER;
    }

    @GwtIncompatible
    private final static Predicate<CharSequence> JAVA_IDENTIFIER = Predicates.initialAndPart(
        Character::isJavaIdentifierStart,
        Character::isJavaIdentifierPart);

    /**
     * {@see PredicateNever}.
     */
    public static <T> Predicate<T> never() {
        return PredicateNever.instance();
    }

    /**
     * {@see PredicateNot}.
     */
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return PredicateNot.wrap(predicate);
    }

    /**
     * {@see PredicateSetContains}
     */
    public static <T> Predicate<T> setContains(final Set<T> set) {
        return PredicateSetContains.with(set);
    }

    /**
     * Stop creation
     */
    private Predicates() {
        throw new UnsupportedOperationException();
    }
}
