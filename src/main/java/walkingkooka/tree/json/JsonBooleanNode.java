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

import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.tree.search.SearchNode;

/**
 * Represents an immutable json boolean.
 */
public final class JsonBooleanNode extends JsonLeafNode<Boolean>{

    /**
     * Simply returns the given {@link JsonNode}.
     */
    static JsonBooleanNode fromJsonNode0(final JsonNode node) {
        return node.toJsonNode()
                .cast();
    }

    static JsonBooleanNode with(final boolean value) {
        return new JsonBooleanNode(NAME, NO_INDEX, value);
    }

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonBooleanNode.class);

    private JsonBooleanNode(final JsonNodeName name, final int index, final boolean value) {
        super(name, index, value);
    }

    @Override
    public JsonBooleanNode setName(final JsonNodeName name) {
        checkName(name);
        return this.setName0(name).cast();
    }

    public JsonBooleanNode setValue(final boolean value) {
        return this.setValue0(value).cast();
    }

    final JsonBooleanNode create(final JsonNodeName name, final int index, final Boolean value) {
        return new JsonBooleanNode(name, index, value);
    }

    // HasJsonNode...............................................................................................

    @Override
    JsonNodeName defaultName() {
        return NAME;
    }

    // HasSearchNode...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        final String text = this.text();
        return SearchNode.text(text, text);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public void accept(final JsonNodeVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonBooleanNode;
    }

    @Override
    void printJson0(final IndentingPrinter printer) {
        printer.print(String.valueOf(this.value));
    }
}
