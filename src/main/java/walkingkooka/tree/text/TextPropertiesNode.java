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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
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
        return new TextPropertiesNode(NO_INDEX,
                copy(children),
                NO_ATTRIBUTES);
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
        final Map<TextPropertyName<?>, Object> copy = TextPropertiesMap.with(attributes);
        return copy.isEmpty() ?
                this :
                this.setAttributes0(attributes);
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
        Map<TextPropertyName<?>, Object> properties = NO_ATTRIBUTES;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case PROPERTIES:
                    properties = propertiesFromJson(child.objectOrFail());
                    break;
                case VALUES:
                    children = child.arrayOrFail().fromJsonNodeWithTypeList();
                    break;
                default:
                    NeverError.unhandledCase(child, PROPERTIES_PROPERTY, VALUES_PROPERTY);
            }
        }

        return TextPropertiesNode.with(children)
                .setAttributes(properties);
    }

    private static Map<TextPropertyName<?>, Object> propertiesFromJson(final JsonObjectNode json) {
        final Map<TextPropertyName<?>, Object> properties = Maps.ordered();

        for (JsonNode child : json.children()) {
            final TextPropertyName name = TextPropertyName.fromJsonNodeName(child);
            properties.put(name,
                    name.handler.fromJsonNode(child));
        }

        return properties;
    }

    @Override
    public JsonNode toJsonNode() {
        JsonObjectNode json = JsonNode.object();
        if (!this.attributes.isEmpty()) {
            json = json.set(PROPERTIES_PROPERTY, this.propertiesToJson());
        }

        return this.addChildrenValuesJson(json);
    }

    /**
     * Creates an object where the {@link TextPropertyName} becomes the json property name, and the value is turned into json.
     * If there are no properties defined, then an empty object will be returned.
     */
    private JsonNode propertiesToJson() {
        final List<JsonNode> json = Lists.array();

        for (Entry<TextPropertyName<?>, Object> propertyAndValue : this.attributes.entrySet()) {
            final TextPropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.toJsonNode(Cast.to(propertyAndValue.getValue()));

            json.add(value.setName(propertyName.toJsonNodeName()));
        }

        return JsonNode.object()
                .setChildren(json);
    }

    final static String PROPERTIES = "properties";
    final static JsonNodeName PROPERTIES_PROPERTY = JsonNodeName.with(PROPERTIES);

    static {
        HasJsonNode.register("text-properties", TextPropertiesNode::fromJsonNode, TextPropertiesNode.class);
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
