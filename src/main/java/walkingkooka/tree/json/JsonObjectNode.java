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

package walkingkooka.tree.json;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an immutable json object
 */
public final class JsonObjectNode extends JsonParentNode<JsonObjectNodeList> {

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonObjectNode.class);

    /**
     * An empty json object.
     */
    final static JsonObjectNode EMPTY = new JsonObjectNode(NAME, NO_INDEX, JsonObjectNodeList.EMPTY);

    private final static CharacterConstant BEGIN = CharacterConstant.with('{');
    private final static CharacterConstant END = CharacterConstant.with('}');
    private final static Indentation INDENT = Indentation.with("  ");
    private final static String KEY_VALUE_SEPARATOR = ": ";
    private final static String AFTER = ",";

    /**
     * Private ctor use {@link #EMPTY} to start.
     */
    private JsonObjectNode(final JsonNodeName name, final int index, final JsonObjectNodeList children) {
        super(name, index, children);
    }

    /**
     * Makes a copy of the list and sets the parent upon the children.
     */
    @Override
    final JsonObjectNodeList adoptChildren(final JsonObjectNodeList children) {
        final Optional<JsonNode> parent = Optional.of(this);

        final Map<JsonNodeName, JsonNode> nameToValues = Maps.ordered();
        int i = 0;
        for (JsonNode child : children) {
            nameToValues.put(child.name(), child.setParent(parent, child.name, i));
            i++;
        }

        return JsonObjectNodeList.with(nameToValues);
    }

    /**
     * Returns a {@link JsonObjectNode} with no parent but equivalent children.
     */
    @Override
    public JsonObjectNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    boolean childrenEquals(final List<JsonNode> children) {
        final Map<JsonNodeName, JsonNode> nameToValues = this.children.nameToValues;

        boolean equals = nameToValues.size() == children.size();
        if (equals) {

            for (JsonNode child : children) {
                equals = equals && JsonParentNodeChildPredicate.INSTANCE.test(
                        nameToValues.get(child.name),
                        child); // predicate doesnt throw if 1st param is null returns false.
                if (!equals) {
                    break;
                }
            }
        }

        return equals;
    }

    /**
     * Retrieves a property using its name, returning empty if its absent.
     */
    public Optional<JsonNode> get(final JsonNodeName name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(this.children.nameToValues.get(name));
    }

    /**
     * Retrieves a property value or throws a {@link IllegalArgumentException}.
     */
    public JsonNode getOrFail(final JsonNodeName name) {
        return this.get(name).orElseThrow(() -> {
            HasJsonNode.unknownPropertyPresent(name, this);
            return null;
        });
    }

    @Override
    public JsonObjectNode setChild(final JsonNodeName name, final JsonNode value) {
        return this.set(name, value);
    }

    /**
     * Sets a new property or replaces an existing.
     */
    public JsonObjectNode set(final JsonNodeName name, final JsonNode value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        final JsonNode previous = this.children.nameToValues.get(name);
        final JsonNode value2 = value.setName0(name);
        return null != previous ?
                this.setChild(previous, name, value2) :
                this.addChild(name, value2);
    }

    private JsonObjectNode setChild(final JsonNode previous,
                                    final JsonNodeName name,
                                    final JsonNode value) {
        return JsonParentNodeChildPredicate.INSTANCE.test(previous, value) ?
                this :
                this.setChild0(previous.index(), name, value);
    }

    private JsonObjectNode setChild0(final int index,
                                     final JsonNodeName name,
                                     final JsonNode value) {
        final Map<JsonNodeName, JsonNode> children = Maps.ordered();

        int i = 0;
        for (Entry<JsonNodeName, JsonNode> nameAndValue : this.children.nameToValues.entrySet()) {
            if (index == i) {
                children.put(name, value);
            } else {
                children.put(nameAndValue.getKey(), nameAndValue.getValue());
            }
            i++;
        }

        return this.setChildren0(JsonObjectNodeList.with(children)).cast();
    }

    private JsonObjectNode addChild(final JsonNodeName name, final JsonNode value) {
        final Map<JsonNodeName, JsonNode> children = Maps.ordered();
        children.putAll(this.children.nameToValues);
        children.put(name, value.setName0(name));

        return this.replaceChildren(JsonObjectNodeList.with(children)).cast();
    }

    /**
     * Creates a new list of children and replaces the child at the given slot.
     */
    //@Override
    private JsonObjectNode replaceChild0(final JsonNode newChild, final int index) {
        final Map<JsonNodeName, JsonNode> newChildren = Maps.ordered();
        final JsonNodeName newChildName = newChild.name;

        for (Entry<JsonNodeName, JsonNode> nameAndValue : this.children.nameToValues.entrySet()) {
            final JsonNodeName name = nameAndValue.getKey();
            newChildren.put(name,
                    newChildName.equals(name) ?
                            newChild :
                            nameAndValue.getValue());
        }

        return this.replaceChildren(JsonObjectNodeList.with(newChildren)).cast();
    }

    @Override
    public JsonObjectNode setName(final JsonNodeName name) {
        checkName(name);
        return this.setName0(name).cast();
    }

    @Override
    public final JsonObjectNode setChildren(final List<JsonNode> children) {
        Objects.requireNonNull(children, "children");

        final Map<JsonNodeName, JsonNode> copy = Maps.ordered();
        for (JsonNode child : children) {
            copy.put(child.name(), child);
        }

        return this.setChildren0(JsonObjectNodeList.with(copy)).cast();
    }

    @Override
    final JsonNode setChild0(final JsonNode newChild, final int index) {
        return JsonParentNodeChildPredicate.INSTANCE.test(this.children.get(index), newChild) ?
                this :
                this.replaceChild0(newChild, index).children.nameToValues.get(newChild.name);
    }

    /**
     * Returns a {@link JsonObjectNode} without the given key. If they key does not exist the original (this) is returned.
     */
    public JsonObjectNode remove(final JsonNodeName name) {
        Objects.requireNonNull(name);

        final Map<JsonNodeName, JsonNode> copy = Maps.ordered();
        for (JsonNode child : children) {
            copy.put(child.name(), child);
        }
        copy.remove(name);

        return this.setChildren0(JsonObjectNodeList.with(copy))
                .cast();
    }

    /**
     * Tests if the given property is present.
     */
    public boolean contains(final JsonNodeName name) {
        return this.get(name).isPresent();
    }

    @Override
    final JsonObjectNode replace0(final JsonNodeName name, final int index, final JsonObjectNodeList children) {
        return new JsonObjectNode(name, index, children);
    }

    /**
     * Returns a {@link Map} view of the object's properties.
     */
    public Map<JsonNodeName, JsonNode> asMap() {
        return Maps.readOnly(this.children.nameToValues);
    }

    // HasJsonNode...............................................................................................

    @Override
    <T> List<T> fromJsonNodeList0(final Class<T> elementType) {
        return this.reportInvalidNodeArray();
    }

    @Override
    <T> Set<T> fromJsonNodeSet0(final Class<T> elementType) {
        return this.reportInvalidNodeArray();
    }

    @Override
    <K, V> Map<K, V> fromJsonNodeMap0(final Class<K> keyType, final Class<V> valueType) {
        return this.reportInvalidNodeArray();
    }

    @Override
    public <T> List<T> fromJsonNodeWithTypeList() {
        return this.reportInvalidNodeArray();
    }

    @Override
    public <T> Set<T> fromJsonNodeWithTypeSet() {
        return this.reportInvalidNodeArray();
    }

    @Override
    public <K, V> Map<K, V> fromJsonNodeWithTypeMap() {
        return this.reportInvalidNodeArray();
    }

    /**
     * Expects an object with two keys, the first holding the type and the second holding the value.
     * <pre>
     * {
     *     "type": "string",
     *     "value": "abc"
     * }
     * </pre>
     */
    @Override
    public <T> T fromJsonNodeWithType() {
        String type;
        try {
            type = this.getOrFail(TYPE).stringValueOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        return Cast.to(HasJsonNodeMapper.mapperOrFail(type).fromJsonNode(this.getOrFail(VALUE)));
    }

    // @VisibleTesting
    final static JsonNodeName TYPE = JsonNodeName.with("type");

    // @VisibleForTesting
    final static JsonNodeName VALUE = JsonNodeName.with("value");

    @Override
    JsonNodeName defaultName() {
        return NAME;
    }

    // HasSearchNode...............................................................................................

    @Override
    SearchNode toSearchNode0() {
        return SearchNode.sequence(this.children.stream()
                .map(c -> c.toSearchNode().setName(c.name().toSearchNodeName()))
                .collect(Collectors.toList()));
    }

    // isXXX......................................................................................................

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    /**
     * Objects are not an array so fail.
     */
    @Override
    public final JsonArrayNode arrayOrFail() {
        return this.reportInvalidNode("Array");
    }

    @Override
    public JsonObjectNode objectOrFail() {
        return this;
    }

    // JsonNodeVisitor .................................................................................................

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // JsonNode......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonObjectNode;
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    @Override
    boolean equalsDescendants0(final JsonNode child, final JsonObjectNodeList otherChildren, final int i) {
        return child.equalsNameValueAndDescendants(otherChildren.nameToValues.get(child.name));
    }

    @Override
    void printJson0(final IndentingPrinter printer) {
        final LineEnding eol = printer.lineEnding();

        printer.print(BEGIN.string());

        final int size = this.children.size();
        if (size > 0) {
            printer.print(eol);
            printer.indent(INDENT);

            int i = size - 1;
            for (JsonNode child : this.children) {
                printer.print(CharSequences.quoteAndEscape(child.name().value()));
                printer.print(KEY_VALUE_SEPARATOR);
                child.printJson(printer);

                if (i > 0) {
                    printer.print(AFTER);
                }
                i--;
                printer.print(eol);
            }

            printer.outdent();
        }

        printer.print(END.string());
    }
}
