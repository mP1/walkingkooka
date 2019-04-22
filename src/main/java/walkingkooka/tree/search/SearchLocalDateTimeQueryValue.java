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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A {@link SearchQueryValue} that holds a {@link LocalDateTime}
 */
public final class SearchLocalDateTimeQueryValue extends SearchQueryValue {

    static SearchLocalDateTimeQueryValue with(final LocalDateTime value) {
        check(value);
        return new SearchLocalDateTimeQueryValue(value);
    }

    private SearchLocalDateTimeQueryValue(final LocalDateTime value) {
        super();
        this.value = value;
    }

    public SearchQuery equalsQuery() {
        return SearchQuery.equalsQuery(this, SearchLocalDateTimeQueryValueSearchQueryTester.equalsTester(this.value));
    }

    public SearchQuery greaterThan() {
        return SearchQuery.greaterThan(this, SearchLocalDateTimeQueryValueSearchQueryTester.greaterThan(this.value));
    }

    public SearchQuery greaterThanEquals() {
        return SearchQuery.greaterThanEquals(this, SearchLocalDateTimeQueryValueSearchQueryTester.greaterThanEquals(this.value));
    }

    public SearchQuery lessThan() {
        return SearchQuery.lessThan(this, SearchLocalDateTimeQueryValueSearchQueryTester.lessThan(this.value));
    }

    public SearchQuery lessThanEquals() {
        return SearchQuery.lessThanEquals(this, SearchLocalDateTimeQueryValueSearchQueryTester.lessThanEquals(this.value));
    }

    public SearchQuery notEquals() {
        return SearchQuery.notEquals(this, SearchLocalDateTimeQueryValueSearchQueryTester.notEquals(this.value));
    }

    final LocalDateTime value;

    @Override
    final String text() {
        throw new UnsupportedOperationException();
    }

    @Override
    final LocalDateTime value() {
        return this.value;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchLocalDateTimeQueryValue;
    }

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this.value);
    }
}
