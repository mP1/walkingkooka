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
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents an immutable json object
 */
public final class JsonObjectNode extends JsonParentNode{

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonObjectNode.class);

    final static JsonObjectNode EMPTY = new JsonObjectNode(NAME, NO_PARENT_INDEX, Lists.empty());

    final static CharacterConstant BEGIN = CharacterConstant.with('{');
    final static CharacterConstant END = CharacterConstant.with('}');
    private final static Indentation INDENT = Indentation.with("  ");
    final static String KEY_VALUE_SEPARATOR = ": ";
    private final static String AFTER = ",";

    /**
     * Private ctor use {@link #EMPTY} to start.
     */
    private JsonObjectNode(final JsonNodeName name, final int index, final List<JsonNode> children) {
        super(name, index, children);
    }

    /**
     * Retrieves a property using its name, returning empty if its absent.
     */
    public Optional<JsonNode> get(final JsonNodeName name) {
        Objects.requireNonNull(name, "name");

        return this.children.stream()
                .filter(c -> c.name().equals(name))
                .findFirst();
    }

    /**
     * Sets a new property or replaces an existing.
     */
    public JsonObjectNode set(final JsonNodeName name, final JsonNode value) {
        Objects.requireNonNull(value, "value");

        final Optional<JsonNode> previous = this.get(name);
        return previous.isPresent() ?
               this.setChild(previous.get(), name, value) :
               this.addChild(name, value);
    }

    private JsonObjectNode setChild(final JsonNode previous, final JsonNodeName name, final JsonNode value) {
        return previous.equals(value) ?
               this :
               this.setChild0(previous.index(), name, value);
    }

    private JsonObjectNode setChild0(final int index, final JsonNodeName name, final JsonNode value) {
        final List<JsonNode> children = this.copyChildren();
        children.set(index, value.setName(name));

        return this.setChildren1(children).cast();
    }

    private JsonObjectNode addChild(final JsonNodeName name, final JsonNode value) {
        final List<JsonNode> children = this.copyChildren();
        children.add(value.setName(name));

        return this.replaceChildren(children).cast();
    }

    @Override
    public final JsonObjectNode setChildren(final List<JsonNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children).cast();
    }

    final JsonNode setChildren0(final List<JsonNode> children) {
        final List<JsonNode> copy = Lists.array();
        copy.addAll(children);

        return this.setChildren1(copy);
    }

    /**
     * Returns a {@link JsonObjectNode} without the given key. If they key does not exist the original (this) is returned.
     */
    public JsonObjectNode remove(final JsonNodeName name) {
        Objects.requireNonNull(name);

        return this.setChildren(this.children().stream()
                .filter(n -> n.name().equals(name))
                .collect(Collectors.toList()));
    }

    /**
     * Tests if the given property is present.
     */
    public boolean contains(final JsonNodeName name) {
        return this.get(name).isPresent();
    }

    @Override
    final JsonObjectNode wrap0(final JsonNodeName name, final int index, final List<JsonNode> children) {
        return new JsonObjectNode(name, index, children);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
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
        return other instanceof JsonObjectNode;
    }

    @Override
    void prettyPrint(final IndentingPrinter printer) {
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
                child.prettyPrint(printer);

                if(i > 0) {
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
