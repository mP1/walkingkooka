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
 *
 */

package walkingkooka.tree.search;

import java.util.Comparator;

/**
 * Returns true if the condition is met, testing the predicate of a {@link Number} comparing against another
 * number of the same type.
 */
enum SearchQueryValueSearchQueryTesterComparisonPredicate {
    EQUALS {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return NOT_EQUALS;
        }

        @Override
        boolean test0(final int predicate) {
            return 0 == predicate;
        }
    },
    GREATER_THAN {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return LESS_THAN_EQUALS;
        }

        @Override
        boolean test0(final int predicate) {
            return predicate > 0;
        }
    },
    GREATER_THAN_EQUALS {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return LESS_THAN;
        }

        @Override
        boolean test0(final int predicate) {
            return predicate >= 0;
        }
    },
    LESS_THAN {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return GREATER_THAN_EQUALS;
        }

        @Override
        boolean test0(final int predicate) {
            return predicate < 0;
        }
    },
    LESS_THAN_EQUALS {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return GREATER_THAN;
        }

        @Override
        boolean test0(final int predicate) {
            return predicate <= 0;
        }
    },
    NOT_EQUALS {
        @Override
        SearchQueryValueSearchQueryTesterComparisonPredicate not() {
            return EQUALS;
        }

        @Override
        boolean test0(final int predicate) {
            return 0 != predicate;
        }
    };

    abstract SearchQueryValueSearchQueryTesterComparisonPredicate not();

    final <N extends Number & Comparable<N>> boolean test(final N value, final N otherValue) {
        return this.test0(otherValue.compareTo(value));
    }

    final <C extends Comparable<C>> boolean test(final Comparator<C> comparator, final C value, final C otherValue) {
        return this.test0(comparator.compare(otherValue, value));
    }

    abstract boolean test0(int predicate);
}
