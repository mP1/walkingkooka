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

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalDateQueryValue}
 */
abstract class SearchLocalDateQueryValueSearchQueryTester extends SearchQueryTester<LocalDate> {

    static SearchLocalDateQueryValueEqualsSearchQueryTester equalsTester(final LocalDate value) {
        return SearchLocalDateQueryValueEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateQueryValueGreaterThanSearchQueryTester greaterThan(final LocalDate value) {
        return SearchLocalDateQueryValueGreaterThanSearchQueryTester.with(value);
    }

    static SearchLocalDateQueryValueGreaterThanEqualsSearchQueryTester greaterThanEquals(final LocalDate value) {
        return SearchLocalDateQueryValueGreaterThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateQueryValueLessThanSearchQueryTester lessThan(final LocalDate value) {
        return SearchLocalDateQueryValueLessThanSearchQueryTester.with(value);
    }

    static SearchLocalDateQueryValueLessThanEqualsSearchQueryTester lessThanEquals(final LocalDate value) {
        return SearchLocalDateQueryValueLessThanEqualsSearchQueryTester.with(value);
    }

    static SearchLocalDateQueryValueNotEqualsSearchQueryTester notEquals(final LocalDate value) {
        return SearchLocalDateQueryValueNotEqualsSearchQueryTester.with(value);
    }

    SearchLocalDateQueryValueSearchQueryTester(final LocalDate value) {
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
    final boolean test(final SearchLocalDateTimeNode node) {
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
