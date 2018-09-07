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
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A container or parent for one or more {@link SearchNode}, adding tags or meta attributes which can then be queried
 * and matched.
 */
public final class SearchMetaNode extends SearchParentNode {

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchMetaNode.class);

    final static Map<SearchNodeAttributeName, String> NO_ATTRIBUTES = Maps.empty();

    static SearchMetaNode with(final SearchNode child, final Map<SearchNodeAttributeName, String> attributes) {
        Objects.requireNonNull(child, "child");

        return new SearchMetaNode(NO_PARENT_INDEX, Lists.of(child), copy(attributes));
    }

    private static Map<SearchNodeAttributeName, String> copy(final Map<SearchNodeAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        final Map<SearchNodeAttributeName, String> copy = Maps.ordered();
        copy.putAll(attributes);

        if(copy.isEmpty()) {
            throw new IllegalArgumentException("Attributes must not be empty");
        }

        return copy;
    }

    private SearchMetaNode(final int index,
                           final List<SearchNode> children,
                           final Map<SearchNodeAttributeName, String> attributes) {
        super(index, children);
        this.attributes = Maps.readOnly(attributes);
    }

    @Override
    public SearchNodeName name() {
        return NAME;
    }

    public SearchNode child() {
        return this.children().get(0);
    }

    @Override
    public SearchMetaNode setChildren(final List<SearchNode> children) {
        return this.setChildren0(children).cast();
    }

    /**
     * While copying unwraps any {@link SearchIgnoredNode}
     */
    @Override
    final List<SearchNode> copyChildren(final List<SearchNode> children) {
        return copy(children);
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
        return new SearchMetaNode(index, children, this.attributes);
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
    public Map<SearchNodeAttributeName, String> attributes() {
        return this.attributes;
    }

    private final Map<SearchNodeAttributeName, String> attributes;

    /**
     * Would be setter that returns a {@link SearchMetaNode} with the given attributes, or this if attributes also match.
     */
    @Override
    public final SearchMetaNode setAttributes(final Map<SearchNodeAttributeName, String> attributes) {
        final Map<SearchNodeAttributeName, String> copy = copy(attributes);

        return this.attributes.equals(copy) ?
               this :
               new SearchMetaNode(this.index, this.children, copy)
                       .replaceChild(this.parent(), this.index)
                       .cast();
    }

    @Override
    SearchNode replaceAll(final SearchNode replace) {
        return this.replaceAll0(replace).selected();
    }

    @Override
    SearchNode replace0(final int beginOffset, final int endOffset, final SearchNode replace, final String text) {
        return this.child()
                .replace0(beginOffset, endOffset, replace, text)
                .selected();
    }

    @Override
    final SearchNode extract0(final int beginOffset, final int endOffset, final String text) {
        return this.text1(beginOffset, endOffset, text).selected();
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public boolean isMeta() {
        return true;
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public boolean isSequence() {
        return false;
    }

    // SearchQuery ...............................................................................................

    @Override
    void select(final SearchQuery query, final SearchQueryContext context) {
        query.visit(this, context);
        this.child().select(query, context);
    }

    @Override
    public SearchIgnoredNode ignored() {
        return SearchNode.ignored(this.child());
    }

    @Override
    public SearchMetaNode meta(final Map<SearchNodeAttributeName, String> attributes) {
        return this.setAttributes(attributes);
    }

    @Override
    public SearchSelectNode selected() {
        return SearchNode.select(this);
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
        return other instanceof SearchMetaNode;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final SearchNode other) {
        return this.canBeEqual(other) && this.equalsIgnoringParentAndChildren0(other.cast());
    }

    private boolean equalsIgnoringParentAndChildren0(final SearchMetaNode other) {
        return this.attributes.equals(other.attributes);
    }

    @Override
    String toStringPrefix() {
        return "( ";
    }

    @Override
    void toStringSuffix(final StringBuilder b) {
        b.append(" ");
        b.append(this.attributes());
        b.append(')');
    }
}
