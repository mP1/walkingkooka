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

import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.RepeatedOrSequenceParserToken;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Helps transform the EBNF grammar into a {@link Parser} which will then return a {@link SpreadsheetFormatParserToken}
 */
final class SpreadsheetFormatEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer {

    @Override
    public Parser<ParserToken, ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser.transform(this::alternatives); // leave alternative declarations as is.
    }

    private ParserToken alternatives(final ParserToken sequence, final ParserContext context) {
        return sequence;
    }

    @Override
    public Parser<ParserToken, ParserContext> concatenation(final EbnfConcatenationParserToken token, final Parser<SequenceParserToken, ParserContext> parser) {
        return parser.transform(this::concatenation);
    }

    private ParserToken concatenation(final SequenceParserToken sequence, final ParserContext context) {
        return sequence.removeMissing();
    }

    @Override
    public Parser<ParserToken, ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserToken, ParserContext> parser) {
        throw new UnsupportedOperationException(token.text()); // there are no exception tokens.
    }

    @Override
    public Parser<ParserToken, ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser; //leaver group definitions as they are.
    }

    /**
     * For identified rules, the {@link SequenceParserToken} are flattened, missings removed and the {@link SpreadsheetFormatParentParserToken}
     * created.
     */
    @Override
    public Parser<ParserToken, ParserContext> identifier(final EbnfIdentifierParserToken token,
                                                         final Parser<ParserToken, ParserContext> parser) {
        final EbnfIdentifierName name = token.value();
        return name.equals(SpreadsheetFormatParsers.COLOR_IDENTIFIER) ?
                parser.transform(this::color) :
                name.equals(CONDITION_EQUAL_IDENTIFIER) ?
                        parser.transform(this::conditionEqual) :
                        name.equals(CONDITION_GREATER_THAN_IDENTIFIER) ?
                                parser.transform(this::conditionGreaterThan) :
                                name.equals(CONDITION_GREATER_THAN_EQUAL_IDENTIFIER) ?
                                        parser.transform(this::conditionGreaterThanEqual) :
                                        name.equals(CONDITION_LESS_THAN_IDENTIFIER) ?
                                                parser.transform(this::conditionLessThan) :
                                                name.equals(CONDITION_LESS_THAN_EQUAL_IDENTIFIER) ?
                                                        parser.transform(this::conditionLessThanEqual) :
                                                        name.equals(CONDITION_NOT_EQUAL_IDENTIFIER) ?
                                                                parser.transform(this::conditionNotEqual) :
                                                                name.equals(DATE_IDENTIFIER) || name.equals(DATE2_IDENTIFIER) ?
                                                                        parser.transform(this::date) :
                                                                        name.equals(DATETIME_IDENTIFIER) || name.equals(DATETIME2_IDENTIFIER) ?
                                                                                parser.transform(this::dateTime) :
                                                                                name.equals(SpreadsheetFormatParsers.EXPRESSION_IDENTIFIER) ?
                                                                                        parser.transform(this::expression) :
                                                                                        name.equals(BIGDECIMAL_IDENTIFIER) ?
                                                                                                parser.transform(this::bigDecimal) :
                                                                                                name.equals(BIGDECIMAL_EXPONENT_IDENTIFIER) ?
                                                                                                        parser.transform(this::bigDecimalExponent) :
                                                                                                        name.equals(BIGDECIMAL_EXPONENT_SYMBOL_IDENTIFIER) ?
                                                                                                                parser.transform(this::exponentSymbol) :
                                                                                                                name.equals(FRACTION_IDENTIFIER) ?
                                                                                                                        parser.transform(this::fraction) :
                                                                                                                        name.equals(SpreadsheetFormatParsers.GENERAL_IDENTIFIER) ?
                                                                                                                                parser.transform(this::general) :
                                                                                                                                name.equals(SpreadsheetFormatParsers.TEXT_IDENTIFIER) ?
                                                                                                                                        parser.transform(this::text) :
                                                                                                                                        name.equals(TIME_IDENTIFIER) || name.equals(TIME2_IDENTIFIER) ?
                                                                                                                                                parser.transform(this::time) :
                                                                                                                                                this.requiredCheck(name, parser);
    }

    private ParserToken color(final ParserToken token, final ParserContext context) {
        return SpreadsheetFormatParserToken.color(this.clean0(token.cast()), token.text());
    }

    private ParserToken conditionEqual(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::equalsParserToken);
    }

    private static final EbnfIdentifierName CONDITION_EQUAL_IDENTIFIER = EbnfIdentifierName.with("CONDITION_EQUAL");

    private ParserToken conditionGreaterThan(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::greaterThan);
    }

    private static final EbnfIdentifierName CONDITION_GREATER_THAN_IDENTIFIER = EbnfIdentifierName.with("CONDITION_GREATER_THAN");

    private ParserToken conditionGreaterThanEqual(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::greaterThanEquals);
    }

    private static final EbnfIdentifierName CONDITION_GREATER_THAN_EQUAL_IDENTIFIER = EbnfIdentifierName.with("CONDITION_GREATER_THAN_EQUAL");

    private ParserToken conditionLessThan(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::lessThan);
    }

    private static final EbnfIdentifierName CONDITION_LESS_THAN_IDENTIFIER = EbnfIdentifierName.with("CONDITION_LESS_THAN");

    private ParserToken conditionLessThanEqual(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::lessThanEquals);
    }

    private static final EbnfIdentifierName CONDITION_LESS_THAN_EQUAL_IDENTIFIER = EbnfIdentifierName.with("CONDITION_LESS_THAN_EQUAL");

    private ParserToken conditionNotEqual(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::notEquals);
    }

    private static final EbnfIdentifierName CONDITION_NOT_EQUAL_IDENTIFIER = EbnfIdentifierName.with("CONDITION_NOT_EQUAL");

    private ParserToken date(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::date);
    }

    private static final EbnfIdentifierName DATE_IDENTIFIER = EbnfIdentifierName.with("DATE");
    private static final EbnfIdentifierName DATE2_IDENTIFIER = EbnfIdentifierName.with("DATE2");

    private ParserToken dateTime(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::dateTime);
    }

    private static final EbnfIdentifierName DATETIME_IDENTIFIER = EbnfIdentifierName.with("DATETIME");
    private static final EbnfIdentifierName DATETIME2_IDENTIFIER = EbnfIdentifierName.with("DATETIME2");

    private ParserToken bigDecimal(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::bigDecimal);
    }

    private static final EbnfIdentifierName BIGDECIMAL_IDENTIFIER = EbnfIdentifierName.with("BIGDECIMAL");

    private static final EbnfIdentifierName BIGDECIMAL_EXPONENT_IDENTIFIER = EbnfIdentifierName.with("BIGDECIMAL_EXPONENT");

    private ParserToken bigDecimalExponent(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::exponent);
    }

    private static final EbnfIdentifierName BIGDECIMAL_EXPONENT_SYMBOL_IDENTIFIER = EbnfIdentifierName.with("BIGDECIMAL_EXPONENT_SYMBOL");

    private ParserToken exponentSymbol(final ParserToken token, final ParserContext context) {
        final StringParserToken stringParserToken = StringParserToken.class.cast(token);
        return SpreadsheetFormatParserToken.exponentSymbol(stringParserToken.value(), stringParserToken.text());
    }

    private ParserToken expression(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::expression);
    }

    private ParserToken fraction(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::fraction);
    }

    private static final EbnfIdentifierName FRACTION_IDENTIFIER = EbnfIdentifierName.with("FRACTION");

    private ParserToken general(final ParserToken token, final ParserContext context) {
        return SpreadsheetFormatParserToken.general(this.clean0(token.cast()), token.text());
    }

    private ParserToken text(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::text);
    }

    private ParserToken time(final ParserToken token, final ParserContext context) {
        return this.clean(token, SpreadsheetFormatParserToken::time);
    }

    private static final EbnfIdentifierName TIME_IDENTIFIER = EbnfIdentifierName.with("TIME");
    private static final EbnfIdentifierName TIME2_IDENTIFIER = EbnfIdentifierName.with("TIME2");

    private ParserToken clean(final ParserToken token, final BiFunction<List<ParserToken>, String, ParserToken> factory) {
        return token.isMissing() ?
                token :
                factory.apply(clean0(token.cast()), token.text());
    }

    private List<ParserToken> clean0(final RepeatedOrSequenceParserToken<?> token) {
        return token.flat()
                .value();
    }

    private Parser<ParserToken, ParserContext> requiredCheck(final EbnfIdentifierName name, final Parser<ParserToken, ParserContext> parser) {
        return name.value().endsWith("REQUIRED") ?
                parser.orReport(ParserReporters.basic()) :
                parser; // leave as is...
    }

    @Override
    public Parser<ParserToken, ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser; // leave optionals alone...
    }

    @Override
    public Parser<ParserToken, ParserContext> range(final EbnfRangeParserToken token, final Parser<SequenceParserToken, ParserContext> parserd) {
        throw new UnsupportedOperationException(token.text()); // there are no ranges...
    }

    @Override
    public Parser<RepeatedParserToken, ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<RepeatedParserToken, ParserContext> parser) {
        return parser.transform(this::repeated);
    }

    private RepeatedParserToken repeated(final RepeatedParserToken sequence, final ParserContext context) {
        return sequence;
    }

    @Override
    public Parser<ParserToken, ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<StringParserToken, ParserContext> parser) {
        return parser.cast();
    }
}
