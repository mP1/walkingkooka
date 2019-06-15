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

package walkingkooka.compare;

import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * The 6 standard {@link ComparisonRelation}.
 */
public enum ComparisonRelation implements Predicate<Integer> {

    EQ("=") {
        @Override
        boolean test0(final int value) {
            return 0 == value;
        }

        @Override
        public ComparisonRelation invert() {
            return NE;
        }

        /**
         * Operand side does not affect test.
         */
        @Override
        public ComparisonRelation swap() {
            return this;
        }
    },
    GTE(">=") {
        @Override
        boolean test0(final int value) {
            return value >= 0;
        }

        @Override
        public ComparisonRelation invert() {
            return LT;
        }

        @Override
        public ComparisonRelation swap() {
            return LTE;
        }
    },
    GT(">") {
        @Override
        boolean test0(final int value) {
            return value > 0;
        }

        @Override
        public ComparisonRelation invert() {
            return LTE;
        }

        @Override
        public ComparisonRelation swap() {
            return LT;
        }
    },
    LTE("<=") {
        @Override
        boolean test0(final int value) {
            return value <= 0;
        }

        @Override
        public ComparisonRelation invert() {
            return GT;
        }

        @Override
        public ComparisonRelation swap() {
            return GTE;
        }
    },
    LT("<") {
        @Override
        boolean test0(final int value) {
            return value < 0;
        }

        @Override
        public ComparisonRelation invert() {
            return GTE;
        }

        @Override
        public ComparisonRelation swap() {
            return GT;
        }
    },
    NE("!=") {
        @Override
        boolean test0(final int value) {
            return 0 != value;
        }

        @Override
        public ComparisonRelation invert() {
            return EQ;
        }

        /**
         * Operand side does not affect test.
         */
        @Override
        public ComparisonRelation swap() {
            return this;
        }
    };

    ComparisonRelation(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns true if the integer satisfies the relation. The numeric values follow the {@link Comparator} return values.
     */
    @Override
    public boolean test(final Integer integer) {
        return this.test0(integer.intValue());
    }

    abstract boolean test0(final int value);

    /**
     * The mathematical symbol for the relation, eg {@link #LTE} returns "<=".
     */
    public final String symbol() {
        return this.symbol;
    }

    private final String symbol;

    /**
     * Returns a {@link Predicate} that uses the given value as the right of a comparison.
     */
    public <C extends Comparable<C>> Predicate<C> predicate(final C right) {
        return Predicates.comparisonRelation(this, right);
    }

    /**
     * The inverted or opposite relation.
     */
    abstract public ComparisonRelation invert();

    /**
     * Returns the {@link ComparisonRelation} when operands in a comparison sides are swapped.
     */
    abstract public ComparisonRelation swap();

    /**
     * Finds a {@link ComparisonRelation} with the {@link String symbol} or throws a {@link IllegalArgumentException}.
     */
    public static ComparisonRelation findWithSymbol(final String symbol) {
        Objects.requireNonNull(symbol);

        return Arrays.stream(ComparisonRelation.values())
                .filter(r -> r.symbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find symbol " + CharSequences.quote(symbol)));
    }
}
