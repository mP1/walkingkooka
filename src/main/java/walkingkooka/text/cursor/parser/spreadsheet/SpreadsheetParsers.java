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

package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public final class SpreadsheetParsers implements PublicStaticHelper {

    private static final Parser<ParserToken, SpreadsheetParserContext> PLUS_SYMBOL = symbol("+", SpreadsheetParserToken::plusSymbol, SpreadsheetPlusSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> MINUS_SYMBOL = symbol("-", SpreadsheetParserToken::minusSymbol, SpreadsheetMinusSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> MULTIPLY_SYMBOL = symbol("*", SpreadsheetParserToken::multiplySymbol, SpreadsheetMultiplySymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> DIVIDE_SYMBOL = symbol("/", SpreadsheetParserToken::divideSymbol, SpreadsheetDivideSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> POWER_SYMBOL = symbol("^", SpreadsheetParserToken::powerSymbol, SpreadsheetPowerSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> BETWEEN_SYMBOL = symbol(":", SpreadsheetParserToken::betweenSymbol, SpreadsheetBetweenSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> PERCENT_SYMBOL = symbol("%", SpreadsheetParserToken::percentSymbol, SpreadsheetPercentSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> FUNCTION_PARAMETER_SEPARATOR_SYMBOL = symbol(",", SpreadsheetParserToken::functionParameterSymbol, SpreadsheetFunctionParameterSeparatorSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> OPEN_PARENTHESIS_SYMBOL = symbol("(", SpreadsheetParserToken::openParenthesisSymbol, SpreadsheetOpenParenthesisSymbolParserToken.class);
    private static final Parser<ParserToken, SpreadsheetParserContext> CLOSE_PARENTHESIS_SYMBOL = symbol(")", SpreadsheetParserToken::closeParenthesisSymbol, SpreadsheetCloseParenthesisSymbolParserToken.class);
   
    private static final EbnfIdentifierName COLUMN_ROW_IDENTIFIER = EbnfIdentifierName.with("COLUMN_ROW");
    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");
    private static final EbnfIdentifierName NUMBER_IDENTIFIER = EbnfIdentifierName.with("NUMBER");
    private static final EbnfIdentifierName FUNCTION_NAME_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_NAME");
    private static final EbnfIdentifierName LABEL_NAME_IDENTIFIER = EbnfIdentifierName.with("LABEL_NAME");
    private static final EbnfIdentifierName PLUS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("PLUS_SYMBOL");
    private static final EbnfIdentifierName MINUS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("MINUS_SYMBOL");
    private static final EbnfIdentifierName MULTIPLY_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("MULTIPLY_SYMBOL");
    private static final EbnfIdentifierName DIVIDE_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("DIVIDE_SYMBOL");
    private static final EbnfIdentifierName POWER_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("POWER_SYMBOL");
    private static final EbnfIdentifierName BETWEEN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("BETWEEN_SYMBOL");
    private static final EbnfIdentifierName PERCENT_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("PERCENT_SYMBOL");
    private static final EbnfIdentifierName FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_PARAMETER_SEPARATOR_SYMBOL");
    private static final EbnfIdentifierName OPEN_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OPEN_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("CLOSE_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName TEXT_IDENTIFIER = EbnfIdentifierName.with("TEXT");
    private static final EbnfIdentifierName EXPRESSION_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION");

    /**
     * {@see SpreadsheetColumnParser}
     */
    public static Parser<ParserToken, SpreadsheetParserContext> column() {
        return Cast.to(SpreadsheetColumnParser.INSTANCE);
    }

    /**
     * {@see SpreadsheetColumnParser}
     */
    public static Parser<ParserToken, SpreadsheetParserContext> columnRow() {
        return column().builder(SpreadsheetColumnParserToken.NAME)
                .required(row(), SpreadsheetRowParserToken.NAME)
                .build()
                .transform((sequenceParserToken, spreadsheetParserContext) -> {
                    return SpreadsheetParserToken.cell(sequenceParserToken.value(), sequenceParserToken.text()).cast();
                });
    }

    /**
     * Returns a {@link Parser} that returns a {@link SpreadsheetParserToken}.
     */
    public static Parser<SpreadsheetParserToken, SpreadsheetParserContext> expression(final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number) {
        Objects.requireNonNull(number, "number");

        try {
            final TextCursor grammarFile = TextCursors.charSequence(readGrammarFile());
            final Optional<EbnfGrammarParserToken> grammar = EbnfParserToken.grammarParser().parse(grammarFile, new EbnfParserContext());
            if (!grammar.isPresent() || !grammarFile.isEmpty()) {
                final TextCursorSavePoint save = grammarFile.save();
                grammarFile.end();
                throw new UnsupportedOperationException("Unable to load grammar file\nGrammar...\n" + grammar + "\n\nRemaining...\n" + save.textBetween());
            }

            final Map<EbnfIdentifierName, Parser<ParserToken, SpreadsheetParserContext>> predefined = Maps.ordered();
            predefined.put(COLUMN_ROW_IDENTIFIER, columnRow());
            predefined.put(WHITESPACE_IDENTIFIER, whitespace());
            predefined.put(NUMBER_IDENTIFIER, number.castC());
            predefined.put(FUNCTION_NAME_IDENTIFIER, functionName());
            predefined.put(LABEL_NAME_IDENTIFIER, labelName());

            predefined.put(PLUS_SYMBOL_IDENTIFIER, PLUS_SYMBOL);
            predefined.put(MINUS_SYMBOL_IDENTIFIER, MINUS_SYMBOL);

            predefined.put(MULTIPLY_SYMBOL_IDENTIFIER, MULTIPLY_SYMBOL);
            predefined.put(DIVIDE_SYMBOL_IDENTIFIER, DIVIDE_SYMBOL);

            predefined.put(POWER_SYMBOL_IDENTIFIER, POWER_SYMBOL);

            predefined.put(BETWEEN_SYMBOL_IDENTIFIER, BETWEEN_SYMBOL);
            predefined.put(PERCENT_SYMBOL_IDENTIFIER, PERCENT_SYMBOL);
            predefined.put(FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER, FUNCTION_PARAMETER_SEPARATOR_SYMBOL);

            predefined.put(OPEN_PARENTHESIS_SYMBOL_IDENTIFIER, OPEN_PARENTHESIS_SYMBOL);
            predefined.put(CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER, CLOSE_PARENTHESIS_SYMBOL);

            predefined.put(TEXT_IDENTIFIER, text());

            final Map<EbnfIdentifierName, Parser<ParserToken, SpreadsheetParserContext>> result = grammar.get()
                    .combinator(predefined,
                            new SpreedsheetEbnfParserCombinatorSyntaxTreeTransformer());

            return result.get(EXPRESSION_IDENTIFIER).cast(SpreadsheetParserToken.class);
        } catch (final SpreadsheetParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause){
            throw new SpreadsheetParserException("Failed to return parsers from spreadsheet grammar file, message: " + cause.getMessage(), cause);
        }
    }

    private static CharSequence readGrammarFile() throws IOException, SpreadsheetParserException{
        final String grammarFilename = "spreadsheet-parsers.grammar";
        final InputStream inputStream = SpreadsheetParsers.class.getResourceAsStream(grammarFilename);
        if(null == inputStream){
            throw new SpreadsheetParserException("Unable to find " + CharSequences.quote(grammarFilename));
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
     * A parser that returns {@see SpreadsheetFunctionName}
     */
    public static Parser<ParserToken, SpreadsheetParserContext> functionName() {
        return FUNCTION_NAME;
    }

    private final static Parser<ParserToken, SpreadsheetParserContext> FUNCTION_NAME = Parsers.<SpreadsheetParserContext>stringInitialAndPartCharPredicate(
            SpreadsheetFunctionName.INITIAL,
            SpreadsheetFunctionName.PART,
            1,
            SpreadsheetFunctionName.MAX_LENGTH)
            .transform((stringParserToken, spreadsheetParserContext) -> Cast.to(SpreadsheetParserToken.functionName(SpreadsheetFunctionName.with(stringParserToken.value()), stringParserToken.text())))
            .setToString(SpreadsheetFunctionName.class.getSimpleName())
            .castC();

    /**
     * {@see SpreadsheetLabelNameParser}
     */
    public static Parser<ParserToken, SpreadsheetParserContext> labelName() {
        return SpreadsheetLabelNameParser.INSTANCE.castC();
    }

    /**
     * {@see SpreadsheetRowParser}
     */
    public static Parser<ParserToken, SpreadsheetParserContext> row() {
        return Cast.to(SpreadsheetRowParser.INSTANCE);
    }

    /**
     * Text
     */
    public static Parser<ParserToken, SpreadsheetParserContext> text() {
        return TEXT;
    }

    private final static Parser<ParserToken, SpreadsheetParserContext> TEXT = Parsers.<SpreadsheetParserContext>doubleQuoted()
            .transform((doubleQuotedParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.text(doubleQuotedParserToken.value(), doubleQuotedParserToken.text()).cast())
            .setToString(SpreadsheetTextParserToken.class.getSimpleName());

    /**
     * Whitespace
     */
    public static Parser<ParserToken, SpreadsheetParserContext> whitespace() {
        return WHITESPACE;
    }

    private final static Parser<ParserToken, SpreadsheetParserContext> WHITESPACE = Parsers.<SpreadsheetParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
            .transform((stringParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.whitespace(stringParserToken.value(), stringParserToken.text()).cast())
            .setToString(SpreadsheetWhitespaceParserToken.class.getSimpleName())
            .castC();

    private static Parser<ParserToken, SpreadsheetParserContext> symbol(final String c, final BiFunction<String, String, ParserToken> factory, final Class<? extends SpreadsheetSymbolParserToken> tokenClass) {
        return Parsers.<SpreadsheetParserContext>string(c)
                .transform((stringParserToken, context) -> factory.apply(stringParserToken.value(), stringParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .castC();
    };

    /**
     * Stop construction
     */
    private SpreadsheetParsers() {
        throw new UnsupportedOperationException();
    }
}
