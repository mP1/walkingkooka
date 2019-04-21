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
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.DoubleQuotedParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarLoader;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.type.PublicStaticHelper;

import java.math.MathContext;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Numerous {@link Parser parsers} that parse individual components of a formula or an entire formula.
 */
public final class SpreadsheetParsers implements PublicStaticHelper {

    /**
     * Range separator character used to separate the lower and upper bounds.
     */
    public static final CharacterConstant RANGE_SEPARATOR = CharacterConstant.with(':');

    /**
     * A {@link Parser} that returns a cell reference token of some sort.
     */
    public static Parser<SpreadsheetParserContext> cellReferences() {
        return parserFromGrammar(CELL_IDENTIFIER);
    }

    private static final EbnfIdentifierName CELL_IDENTIFIER = EbnfIdentifierName.with("CELL");

    private static void cellReferences(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(COLUMN_ROW_IDENTIFIER, columnAndRow());
        predefined.put(LABEL_NAME_IDENTIFIER, labelName());
        predefined.put(BETWEEN_SYMBOL_IDENTIFIER, BETWEEN_SYMBOL);
    }

    private static final EbnfIdentifierName COLUMN_ROW_IDENTIFIER = EbnfIdentifierName.with("COLUMN_ROW");
    private static final EbnfIdentifierName LABEL_NAME_IDENTIFIER = EbnfIdentifierName.with("LABEL_NAME");
    private static final EbnfIdentifierName BETWEEN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("BETWEEN_SYMBOL");

    private static final Parser<ParserContext> BETWEEN_SYMBOL = symbol(RANGE_SEPARATOR.character(),
            SpreadsheetParserToken::betweenSymbol,
            SpreadsheetBetweenSymbolParserToken.class);

    /**
     * {@see SpreadsheetColumnReferenceParser}
     */
    public static Parser<ParserContext> column() {
        return Cast.to(SpreadsheetColumnReferenceParser.INSTANCE);
    }

    /**
     * {@see SpreadsheetColumnReferenceParser}
     */
    public static Parser<ParserContext> columnAndRow() {
        return COLUMN_AND_ROW;
    }

    private static final Parser<ParserContext> COLUMN_AND_ROW = column()
            .builder()
            .required(row())
            .build()
            .transform((sequenceParserToken, spreadsheetParserContext) -> {
                return SpreadsheetParserToken.cellReference(SequenceParserToken.class.cast(sequenceParserToken).value(), sequenceParserToken.text()).cast();
            });

    /**
     * Returns a {@link Parser} that parsers expressions.
     */
    public static Parser<SpreadsheetParserContext> expression() {
        return parserFromGrammar(EXPRESSION_IDENTIFIER);
    }

    private static final EbnfIdentifierName EXPRESSION_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION");

    /**
     * Returns a {@link Parser} that parsers function invocations, starting with the name and parameters.
     */
    public static Parser<SpreadsheetParserContext> function() {
        return parserFromGrammar(FUNCTION_IDENTIFIER);
    }

    static final EbnfIdentifierName FUNCTION_IDENTIFIER = EbnfIdentifierName.with("FUNCTION");

    /**
     * A parser that returns {@see SpreadsheetFunctionName}
     */
    public static Parser<ParserContext> functionName() {
        return FUNCTION_NAME;
    }

    private final static Parser<ParserContext> FUNCTION_NAME = Parsers.<SpreadsheetParserContext>stringInitialAndPartCharPredicate(
            SpreadsheetFunctionName.INITIAL,
            SpreadsheetFunctionName.PART,
            1,
            SpreadsheetFunctionName.MAX_LENGTH)
            .transform((stringParserToken, spreadsheetParserContext) -> Cast.to(SpreadsheetParserToken.functionName(SpreadsheetFunctionName.with(StringParserToken.class.cast(stringParserToken).value()), stringParserToken.text())))
            .setToString(SpreadsheetFunctionName.class.getSimpleName())
            .cast();

    private static void functions(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(FUNCTION_NAME_IDENTIFIER, functionName());
        predefined.put(FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER, FUNCTION_PARAMETER_SEPARATOR_SYMBOL);
    }

    private static final Parser<ParserContext> FUNCTION_PARAMETER_SEPARATOR_SYMBOL = symbol(',',
            SpreadsheetParserToken::functionParameterSeparatorSymbol,
            SpreadsheetFunctionParameterSeparatorSymbolParserToken.class);

    private static final EbnfIdentifierName FUNCTION_NAME_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_NAME");
    private static final EbnfIdentifierName FUNCTION_PARAMETER_SEPARATOR_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("FUNCTION_PARAMETER_SEPARATOR_SYMBOL");


