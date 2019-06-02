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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a collection of style properties that apply upon a list of {@link TextNode children}.
 */
public final class TextStyleNode extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.fromClass(TextStyleNode.class);

    final static TextStyleMap NO_ATTRIBUTES_MAP = TextStyleMap.with(Maps.empty());

    /**
     * Factory that creates a {@link TextStyleNode} with the given children and style.
     */
    // TextStyle.setTextNodes
    static TextStyleNode with(final List<TextNode> children,
                              final TextStyleMap properties) {
        return new TextStyleNode(NO_INDEX,
                copy(children),
                properties);
    }

    /**
     * Private ctor
     */
    private TextStyleNode(final int index,
                          final List<TextNode> children,
                          final TextStyleMap attributes) {
        super(index, children);
        this.attributes = attributes;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Returns a {@link TextStyleNode} with no parent but equivalent children.
     */
    @Override
    public TextStyleNode removeParent() {
        return this.removeParent0().cast();
    }

    // children.........................................................................................................

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public TextStyleNode setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children).cast();
    }

    @Override
    public TextStyleNode appendChild(final TextNode child) {
        return super.appendChild(child).cast();
    }

    @Override
    public TextStyleNode replaceChild(final TextNode oldChild, final TextNode newChild) {
        return super.replaceChild(oldChild, newChild).cast();
    }

    @Override
    public TextStyleNode removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    // attributes.......................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> attributes() {
        return this.attributes;
    }

    @Override
    TextNode setAttributesEmptyTextPropertiesMap() {
        return this.setAttributesTextPropertiesMap(TextStyleMap.EMPTY);
    }

    @Override
    final TextStyleNode setAttributesNonEmptyTextPropertiesMap(final TextStyleMap textStyleMap) {
        return this.setAttributesTextPropertiesMap(textStyleMap);
    }

    private TextStyleNode setAttributesTextPropertiesMap(final TextStyleMap textStyleMap) {
        return this.attributes.equals(textStyleMap) ?
                this :
                this.replaceAttributes(textStyleMap);
    }

    /**
     * Create a new {@link TextStylePropertyName}.
     */
    private TextStyleNode replaceAttributes(final TextStyleMap attributes) {
        return new TextStyleNode(this.index, this.children, attributes);
    }

    private final TextStyleMap attributes;

    // replace.........................................................................................................

    @Override
    TextStyleNode replace0(final int index,
                           final List<TextNode> children) {
        return new TextStyleNode(index, children, this.attributes);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isStyle() {
        return true;
    }

    @Override
    public boolean isStyleName() {
        return false;
    }

    // HasJsonNode.....................................................................................................

    /**
     * Accepts a json object which holds a {@link TextStyleNode}.
     */
    public static TextStyleNode fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return fromJsonNode0(node.objectOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static TextStyleNode fromJsonNode0(final JsonObjectNode node) {
        TextStyle textStyle = TextStyle.EMPTY;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case STYLE:
                    textStyle = TextStyle.withTextPropertiesMap(TextStyleMap.fromJson(child));
                    break;
                case VALUES:
                    children = child.arrayOrFail().fromJsonNodeWithTypeList();
                    break;
                default:
                    NeverError.unhandledCase(child, STYLE_PROPERTY, VALUES_PROPERTY);
            }
        }

        return textStyle.setChildren(children);
    }

    @Override
    public JsonNode toJsonNode() {
        JsonObjectNode json = JsonNode.object();
        if (!this.attributes.isEmpty()) {
            json = json.set(STYLE_PROPERTY, this.attributes.toJson());
        }

        return this.addChildrenValuesJson(json);
    }

    final static String STYLE = "style";
    final static JsonNodeName STYLE_PROPERTY = JsonNodeName.with(STYLE);

    static {
        HasJsonNode.register("text-style-node", TextStyleNode::fromJsonNode, TextStyleNode.class);
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
        return other instanceof TextStyleNode;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final TextNode other) {
        return other instanceof TextStyleNode &&
                this.equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final TextStyleNode other) {
        return this.attributes.equals(other.attributes);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        final Map<TextStylePropertyName<?>, Object> attributes = this.attributes;
        if(!attributes.isEmpty()) {
            //b.valueSeparator(", ");
            b.surroundValues("{", "}");
            b.value(attributes);
        }
    }
}
