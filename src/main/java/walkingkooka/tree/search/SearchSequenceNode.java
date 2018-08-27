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

import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * A container or parent for one or more {@link SearchNode}.
 */
public final class SearchSequenceNode extends SearchParentNode{

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchSequenceNode.class);

    static SearchSequenceNode with(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");

        final List<SearchNode> copy = copy(children);
        if(copy.isEmpty()) {
            throw new IllegalArgumentException("Expected at least one child");
        }

        return new SearchSequenceNode(NO_PARENT_INDEX, copy);
    }

    private SearchSequenceNode(final int index, final List<SearchNode> children) {
        super(index, children);
    }

    @Override
    public SearchNodeName name() {
        return NAME;
    }

    @Override
    public SearchSequenceNode setChildren(final List<SearchNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    void replaceChildrenCheck(final List<SearchNode> children) {
        if(children.isEmpty()) {
            throw new IllegalArgumentException("Expected at least one child");
        }
    }

    @Override
    SearchParentNode wrap0(final int index, final List<SearchNode> children) {
        return new SearchSequenceNode(index, children);
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public boolean isSequence() {
        return true;
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final SearchNodeVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object.........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchSequenceNode;
    }

    @Override
    String toStringPrefix() {
        return "[ ";
    }

    @Override
    String toStringSuffix() {
        return " ]";
    }
}
