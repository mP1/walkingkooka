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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public abstract class EbnfAlternativeOrConcatenationParserTestCase<T extends EbnfParserToken> extends EbnfParserTestCase2<T> {

    @Test
    public final void testOnlyToken() {
        final String text = this.text();
        final T token = this.token(text);
        this.parseAndCheck(text, token, text);
    }

    @Test
    public final void testWhitespaceIdentifier() {
        final String text = WHITESPACE1 + IDENTIFIER1 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), identifier1(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testWhitespaceIdentifierWhitespace() {
        final String text = WHITESPACE1 + IDENTIFIER1 + WHITESPACE2 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), identifier1(), whitespace2(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testWhitespaceIdentifierWhitespaceWhitespaceIdentifier2() {
        final String text = WHITESPACE1 + IDENTIFIER1 + WHITESPACE2 + this.separator() + WHITESPACE1 + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), identifier1(), whitespace2(), this.separatorCharToken(), whitespace1(), identifier2()),
                text);
    }

    @Test
    public final void testWhitespaceIdentifierWhitespaceWhitespaceIdentifier2Whitespace() {
        final String text = WHITESPACE1 + IDENTIFIER1 + WHITESPACE2 + this.separator() + WHITESPACE1 + IDENTIFIER2;
        this.parseAndCheck(text + WHITESPACE2,
                this.token(text, whitespace1(), identifier1(), whitespace2(), this.separatorCharToken(), whitespace1(), identifier2()),
                text,
                WHITESPACE2);
    }

    @Test
    public final void testCommentIdentifier() {
        final String text = COMMENT1 + IDENTIFIER1 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, comment1(), identifier1(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testCommentIdentifierComment() {
        final String text = COMMENT1 + IDENTIFIER1 + COMMENT2 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, comment1(), identifier1(), comment2(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testCommentIdentifierCommentCommentIdentifier2() {
        final String text = COMMENT1 + IDENTIFIER1 + COMMENT2 + this.separator() + COMMENT1 + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, comment1(), identifier1(), comment2(), this.separatorCharToken(), comment1(), identifier2()),
                text);
    }

    @Test
    public final void testCommentIdentifierCommentCommentIdentifier2Comment() {
        final String text = COMMENT1 + IDENTIFIER1 + COMMENT2 + this.separator() + COMMENT1 + IDENTIFIER2;
        this.parseAndCheck(text + COMMENT2,
                this.token(text, comment1(), identifier1(), comment2(), this.separatorCharToken(), comment1(), identifier2()),
                text,
                COMMENT2);
    }

    @Test
    public final void testWhitespaceCommentIdentifier() {
        final String text = WHITESPACE1 + COMMENT1 + IDENTIFIER1 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), comment1(), identifier1(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testWhitespaceCommentWhitespaceIdentifier() {
        final String text = WHITESPACE1 + COMMENT1 + WHITESPACE2 + IDENTIFIER1 + this.separator() + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), comment1(), whitespace2(), identifier1(), this.separatorCharToken(), identifier2()),
                text);
    }

    @Test
    public final void testThreeTokens() {
        final String text = IDENTIFIER1 + this.separator() + IDENTIFIER2 + this.separator() + IDENTIFIER3;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.separatorCharToken(), identifier2(), this.separatorCharToken(), identifier3()),
                text);
    }

    @Test
    public final void testIdentifierIdentifierSeparatorWhitespaceIdentifier() {
        final String text = IDENTIFIER1 + this.separator() + IDENTIFIER2 + this.separator() + this.whitespace1() + IDENTIFIER3;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.separatorCharToken(), identifier2(), this.separatorCharToken(), this.whitespace1(), identifier3()),
                text);
    }

    @Test
    public final void testIdentifierIdentifierWhitespaceSeparatorWhitespaceIdentifier() {
        final String text = IDENTIFIER1 + this.separator() + IDENTIFIER2 + this.whitespace1() + this.separator() + this.whitespace2() + IDENTIFIER3;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.separatorCharToken(), identifier2(), this.whitespace1(), this.separatorCharToken(), this.whitespace2(), identifier3()),
                text);
    }

    @Test
    public final void testIdentifierIdentifierWhitespaceSeparatorIdentifier() {
        final String text = IDENTIFIER1 + this.separator() + IDENTIFIER2 + this.whitespace1() + this.separator() + IDENTIFIER3;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.separatorCharToken(), identifier2(), this.whitespace1(), this.separatorCharToken(), identifier3()),
                text);
    }

    @Override
    final String text() {
        return IDENTIFIER1 + this.separator() + IDENTIFIER2;
    }

    abstract String separator();

    final EbnfSymbolParserToken separatorCharToken() {
        final String separator = this.separator();
        return EbnfParserToken.symbol(separator, separator);
    }

    @Override
    final T token(final String text) {
        return this.token(text,
                identifier1(),
                separatorCharToken(),
                identifier2());
    }

    final T token(final String text, final ParserToken... tokens) {
        return this.token(text, Lists.of(tokens));
    }

    abstract T token(final String text, final List<ParserToken> tokens);
}
