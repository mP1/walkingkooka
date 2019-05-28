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

/**
 * Represents a open object (parens) symbol token.
 */
public final class JsonNodeObjectBeginSymbolParserToken extends JsonNodeSymbolParserToken {

    static JsonNodeObjectBeginSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkText(text);

        return new JsonNodeObjectBeginSymbolParserToken(value, text);
    }

    private JsonNodeObjectBeginSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public JsonNodeObjectBeginSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeObjectBeginSymbolParserToken replaceText(final String text) {
        return new JsonNodeObjectBeginSymbolParserToken(this.value, text);
    }

    // is ...............................................................................................

    @Override
    public boolean isArrayBeginSymbol() {
        return false;
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
        return true;
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
    public boolean isWhitespace() {
        return false;
    }

    // visitor ...............................................................................................

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object ...............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeObjectBeginSymbolParserToken;
    }
}
