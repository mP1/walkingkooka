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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * A container or parent for one or more {@link SearchNode}.
 */
public final class SearchSelectNode extends SearchParentNode{

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchSelectNode.class);

    static SearchSelectNode with(final SearchNode child) {
        Objects.requireNonNull(child, "child");
        return new SearchSelectNode(NO_PARENT_INDEX, Lists.of(child));
    }

    private SearchSelectNode(final int index, final List<SearchNode> children) {
        super(index, children);
    }

    @Override
    public SearchNodeName name() {
        return NAME;
    }

    @Override
    public SearchSelectNode setChildren(final List<SearchNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    void replaceChildrenCheck(final List<SearchNode> children) {
        final int count = children.size();
        if(1 != count) {
            throw new IllegalArgumentException("Expected only 1 child but got " + count + "=" + children);
        }
    }

    @Override
    SearchParentNode wrap0(final int index, final List<SearchNode> children) {
        return new SearchSelectNode(index, children);
    }

    @Override
    public SearchNode appendChild(final SearchNode child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SearchNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSelect() {
        return true;
    }

    @Override
    public boolean isSequence() {
        return false;
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
        return other instanceof SearchSelectNode;
    }
}
