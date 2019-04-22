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

import walkingkooka.Context;

/**
 * The {@link Context} that accompanies each and every {@link SearchNode} test and accumulates matches.
 */
abstract class SearchQueryContext implements Context {

    /**
     * Package private to limit sub classing.
     */
    SearchQueryContext() {
        super();
    }

    /**
     * This method is called for each node that is not matched.
     */
    abstract void failure(final SearchNode node);

    /**
     * This method is called for each matched node. Currently only called by {@link SearchContainsQuery}.
     */
    final void success(final SearchNode match) {
        this.success(match, match);
    }

    /**
     * This method is called for each matched node, that has an alternate replacement.
     */
    abstract void success(final SearchNode match, final SearchNode replacement);

    /**
     * This is called after the node traversal is finished and contains the resulting node which may or may not
     * have some selected nodes.
     */
    abstract SearchNode finish();
}
