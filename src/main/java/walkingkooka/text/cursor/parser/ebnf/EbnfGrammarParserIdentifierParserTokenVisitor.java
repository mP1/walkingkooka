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

import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;

/**
 * A {@link ParserTokenVisitor} that turns tokens into a {@link EbnfIdentifierParserToken}.
 */
final class EbnfGrammarParserIdentifierParserTokenVisitor extends ParserTokenVisitor {

    static EbnfIdentifierParserToken ebnfIdentifierParserToken(final ParserToken token, final EbnfParserContext context) {
        final EbnfGrammarParserIdentifierParserTokenVisitor visitor = new EbnfGrammarParserIdentifierParserTokenVisitor();
        visitor.accept(token);
        return EbnfParserToken.identifier(EbnfIdentifierName.with(visitor.text.toString()), token.text());
    }

    EbnfGrammarParserIdentifierParserTokenVisitor() {
        super();
    }

    @Override
    protected void visit(final CharacterParserToken token) {
        this.text.append(token.value());
    }

    private final StringBuilder text = new StringBuilder();

    @Override
    public String toString() {
        return this.text.toString();
    }
}
