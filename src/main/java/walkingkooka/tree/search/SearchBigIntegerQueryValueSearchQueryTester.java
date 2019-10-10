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

package walkingkooka.tree.search;

import java.math.BigInteger;

final class SearchBigIntegerQueryValueSearchQueryTester extends SearchQueryValueSearchQueryTesterComparable<BigInteger> {

    static SearchBigIntegerQueryValueSearchQueryTester with(final BigInteger value, final SearchQueryValueSearchQueryTesterComparisonPredicate predicate) {
        return new SearchBigIntegerQueryValueSearchQueryTester(value, predicate);
    }

    private SearchBigIntegerQueryValueSearchQueryTester(final BigInteger value, final SearchQueryValueSearchQueryTesterComparisonPredicate predicate) {
        super(value, predicate);
    }

    @Override
    SearchBigIntegerQueryValueSearchQueryTester not() {
        return new SearchBigIntegerQueryValueSearchQueryTester(this.value, this.predicate.not());
    }

    @Override
    final boolean test(final SearchBigDecimalNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchBigIntegerNode node) {
        return this.predicate.test(this.value, node.value());
    }

    @Override
    final boolean test(final SearchDoubleNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchLongNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchTextNode node) {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchBigIntegerQueryValueSearchQueryTester;
    }

    @Override
    boolean equals3(final SearchQueryValueSearchQueryTesterComparable other) {
        return true; // no extra properties
    }
}
