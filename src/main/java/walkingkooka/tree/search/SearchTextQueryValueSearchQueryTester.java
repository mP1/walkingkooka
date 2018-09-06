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

import walkingkooka.Cast;
import walkingkooka.text.CaseSensitivity;

/**
 * The {@link SearchQueryTester} used by {@link SearchTextQueryValue}
 */
final class SearchTextQueryValueSearchQueryTester extends SearchComparableQueryValueSearchQueryTester<String> {

    static SearchTextQueryValueSearchQueryTester with(final String value, final CaseSensitivity caseSensitivity, final SearchQueryValueSearchQueryTesterComparisonPredicate result) {
        return new SearchTextQueryValueSearchQueryTester(value, caseSensitivity, result);
    }

    private SearchTextQueryValueSearchQueryTester(final String value, final CaseSensitivity caseSensitivity, final SearchQueryValueSearchQueryTesterComparisonPredicate result) {
        super(value, result);
        this.caseSensitivity = caseSensitivity;
    }

    @Override
    SearchTextQueryValueSearchQueryTester not() {
        return new SearchTextQueryValueSearchQueryTester(this.value, this.caseSensitivity, this.result.not());
    }

    @Override
    final boolean test(final SearchBigDecimalNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchBigIntegerNode node) {
        return false;
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
        return this.result.test(this.caseSensitivity.<String>comparator(), this.value, node.value());
    }

    final CaseSensitivity caseSensitivity;

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchTextQueryValueSearchQueryTester;
    }

    @Override
    boolean equals3(final SearchComparableQueryValueSearchQueryTester other) {
        return this.equals3(Cast.to(other));
    }

    private boolean equals3(final SearchTextQueryValueSearchQueryTester other) {
        return this.caseSensitivity.equals(other.caseSensitivity);
    }
}
