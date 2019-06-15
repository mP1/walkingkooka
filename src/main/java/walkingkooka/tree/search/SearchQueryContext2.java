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

import walkingkooka.Context;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.pointer.NodePointer;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Context} that accompanies each and every {@link SearchNode} test and accumulates matches.
 */
final class SearchQueryContext2 extends SearchQueryContext {

    static SearchQueryContext2 with(final SearchNode start) {
        return new SearchQueryContext2(start);
    }

    private SearchQueryContext2(final SearchNode start) {
        super();
        this.set(start);
    }

    @Override
    void failure(final SearchNode node) {
        // nothing
    }

    /**
     * Records a match and its replacement, highlighting the node
     */
    @Override
    void success(final SearchNode node, final SearchNode replacement) {
        this.replace0(equivalent(node),
                replacement.isSequence() ?
                        replacement :
                        replacement.selected());
    }

    private SearchNode equivalent(final SearchNode node) {
        final NodePointer<SearchNode, SearchNodeName> pointer = node.pointer();
        final Optional<SearchNode> equivalent = pointer.traverse(this.result);
        if (!equivalent.isPresent()) {
            throw new SearchQueryException("Unable to find equivalent node for match=" + node);
        }
        return equivalent.get();
    }

    private void replace0(final SearchNode node, final SearchNode replacement) {
        final Optional<SearchNode> maybeParent = node.parent();
        if (maybeParent.isPresent()) {
            final SearchNode parent = maybeParent.get();

            if (parent.isSequence()) {
                this.replaceSequence(node, parent.cast(), replacement);
            } else {
                this.replace(parent, replacement);
            }
        } else {
            this.set(replacement);
        }
    }

    private void replaceSequence(final SearchNode match, final SearchSequenceNode parent, final SearchNode replacement) {
        final List<SearchNode> all = Lists.array();
        all.addAll(parent.children());
        all.set(match.index(), replacement);

        this.set(parent.setChildren(all));
    }

    private void replace(final SearchNode parent, final SearchNode replacement) {
        this.set(parent.setChildren(Lists.of(replacement)));
    }

    private void set(final SearchNode node) {
        this.result = node.root();
    }

    @Override
    SearchNode finish() {
        return this.result;
    }

    /**
     * A {@link SearchNode} (probably a {@link SearchSequenceNode} that holds each match wrapped inside a {@link SearchSelectNode}.
     */
    private SearchNode result;

    @Override
    public String toString() {
        return this.result.toString();
    }
}
