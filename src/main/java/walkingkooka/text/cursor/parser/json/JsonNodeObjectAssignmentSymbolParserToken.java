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
 * Represents a close / right parens symbol token.
 */
public final class JsonNodeObjectAssignmentSymbolParserToken extends JsonNodeSymbolParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeObjectAssignmentSymbolParserToken.class);

    static JsonNodeObjectAssignmentSymbolParserToken with(final String value, final String text){
        checkValue(value);
        checkText(text);

        return new JsonNodeObjectAssignmentSymbolParserToken(value, text);
    }

    private JsonNodeObjectAssignmentSymbolParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    public JsonNodeObjectAssignmentSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeObjectAssignmentSymbolParserToken replaceText(final String text) {
        return new JsonNodeObjectAssignmentSymbolParserToken(this.value, text);
    }

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
        return true;
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
        return other instanceof JsonNodeObjectAssignmentSymbolParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
