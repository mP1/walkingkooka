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

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalDateTimeQueryValue}
 */
abstract class SearchLocalDateTimeQueryValueSearchQueryTester extends SearchQueryTester<LocalDateTime> {

    static SearchLocalDateTimeQueryValueEqualsSearchQueryTester equalsTester(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester greaterThan(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester.with(value);
    }

    static SearchLocalDateTimeQueryValueGreaterThanEqualsSearchQueryTester greaterThanEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueGreaterThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateTimeQueryValueLessThanSearchQueryTester lessThan(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueLessThanSearchQueryTester.with(value);
    }

    static SearchLocalDateTimeQueryValueLessThanEqualsSearchQueryTester lessThanEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueLessThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateTimeQueryValueNotEqualsSearchQueryTester notEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueNotEqualsSearchQueryTester.with(value);
    }

    SearchLocalDateTimeQueryValueSearchQueryTester(final LocalDateTime value) {
        super(value);
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
    final boolean test(final SearchLocalDateNode node) {
        return false;
    }

    @Override
    final boolean test(final SearchLocalTimeNode node) {
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
    boolean equals1(final SearchQueryTester other) {
        return true; // no extra properties to test.
    }
}
