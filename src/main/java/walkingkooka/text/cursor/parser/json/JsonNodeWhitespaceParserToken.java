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
import walkingkooka.tree.search.SearchNode;

import java.util.List;
import java.util.Optional;

/**
 * Holds the combination of whitespace or comments.
 */
public final class JsonNodeWhitespaceParserToken extends JsonNodeLeafParserToken2<String> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeWhitespaceParserToken.class);

    static JsonNodeWhitespaceParserToken with(final String value, final String text){
        checkValue(value);
        CharSequences.failIfNullOrEmpty(text, "text");

        return new JsonNodeWhitespaceParserToken(value, text);
    }

    private JsonNodeWhitespaceParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    public JsonNodeWhitespaceParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeWhitespaceParserToken replaceText(final String text) {
        return new JsonNodeWhitespaceParserToken(this.value, text);
    }

    @Override
    public Optional<JsonNodeParserToken> withoutSymbolsOrWhitespace() {
        return Optional.empty();
    }

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
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return true;
    }

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    JsonNode toJsonNodeOrNull() {
        return null;
    }

    @Override
    void addJsonNode(final List<JsonNode> children) {
        // skip whitespace
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeWhitespaceParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public boolean isNoise() {
        return true;
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode()  {
        return SearchNode.text(this.text(), this.value());
    }
}
