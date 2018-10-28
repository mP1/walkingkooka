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

import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.search.SearchNode;

import java.util.List;

/**
 * Holds a single integer or decimal number.
 */
public final class JsonNodeNumberParserToken extends JsonNodeValueParserToken<Double> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeNumberParserToken.class);

    static JsonNodeNumberParserToken with(final double value, final String text) {
        checkText(text);

        return new JsonNodeNumberParserToken(value, text);
    }

    private JsonNodeNumberParserToken(final double value, final String text) {
        super(value, text);
    }

    @Override
    public JsonNodeNumberParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeNumberParserToken replaceText(final String text) {
        return new JsonNodeNumberParserToken(this.value, text);
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
        return false;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    JsonNode toJsonNodeOrNull() {
        return JsonNode.number(this.value());
    }

    @Override
    void addJsonNode(final List<JsonNode> children) {
        children.add(JsonNode.number(value()));
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        return SearchNode.doubleNode(this.text(), this.value());
    }

    // visitor ...............................................................................................

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object ...............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeNumberParserToken;
    }
}
