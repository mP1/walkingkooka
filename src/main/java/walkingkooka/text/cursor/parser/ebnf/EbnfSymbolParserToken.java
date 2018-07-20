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
 */
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.ParserTokenNodeName;

/**
 * Holds any of the symbols that separate actual tokens, such as the parens around a grouping.
 */
final public class EbnfSymbolParserToken extends EbnfLeafParserToken<Character> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfSymbolParserToken.class);

    static EbnfSymbolParserToken with(final char symbol, final String text){
        return new EbnfSymbolParserToken(symbol, checkText(text));
    }

    EbnfSymbolParserToken(final char symbol, final String text){
        super(symbol, text);
    }

    @Override
    public EbnfSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfSymbolParserToken replaceText(final String text) {
        return new EbnfSymbolParserToken(this.value, text);
    }

    @Override
    public boolean isComment() {
        return false;
    }

    @Override
    public boolean isIdentifier() {
        return false;
    }

    @Override
    public boolean isSymbol() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfSymbolParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
