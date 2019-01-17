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

public final class SearchDoubleQueryValueTest extends SearchQueryValueTestCase<SearchDoubleQueryValue, Double> {

    @Override
    SearchDoubleQueryValue createSearchQueryValue(final Double value) {
        return SearchDoubleQueryValue.with(value);
    }

    @Override
    Double value() {
        return Double.valueOf(123.5);
    }

    @Override
    Double differentValue() {
        return Double.valueOf(999);
    }

    @Override
    String searchQueryValueToString() {
        return this.value().toString();
    }

    @Override
    protected Class<SearchDoubleQueryValue> type() {
        return SearchDoubleQueryValue.class;
    }
}
