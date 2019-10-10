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

import java.time.LocalTime;

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalTimeQueryValue}
 */
abstract class SearchLocalTimeQueryValueSearchQueryTester extends SearchQueryTester<LocalTime> {

    static SearchLocalTimeQueryValueSearchQueryTesterEquals equalsTester(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterEquals.with(value);
    }

    static SearchLocalTimeQueryValueSearchQueryTesterGreaterThan greaterThan(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterGreaterThan.with(value);
    }

    static SearchLocalTimeQueryValueSearchQueryTesterGreaterThanEquals greaterThanEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterGreaterThanEquals.with(value);
    }

    static SearchLocalTimeQueryValueSearchQueryTesterLessThan lessThan(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterLessThan.with(value);
    }

    static SearchLocalTimeQueryValueSearchQueryTesterLessThanEquals lessThanEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterLessThanEquals.with(value);
    }

    static SearchLocalTimeQueryValueSearchQueryTesterNotEquals notEquals(final LocalTime value) {
        return SearchLocalTimeQueryValueSearchQueryTesterNotEquals.with(value);
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
