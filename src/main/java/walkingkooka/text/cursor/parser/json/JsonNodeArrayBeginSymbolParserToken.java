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

/**
 * Represents a open array (parens) symbol token.
 */
public final class JsonNodeArrayBeginSymbolParserToken extends JsonNodeSymbolParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeArrayBeginSymbolParserToken.class);

    static JsonNodeArrayBeginSymbolParserToken with(final String value, final String text){
        checkValue(value);
        checkText(text);

        return new JsonNodeArrayBeginSymbolParserToken(value, text);
    }

    private JsonNodeArrayBeginSymbolParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    public JsonNodeArrayBeginSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeArrayBeginSymbolParserToken replaceText(final String text) {
        return new JsonNodeArrayBeginSymbolParserToken(this.value, text);
    }

    @Override
    public boolean isArrayBeginSymbol() {
        return true;
    }

    @Override
    public boolean isArrayEndSymbol() {
        return false;
    }

    @Override
    public boolean isObjectAssignmentSymbol() {
        return false;
    }

    @Override
    public boolean isObjectBeginSymbol() {
        return false;
    }

    @Override
    public boolean isObjectEndSymbol() {
        return false;
    }

    @Override
    public boolean isSeparatorSymbol() {
        return false;
    }

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeArrayBeginSymbolParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
