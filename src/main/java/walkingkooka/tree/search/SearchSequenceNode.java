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
import java.util.function.Function;

/**
 * A container or parent for one or more {@link SearchNode}.
 */
public final class SearchSequenceNode extends SearchParentNode2 {

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchSequenceNode.class);

    static SearchSequenceNode with(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");

        return new SearchSequenceNode(NO_INDEX, NAME, children);
    }

    private SearchSequenceNode(final int index, final SearchNodeName name, final List<SearchNode> children) {
        super(index, name, children);
    }

    @Override
    SearchNodeName defaultName() {
        return NAME;
    }

    @Override
    public SearchSequenceNode setName(final SearchNodeName name) {
        return super.setName0(name).cast();
    }

    @Override
    public SearchSequenceNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    List<SearchNode> copyChildren(final List<SearchNode> children) {
        return Lists.immutable(children);
    }

    @Override
    public SearchSequenceNode setChildren(final List<SearchNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    void replaceChildrenCheck(final List<SearchNode> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Expected at least one child");
        }
    }

    @Override
    SearchParentNode replace0(final int index, final SearchNodeName name, final List<SearchNode> children) {
        return new SearchSequenceNode(index, name, children);
    }

    @Override
    SearchNode replaceAll(final SearchNode replace) {
        return this.replaceAll0(replace);
    }

    // ABC DEF GHI
    // begin=0 XY
    // XY A DEF GHI
    // ==========
    // ABC DEF GHI
    // begin = 5 XY
    // ABC D XY GHI
    // ==========

    @Override
    final SearchNode replace0(final int replaceBeginOffset, final int replaceEndOffset, final SearchNode replace, final String ignored) {
        final List<SearchNode> newChildren = Lists.array();
        int textOffset = 0;

        for (SearchNode child : this.children()) {
            final String childText = child.text();
            final int childTextLength = childText.length();

            final int childBeginOffset = textOffset;
            final int childEndOffset = childBeginOffset + childTextLength;

            textOffset = childEndOffset;

            // replace begin is after child, just add child
            if (replaceBeginOffset >= childEndOffset) {
                newChildren.add(child);
                continue;
            }

            // replace end is before child, just add child
            if (replaceEndOffset <= childBeginOffset) {
                newChildren.add(child);
                continue;
            }

            if (childBeginOffset == replaceBeginOffset) {
                // replacement
                if (replaceEndOffset <= childEndOffset) {
                    newChildren.add(
                            child.replace(
                                    0,
                                    replaceEndOffset - childBeginOffset,
                                    replace));

                    continue;
                }

                // replacement probably covers several old children..
                newChildren.add(replace);

                // part of child may remain...
                if (replaceEndOffset < childEndOffset) {
                    newChildren.add(child.extract(replaceEndOffset - childBeginOffset, childEndOffset - childBeginOffset));
                }
                continue;
            }

            if (replaceBeginOffset > childBeginOffset) {
                if (replaceEndOffset <= childEndOffset) {
                    newChildren.add(
                            child.replace(
                                    replaceBeginOffset - childBeginOffset,
                                    replaceEndOffset - childBeginOffset,
                                    replace));

                    continue;
                }

                newChildren.add(child.extract(0, replaceBeginOffset - childBeginOffset));
                newChildren.add(replace);
                continue;
            }

            // replace must have already been added...
            if (replaceEndOffset >= childEndOffset) {
                continue;
            }

            // part of child was not overlapped.
            newChildren.add(child.extract(replaceEndOffset - childBeginOffset, childEndOffset - childBeginOffset));
        }

        return this.setChildren(newChildren);
    }

    @Override
    final SearchNode extract0(final int extractBeginOffset, final int extractEndOffset, final String ignoredText) {
        final List<SearchNode> extracted = Lists.array();
        int textOffset = 0;

        for (SearchNode child : this.children()) {
            final String childText = child.text();
            final int childTextLength = childText.length();

            final int childBeginOffset = textOffset;
            final int childEndOffset = childBeginOffset + childTextLength;

            textOffset = childEndOffset;

            // child is before extract begin
            if (extractBeginOffset >= childEndOffset) {
                continue;
            }

            // child is after extract range stop looking...
            if (childBeginOffset >= extractEndOffset) {
                break;
            }

            extracted.add(
                    child.extract(
                            Math.max(extractBeginOffset - childBeginOffset, 0),
                            Math.min(childEndOffset, extractEndOffset) - childBeginOffset));
        }

        // only wrap multiple extracted nodes in a SearchSequenceNode.
        return extracted.size() == 1 ?
                extracted.get(0) :
                new SearchSequenceNode(NO_INDEX, this.name, extracted);
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public boolean isSequence() {
        return true;
    }

    // SearchQuery ...............................................................................................

    @Override
    void select(final SearchQuery query, final SearchQueryContext context) {
        for (SearchNode child : this.children) {
            child.select(query, context);
        }
    }

    @Override
    public SearchIgnoredNode ignored() {
        return SearchNode.ignored(this);
    }

    @Override
    public SearchSelectNode selected() {
        return SearchNode.select(this);
    }

    /**
     * Loop over all children and perform {@link #replaceSelected(Function)} and then calling setChildren.
     */
    @Override
    SearchNode replaceSelected0(final Function<SearchSelectNode, SearchNode> replacer) {
        final List<SearchNode> children = Lists.array();
        for (SearchNode child : this.children()) {
            children.add(child.replaceSelected0(replacer));
        }

        return this.setChildren(children);
    }

    // Visitor.........................................................................................................

    @Override
    public void accept(final SearchNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
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
