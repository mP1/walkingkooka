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

import java.time.LocalDateTime;

/**
 * Base class for all {@link SearchQueryTester} used by {@link SearchLocalDateTimeQueryValue}
 */
abstract class SearchLocalDateTimeQueryValueSearchQueryTester extends SearchQueryTester<LocalDateTime> {

    static SearchLocalDateTimeQueryValueSearchQueryTesterEquals equalsTester(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterEquals.with(value);
    }

    static SearchLocalDateTimeQueryValueSearchQueryTesterGreaterThan greaterThan(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterGreaterThan.with(value);
    }

    static SearchLocalDateTimeQueryValueSearchQueryTesterGreaterThanEquals greaterThanEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterGreaterThanEquals.with(value);
    }

    static SearchLocalDateTimeQueryValueSearchQueryTesterLessThan lessThan(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterLessThan.with(value);
    }

    static SearchLocalDateTimeQueryValueSearchQueryTesterLessThanEquals lessThanEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterLessThanEquals.with(value);
    }

    static SearchLocalDateTimeQueryValueSearchQueryTesterNotEquals notEquals(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValueSearchQueryTesterNotEquals.with(value);
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
