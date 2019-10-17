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

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * The base {@link SearchNode} for all leaf values that hold a value.
 */
abstract class SearchLeafNode<V> extends SearchNode implements Value<V> {

    static <V> void check(final String text, final V value) {
        Objects.requireNonNull(text, "text");
        checkValue(value);
    }

    SearchLeafNode(final int index, final SearchNodeName name, final String text, final V value) {
        super(index, name);
        this.text = text;
        this.value = value;
    }

    public final String text() {
        return this.text;
    }

    private final String text;

    @Override
    final void appendText(final StringBuilder b) {
        b.append(this.text);
    }

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    /**
     * Would be value setter, that returns an instance with the given value creating a new instance if necessary.
     */
    abstract public SearchLeafNode<V> setValue(final V value);

    final SearchLeafNode<V> setValue0(final V value) {
        checkValue(value);
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    final SearchLeafNode<V> replaceValue(final V value) {
        final int index = this.index();

        return this.replace0(index, this.name, this.text, value)
                .replaceChild(this.parent(), index)
                .cast();
    }

    private static void checkValue(final Object value) {
        Objects.requireNonNull(value, "value");
    }

    @Override
    final SearchNode replace(final int index, final SearchNodeName name) {
        return this.replace0(index, name, this.text, this.value);
    }

    /**
     * Sub classes of {@link SearchLeafNode} should call this and cast.
     */
    final SearchLeafNode removeParent1() {
        return this.replace0(NO_INDEX, this.defaultName(), this.text, this.value);
    }

    abstract SearchLeafNode replace0(final int index, final SearchNodeName name, final String text, final V value);

    @Override
    final SearchNode replaceAll(final SearchNode replace) {
        return this.replaceAll0(replace);
    }

    @Override
    final SearchNode replace0(final int beginOffset, final int endOffset, final SearchNode replace, final String text) {
        final int textLength = text.length();

        return this.replaceAll(0 == beginOffset ?
                sequence(Lists.of(replace, text0(text.substring(endOffset)))) :
                sequence(
                        endOffset == textLength ?
                                Lists.of(text0(text.substring(0, beginOffset)),
                                        replace) : // nothing after
                                Lists.of(
                                        text0(text.substring(0, beginOffset)),
                                        replace,
                                        text0(text.substring(endOffset)))
                )
        );
    }

    @Override
    final SearchNode extract0(final int beginOffset, final int endOffset, final String text) {
        return this.text1(beginOffset, endOffset, text);
    }

    @Override
    public final List<SearchNode> children() {
        return NO_CHILDREN;
    }

    public final SearchNode setChildren(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    @Override
    final SearchNode setChild(final SearchNode newChild, final int index) {
        throw new UnsupportedOperationException();
    }

    // attributes........................................................................................

    @Override
    public final Map<SearchNodeAttributeName, String> attributes() {
        return Maps.empty();
    }

    @Override
    final SearchMetaNode setAttributes0(final Map<SearchNodeAttributeName, String> attributes) {
        return SearchMetaNode.with0(this, attributes);
    }

    // Select...........................................................................................

    @Override
    public final SearchIgnoredNode ignored() {
        return SearchNode.ignored(this);
    }

    @Override
    public final SearchSelectNode selected() {
        return SearchNode.select(this);
    }

    /**
     * By definition {@link SearchLeafNode} never have a child so theres nothing to replace.
     */
    @Override
    final SearchNode replaceSelected0(final Function<SearchSelectNode, SearchNode> replacer) {
        return this;
    }

    // Object....................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    final boolean equalsDescendants0(final SearchNode other) {
        return true;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren0(final SearchNode other) {
        return other instanceof SearchLeafNode && this.equalsIgnoringParentAndChildren1(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren1(final SearchLeafNode<?> other) {
        return this.text.equals(other.text) &&
                this.value.equals(other.value);
    }

    @Override
    final String toStringNameSuffix() {
        return "=";
    }

    abstract void toString1(final StringBuilder b);
}
