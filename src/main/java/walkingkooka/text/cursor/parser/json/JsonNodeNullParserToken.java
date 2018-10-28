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
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.json.JsonNode;

import java.util.List;

/**
 * Holds a json null value.
 */
public final class JsonNodeNullParserToken extends JsonNodeValueParserToken<Void> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeNullParserToken.class);

    static JsonNodeNullParserToken with(final Void value, final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return new JsonNodeNullParserToken(value, text);
    }

    private JsonNodeNullParserToken(final Void value, final String text) {
        super(value, text);
    }

    @Override
    public JsonNodeNullParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeNullParserToken replaceText(final String text) {
        return new JsonNodeNullParserToken(this.value, text);
    }

    // name ...............................................................................................

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // is ...............................................................................................

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

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override final JsonNode toJsonNodeOrNull() {
        return JsonNode.nullNode();
    }

    @Override
    void addJsonNode(final List<JsonNode> children) {
        children.add(JsonNode.nullNode());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeNullParserToken;
    }
}
