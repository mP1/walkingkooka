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
 * Holds the text for a comment.
 */
public final class EbnfCommentParserToken extends EbnfLeafParserToken<String> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfCommentParserToken.class);

    static EbnfCommentParserToken with(final String value, final String text){
        checkValue(value);
        checkText(text);

        return new EbnfCommentParserToken(value, text);
    }

    EbnfCommentParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    public EbnfCommentParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfCommentParserToken replaceText(final String text) {
        return new EbnfCommentParserToken(this.value, text);
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public boolean isIdentifier() {
        return false;
    }

    @Override
    public boolean isSymbol() {
        return false;
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
        return other instanceof EbnfCommentParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
