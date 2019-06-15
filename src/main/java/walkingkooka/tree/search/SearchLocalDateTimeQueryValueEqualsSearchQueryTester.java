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
 * A {@link SearchQueryTester} that only returns true if a {@link LocalDateTime} is equal {@link SearchLocalDateTimeNode}.
 */
final class SearchLocalDateTimeQueryValueEqualsSearchQueryTester extends SearchLocalDateTimeQueryValueSearchQueryTester {

    static SearchLocalDateTimeQueryValueEqualsSearchQueryTester with(final LocalDateTime value) {
        return new SearchLocalDateTimeQueryValueEqualsSearchQueryTester(value);
    }

    private SearchLocalDateTimeQueryValueEqualsSearchQueryTester(final LocalDateTime value) {
        super(value);
    }

    @Override
    SearchQueryTester not() {
        return notEquals(this.value);
    }

    @Override
    final boolean test(final SearchLocalDateTimeNode node) {
        return this.value.equals(node.value());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchLocalDateTimeQueryValueEqualsSearchQueryTester;
    }
}
