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

final class SearchNotQuery extends SearchUnaryQuery {

    static SearchNotQuery with(final SearchQuery query) {
        check(query);
        return new SearchNotQuery(query);
    }

    private SearchNotQuery(final SearchQuery query) {
        super(query);
    }

    @Override
    public SearchQuery not() {
        return this.query;
    }

    @Override
    SearchNotQueryContext context(final SearchQueryContext context) {
        return SearchNotQueryContext.with(context);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchNotQuery;
    }

    @Override
    public String toString() {
        return "!" + this.query;
    }
}
