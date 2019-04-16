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

package walkingkooka.tree.patch;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.visit.Visitor;

/**
 * A visitor used by all {@link NodePatch} sub classes to build a {@link NodePatch} from a {@link JsonNode}.
 */
abstract class NodePatchJsonObjectNodePropertyVisitor extends Visitor<JsonNode> {

    NodePatchJsonObjectNodePropertyVisitor(final JsonNode patch) {
        super();
        this.patch = patch;
    }

    @Override
    public final void accept(final JsonNode node) {
        this.accept0(node.objectOrFail());
    }

    private void accept0(final JsonObjectNode node) {
        for(JsonNode property : node.children()) {
            final JsonNodeName propertyName = property.name();

            switch(propertyName.value()) {
                case NodePatch.OP:
                    break;
                case NodePatch.PATH_NAME_TYPE:
                    this.visitPathNameType(property);
                    break;
                case NodePatch.FROM:
                    this.visitFrom(stringOrFail(property, propertyName));
                    break;
                case NodePatch.PATH:
                    this.visitPath(stringOrFail(property, propertyName));
                    break;
                case NodePatch.VALUE_TYPE:
                    this.visitValueType(property);
                    break;
                case NodePatch.VALUE:
                    this.visitValue(property);
                    break;
                default:
                    HasJsonNode.unknownPropertyPresent(propertyName, property.removeParent());
            }
        }
    }

    // PATH COMPONENT TYPE .............................................................................................

    final void visitPathNameType(final JsonNode pathNameType) {
        this.pathNameType = typeOrFail(pathNameType, NodePatch.PATH_NAME_TYPE_PROPERTY);
    }

    private Class<?> pathNameType;

    final Class<Name> pathNameTypeOrFail() {
        return Cast.to(propertyOrFail(this.pathNameType, NodePatch.PATH_NAME_TYPE_PROPERTY));
    }

    // FROM ........................................................................................................

    abstract void visitFrom(final JsonStringNode from);

    // PATH ........................................................................................................

    final void visitPath(final JsonStringNode path) {
        this.path = path;
    }

    final NodePointer<?, ?> pathOrFail() {
        return this.pathOrFail0(this.path, NodePatch.PATH_PROPERTY);
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    private JsonStringNode path;

    /**
     * Creates a {@link NodePointer} from the {@link JsonStringNode} using the property name in any error messages.
     */
    final NodePointer<?, ?> pathOrFail0(final JsonStringNode node,
                                        final JsonNodeName property) {
        try {
            return NodePointer.parse(node.value(),
                    this::nameFactory,
                    Node.class);
        } catch (final RuntimeException cause) {
            throw new IllegalArgumentException("Invalid " + property + " in " + this.patch);
        }
    }

    /**
     * Factory that uses type in {@link NodePatch#PATH_NAME_TYPE_PROPERTY} to create a {@link Name}.
     */
    private Name nameFactory(final String value) {
        return JsonNode.string(value).fromJsonNode(this.pathNameTypeOrFail());
    }

    // VALUE ........................................................................................................

    abstract void visitValueType(final JsonNode valueType);

    abstract void visitValue(final JsonNode value);

    // HELPER ...........................................................................................................

    /**
     * Helper that fails if the value is null using the property name in the message detail.
     */
    <T> T propertyOrFail(final T value, final JsonNodeName property) {
        if(null==value) {
            throw new IllegalArgumentException("Required property " + property + " missing " + this.patch);
        }
        return value;
    }

    /**
     * Reports that an unknown property is present.
     */
    final void unknownPropertyPresent(final JsonNodeName property) {
        HasJsonNode.unknownPropertyPresent(property, this.patch);
    }

    /**
     * The json object representing the patch.
     */
    final JsonNode patch;

    @Override
    public final String toString() {
        return this.patch.toString();
    }

    // helpers....................................................................................................

    static JsonStringNode stringOrFail(final JsonNode node, final JsonNodeName property) {
        if(!node.isString()) {
            throw new IllegalArgumentException(property + " is not a String with type=" + node);
        }
        return node.cast();
    }

    /**
     * Returns the {@link Class} assuming the node is a string or fails throwing a {@link JsonNodeException}
     */
    static Class<?> typeOrFail(final JsonNode node, final JsonNodeName property) {
        return HasJsonNode.registeredType(stringOrFail(node, property))
                .orElseThrow(() -> new IllegalArgumentException("Type doesnt support from Json: " + node));
    }
}