    /**
     * {@see SpreadsheetLabelNameParser}
     */
    public static Parser<ParserContext> labelName() {
        return SpreadsheetLabelNameParser.INSTANCE.cast();
    }

    /**
     * A {@link Parser} that returns a range which will include cell references or labels.
     */
    public static Parser<SpreadsheetParserContext> range() {
        return parserFromGrammar(RANGE_IDENTIFIER);
    }

    private static final EbnfIdentifierName RANGE_IDENTIFIER = EbnfIdentifierName.with("RANGE");

    /**
     * {@see SpreadsheetRowReferenceParser}
     */
    public static Parser<ParserContext> row() {
        return Cast.to(SpreadsheetRowReferenceParser.INSTANCE);
    }

    // conditions.............................................................................................................

    private static void conditions(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(EQUALS_SYMBOL_IDENTIFIER, EQUALS_SYMBOL);
        predefined.put(NOT_EQUALS_SYMBOL_IDENTIFIER, NOT_EQUALS_SYMBOL);

        predefined.put(GREATER_THAN_SYMBOL_IDENTIFIER, GREATER_THAN_SYMBOL);
        predefined.put(GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER, GREATER_THAN_EQUALS_SYMBOL);
        predefined.put(LESS_THAN_SYMBOL_IDENTIFIER, LESS_THAN_SYMBOL);
        predefined.put(LESS_THAN_EQUALS_SYMBOL_IDENTIFIER, LESS_THAN_EQUALS_SYMBOL);
    }

    private static final Parser<ParserContext> EQUALS_SYMBOL = symbol("==",
            SpreadsheetParserToken::equalsSymbol,
            SpreadsheetEqualsSymbolParserToken.class);
    private static final Parser<ParserContext> NOT_EQUALS_SYMBOL = symbol("!=",
            SpreadsheetParserToken::notEqualsSymbol,
            SpreadsheetNotEqualsSymbolParserToken.class);
    private static final Parser<ParserContext> GREATER_THAN_SYMBOL = symbol('>',
            SpreadsheetParserToken::greaterThanSymbol,
            SpreadsheetGreaterThanSymbolParserToken.class);
    private static final Parser<ParserContext> GREATER_THAN_EQUALS_SYMBOL = symbol(">=",
            SpreadsheetParserToken::greaterThanEqualsSymbol,
            SpreadsheetGreaterThanEqualsSymbolParserToken.class);
    private static final Parser<ParserContext> LESS_THAN_SYMBOL = symbol('<',
            SpreadsheetParserToken::lessThanSymbol,
            SpreadsheetLessThanSymbolParserToken.class);
    private static final Parser<ParserContext> LESS_THAN_EQUALS_SYMBOL = symbol("<=",
            SpreadsheetParserToken::lessThanEqualsSymbol,
            SpreadsheetLessThanEqualsSymbolParserToken.class);

    private static final EbnfIdentifierName EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("EQUALS_SYMBOL");
    private static final EbnfIdentifierName NOT_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("NOT_EQUALS_SYMBOL");

    private static final EbnfIdentifierName GREATER_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_SYMBOL");
    private static final EbnfIdentifierName GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_EQUALS_SYMBOL");
    private static final EbnfIdentifierName LESS_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_SYMBOL");
    private static final EbnfIdentifierName LESS_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_EQUALS_SYMBOL");

    // math.............................................................................................................

