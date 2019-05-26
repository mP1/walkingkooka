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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Represents a collection properties that apply upon a list of {@link TextNode children}.
 */
public final class TextPropertiesNode extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.fromClass(TextPropertiesNode.class);

    /**
     * Factory that creates a {@link TextPropertiesNode} with the given children.
     */
    static TextPropertiesNode with(final List<TextNode> children) {
        return new TextPropertiesNode(NO_INDEX, children, NO_ATTRIBUTES);
    }

    /**
     * Private ctor
     */
    private TextPropertiesNode(final int index,
                               final List<TextNode> children,
                               final Map<TextPropertyName<?>, Object> attributes) {
        super(index, children);
        this.attributes = attributes;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Returns a {@link TextPropertiesNode} with no parent but equivalent children.
     */
    @Override
    public TextPropertiesNode removeParent() {
        return this.removeParent0().cast();
    }

    // children.........................................................................................................

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public TextPropertiesNode setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children).cast();
    }

    @Override
    public TextPropertiesNode appendChild(final TextNode child) {
        return super.appendChild(child).cast();
    }

    @Override
    public TextPropertiesNode replaceChild(final TextNode oldChild, final TextNode newChild) {
        return super.replaceChild(oldChild, newChild).cast();
    }

    @Override
    public TextPropertiesNode removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    // attributes.......................................................................................................

    @Override
    public Map<TextPropertyName<?>, Object> attributes() {
        return this.attributes;
    }

    @Override
    public TextPropertiesNode setAttributes(final Map<TextPropertyName<?>, Object> attributes) {
        final Map<TextPropertyName<?>, Object> copy = copyAttributes(attributes);
        return copy.isEmpty() ?
                this :
                this.setAttributes0(attributes);
    }

    /**
     * Makes a defensive copy of the provided attributes.
     */
    private static Map<TextPropertyName<?>, Object> copyAttributes(final Map<TextPropertyName<?>, Object> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        final Map<TextPropertyName<?>, Object> copy = Maps.ordered();
        for (Entry<TextPropertyName<?>, Object> propertyAndValue : attributes.entrySet()) {
            final TextPropertyName<?> propertyName = propertyAndValue.getKey();
            final Object value = propertyAndValue.getValue();

            propertyName.converter.check(value, propertyName);

            copy.put(propertyName, value);
        }

        return copy;
    }

    /**
     * Most classes except for {@link TextPropertyName} create a new {@link TextPropertyName}.
     */
    private TextPropertiesNode setAttributes0(final Map<TextPropertyName<?>, Object> attributes) {
        return new TextPropertiesNode(this.index, this.children, attributes);
    }

    private final Map<TextPropertyName<?>, Object> attributes;

    // replace.........................................................................................................

    @Override
    TextPropertiesNode replace0(final int index,
                                final List<TextNode> children) {
        return new TextPropertiesNode(index, children, this.attributes);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isProperties() {
        return true;
    }

    @Override
    public boolean isStyled() {
        return false;
    }

    // Visitor .................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextPropertiesNode;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final TextNode other) {
        return this.equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final TextPropertiesNode other) {
        return this.attributes.equals(other.attributes);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        final Map<TextPropertyName<?>, Object> attributes = this.attributes;
        if(!attributes.isEmpty()) {
            //b.valueSeparator(", ");
            b.surroundValues("{", "}");
            b.value(attributes);
        }
    }
}
