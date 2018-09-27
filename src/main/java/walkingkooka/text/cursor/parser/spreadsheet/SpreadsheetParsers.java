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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarLoader;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.type.PublicStaticHelper;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public final class SpreadsheetParsers implements PublicStaticHelper {

    /**
     * A {@link Parser} that returns a cell reference token of some sort.
     */
    public static Parser<SpreadsheetParserToken, SpreadsheetParserContext> cellReferences() {
        return parserFromGrammar(Parsers.fake(), CELL_IDENTIFIER);
    }

    private static final EbnfIdentifierName CELL_IDENTIFIER = EbnfIdentifierName.with("CELL");

    private static void cellReferences(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(COLUMN_ROW_IDENTIFIER, columnAndRow());
        predefined.put(LABEL_NAME_IDENTIFIER, labelName());
        predefined.put(BETWEEN_SYMBOL_IDENTIFIER, BETWEEN_SYMBOL);
    }

    private static final EbnfIdentifierName COLUMN_ROW_IDENTIFIER = EbnfIdentifierName.with("COLUMN_ROW");
    private static final EbnfIdentifierName LABEL_NAME_IDENTIFIER = EbnfIdentifierName.with("LABEL_NAME");
    private static final EbnfIdentifierName BETWEEN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("BETWEEN_SYMBOL");

    private static final Parser<ParserToken, ParserContext> BETWEEN_SYMBOL = symbol(':',
            SpreadsheetParserToken::betweenSymbol,
            SpreadsheetBetweenSymbolParserToken.class);

    /**
     * {@see SpreadsheetColumnReferenceParser}
     */
    public static Parser<ParserToken, ParserContext> column() {
        return Cast.to(SpreadsheetColumnReferenceParser.INSTANCE);
    }

    /**
     * {@see SpreadsheetColumnReferenceParser}
     */
    public static Parser<ParserToken, ParserContext> columnAndRow() {
        return column().builder(SpreadsheetColumnReferenceParserToken.NAME)
                .required(row(), SpreadsheetRowReferenceParserToken.NAME)
                .build()
                .transform((sequenceParserToken, spreadsheetParserContext) -> {
                    return SpreadsheetParserToken.cellReference(sequenceParserToken.value(), sequenceParserToken.text()).cast();
                });
    }

    /**
     * Returns a {@link Parser} that parsers expressions.
     */
    public static Parser<SpreadsheetParserToken, SpreadsheetParserContext> expression(final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number) {
        return parserFromGrammar(number, EXPRESSION_IDENTIFIER);
    }

    private static final EbnfIdentifierName EXPRESSION_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION");

    /**
     * Returns a {@link Parser} that parsers function invocations, starting with the name and parameters.
     */
    public static Parser<SpreadsheetParserToken, SpreadsheetParserContext> function(final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number) {
        return parserFromGrammar(number, FUNCTION_IDENTIFIER);
    }

    static final EbnfIdentifierName FUNCTION_IDENTIFIER = EbnfIdentifierName.with("FUNCTION");

    /**
     * A parser that returns {@see SpreadsheetFunctionName}
     */
    public static Parser<ParserToken, ParserContext> functionName() {
        return FUNCTION_NAME;
    }

    private final static Parser<ParserToken, ParserContext> FUNCTION_NAME = Parsers.<SpreadsheetParserContext>stringInitialAndPartCharPredicate(
            SpreadsheetFunctionName.INITIAL,
            SpreadsheetFunctionName.PART,
            1,
            SpreadsheetFunctionName.MAX_LENGTH)
            .transform((stringParserToken, spreadsheetParserContext) -> Cast.to(SpreadsheetParserToken.functionName(SpreadsheetFunctionName.with(stringParserToken.value()), stringParserToken.text())))
            .setToString(SpreadsheetFunctionName.class.getSimpleName())
            .cast();

    private static void functions(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(FUNCTION_NAME_IDENTIFIER, functionName());
        predefined.put(FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER, FUNCTION_PARAMETER_SEPARATOR_SYMBOL);
    }

    private static final Parser<ParserToken, ParserContext> FUNCTION_PARAMETER_SEPARATOR_SYMBOL = symbol(',',
            SpreadsheetParserToken::functionParameterSeparatorSymbol,
            SpreadsheetFunctionParameterSeparatorSymbolParserToken.class);

    private static final EbnfIdentifierName FUNCTION_NAME_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_NAME");
    private static final EbnfIdentifierName FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_PARAMETER_SEPARATOR_SYMBOL");


    /**
     * {@see SpreadsheetLabelNameParser}
     */
    public static Parser<ParserToken, ParserContext> labelName() {
        return SpreadsheetLabelNameParser.INSTANCE.cast();
    }

    /**
     * A {@link Parser} that returns a range which will include cell references or labels.
     */
    public static Parser<SpreadsheetParserToken, SpreadsheetParserContext> range() {
        return parserFromGrammar(Parsers.fake(), RANGE_IDENTIFIER);
    }

    private static final EbnfIdentifierName RANGE_IDENTIFIER = EbnfIdentifierName.with("RANGE");

    /**
     * {@see SpreadsheetRowReferenceParser}
     */
    public static Parser<ParserToken, ParserContext> row() {
        return Cast.to(SpreadsheetRowReferenceParser.INSTANCE);
    }

    // conditions.............................................................................................................

    private static void conditions(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(EQUALS_SYMBOL_IDENTIFIER, EQUALS_SYMBOL);
        predefined.put(NOT_EQUALS_SYMBOL_IDENTIFIER, NOT_EQUALS_SYMBOL);

        predefined.put(GREATER_THAN_SYMBOL_IDENTIFIER, GREATER_THAN_SYMBOL);
        predefined.put(GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER, GREATER_THAN_EQUALS_SYMBOL);
        predefined.put(LESS_THAN_SYMBOL_IDENTIFIER, LESS_THAN_SYMBOL);
        predefined.put(LESS_THAN_EQUALS_SYMBOL_IDENTIFIER, LESS_THAN_EQUALS_SYMBOL);
    }

    private static final Parser<ParserToken, ParserContext> EQUALS_SYMBOL = symbol("==",
            SpreadsheetParserToken::equalsSymbol,
            SpreadsheetEqualsSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> NOT_EQUALS_SYMBOL = symbol("!=",
            SpreadsheetParserToken::notEqualsSymbol,
            SpreadsheetNotEqualsSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> GREATER_THAN_SYMBOL = symbol('>',
            SpreadsheetParserToken::greaterThanSymbol,
            SpreadsheetGreaterThanSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> GREATER_THAN_EQUALS_SYMBOL = symbol(">=",
            SpreadsheetParserToken::greaterThanEqualsSymbol,
            SpreadsheetGreaterThanEqualsSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> LESS_THAN_SYMBOL = symbol('<',
            SpreadsheetParserToken::lessThanSymbol,
            SpreadsheetLessThanSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> LESS_THAN_EQUALS_SYMBOL = symbol("<=",
            SpreadsheetParserToken::lessThanEqualsSymbol,
            SpreadsheetLessThanEqualsSymbolParserToken.class);

    private static final EbnfIdentifierName EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("EQUALS_SYMBOL");
    private static final EbnfIdentifierName NOT_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("NOT_EQUALS_SYMBOL");

    private static final EbnfIdentifierName GREATER_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_SYMBOL");
    private static final EbnfIdentifierName GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_EQUALS_SYMBOL");
    private static final EbnfIdentifierName LESS_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_SYMBOL");
    private static final EbnfIdentifierName LESS_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_EQUALS_SYMBOL");

    // math.............................................................................................................

    private static void math(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(PLUS_SYMBOL_IDENTIFIER, PLUS_SYMBOL);
        predefined.put(MINUS_SYMBOL_IDENTIFIER, MINUS_SYMBOL);

        predefined.put(MULTIPLY_SYMBOL_IDENTIFIER, MULTIPLY_SYMBOL);
        predefined.put(DIVIDE_SYMBOL_IDENTIFIER, DIVIDE_SYMBOL);

        predefined.put(POWER_SYMBOL_IDENTIFIER, POWER_SYMBOL);
    }

    private static final EbnfIdentifierName PLUS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("PLUS_SYMBOL");
    private static final EbnfIdentifierName MINUS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("MINUS_SYMBOL");
    private static final EbnfIdentifierName MULTIPLY_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("MULTIPLY_SYMBOL");
    private static final EbnfIdentifierName DIVIDE_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("DIVIDE_SYMBOL");
    private static final EbnfIdentifierName POWER_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("POWER_SYMBOL");

    private static final Parser<ParserToken, ParserContext> PLUS_SYMBOL = symbol('+',
            SpreadsheetParserToken::plusSymbol,
            SpreadsheetPlusSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> MINUS_SYMBOL = symbol('-',
            SpreadsheetParserToken::minusSymbol,
            SpreadsheetMinusSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> MULTIPLY_SYMBOL = symbol('*',
            SpreadsheetParserToken::multiplySymbol,
            SpreadsheetMultiplySymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> DIVIDE_SYMBOL = symbol('/',
            SpreadsheetParserToken::divideSymbol,
            SpreadsheetDivideSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> POWER_SYMBOL = symbol('^',
            SpreadsheetParserToken::powerSymbol,
            SpreadsheetPowerSymbolParserToken.class);

    // misc.............................................................................................................

    private static void misc(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined,
                             final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number) {
        predefined.put(NUMBER_IDENTIFIER, number.cast());
        predefined.put(PERCENT_SYMBOL_IDENTIFIER, PERCENT_SYMBOL);

        predefined.put(OPEN_PARENTHESIS_SYMBOL_IDENTIFIER, OPEN_PARENTHESIS_SYMBOL);
        predefined.put(CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER, CLOSE_PARENTHESIS_SYMBOL);

        predefined.put(TEXT_IDENTIFIER, text());

        predefined.put(WHITESPACE_IDENTIFIER, whitespace());
    }
    private static final EbnfIdentifierName NUMBER_IDENTIFIER = EbnfIdentifierName.with("NUMBER");
    private static final EbnfIdentifierName PERCENT_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("PERCENT_SYMBOL");
    private static final EbnfIdentifierName OPEN_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OPEN_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("CLOSE_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName TEXT_IDENTIFIER = EbnfIdentifierName.with("TEXT");
    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");

    private static final Parser<ParserToken, ParserContext> PERCENT_SYMBOL = symbol('%',
            SpreadsheetParserToken::percentSymbol,
            SpreadsheetPercentSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> OPEN_PARENTHESIS_SYMBOL = symbol('(',
            SpreadsheetParserToken::openParenthesisSymbol,
            SpreadsheetOpenParenthesisSymbolParserToken.class);
    private static final Parser<ParserToken, ParserContext> CLOSE_PARENTHESIS_SYMBOL = symbol(')',
            SpreadsheetParserToken::closeParenthesisSymbol,
            SpreadsheetCloseParenthesisSymbolParserToken.class);

    /**
     * Text
     */
    public static Parser<ParserToken, ParserContext> text() {
        return TEXT;
    }

    private final static Parser<ParserToken, ParserContext> TEXT = Parsers.doubleQuoted()
            .transform((doubleQuotedParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.text(doubleQuotedParserToken.value(), doubleQuotedParserToken.text()).cast())
            .setToString(SpreadsheetTextParserToken.class.getSimpleName());

    /**
     * Whitespace
     */
    public static Parser<ParserToken, ParserContext> whitespace() {
        return WHITESPACE;
    }

    private final static Parser<ParserToken, ParserContext> WHITESPACE = Parsers.<SpreadsheetParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
            .transform((stringParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.whitespace(stringParserToken.value(), stringParserToken.text()).cast())
            .setToString(SpreadsheetWhitespaceParserToken.class.getSimpleName())
            .cast();


    // helpers ...................................................................................................

    /**
     * Uses the grammar file to fetch one of the parsers.
     */
    private static Parser<SpreadsheetParserToken, SpreadsheetParserContext> parserFromGrammar(final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number,
                                                                                              final EbnfIdentifierName parserName) {
        Objects.requireNonNull(number, "number");

        try {
            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined = Maps.sorted();

            cellReferences(predefined);
            conditions(predefined);
            functions(predefined);
            math(predefined);
            misc(predefined, number);

            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> result = grammarLoader.grammar().get()
                    .combinator(predefined,
                            new SpreadsheetEbnfParserCombinatorSyntaxTreeTransformer());

            return result.get(parserName).orReport(ParserReporters.basic()).cast();
        } catch (final SpreadsheetParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause){
            throw new SpreadsheetParserException("Failed to return parser " + parserName + " from  file, message: " + cause.getMessage(), cause);
        }
    }

    /**
     * The filename of the resource holding the parsers grammar.
     */
    private final static String FILE_NAME = "spreadsheet-parsers.grammar";

    private final static EbnfGrammarLoader grammarLoader = EbnfGrammarLoader.with(FILE_NAME, SpreadsheetParsers.class);

    private static Parser<ParserToken, ParserContext> symbol(final char c,
                                                             final BiFunction<String, String, ParserToken> factory,
                                                             final Class<? extends SpreadsheetSymbolParserToken> tokenClass) {
        return Parsers.character(CharPredicates.is(c))
                .transform((charParserToken, context) -> factory.apply(charParserToken.value().toString(), charParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    private static Parser<ParserToken, ParserContext> symbol(final String text,
                                                             final BiFunction<String, String, ParserToken> factory,
                                                             final Class<? extends SpreadsheetSymbolParserToken> tokenClass) {
        return CaseSensitivity.INSENSITIVE.parser(text)
                .transform((stringParserToken, context) -> factory.apply(stringParserToken.value(), stringParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    /**
     * Stop construction
     */
    private SpreadsheetParsers() {
        throw new UnsupportedOperationException();
    }
}
