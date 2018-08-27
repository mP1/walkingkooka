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

/**
 * Base class for all values that are also {@link Comparable}.
 */
abstract class SearchNumberQueryValue<N extends Number & Comparable<N>> extends SearchQueryValue{

    SearchNumberQueryValue(final N value) {
        super();
        this.value = value;
    }

    public final SearchQuery equalsQuery() {
        return SearchQuery.equalsQuery(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.EQUALS));
    }

    public final SearchQuery greaterThan() {
        return SearchQuery.greaterThan(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.GREATER_THAN));
    }

    public final SearchQuery greaterThanEquals() {
        return SearchQuery.greaterThanEquals(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.GREATER_THAN_EQUALS));
    }

    public final SearchQuery lessThan() {
        return SearchQuery.lessThan(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.LESS_THAN));
    }

    public final SearchQuery lessThanEquals() {
        return SearchQuery.lessThanEquals(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.LESS_THAN_EQUALS));
    }

    public final SearchQuery notEquals() {
        return SearchQuery.notEquals(this, this.tester(SearchQueryValueSearchQueryTesterComparisonPredicate.NOT_EQUALS));
    }

    /**
     * Factory that creates a sub class of {@link SearchComparableQueryValueSearchQueryTester}
     */
    abstract SearchComparableQueryValueSearchQueryTester<N> tester(final SearchQueryValueSearchQueryTesterComparisonPredicate resultPredicate);

    @Override
    public final String toString() {
        return this.value.toString();
    }

    final N value;
}
