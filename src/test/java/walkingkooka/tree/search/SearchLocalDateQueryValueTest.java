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

public final class SearchLocalDateQueryValueTest extends SearchQueryValueTestCase2<SearchLocalDateQueryValue, LocalDate> {
    @Override
    SearchLocalDateQueryValue createSearchQueryValue(final LocalDate value) {
        return SearchLocalDateQueryValue.with(value);
    }

    @Override
    LocalDate value() {
        return LocalDate.of(2000, 1, 2);
    }

    @Override
    LocalDate differentValue() {
        return LocalDate.of(1999, 12, 31);
    }

    @Override
    String searchQueryValueToString() {
        return "2000-01-02";
    }

    @Override
    public Class<SearchLocalDateQueryValue> type() {
        return SearchLocalDateQueryValue.class;
    }
}
