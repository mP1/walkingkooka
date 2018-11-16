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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;

import java.util.Optional;

/**
 * A proxy for a parser.
 */
final class EbnfParserCombinatorProxyParser<T extends ParserToken, C extends ParserContext> implements Parser<T, C> {

    static <T extends ParserToken, C extends ParserContext> EbnfParserCombinatorProxyParser<T, C> with(final EbnfIdentifierParserToken identifier){
        return new EbnfParserCombinatorProxyParser<T, C>(identifier);
    }

    private EbnfParserCombinatorProxyParser(final EbnfIdentifierParserToken identifier){
        this.identifier = identifier;
    }

    @Override
    public Optional<T> parse(final TextCursor cursor, final C context) {
        return this.parser.parse(cursor, context);
    }

    private final EbnfIdentifierParserToken identifier;

    /**
     * The actual parser is once its rule is eventually visited completely.
     */
    Parser<T, C> parser;

    /**
     * Return the identifier rather than the full rule definition, this will help prevent cycles and very long and complex toStrings.
     */
    @Override
    public String toString() {
        return this.identifier.toString();
    }
}
