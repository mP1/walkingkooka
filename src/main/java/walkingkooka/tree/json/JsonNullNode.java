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

import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.tree.search.SearchNode;

/**
 * Represents a json null.
 */
public final class JsonNullNode extends JsonLeafNode<Void> {

    private final static JsonNodeName NAME = JsonNodeName.fromClass(JsonNullNode.class);

    /**
     * Singleton
     */
    final static JsonNullNode INSTANCE = new JsonNullNode(NAME, NO_INDEX, null);

    /**
     * Private ctor use singleton
     */
    private JsonNullNode(final JsonNodeName name, final int index, final Void value) {
        super(name, index, value);
    }

    @Override
    public JsonNullNode setName(final JsonNodeName name) {
        checkName(name);
        return this.setName0(name).cast();
    }

    /**
     * Always returns this, only included for completeness.
     */
    public JsonNullNode setValue(final Void value) {
        return this;
    }

    /**
     * Returns the singleton which doesnt have a parent.
     */
    @Override
    public JsonNullNode removeParent() {
        return INSTANCE;
    }

    @Override
    JsonNullNode replace0(final JsonNodeName name, final int index, final Void value) {
        return new JsonNullNode(name, index, value);
    }

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
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    // HasText......................................................................................................

    @Override
    public final String text() {
        return NULL;
    }

    private final static String NULL = "null";

    // Object......................................................................................................

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    final boolean equalsDescendants(final JsonNode other) {
        return true;
    }

    @Override
    final boolean equalsValue(final JsonNode other) {
        return true;
    }

    // Visitor .................................................................................................

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNullNode;
    }

    @Override
    void printJson0(final IndentingPrinter printer) {
        printer.print(String.valueOf(this.value));
    }
}
