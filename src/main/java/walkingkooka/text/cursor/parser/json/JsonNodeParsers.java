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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserContext;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.type.PublicStaticHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

public final class JsonNodeParsers implements PublicStaticHelper {

    private static final Parser<ParserToken, ParserContext> ARRAY_BEGIN_SYMBOL = symbol("[", JsonNodeParserToken::arrayBeginSymbol, JsonNodeArrayBeginSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> ARRAY_END_SYMBOL = symbol("]", JsonNodeParserToken::arrayEndSymbol, JsonNodeArrayEndSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> OBJECT_ASSIGNMENT_SYMBOL = symbol(":", JsonNodeParserToken::objectAssignmentSymbol, JsonNodeObjectAssignmentSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> OBJECT_BEGIN_SYMBOL = symbol("{", JsonNodeParserToken::objectBeginSymbol, JsonNodeObjectBeginSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> OBJECT_END_SYMBOL = symbol("}", JsonNodeParserToken::objectEndSymbol, JsonNodeObjectEndSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> SEPARATOR_SYMBOL = symbol(",", JsonNodeParserToken::separatorSymbol, JsonNodeSeparatorSymbolParserToken.class);
    
    private static final EbnfIdentifierName ARRAY_BEGIN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("ARRAY_BEGIN");
    private static final EbnfIdentifierName ARRAY_END_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("ARRAY_END");

    private static final EbnfIdentifierName BOOLEAN_IDENTIFIER = EbnfIdentifierName.with("BOOLEAN");
    private static final EbnfIdentifierName NULL_IDENTIFIER = EbnfIdentifierName.with("NULL");
    private static final EbnfIdentifierName NUMBER_IDENTIFIER = EbnfIdentifierName.with("NUMBER");

    private static final EbnfIdentifierName OBJECT_ASSIGNMENT_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OBJECT_ASSIGNMENT");
    private static final EbnfIdentifierName OBJECT_BEGIN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OBJECT_BEGIN");
    private static final EbnfIdentifierName OBJECT_END_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OBJECT_END");

    static final EbnfIdentifierName SEPARATOR_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("SEPARATOR");

    private static final EbnfIdentifierName STRING_IDENTIFIER = EbnfIdentifierName.with("STRING");

    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");

    private static final EbnfIdentifierName VALUE_IDENTIFIER = EbnfIdentifierName.with("VALUE");

    // required by JsonNodeEbnfParserCombinatorSyntaxTreeTransformer
    static final Set<EbnfIdentifierName> REPORT_FAILURE_IDENTIFIER_NAMES = Sets.of(
            OBJECT_ASSIGNMENT_SYMBOL_IDENTIFIER
            );

    /**
     * BOOLEAN
     */
    public static Parser<ParserToken, ParserContext> booleanParser() {
        return BOOLEAN;
    }

    private final static Parser<ParserToken, ParserContext> BOOLEAN_FALSE = Parsers.string("false")
            .transform((stringParserToken, context) -> JsonNodeParserToken.booleanParserToken(false, stringParserToken.text()).cast());

    private final static Parser<ParserToken, ParserContext> BOOLEAN_TRUE = Parsers.string("true")
            .transform((stringParserToken, context) -> JsonNodeParserToken.booleanParserToken(true, stringParserToken.text()).cast());

    private final static Parser<ParserToken, ParserContext> BOOLEAN = BOOLEAN_FALSE.or(BOOLEAN_TRUE)
            .setToString(JsonNodeBooleanParserToken.class.getSimpleName());

    /**
     * NULL
     */
    public static Parser<ParserToken, ParserContext> nullParser() {
        return NULL;
    }

    private final static Parser<ParserToken, ParserContext> NULL = Parsers.string("null")
            .transform((stringParserToken, context) -> JsonNodeParserToken.nullParserToken(stringParserToken.text()).cast())
            .setToString(JsonNodeNullParserToken.class.getSimpleName());

    /**
     * NUMBER
     */
    public static Parser<ParserToken, ParserContext> number() {
        return NUMBER;
    }

    private final static Parser<ParserToken, ParserContext> NUMBER = Parsers.doubleParser('.')
            .transform((doubleParserToken, context) -> JsonNodeParserToken.number(doubleParserToken.value(), doubleParserToken.text()).cast())
            .setToString(JsonNodeNumberParserToken.class.getSimpleName());

    /**
     * String
     */
    public static Parser<ParserToken, ParserContext> string() {
        return STRING;
    }

    private final static Parser<ParserToken, ParserContext> STRING = Parsers.doubleQuoted()
            .transform((doubleQuotedParserToken, JsonNodeParserContext) -> JsonNodeParserToken.string(doubleQuotedParserToken.value(), doubleQuotedParserToken.text()).cast())
            .setToString(JsonNodeStringParserToken.class.getSimpleName());

    /**
     * Whitespace
     */
    public static Parser<ParserToken, ParserContext> whitespace() {
        return WHITESPACE;
    }

    private final static Parser<ParserToken, ParserContext> WHITESPACE = Parsers.stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
            .transform((stringParserToken, JsonNodeParserContext) -> JsonNodeParserToken.whitespace(stringParserToken.value(), stringParserToken.text()).cast())
            .setToString(JsonNodeWhitespaceParserToken.class.getSimpleName())
            .cast();

    private static Parser<ParserToken, ParserContext> symbol(final String c, final BiFunction<String, String, ParserToken> factory, final Class<? extends JsonNodeSymbolParserToken> tokenClass) {
        return Parsers.string(c)
                .transform((stringParserToken, context) -> factory.apply(stringParserToken.value(), stringParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    /**
     * VALUE
     */
    public static Parser<ParserToken, ParserContext> value() {
        return VALUE;
    }

    private final static Parser<ParserToken, ParserContext> VALUE = value0();

    private static Parser<ParserToken, ParserContext> value0() {
        try {
            final TextCursor grammarFile = TextCursors.charSequence(readGrammarFile());
            final Optional<EbnfGrammarParserToken> grammar = EbnfParserToken.grammarParser().parse(grammarFile, new EbnfParserContext());
            if (!grammar.isPresent() || !grammarFile.isEmpty()) {
                final TextCursorSavePoint save = grammarFile.save();
                grammarFile.end();
                throw new UnsupportedOperationException("Unable to load grammar file\nGrammar...\n" + grammar + "\n\nRemaining...\n" + save.textBetween());
            }

            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined = Maps.ordered();
            predefined.put(ARRAY_BEGIN_SYMBOL_IDENTIFIER, ARRAY_BEGIN_SYMBOL);
            predefined.put(ARRAY_END_SYMBOL_IDENTIFIER, ARRAY_END_SYMBOL);

            predefined.put(BOOLEAN_IDENTIFIER, booleanParser());
            predefined.put(NULL_IDENTIFIER, nullParser());
            predefined.put(NUMBER_IDENTIFIER, number());

            predefined.put(OBJECT_ASSIGNMENT_SYMBOL_IDENTIFIER, OBJECT_ASSIGNMENT_SYMBOL);
            predefined.put(OBJECT_BEGIN_SYMBOL_IDENTIFIER, OBJECT_BEGIN_SYMBOL);
            predefined.put(OBJECT_END_SYMBOL_IDENTIFIER, OBJECT_END_SYMBOL);

            predefined.put(SEPARATOR_SYMBOL_IDENTIFIER, SEPARATOR_SYMBOL);

            predefined.put(STRING_IDENTIFIER, string());

            predefined.put(WHITESPACE_IDENTIFIER, whitespace());

            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> result = grammar.get()
                    .combinator(predefined,
                            new JsonNodeEbnfParserCombinatorSyntaxTreeTransformer());

            return result.get(VALUE_IDENTIFIER);
        } catch (final JsonNodeParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause){
            throw new JsonNodeParserException("Failed to return parsers from JsonNode grammar file, message: " + cause.getMessage(), cause);
        }
    }

    private static CharSequence readGrammarFile() throws IOException, JsonNodeParserException {
        final String grammarFilename = "json-parsers.grammar";
        final InputStream inputStream = JsonNodeParsers.class.getResourceAsStream(grammarFilename);
        if(null == inputStream){
            throw new JsonNodeParserException("Unable to find " + CharSequences.quote(grammarFilename));
        }

        final char[] buffer = new char[ 4096];
        final StringBuilder b = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            for(;;) {
                final int read = reader.read(buffer);
                if (-1 == read) {
                    break;
                }
                b.append(buffer, 0, read);
            }
        }
        return b;
    }

    /**
     * Stop construction
     */
    private JsonNodeParsers() {
        throw new UnsupportedOperationException();
    }
}
