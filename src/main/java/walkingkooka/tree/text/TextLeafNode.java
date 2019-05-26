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

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A leaf node, where a leaf has no children, but will have a value.
 */
abstract class TextLeafNode<V> extends TextNode implements Value<V> {

    /**
     * Package private to limit sub classing.
     */
    TextLeafNode(final int index, final V value) {
        super(index);
        this.value = value;
    }

    @Override
    public final V value() {
        return this.value;
    }

    final V value;

    final TextLeafNode<V> setValue0(final V value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    final TextLeafNode<V> replaceValue(final V value) {
        return this.replace1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    final TextNode replace(final int index) {
        return this.replace0(index);
    }

    final TextLeafNode replace0(final int index) {
        return this.replace1(index, this.value);
    }

    abstract TextLeafNode replace1(final int index, final V value);

    // children.........................................................................................................

    @Override
    public final List<TextNode> children() {
        return Lists.empty();
    }

    /**
     * By definition a leaf node has no children.
     */
    @Override
    public final TextNode setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    @Override
    final TextNode setChild(final TextNode newChild) {
        return NeverError.unexpectedMethodCall(this, "setChild", newChild);
    }

    // is ..............................................................................................................

    @Override
    public Map<TextPropertyName<?>, Object> attributes() {
        return Maps.empty();
    }

    @Override
    public TextNode setAttributes(Map<TextPropertyName<?>, Object> attributes) {
        throw new UnsupportedOperationException();
    }

    // is ..............................................................................................................

    @Override
    public final boolean isProperties() {
        return false;
    }

    @Override
    public final boolean isStyled() {
        return false;
    }

    // Object ..........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    final boolean equalsDescendants0(final TextNode other) {
        return true;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final TextNode other) {
        return other instanceof TextLeafNode &&
                equalsIgnoringParentAndChildren0(Cast.to(other));

    }

    private boolean equalsIgnoringParentAndChildren0(final TextLeafNode other) {
        return this.value.equals(other.value);
    }
}
