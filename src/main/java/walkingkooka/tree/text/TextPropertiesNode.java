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
 * Represents a collection properties that apply upon a list of {@link TextNode children}.
 */
public final class TextPropertiesNode extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.fromClass(TextPropertiesNode.class);

    final static TextPropertiesMap NO_ATTRIBUTES_MAP = TextPropertiesMap.with(Maps.empty());

    /**
     * Factory that creates a {@link TextPropertiesNode} with the given children and properties.
     */
    // TextProperties.setTextNodes
    static TextPropertiesNode with(final List<TextNode> children,
                                   final TextPropertiesMap properties) {
        return new TextPropertiesNode(NO_INDEX,
                copy(children),
                properties);
    }

    /**
     * Private ctor
     */
    private TextPropertiesNode(final int index,
                               final List<TextNode> children,
                               final TextPropertiesMap attributes) {
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
    TextNode setAttributesEmptyTextPropertiesMap() {
        return this.setAttributesTextPropertiesMap(TextPropertiesMap.EMPTY);
    }

    @Override
    final TextPropertiesNode setAttributesNonEmptyTextPropertiesMap(final TextPropertiesMap textPropertiesMap) {
        return this.setAttributesTextPropertiesMap(textPropertiesMap);
    }

    private TextPropertiesNode setAttributesTextPropertiesMap(final TextPropertiesMap textPropertiesMap) {
        return this.attributes.equals(textPropertiesMap) ?
                this :
                this.replaceAttributes(textPropertiesMap);
    }

    /**
     * Create a new {@link TextPropertyName}.
     */
    private TextPropertiesNode replaceAttributes(final TextPropertiesMap attributes) {
        return new TextPropertiesNode(this.index, this.children, attributes);
    }

    private final TextPropertiesMap attributes;

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
    public boolean isStyleName() {
        return false;
    }

    // HasJsonNode.....................................................................................................

    /**
     * Accepts a json object which holds a {@link TextPropertiesNode}.
     */
    public static TextPropertiesNode fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return fromJsonNode0(node.objectOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static TextPropertiesNode fromJsonNode0(final JsonObjectNode node) {
        TextProperties textProperties = TextProperties.EMPTY;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case PROPERTIES:
                    textProperties = TextProperties.withTextPropertiesMap(TextPropertiesMap.fromJson(child));
                    break;
                case VALUES:
                    children = child.arrayOrFail().fromJsonNodeWithTypeList();
                    break;
                default:
                    NeverError.unhandledCase(child, PROPERTIES_PROPERTY, VALUES_PROPERTY);
            }
        }

        return textProperties.setChildren(children);
    }

    @Override
    public JsonNode toJsonNode() {
        JsonObjectNode json = JsonNode.object();
        if (!this.attributes.isEmpty()) {
            json = json.set(PROPERTIES_PROPERTY, this.attributes.toJson());
        }

        return this.addChildrenValuesJson(json);
    }

    final static String PROPERTIES = "properties";
    final static JsonNodeName PROPERTIES_PROPERTY = JsonNodeName.with(PROPERTIES);

    static {
        HasJsonNode.register("text-properties-node", TextPropertiesNode::fromJsonNode, TextPropertiesNode.class);
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
        return other instanceof TextPropertiesNode &&
                this.equalsIgnoringParentAndChildren0(Cast.to(other));
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
