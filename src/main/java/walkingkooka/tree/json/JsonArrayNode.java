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

package walkingkooka.tree.json;

import walkingkooka.collect.list.Lists;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents an immutable json array
 */
public final class JsonArrayNode extends JsonParentNode<List<JsonNode>>{

    /**
     * Simply returns the given {@link JsonNode}.
     */
    static JsonArrayNode fromJsonNode0(final JsonNode node) {
        return node.cast();
    }

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonArrayNode.class);

    final static JsonArrayNode EMPTY = new JsonArrayNode(NAME, NO_INDEX, Lists.empty());

    final static CharacterConstant BEGIN = CharacterConstant.with('[');
    final static CharacterConstant END = CharacterConstant.with(']');

    private JsonArrayNode(final JsonNodeName name, final int index, final List<JsonNode> children) {
        super(name, index, children);
    }

    /**
     * Makes a copy of the list and sets the parent upon the children.
     */
    @Override
    final List<JsonNode> adoptChildren(final List<JsonNode> children) {
        final Optional<JsonNode> parent = Optional.of(this);

        final List<JsonNode> copy = Lists.array();
        int i = 0;
        for(JsonNode child : children) {
            copy.add(child.setParent(parent,
                    JsonNodeName.index(i),
                    i));
            i++;
        }

        return copy;
    }

    @Override
    public JsonArrayNode setName(final JsonNodeName name) {
        checkName(name);
        return this.setName0(name).cast();
    }

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public final JsonArrayNode setChildren(final List<JsonNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children).cast();
    }

    /**
     * Compares the two lists, with a custom {@link java.util.function.BiPredicate} to compare child/elements.
     */
    @Override
    boolean childrenEquals(final List<JsonNode> children) {
        return Lists.equals(this.children, children, JsonParentNodeChildPredicate.INSTANCE);
    }

    /**
     * Retrieves the element at the provided index.
     */
    public JsonNode get(final int index) {
        return this.children().get(index);
    }

    /**
     * Sets or replaces the element at the given index, returning a new array if necessary.
     */
    public JsonArrayNode set(final int index, final JsonNode element) {
        Objects.requireNonNull(element, "element");

        final List<JsonNode> children = this.copyChildren();
        children.set(index, element);
        return this.setChildren0(children).cast();
    }

    /**
     * Appends the given element returning a new instance.
     */
    @Override
    public JsonArrayNode appendChild(final JsonNode element) {
        Objects.requireNonNull(element, "element");

        final List<JsonNode> children = this.copyChildren();
        children.add(element);

        return this.create(this.name, this.index, children).cast();
    }

    /**
     * Removes the child at the given index.
     */
    public JsonArrayNode remove(final int index) {
        final List<JsonNode> children = this.copyChildren();
        children.remove(index);

        return this.create(this.name, this.index, children);
    }

    /**
     * Creates a new list of children and replaces the child at the given slot, returning the new child.
     */
    @Override
    final JsonNode setChild(final JsonNode newChild, final int index) {
        final List<JsonNode> newChildren = this.copyChildren();
        newChildren.set(index, newChild);

        return this.replaceChildren(newChildren).children().get(index);
    }

    @Override
    JsonArrayNode create(final JsonNodeName name, final int index, final List<JsonNode> children) {
        return new JsonArrayNode(name, index, children);
    }

    /**
     * Creates a defensive copy of all children.
     */
    private List<JsonNode> copyChildren() {
        final List<JsonNode> copy = Lists.array();
        copy.addAll(this.children);
        return copy;
    }

    // HasSearchNode............................................................................................................

    @Override
    SearchNode toSearchNode0() {
        return SearchNode.sequence(this.children.stream()
                .map(c -> c.toSearchNode())
                .collect(Collectors.toList()));
    }

    // isXXX............................................................................................................

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    /**
     * Arrays are not an object.
     */
    @Override
    public final JsonObjectNode objectOrFail() {
        return this.reportInvalidNode(Object.class);
    }

    @Override
    public void accept(final JsonNodeVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonArrayNode;
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    @Override
    boolean equalsDescendants0(final JsonNode child, final List<JsonNode> otherChildren, final int i) {
        return child.equalsNameValueAndDescendants(otherChildren.get(i));
    }

    @Override
    void printJson0(final IndentingPrinter printer) {
        printer.print(BEGIN.string());

        String separator = "";
        for(JsonNode child : this.children) {
            printer.print(separator);
            separator = ", ";

            child.printJson(printer);
        }

        printer.print(END.string());
    }
}
