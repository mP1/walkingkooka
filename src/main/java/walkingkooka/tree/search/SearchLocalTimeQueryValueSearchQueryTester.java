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

import java.time.LocalTime;

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalTimeQueryValue}
 */
abstract class SearchLocalTimeQueryValueSearchQueryTester extends SearchQueryTester<LocalTime> {

    static SearchLocalTimeQueryValueEqualsSearchQueryTester equalsTester(final LocalTime value) {
        return SearchLocalTimeQueryValueEqualsSearchQueryTester.with(value);
    }

    static SearchLocalTimeQueryValueGreaterThanSearchQueryTester greaterThan(final LocalTime value) {
        return SearchLocalTimeQueryValueGreaterThanSearchQueryTester.with(value);
    }

    static SearchLocalTimeQueryValueGreaterThanEqualsSearchQueryTester greaterThanEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueGreaterThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalTimeQueryValueLessThanSearchQueryTester lessThan(final LocalTime value) {
        return SearchLocalTimeQueryValueLessThanSearchQueryTester.with(value);
    }

    static SearchLocalTimeQueryValueLessThanEqualsSearchQueryTester lessThanEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueLessThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalTimeQueryValueNotEqualsSearchQueryTester notEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueNotEqualsSearchQueryTester.with(value);
    }

    SearchLocalTimeQueryValueSearchQueryTester(final LocalTime value) {
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
    final boolean test(final SearchLocalDateTimeNode node) {
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
