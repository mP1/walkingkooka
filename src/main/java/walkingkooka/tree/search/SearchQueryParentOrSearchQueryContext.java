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
 * The {@link SearchQueryContext} used by {@link SearchQueryParentBinaryOr}.
 */
final class SearchQueryParentOrSearchQueryContext extends SearchQueryParentBinarySearchQueryContext {

    /**
     * Factory only called by {@link SearchQueryParentBinaryOr}.
     */
    static SearchQueryParentOrSearchQueryContext with(final SearchQueryContext context, final SearchQuery right) {
        return new SearchQueryParentOrSearchQueryContext(context, right);
    }

    private SearchQueryParentOrSearchQueryContext(final SearchQueryContext context, final SearchQuery right) {
        super(context, right);
    }

    @Override
    void failure(final SearchNode node) {
        // try again with the right query...
        node.select(this.right, this.context);
    }

    @Override
    void success(final SearchNode match, final SearchNode replacement) {
        // mark node as matched, ignore right query.
        this.context.success(match, replacement);
    }
}
