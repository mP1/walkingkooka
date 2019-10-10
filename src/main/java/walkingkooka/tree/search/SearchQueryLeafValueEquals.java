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

final class SearchQueryLeafValueEquals extends SearchQueryLeafValue {

    static SearchQueryLeafValueEquals with(final SearchQueryValue value, final SearchQueryTester tester) {
        return new SearchQueryLeafValueEquals(value, tester);
    }

    private SearchQueryLeafValueEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        super(value, tester);
    }

    public SearchQuery not() {
        return notEquals(this.value, this.tester.not());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchQueryLeafValueEquals;
    }

    @Override
    void toStringPrefix(final StringBuilder b) {
        b.append('=');
    }
}
