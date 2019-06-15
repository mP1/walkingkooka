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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public abstract class EbnfParserTestCase4<T extends EbnfParserToken> extends EbnfParserTestCase2<T> {

    EbnfParserTestCase4() {
        super();
    }

    @Test
    public final void testWhitespaceIdentifier() {
        final String text = this.beginChar() + WHITESPACE1 + IDENTIFIER1 + this.endChar();
        this.parseAndCheck(text,
                this.token(text, this.beginCharToken(), whitespace1(), this.identifier1(), this.endCharToken()),
                text);
    }

    @Test
    public final void testWhitespaceIdentifierWhitespace() {
        final String text = this.beginChar() + WHITESPACE1 + IDENTIFIER1 + WHITESPACE2 + this.endChar();
        this.parseAndCheck(text,
                this.token(text, this.beginCharToken(), whitespace1(), this.identifier1(), this.whitespace2(), this.endCharToken()),
                text);
    }

    @Test
    public final void testCommentIdentifier() {
        final String text = this.beginChar() + COMMENT1 + IDENTIFIER1 + this.endChar();
        this.parseAndCheck(text,
                this.token(text, this.beginCharToken(), comment1(), this.identifier1(), this.endCharToken()),
                text);
    }

    @Test
    public final void testCommentIdentifierComment() {
        final String text = this.beginChar() + COMMENT1 + IDENTIFIER1 + COMMENT2 + this.endChar();
        this.parseAndCheck(text,
                this.token(text, this.beginCharToken(), comment1(), this.identifier1(), this.comment2(), this.endCharToken()),
                text);
    }

    @Test
    public final void testWhitespaceIdentifierComment() {
        final String text = this.beginChar() + WHITESPACE1 + IDENTIFIER1 + COMMENT2 + this.endChar();
        this.parseAndCheck(text,
                this.token(text, this.beginCharToken(), whitespace1(), this.identifier1(), this.comment2(), this.endCharToken()),
                text);
    }

    @Override
    String text() {
        return this.beginChar() + IDENTIFIER1 + this.endChar();
    }

    abstract String beginChar();

    private EbnfSymbolParserToken beginCharToken() {
        return symbolToken(this.beginChar());
    }

    abstract String endChar();

    private EbnfSymbolParserToken endCharToken() {
        return symbolToken(this.endChar());
    }

    private EbnfSymbolParserToken symbolToken(final String s) {
        return EbnfParserToken.symbol(s, s);
    }

    final T token(final String text) {
        return this.token(text, identifier1());
    }

    final T token(final String text, final ParserToken... tokens) {
        return this.token(text, Lists.of(tokens));
    }

    abstract T token(final String text, final List<ParserToken> tokens);
}