    private static void math(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
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

    private static final Parser<ParserContext> PLUS_SYMBOL = symbol('+',
            SpreadsheetParserToken::plusSymbol,
            SpreadsheetPlusSymbolParserToken.class);
    private static final Parser<ParserContext> MINUS_SYMBOL = symbol('-',
            SpreadsheetParserToken::minusSymbol,
            SpreadsheetMinusSymbolParserToken.class);
    private static final Parser<ParserContext> MULTIPLY_SYMBOL = symbol('*',
            SpreadsheetParserToken::multiplySymbol,
            SpreadsheetMultiplySymbolParserToken.class);
    private static final Parser<ParserContext> DIVIDE_SYMBOL = symbol('/',
            SpreadsheetParserToken::divideSymbol,
            SpreadsheetDivideSymbolParserToken.class);
    private static final Parser<ParserContext> POWER_SYMBOL = symbol('^',
            SpreadsheetParserToken::powerSymbol,
            SpreadsheetPowerSymbolParserToken.class);

    // misc.............................................................................................................

    private static void misc(final Map<EbnfIdentifierName, Parser<ParserContext>> predefined) {
        predefined.put(NUMBER_IDENTIFIER, NUMBER);
        predefined.put(PERCENT_SYMBOL_IDENTIFIER, PERCENT_SYMBOL);

        predefined.put(OPEN_PARENTHESIS_SYMBOL_IDENTIFIER, OPEN_PARENTHESIS_SYMBOL);
        predefined.put(CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER, CLOSE_PARENTHESIS_SYMBOL);

        predefined.put(TEXT_IDENTIFIER, text());

        predefined.put(WHITESPACE_IDENTIFIER, whitespace());
    }

    private static final EbnfIdentifierName NUMBER_IDENTIFIER = EbnfIdentifierName.with("NUMBER");
    private static final Parser<ParserContext> NUMBER = Parsers.bigDecimal(MathContext.UNLIMITED)
            .transform((numberParserToken, parserContext) -> SpreadsheetParserToken.bigDecimal(BigDecimalParserToken.class.cast(numberParserToken).value(), numberParserToken.text()))
            .cast();

    private static final EbnfIdentifierName PERCENT_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("PERCENT_SYMBOL");
    private static final EbnfIdentifierName OPEN_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("OPEN_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName CLOSE_PARENTHESIS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("CLOSE_PARENTHESIS_SYMBOL");
    private static final EbnfIdentifierName TEXT_IDENTIFIER = EbnfIdentifierName.with("TEXT");
    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");

    private static final Parser<ParserContext> PERCENT_SYMBOL = symbol('%',
            SpreadsheetParserToken::percentSymbol,
            SpreadsheetPercentSymbolParserToken.class);
    private static final Parser<ParserContext> OPEN_PARENTHESIS_SYMBOL = symbol('(',
            SpreadsheetParserToken::openParenthesisSymbol,
            SpreadsheetOpenParenthesisSymbolParserToken.class);
    private static final Parser<ParserContext> CLOSE_PARENTHESIS_SYMBOL = symbol(')',
            SpreadsheetParserToken::closeParenthesisSymbol,
            SpreadsheetCloseParenthesisSymbolParserToken.class);

    /**
     * Text
     */
    public static Parser<ParserContext> text() {
        return TEXT;
    }

    private final static Parser<ParserContext> TEXT = Parsers.doubleQuoted()
            .transform((doubleQuotedParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.text(DoubleQuotedParserToken.class.cast(doubleQuotedParserToken).value(), doubleQuotedParserToken.text()).cast())
            .setToString(SpreadsheetTextParserToken.class.getSimpleName());

    /**
     * Whitespace
     */
    public static Parser<ParserContext> whitespace() {
        return WHITESPACE;
    }

    private final static Parser<ParserContext> WHITESPACE = Parsers.<SpreadsheetParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
            .transform((stringParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.whitespace(StringParserToken.class.cast(stringParserToken).value(), stringParserToken.text()).cast())
            .setToString(SpreadsheetWhitespaceParserToken.class.getSimpleName())
            .cast();


    // helpers ...................................................................................................

    /**
     * Uses the grammar file to fetch one of the parsers.
     */
    private static Parser<SpreadsheetParserContext> parserFromGrammar(final EbnfIdentifierName parserName) {
        try {
            final Map<EbnfIdentifierName, Parser<ParserContext>> predefined = Maps.sorted();

            cellReferences(predefined);
            conditions(predefined);
            functions(predefined);
            math(predefined);
            misc(predefined);

            final Map<EbnfIdentifierName, Parser<ParserContext>> result = GRAMMAR_LOADER.grammar().get()
                    .combinator(predefined,
                            new SpreadsheetEbnfParserCombinatorSyntaxTreeTransformer());

            return result.get(parserName).cast();
        } catch (final SpreadsheetParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause) {
            throw new SpreadsheetParserException("Failed to return parser " + parserName + " from  file, message: " + cause.getMessage(), cause);
        }
    }

    /**
     * Handles loading, parsing and caching the grammar file and combinators.
     */
    private final static EbnfGrammarLoader GRAMMAR_LOADER = EbnfGrammarLoader.with("spreadsheet-parsers.grammar", SpreadsheetParsers.class);

    private static Parser<ParserContext> symbol(final char c,
                                                final BiFunction<String, String, ParserToken> factory,
                                                final Class<? extends SpreadsheetSymbolParserToken> tokenClass) {
        return Parsers.character(CharPredicates.is(c))
                .transform((charParserToken, context) -> factory.apply(CharacterParserToken.class.cast(charParserToken).value().toString(), charParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    private static Parser<ParserContext> symbol(final String text,
                                                final BiFunction<String, String, ParserToken> factory,
                                                final Class<? extends SpreadsheetSymbolParserToken> tokenClass) {
        return CaseSensitivity.INSENSITIVE.parser(text)
                .transform((stringParserToken, context) -> factory.apply(StringParserToken.class.cast(stringParserToken).value(), stringParserToken.text()))
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
