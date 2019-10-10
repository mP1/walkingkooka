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

/**
 * The {@link SearchQueryContext} used by {@link SearchQueryParentBinaryAnd}.
 */
final class SearchQueryParentAndSearchQueryContext extends SearchQueryParentBinarySearchQueryContext {

    /**
     * Factory only called by {@link SearchQueryParentBinaryAnd}.
     */
    static SearchQueryParentAndSearchQueryContext with(final SearchQueryContext context, final SearchQuery right) {
        return new SearchQueryParentAndSearchQueryContext(context, right);
    }

    private SearchQueryParentAndSearchQueryContext(final SearchQueryContext context, final SearchQuery right) {
        super(context, right);
    }

    @Override
    void failure(final SearchNode node) {
        // skip trying right, left already failed
        this.context.failure(node);
    }

    @Override
    void success(final SearchNode node, final SearchNode replacement) {
        node.select(this.right, this.context); // try again with the right query...
    }
}
