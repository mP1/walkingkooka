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

public final class SearchLocalDateTimeQueryValueTest extends SearchQueryValueTestCase2<SearchLocalDateTimeQueryValue, LocalDateTime> {
    @Override
    SearchLocalDateTimeQueryValue createSearchQueryValue(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValue.with(value);
    }

    @Override
    LocalDateTime value() {
        return LocalDateTime.of(2000, 1, 31, 12, 58, 59);
    }

    @Override
    LocalDateTime differentValue() {
        return LocalDateTime.of(1999, 12, 31, 12, 58, 59);
    }

    @Override
    String searchQueryValueToString() {
        return "2000-01-31T12:58:59";
    }

    @Override
    public Class<SearchLocalDateTimeQueryValue> type() {
        return SearchLocalDateTimeQueryValue.class;
    }
}
