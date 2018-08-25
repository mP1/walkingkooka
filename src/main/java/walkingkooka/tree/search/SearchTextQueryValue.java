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

import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A {@link SearchQueryValue} that holds a {@link String}
 */
public final class SearchTextQueryValue extends SearchQueryValue{

    static SearchTextQueryValue with(final String value) {
        check(value);
        return new SearchTextQueryValue(value);
    }

    private SearchTextQueryValue(final String value) {
        super();
        this.value = value;
    }

    public SearchQuery equalsQuery(final CaseSensitivity caseSensitivity) {
        return SearchQuery.equalsQuery(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.EQUALS));
    }

    public SearchQuery greaterThan(final CaseSensitivity caseSensitivity) {
        return SearchQuery.greaterThan(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.GREATER_THAN));
    }

    public SearchQuery greaterThanEquals(final CaseSensitivity caseSensitivity) {
        return SearchQuery.greaterThanEquals(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.GREATER_THAN_EQUALS));
    }

    public SearchQuery lessThan(final CaseSensitivity caseSensitivity) {
        return SearchQuery.lessThan(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.LESS_THAN));
    }

    public SearchQuery lessThanEquals(final CaseSensitivity caseSensitivity) {
        return SearchQuery.lessThanEquals(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.LESS_THAN_EQUALS));
    }

    public SearchQuery notEquals(final CaseSensitivity caseSensitivity) {
        return SearchQuery.notEquals(this, this.tester(caseSensitivity, SearchQueryValueSearchQueryTesterComparisonPredicate.NOT_EQUALS));
    }

    private SearchTextQueryValueSearchQueryTester tester(final CaseSensitivity caseSensitivity, final SearchQueryValueSearchQueryTesterComparisonPredicate resultPredicate) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");
        return SearchTextQueryValueSearchQueryTester.with(this.value, caseSensitivity, resultPredicate);
    }

    final String value;

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.value).toString();
    }
}
