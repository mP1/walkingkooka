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

final class SearchGreaterThanQuery extends SearchValueComparisonLeafQuery {

    static SearchGreaterThanQuery with(final SearchQueryValue value, final SearchQueryTester tester) {
        return new SearchGreaterThanQuery(value, tester);
    }

    private SearchGreaterThanQuery(final SearchQueryValue value, final SearchQueryTester tester) {
        super(value, tester);
    }

    @Override
    public SearchQuery not() {
        return lessThanEquals(this.value, this.tester.not());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchGreaterThanQuery;
    }

    @Override
    void toStringPrefix(final StringBuilder b) {
        b.append('>');
    }
}
