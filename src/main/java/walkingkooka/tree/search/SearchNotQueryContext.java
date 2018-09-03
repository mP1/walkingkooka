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

/**
 * The {@link SearchQueryContext} used by {@link SearchNotQuery}.
 * It basically inverts its failure to call success on the wrapped context, and success to call failure.
 */
final class SearchNotQueryContext extends SearchUnaryQueryContext {

    /**
     * Factory only called by {@link SearchNotQuery}
     */
    static SearchNotQueryContext with(final SearchQueryContext context) {
        return new SearchNotQueryContext(context);
    }

    private SearchNotQueryContext(final SearchQueryContext context) {
        super(context);
    }

    @Override
    void failure(final SearchNode node) {
        this.context.success(node);
    }

    @Override
    void success(final SearchNode match, final SearchNode replacement) {
        this.context.failure(match);
    }
}
