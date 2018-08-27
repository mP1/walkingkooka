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
 * A {@link SearchQueryTester} that only returns true if a {@link LocalDateTime} is greater than {@link SearchLocalDateTimeNode}.
 */
final class SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester extends SearchLocalDateTimeQueryValueSearchQueryTester {

    static SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester with(final LocalDateTime value) {
        return new SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester(value);
    }

    private SearchLocalDateTimeQueryValueGreaterThanSearchQueryTester(final LocalDateTime value) {
        super(value);
    }

    @Override
    SearchQueryTester not() {
        return lessThanEquals(this.value);
    }

    @Override
    final boolean test(final SearchLocalDateTimeNode node) {
        return node.value().isAfter(this.value);
    }
}
