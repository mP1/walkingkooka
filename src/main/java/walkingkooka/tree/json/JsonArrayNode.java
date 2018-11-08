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
import java.util.stream.Collectors;

/**
 * Represents an immutable json array
 */
public final class JsonArrayNode extends JsonParentNode{

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonArrayNode.class);

    final static JsonArrayNode EMPTY = new JsonArrayNode(NAME, NO_PARENT_INDEX, Lists.empty());

    final static CharacterConstant BEGIN = CharacterConstant.with('[');
    final static CharacterConstant END = CharacterConstant.with(']');

    private JsonArrayNode(final JsonNodeName name, final int index, final List<JsonNode> children) {
        super(name, index, children);
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

    final JsonNode setChildren0(final List<JsonNode> children) {
        int i = 0;
        final List<JsonNode> copy = Lists.array();
        for(JsonNode child : children){
            copy.add(child.setName0(JsonNodeName.index(i)));
            i++;
        }

        return this.setChildren1(copy);
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
        children.set(index, element.setIndexAndName(index));

        return this.setChildren0(children).cast();
    }

    /**
     * Appends the given element returning a new instance.
     */
    @Override
    public JsonArrayNode appendChild(final JsonNode element) {
        Objects.requireNonNull(element, "element");

        final List<JsonNode> children = this.copyChildren();
        children.add(element.setIndexAndName(children.size()));

        return this.wrap0(this.name, this.index, children).cast();
    }

    public JsonArrayNode remove(final int index) {
        final List<JsonNode> children = this.copyChildren();
        children.remove(index);

        return this.wrap0(this.name, this.index, children).cast();
    }

    @Override
    JsonArrayNode wrap0(final JsonNodeName name, final int index, final List<JsonNode> children) {
        return new JsonArrayNode(name, index, children);
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
