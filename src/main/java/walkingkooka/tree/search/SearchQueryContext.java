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
import walkingkooka.ShouldNeverHappenError;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.tree.pointer.NodePointer;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Context} that accompanies each and every {@link SearchNode} test and accumulates matches.
 */
final class SearchQueryContext implements Context {

    static SearchQueryContext create(){
        return new SearchQueryContext();
    }

    private SearchQueryContext(){
        super();
    }

    void replace(final SearchNode match) {
        this.replace(match, match.selected());
    }

    /**
     * Records a match and its replacement, highlighting the node
     */
    void replace(final SearchNode match, final SearchNode replacement) {
        this.replace0(equivalent(match), replacement);
    }

    private SearchNode equivalent(final SearchNode match) {
        final NodePointer<SearchNode, SearchNodeName, Name, Object> pointer = match.pointer();
        final Optional<SearchNode> equivalent = pointer.traverse(this.result);
        if(!equivalent.isPresent()){
            throw new SearchQueryException("Unable to find equivalent node for match=" + match);
        }
        return equivalent.get();
    }

    private void replace0(final SearchNode match, final SearchNode replacement) {
        final Optional<SearchNode> maybeParent = match.parent();
        if(maybeParent.isPresent()) {
            final SearchNode parent = maybeParent.get();
            if(parent.isSelect()) {
                this.replaceSelect(parent.cast(), replacement);
            } else {
                if(parent.isSequence()) {
                    this.replaceSequence(match, parent.cast(), replacement);
                } else {
                    throw new ShouldNeverHappenError("Unknown parent SearchNode " + parent.getClass().getName() + "=" + parent);
                }
            }
        } else {
            this.set(replacement);
        }
    }

    private void replaceSelect(final SearchSelectNode parent, final SearchNode replacement) {
        this.set(parent.setChildren(Lists.of(replacement)));
    }

    private void replaceSequence(final SearchNode match, final SearchSequenceNode parent, final SearchNode replacement) {
        final List<SearchNode> all = Lists.array();
        all.addAll(parent.children());
        all.set(match.index(), replacement);

        this.set(parent.setChildren(all));
    }

    void set(final SearchNode node) {
        this.result = node.root();
    }

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
