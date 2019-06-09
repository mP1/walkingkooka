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

import java.util.Optional;

/**
 * Holds the text for a comment.
 */
public final class EbnfCommentParserToken extends EbnfLeafParserToken<String> {

    static EbnfCommentParserToken with(final String value, final String text) {
        checkValue(value);
        checkText(text);

        return new EbnfCommentParserToken(value, text);
    }

    private EbnfCommentParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace() {
        return Optional.empty();
    }

    // isXXX............................................................................................................

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
    public boolean isNoise() {
        return true;
    }

    // EbnfParserTokenVisitor............................................................................................

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfCommentParserToken;
    }
}
