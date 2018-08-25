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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A {@link SearchQueryValue} that holds a {@link LocalDate}
 */
public final class SearchLocalDateQueryValue extends SearchQueryValue{

    static SearchLocalDateQueryValue with(final LocalDate value) {
        check(value);
        return new SearchLocalDateQueryValue(value);
    }

    private SearchLocalDateQueryValue(final LocalDate value) {
        super();
        this.value = value;
    }

    public SearchQuery equalsQuery() {
        return SearchQuery.equalsQuery(this, SearchLocalDateQueryValueSearchQueryTester.equalsTester(this.value));
    }

    public SearchQuery greaterThan() {
        return SearchQuery.greaterThan(this, SearchLocalDateQueryValueSearchQueryTester.greaterThan(this.value));
    }

    public SearchQuery greaterThanEquals() {
        return SearchQuery.greaterThanEquals(this, SearchLocalDateQueryValueSearchQueryTester.greaterThanEquals(this.value));
    }

    public SearchQuery lessThan() {
        return SearchQuery.lessThan(this, SearchLocalDateQueryValueSearchQueryTester.lessThan(this.value));
    }

    public SearchQuery lessThanEquals() {
        return SearchQuery.lessThanEquals(this, SearchLocalDateQueryValueSearchQueryTester.lessThanEquals(this.value));
    }

    public SearchQuery notEquals() {
        return SearchQuery.notEquals(this, SearchLocalDateQueryValueSearchQueryTester.notEquals(this.value));
    }

    final LocalDate value;

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(this.value);
    }
}
