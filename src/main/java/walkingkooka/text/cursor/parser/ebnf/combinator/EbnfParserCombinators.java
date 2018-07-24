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

import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.type.PublicStaticHelper;

import java.util.Map;
import java.util.Objects;

public final class EbnfParserCombinators implements PublicStaticHelper {

    /**
     * Accepts a grammar and returns a {@link Map} holding all identifiers(the names) to a parser.
     * The {@link Map} will be used as defaults, any new definitions in the grammar will replace those in the map.
     */
    public static <T extends ParserToken, C extends ParserContext> Map<EbnfIdentifierParserToken, Parser<T, C>> transform(final EbnfGrammarParserToken grammar,
                                                                                                                          final Map<EbnfIdentifierParserToken, Parser<ParserToken, C>> identifierToParser,
                                                                                                                          final EbnfParserCombinatorSyntaxTreeTransformer<C> syntaxTreeTransformer) {
        Objects.requireNonNull(grammar, "grammar");
        Objects.requireNonNull(identifierToParser, "identifierToParser");
        Objects.requireNonNull(syntaxTreeTransformer, "syntaxTreeTransformer");

        return new EbnfParserCombinatorContext(grammar, identifierToParser, syntaxTreeTransformer).process();
    }

    /**
     * Stop creation
     */
    private EbnfParserCombinators() {
        throw new UnsupportedOperationException();
    }
}
