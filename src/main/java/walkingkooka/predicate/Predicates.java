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
import walkingkooka.type.PublicStaticHelper;

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
     * {@see FakePredicate}.
     */
    public static <T> Predicate<T> fake() {
        return FakePredicate.create();
    }

    /**
     * {@see ObjectEqualityMatcher}
     */
    public static <T> Predicate<T> is(final T value) {
        return ObjectEqualityPredicate.with(value);
    }

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
