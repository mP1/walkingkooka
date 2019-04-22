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

abstract class SearchComparableQueryValueSearchQueryTester<T extends Comparable<T>> extends SearchQueryTester<T> {

    SearchComparableQueryValueSearchQueryTester(final T value, final SearchQueryValueSearchQueryTesterComparisonPredicate predicate) {
        super(value);
        this.predicate = predicate;
    }

    final SearchQueryValueSearchQueryTesterComparisonPredicate predicate;

    @Override
    final boolean test(final SearchLocalDateNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchLocalDateTimeNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchLocalTimeNode node) {
        return false;
    }

    @Override
    final boolean equals1(final SearchQueryTester other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final SearchComparableQueryValueSearchQueryTester other) {
        return this.predicate == other.predicate &&
                this.equals3(other);
    }

    abstract boolean equals3(final SearchComparableQueryValueSearchQueryTester other);
}
