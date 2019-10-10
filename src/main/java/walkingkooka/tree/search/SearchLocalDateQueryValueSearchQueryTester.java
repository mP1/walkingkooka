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

import java.time.LocalDate;

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalDateQueryValue}
 */
abstract class SearchLocalDateQueryValueSearchQueryTester extends SearchQueryTester<LocalDate> {

    static SearchLocalDateQueryValueSearchQueryTesterEquals equalsTester(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterEquals.with(value);
    }

    static SearchLocalDateQueryValueSearchQueryTesterGreaterThan greaterThan(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterGreaterThan.with(value);
    }

    static SearchLocalDateQueryValueSearchQueryTesterGreaterThanEquals greaterThanEquals(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterGreaterThanEquals.with(value);
    }

    static SearchLocalDateQueryValueSearchQueryTesterLessThan lessThan(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterLessThan.with(value);
    }

    static SearchLocalDateQueryValueSearchQueryTesterLessThanEquals lessThanEquals(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterLessThanEquals.with(value);
    }

    static SearchLocalDateQueryValueSearchQueryTesterNotEquals notEquals(final LocalDate value) {
        return SearchLocalDateQueryValueSearchQueryTesterNotEquals.with(value);
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
