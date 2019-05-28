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
package walkingkooka.text.cursor.parser.json;

import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.search.SearchNode;

import java.util.List;

/**
 * Holds a either true or false boolean value
 */
public final class JsonNodeBooleanParserToken extends JsonNodeValueParserToken<Boolean> {

    static JsonNodeBooleanParserToken with(final boolean value, final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return new JsonNodeBooleanParserToken(value, text);
    }

    private JsonNodeBooleanParserToken(final boolean value, final String text) {
        super(value, text);
    }

    // is ...............................................................................................

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
    final JsonNode toJsonNodeOrNull() {
        return JsonNode.booleanNode(this.value);
    }

    @Override
    void addJsonNode(final List<JsonNode> children) {
        children.add(JsonNode.booleanNode(this.value));
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        return SearchNode.text(this.text(), String.valueOf(this.value()));
    }

    // visitor ...............................................................................................

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object ...............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeBooleanParserToken;
    }
}
