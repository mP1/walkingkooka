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

package walkingkooka.text.cursor.parser.spreadsheet.format;

import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarLoader;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.type.PublicStaticHelper;

import java.math.MathContext;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Parsers that know how to parse formatting patterns<br>.
 * <a href="https://support.office.com/en-us/article/number-format-codes-5026bbd6-04bc-48cd-bf33-80f18b4eae68>Formatting</a>
 */
public final class SpreadsheetFormatParsers implements PublicStaticHelper {


    public final static char DECIMAL_POINT = '.';

    // shared

    private static final EbnfIdentifierName WHITESPACE_IDENTIFIER = EbnfIdentifierName.with("WHITESPACE");
    private final static Parser<ParserToken, ParserContext> WHITESPACE = Parsers.<SpreadsheetFormatParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
            .transform((stringParserToken, spreadsheetFormatParserContext) -> SpreadsheetFormatParserToken.whitespace(stringParserToken.value(), stringParserToken.text()).cast())
            .setToString(SpreadsheetFormatWhitespaceParserToken.class.getSimpleName())
            .cast();

    // color..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a color format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> color() {
        return parserFromGrammar(COLOR_IDENTIFIER);
    }

    static final EbnfIdentifierName COLOR_IDENTIFIER = EbnfIdentifierName.with("COLOR");

    private static void color(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(COLOR_AND_BIGDECIMAL_IDENTIFIER, COLOR_AND_NUMBER);
        predefined.put(COLOR_NAME_IDENTIFIER, COLOR_NAME);
        predefined.put(COLOR_BIGDECIMAL_IDENTIFIER, COLOR_NUMBER);
    }

    private static final EbnfIdentifierName COLOR_AND_BIGDECIMAL_IDENTIFIER = EbnfIdentifierName.with("COLOR_AND_NUMBER");


    private static final EbnfIdentifierName COLOR_NAME_IDENTIFIER = EbnfIdentifierName.with("COLOR_NAME");

    private static final Parser<ParserToken, ParserContext> COLOR_NAME = Parsers.stringCharPredicate(CharPredicates.letter(), 1, Integer.MAX_VALUE)
            .transform(SpreadsheetFormatParsers::colorName)
            .setToString(COLOR_NAME_IDENTIFIER.toString())
            .cast();

    private static SpreadsheetFormatColorNameParserToken colorName(final StringParserToken string, final ParserContext context) {
        return SpreadsheetFormatParserToken.colorName(string.value(), string.text());
    }

    private static final EbnfIdentifierName COLOR_BIGDECIMAL_IDENTIFIER = EbnfIdentifierName.with("COLOR_NUMBER");

    private static final Parser<ParserToken, ParserContext> COLOR_NUMBER = Parsers.bigInteger(10)
            .transform(((bigIntegerParserToken, parserContext) -> SpreadsheetFormatParserToken.colorNumber(bigIntegerParserToken.value().intValueExact(), bigIntegerParserToken.text())))
            .setToString(COLOR_BIGDECIMAL_IDENTIFIER.toString())
            .cast();

    private static final Parser<ParserToken, ParserContext> COLOR_AND_NUMBER = CaseSensitivity.INSENSITIVE.parser("COLOR")
            .transform(SpreadsheetFormatParsers::colorLiteral)
            .builder(SpreadsheetFormatColorLiteralSymbolParserToken.NAME)
            .required(WHITESPACE, SpreadsheetFormatWhitespaceParserToken.NAME)
            .required(COLOR_NUMBER, SpreadsheetFormatColorNumberParserToken.NAME)
            .build()
            .setToString(COLOR_NAME_IDENTIFIER.toString())
            .cast();

    private static SpreadsheetFormatColorLiteralSymbolParserToken colorLiteral(final StringParserToken string, final ParserContext context) {
        return SpreadsheetFormatParserToken.colorLiteralSymbol(string.value(), string.text());
    }

    // conditional..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a condition format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> condition() {
        return parserFromGrammar(CONDITION_IDENTIFIER);
    }

    private static final EbnfIdentifierName CONDITION_IDENTIFIER = EbnfIdentifierName.with("CONDITION");

    private static void condition(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(CONDITION_BIGDECIMAL_LITERAL_IDENTIFIER, Parsers.bigDecimal(MathContext.UNLIMITED)
                .transform(SpreadsheetFormatParsers::conditionNumber)
                .setToString(CONDITION_BIGDECIMAL_LITERAL_IDENTIFIER.toString())
                .cast());

        predefined.put(EQUALS_SYMBOL_IDENTIFIER, EQUALS_SYMBOL);
        predefined.put(NOT_EQUALS_SYMBOL_IDENTIFIER, NOT_EQUALS_SYMBOL);
        predefined.put(GREATER_THAN_SYMBOL_IDENTIFIER, GREATER_THAN_SYMBOL);
        predefined.put(GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER, GREATER_THAN_EQUALS_SYMBOL);
        predefined.put(LESS_THAN_SYMBOL_IDENTIFIER, LESS_THAN_SYMBOL);
        predefined.put(LESS_THAN_EQUALS_SYMBOL_IDENTIFIER, LESS_THAN_EQUALS_SYMBOL);
    }

    private static final EbnfIdentifierName CONDITION_BIGDECIMAL_LITERAL_IDENTIFIER = EbnfIdentifierName.with("CONDITION_NUMBER");

    private static SpreadsheetFormatConditionNumberParserToken conditionNumber(final BigDecimalParserToken token, final ParserContext context) {
        return SpreadsheetFormatConditionNumberParserToken.with(token.value(), token.text());
    }

    private static final Parser<ParserToken, ParserContext> EQUALS_SYMBOL = symbol('=',
            SpreadsheetFormatParserToken::equalsSymbol,
            SpreadsheetFormatEqualsSymbolParserToken.class);
    private static final EbnfIdentifierName EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("EQUALS");

    private static final Parser<ParserToken, ParserContext> NOT_EQUALS_SYMBOL = symbol("!=",
            SpreadsheetFormatParserToken::notEqualsSymbol,
            SpreadsheetFormatNotEqualsSymbolParserToken.class);
    private static final EbnfIdentifierName NOT_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("NOT_EQUALS");

    private static final Parser<ParserToken, ParserContext> GREATER_THAN_SYMBOL = symbol('>',
            SpreadsheetFormatParserToken::greaterThanSymbol,
            SpreadsheetFormatGreaterThanSymbolParserToken.class);
    private static final EbnfIdentifierName GREATER_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN");

    private static final Parser<ParserToken, ParserContext> GREATER_THAN_EQUALS_SYMBOL = symbol(">=",
            SpreadsheetFormatParserToken::greaterThanEqualsSymbol,
            SpreadsheetFormatGreaterThanEqualsSymbolParserToken.class);
    private static final EbnfIdentifierName GREATER_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GREATER_THAN_EQUALS");

    private static final Parser<ParserToken, ParserContext> LESS_THAN_SYMBOL = symbol('<',
            SpreadsheetFormatParserToken::lessThanSymbol,
            SpreadsheetFormatLessThanSymbolParserToken.class);
    private static final EbnfIdentifierName LESS_THAN_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN");

    private static final Parser<ParserToken, ParserContext> LESS_THAN_EQUALS_SYMBOL = symbol("<=",
            SpreadsheetFormatParserToken::lessThanEqualsSymbol,
            SpreadsheetFormatLessThanEqualsSymbolParserToken.class);
    private static final EbnfIdentifierName LESS_THAN_EQUALS_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("LESS_THAN_EQUALS");

    // date..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a date format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> date() {
        return parserFromGrammar(DATE_GENERAL_IDENTIFIER);
    }

    private static final EbnfIdentifierName DATE_GENERAL_IDENTIFIER = EbnfIdentifierName.with("DATE_GENERAL");

    private static void date(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> parsers) {
        parsers.put(DAY_IDENTIFIER, DAY);
        parsers.put(YEAR_IDENTIFIER, YEAR);
    }

    private static final EbnfIdentifierName DAY_IDENTIFIER = EbnfIdentifierName.with("DAY");
    private static final Parser<ParserToken, ParserContext> DAY = repeatingSymbol('D',
            SpreadsheetFormatParserToken::day,
            SpreadsheetFormatDayParserToken.class);

    private static final EbnfIdentifierName YEAR_IDENTIFIER = EbnfIdentifierName.with("YEAR");
    private static final Parser<ParserToken, ParserContext> YEAR = repeatingSymbol('Y',
            SpreadsheetFormatParserToken::year,
            SpreadsheetFormatSecondParserToken.class);

    // dateTime..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a datetime format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> dateTime() {
        return parserFromGrammar(DATETIME_GENERAL_IDENTIFIER);
    }

    private static final EbnfIdentifierName DATETIME_GENERAL_IDENTIFIER = EbnfIdentifierName.with("DATETIME_GENERAL");

    private static void dateAndTime(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(MONTH_MINUTE_IDENTIFIER, MONTH_MINUTE);
    }

    private static final EbnfIdentifierName MONTH_MINUTE_IDENTIFIER = EbnfIdentifierName.with("MONTH_MINUTE");

    private static final Parser<ParserToken, ParserContext> MONTH_MINUTE = repeatingSymbol('M',
            SpreadsheetFormatParserToken::monthOrMinute,
            SpreadsheetFormatMonthOrMinuteParserToken.class);

    // expression...............................................................................................................

    /**
     * Returns a {@link Parser} that given text returns a {@link SpreadsheetFormatParserToken}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> expression() {
        return parserFromGrammar(EXPRESSION_IDENTIFIER);
    }

    static final EbnfIdentifierName EXPRESSION_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION");

    private static void format(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(EXPRESSION_SEPARATOR_IDENTIFIER, EXPRESSION_SEPARATOR_SYMBOL);
    }

    private static final Parser<ParserToken, ParserContext> EXPRESSION_SEPARATOR_SYMBOL = symbol(';',
            SpreadsheetFormatParserToken::separatorSymbol,
            SpreadsheetFormatSeparatorSymbolParserToken.class);
    private static final EbnfIdentifierName EXPRESSION_SEPARATOR_IDENTIFIER = EbnfIdentifierName.with("EXPRESSION_SEPARATOR");

    // general ..........................................................................................................

    /**
     * Returns a {@link Parser} that matches a general token.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> general() {
        return parserFromGrammar(GENERAL_IDENTIFIER);
    }

    static final EbnfIdentifierName GENERAL_IDENTIFIER = EbnfIdentifierName.with("GENERAL");

    private static void general(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(GENERAL_SYMBOL_IDENTIFIER, GENERAL_SYMBOL);
    }

    private static final EbnfIdentifierName GENERAL_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("GENERAL_SYMBOL");

    private static final Parser<ParserToken, ParserContext> GENERAL_SYMBOL = CaseSensitivity.INSENSITIVE
            .parser("GENERAL")
            .transform(SpreadsheetFormatParsers::generalSymbol)
            .setToString(GENERAL_SYMBOL_IDENTIFIER.toString());

    private static ParserToken generalSymbol(final StringParserToken token, final ParserContext context) {
        return SpreadsheetFormatParserToken.generalSymbol(token.value(), token.text());
    }

    // fraction..........................................................................................................

    /**
     * Returns a {@link Parser} that given text returns a {@link SpreadsheetFormatParserToken}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> fraction() {
        return parserFromGrammar(FRACTION_GENERAL_IDENTIFIER);
    }

    private static final EbnfIdentifierName FRACTION_GENERAL_IDENTIFIER = EbnfIdentifierName.with("FRACTION_GENERAL");

    /**
     * /**
     * Returns a {@link Parser} that given text returns a {@link SpreadsheetFormatParserToken}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> bigDecimal() {
        return parserFromGrammar(BIGDECIMAL_GENERAL_IDENTIFIER);
    }

    private static final EbnfIdentifierName BIGDECIMAL_GENERAL_IDENTIFIER = EbnfIdentifierName.with("BIGDECIMAL_GENERAL");

    private static void bigDecimal(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(BIGDECIMAL_DECIMAL_POINT_IDENTIFIER, DECIMAL_POINT_PARSER);
        predefined.put(CURRENCY_IDENTIFIER, CURRENCY);
        predefined.put(FRACTION_SYMBOL_IDENTIFIER, FRACTION_SYMBOL);
        predefined.put(LEADING_ZERO_IDENTIFIER, LEADING_ZERO);
        predefined.put(LEADING_SPACE_IDENTIFIER, LEADING_SPACE);
        predefined.put(NON_ZERO_IDENTIFIER, NON_ZERO);
        predefined.put(PERCENTAGE_IDENTIFIER, PERCENTAGE);
        predefined.put(THOUSANDS_IDENTIFIER, THOUSANDS);
    }

    private static final EbnfIdentifierName BIGDECIMAL_DECIMAL_POINT_IDENTIFIER = EbnfIdentifierName.with("BIGDECIMAL_DECIMAL_POINT");
    private static final Parser<ParserToken, ParserContext> DECIMAL_POINT_PARSER = symbol('.',
            SpreadsheetFormatParserToken::decimalPoint,
            SpreadsheetFormatDecimalPointParserToken.class);

    private static final EbnfIdentifierName CURRENCY_IDENTIFIER = EbnfIdentifierName.with("CURRENCY");
    private static final Parser<ParserToken, ParserContext> CURRENCY = symbol('$',
            SpreadsheetFormatParserToken::currency,
            SpreadsheetFormatCurrencyParserToken.class);


    private static final EbnfIdentifierName FRACTION_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("FRACTION_SYMBOL");
    private static final Parser<ParserToken, ParserContext> FRACTION_SYMBOL = symbol('/',
            SpreadsheetFormatParserToken::fractionSymbol,
            SpreadsheetFormatFractionSymbolParserToken.class);

    private static final EbnfIdentifierName LEADING_ZERO_IDENTIFIER = EbnfIdentifierName.with("LEADING_ZERO");
    private static final Parser<ParserToken, ParserContext> LEADING_ZERO = symbol('0',
            SpreadsheetFormatParserToken::digitLeadingZero,
            SpreadsheetFormatDigitLeadingZeroParserToken.class);

    private static final EbnfIdentifierName LEADING_SPACE_IDENTIFIER = EbnfIdentifierName.with("LEADING_SPACE");
    private static final Parser<ParserToken, ParserContext> LEADING_SPACE = symbol('?',
            SpreadsheetFormatParserToken::digitLeadingSpace,
            SpreadsheetFormatDigitLeadingSpaceParserToken.class);

    private static final EbnfIdentifierName NON_ZERO_IDENTIFIER = EbnfIdentifierName.with("NON_ZERO");
    private static final Parser<ParserToken, ParserContext> NON_ZERO = symbol('#',
            SpreadsheetFormatParserToken::digit,
            SpreadsheetFormatDigitParserToken.class);

    private static final EbnfIdentifierName PERCENTAGE_IDENTIFIER = EbnfIdentifierName.with("PERCENTAGE");
    private static final Parser<ParserToken, ParserContext> PERCENTAGE = symbol('%',
            SpreadsheetFormatParserToken::percentSymbol,
            SpreadsheetFormatPercentSymbolParserToken.class);

    private static final Parser<ParserToken, ParserContext> THOUSANDS = symbol(',',
            SpreadsheetFormatParserToken::thousands,
            SpreadsheetFormatThousandsParserToken.class);
    private static final EbnfIdentifierName THOUSANDS_IDENTIFIER = EbnfIdentifierName.with("THOUSANDS");

    // text..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a text format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> text() {
        return parserFromGrammar(TEXT_IDENTIFIER);
    }

    static final EbnfIdentifierName TEXT_IDENTIFIER = EbnfIdentifierName.with("TEXT");

    private static void text(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(QUOTED_IDENTIFIER, QUOTED);
        predefined.put(STAR_IDENTIFIER, STAR);
        predefined.put(TEXT_PLACEHOLDER_IDENTIFIER, TEXT_PLACEHOLDER);
        predefined.put(UNDERSCORE_IDENTIFIER, UNDERSCORE);
        predefined.put(WHITESPACE_IDENTIFIER, WHITESPACE);
    }

    private static final EbnfIdentifierName QUOTED_IDENTIFIER = EbnfIdentifierName.with("QUOTED");
    private static final Parser<ParserToken, ParserContext> QUOTED = Parsers.doubleQuoted()
            .transform((doubleQuotedParserToken, parserContext) -> SpreadsheetFormatParserToken.quotedText(doubleQuotedParserToken.value(), doubleQuotedParserToken.text()))
            .setToString("QUOTED")
            .cast();

    private static final EbnfIdentifierName STAR_IDENTIFIER = EbnfIdentifierName.with("STAR");
    private static final Parser<ParserToken, ParserContext> STAR = escapeStarOrUnderline('*',
            SpreadsheetFormatParserToken::star,
            SpreadsheetFormatStarParserToken.class);

    private static final EbnfIdentifierName TEXT_PLACEHOLDER_IDENTIFIER = EbnfIdentifierName.with("TEXT_PLACEHOLDER");
    private static final Parser<ParserToken, ParserContext> TEXT_PLACEHOLDER = symbol('@',
            SpreadsheetFormatParserToken::textPlaceholder,
            SpreadsheetFormatTextPlaceholderParserToken.class);

    private static final EbnfIdentifierName UNDERSCORE_IDENTIFIER = EbnfIdentifierName.with("UNDERSCORE");
    private static final Parser<ParserToken, ParserContext> UNDERSCORE = escapeStarOrUnderline('_',
            SpreadsheetFormatParserToken::underscore,
            SpreadsheetFormatUnderscoreParserToken.class);

    // time..............................................................................................................

    /**
     * Returns a {@link Parser} that returns a time format expression as {@link SpreadsheetFormatParserToken tokens}.
     */
    public static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> time() {
        return parserFromGrammar(TIME_GENERAL_IDENTIFIER);
    }

    static final EbnfIdentifierName TIME_GENERAL_IDENTIFIER = EbnfIdentifierName.with("TIME_GENERAL");

    private static void time(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(A_SLASH_P_IDENTIFIER, A_SLASH_P);
        predefined.put(AM_SLASH_PM_IDENTIFIER, AM_SLASH_PM);
        predefined.put(HOUR_IDENTIFIER, HOUR);
        predefined.put(SECOND_IDENTIFIER, SECOND);
    }

    private static final EbnfIdentifierName A_SLASH_P_IDENTIFIER = EbnfIdentifierName.with("A_SLASH_P");
    private static final Parser<ParserToken, ParserContext> A_SLASH_P = symbol("A/P",
            SpreadsheetFormatParserToken::amPm,
            SpreadsheetFormatAmPmParserToken.class);

    private static final EbnfIdentifierName AM_SLASH_PM_IDENTIFIER = EbnfIdentifierName.with("AM_SLASH_PM");
    private static final Parser<ParserToken, ParserContext> AM_SLASH_PM = symbol("AM/PM",
            SpreadsheetFormatParserToken::amPm,
            SpreadsheetFormatAmPmParserToken.class);

    private static final EbnfIdentifierName HOUR_IDENTIFIER = EbnfIdentifierName.with("HOUR");
    private static final Parser<ParserToken, ParserContext> HOUR = repeatingSymbol('H',
            SpreadsheetFormatParserToken::hour,
            SpreadsheetFormatHourParserToken.class);

    private static final EbnfIdentifierName SECOND_IDENTIFIER = EbnfIdentifierName.with("SECOND");
    private static final Parser<ParserToken, ParserContext> SECOND = repeatingSymbol('S',
            SpreadsheetFormatParserToken::second,
            SpreadsheetFormatSecondParserToken.class);

    // misc..............................................................................................................

    private static void misc(final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined) {
        predefined.put(OPEN_SQUARE_BRACKET_IDENTIFIER, OPEN_SQUARE_BRACKET);
        predefined.put(CLOSE_SQUARE_BRACKET_IDENTIFIER, CLOSE_SQUARE_BRACKET);

        predefined.put(ESCAPE_IDENTIFIER, ESCAPE);
        predefined.put(LITERAL_IDENTIFIER, LITERAL);
        predefined.put(LITERAL2_IDENTIFIER, LITERAL2);
    }

    private static final EbnfIdentifierName OPEN_SQUARE_BRACKET_IDENTIFIER = EbnfIdentifierName.with("OPEN_SQUARE_BRACKET");
    private static final Parser<ParserToken, ParserContext> OPEN_SQUARE_BRACKET = symbol('[',
            SpreadsheetFormatParserToken::openBracketSymbol,
            SpreadsheetFormatOpenBracketSymbolParserToken.class);

    private static final EbnfIdentifierName CLOSE_SQUARE_BRACKET_IDENTIFIER = EbnfIdentifierName.with("CLOSE_SQUARE_BRACKET");
    private static final Parser<ParserToken, ParserContext> CLOSE_SQUARE_BRACKET = symbol(']',
            SpreadsheetFormatParserToken::closeBracketSymbol,
            SpreadsheetFormatCloseBracketSymbolParserToken.class);

    private static final EbnfIdentifierName ESCAPE_IDENTIFIER = EbnfIdentifierName.with("ESCAPE");
    private static final Parser<ParserToken, ParserContext> ESCAPE = escapeStarOrUnderline('\\',
            SpreadsheetFormatParserToken::escape,
            SpreadsheetFormatEscapeParserToken.class);

    // used by all formatters except for NUMBER which loses the slash which is reserved for fractions.
    private static final EbnfIdentifierName LITERAL_IDENTIFIER = EbnfIdentifierName.with("LITERAL");
    private static final Parser<ParserToken, ParserContext> LITERAL = literal("$-+/(): ", LITERAL_IDENTIFIER);

    // identical to {@link #LITERAL} less '$' and '/'. $ for number/fraction should will be matched as a currency
    private static final EbnfIdentifierName LITERAL2_IDENTIFIER = EbnfIdentifierName.with("LITERAL2");
    private static final Parser<ParserToken, ParserContext> LITERAL2 = literal("-+(): ", LITERAL2_IDENTIFIER);

    // helpers..............................................................................................................

    /**
     * Parsers the grammar and returns the selected parser.
     */
    private static Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parserFromGrammar(final EbnfIdentifierName parserName) {
        try {
            final Optional<EbnfGrammarParserToken> grammar = grammarLoader.grammar();

            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> predefined = Maps.sorted();

            format(predefined);
            color(predefined);
            condition(predefined);
            date(predefined);
            dateAndTime(predefined);
            general(predefined);
            bigDecimal(predefined);
            text(predefined);
            time(predefined);

            misc(predefined);

            final Map<EbnfIdentifierName, Parser<ParserToken, ParserContext>> result = grammar.get()
                    .combinator(predefined,
                            new SpreadsheetFormatEbnfParserCombinatorSyntaxTreeTransformer());

            return result.get(parserName)
                    .cast();
        } catch (final SpreadsheetFormatParserException rethrow) {
            throw rethrow;
        } catch (final Exception cause) {
            throw new SpreadsheetFormatParserException("Failed to return parsers from excel format grammar file, message: " + cause.getMessage(), cause);
        }
    }

    private final static EbnfGrammarLoader grammarLoader = EbnfGrammarLoader.with("excel-format-parsers.grammar", SpreadsheetFormatParsers.class);

    private static Parser<ParserToken, ParserContext> literal(final String any, final EbnfIdentifierName name) {
        return Parsers.character(CharPredicates.any(any))
                .transform(((characterParserToken, parserContext) -> SpreadsheetFormatParserToken.textLiteral(characterParserToken.value().toString(), characterParserToken.text())))
                .setToString(name.toString())
                .cast();
    }

    /**
     * Matches a token filled with the given c ignoring case.
     */
    private static Parser<ParserToken, ParserContext> repeatingSymbol(final char c,
                                                                      final BiFunction<String, String, ParserToken> factory,
                                                                      final Class<? extends SpreadsheetFormatLeafParserToken> tokenClass) {
        return Parsers.stringCharPredicate(CaseSensitivity.INSENSITIVE.charPredicate(c), 1, Integer.MAX_VALUE)
                .transform((stringParserToken, context) -> factory.apply(stringParserToken.value(), stringParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    /**
     * Matches a token holding a single character.
     */
    private static Parser<ParserToken, ParserContext> symbol(final char c,
                                                             final BiFunction<String, String, ParserToken> factory,
                                                             final Class<? extends SpreadsheetFormatLeafParserToken> tokenClass) {
        return Parsers.character(CaseSensitivity.SENSITIVE.charPredicate(c))
                .transform((charParserToken, context) -> factory.apply(charParserToken.value().toString(), charParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    private static Parser<ParserToken, ParserContext> symbol(final String text,
                                                             final BiFunction<String, String, ParserToken> factory,
                                                             final Class<? extends SpreadsheetFormatLeafParserToken> tokenClass) {
        return CaseSensitivity.INSENSITIVE.parser(text)
                .transform((charParserToken, context) -> factory.apply(charParserToken.value(), charParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    private static Parser<ParserToken, ParserContext> escapeStarOrUnderline(final char initial,
                                                                            final BiFunction<Character, String, ParserToken> factory,
                                                                            final Class<? extends SpreadsheetFormatLeafParserToken> tokenClass) {
        return Parsers.stringInitialAndPartCharPredicate(CharPredicates.is(initial), CharPredicates.always(), 1, 2)
                .transform((stringParserToken, context) -> factory.apply(stringParserToken.value().charAt(1), stringParserToken.text()))
                .setToString(tokenClass.getSimpleName())
                .cast();
    }

    /**
     * Stop construction
     */
    private SpreadsheetFormatParsers() {
        throw new UnsupportedOperationException();
    }
}
