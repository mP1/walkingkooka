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
package walkingkooka.text.cursor.parser;

import walkingkooka.datetime.DateTimeContext;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A collection of factory methods to create parsers.
 */
public final class Parsers implements PublicStaticHelper {

    /**
     * {@see AlternativesParser}
     */
    public static <C extends ParserContext> Parser<C> alternatives(final List<Parser<C>> parsers) {
        return AlternativesParser.with(parsers);
    }

    /**
     * {@see AndNotParser}
     */
    static <C extends ParserContext> Parser<C> andNot(final Parser<C> left, final Parser<C> right) {
        return AndNotParser.with(left, right);
    }

    /**
     * {@see BigDecimalParser}
     */
    public static <C extends ParserContext> Parser<C> bigDecimal() {
        return BigDecimalParser.with();
    }

    /**
     * {@see BigIntegerParser}
     */
    public static <C extends ParserContext> Parser<C> bigInteger(final int radix) {
        return BigIntegerParser.with(radix);
    }

    /**
     * {@see CharacterCharPredicateParser}
     */
    public static <C extends ParserContext> Parser<C> character(final CharPredicate predicate) {
        return CharacterCharPredicateParser.with(predicate);
    }

    /**
     * {@see CustomToStringParser}
     */
    public static <C extends ParserContext> Parser<C> customToString(final Parser<C> parser, final String toString) {
        return CustomToStringParser.wrap(parser, toString);
    }

    /**
     * {@see DoubleParser}
     */
    public static <C extends ParserContext> Parser<C> doubleParser() {
        return DoubleParser.instance();
    }

    /**
     * {@see DoubleQuotedParserToken}
     */
    public static <C extends ParserContext> Parser<C> doubleQuoted() {
        return DoubleQuotedParser.instance();
    }

    /**
     * {@see FakeParser}
     */
    public static <C extends ParserContext> Parser<C> fake() {
        return FakeParser.create();
    }

    /**
     * {@see DateTimeFormatterParserLocalDate}
     */
    public static <C extends ParserContext> Parser<C> localDate(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalDate.with(formatter);
    }

    /**
     * {@see DateTimeFormatterParserLocalDateTime}
     */
    public static <C extends ParserContext> Parser<C> localDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalDateTime.with(formatter);
    }

    /**
     * {@see DateTimeFormatterParserLocalTime}
     */
    public static <C extends ParserContext> Parser<C> localTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalTime.with(formatter);
    }

    /**
     * {@see LongParser}
     */
    public static <C extends ParserContext> Parser<C> longParser(final int radix) {
        return LongParser.with(radix);
    }

    /**
     * {@see DateTimeFormatterParserOffsetDateTime}
     */
    public static <C extends ParserContext> Parser<C> offsetDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserOffsetDateTime.with(formatter);
    }

    /**
     * {@see DateTimeFormatterParserOffsetTime}
     */
    public static <C extends ParserContext> Parser<C> offsetTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserOffsetTime.with(formatter);
    }

    /**
     * {@see RepeatedParser}
     */
    public static <C extends ParserContext> Parser<C> repeated(final Parser<C> parser) {
        return RepeatedParser.with(parser);
    }

    /**
     * {@see ReportingParser}
     */
    public static <C extends ParserContext> Parser<C> report(final ParserReporterCondition condition,
                                                             final ParserReporter<C> reporter,
                                                             final Parser<C> parser) {
        return ReportingParser.with(condition, reporter, parser);
    }

    /**
     * {@see SequenceParserBuilder}
     */
    public static <C extends ParserContext> SequenceParserBuilder<C> sequenceParserBuilder() {
        return SequenceParserBuilder.empty();
    }

    /**
     * {@see SingleQuotedParserToken}
     */
    public static <C extends ParserContext> Parser<C> singleQuoted() {
        return SingleQuotedParser.instance();
    }

    /**
     * {@see StringCharPredicateParser}
     */
    public static <C extends ParserContext> Parser<C> stringCharPredicate(final CharPredicate predicate,
                                                                          final int minLength,
                                                                          final int maxLength) {
        return StringCharPredicateParser.with(predicate, minLength, maxLength);
    }

    /**
     * {@see StringInitialAndPartCharPredicateParser}
     */
    public static <C extends ParserContext> Parser<C> stringInitialAndPartCharPredicate(final CharPredicate initial,
                                                                                        final CharPredicate part,
                                                                                        final int minLength,
                                                                                        final int maxLength) {
        return StringInitialAndPartCharPredicateParser.with(initial, part, minLength, maxLength);
    }

    /**
     * {@see StringParser}
     */
    public static <C extends ParserContext> Parser<C> string(final String literal,
                                                             final CaseSensitivity caseSensitivity) {
        return StringParser.with(literal, caseSensitivity);
    }

    /**
     * {@see SurroundStringParser}
     */
    public static <C extends ParserContext> Parser<C> surround(final String open, final String close) {
        return SurroundStringParser.with(open, close);
    }

    /**
     * {@see TransformingParser}
     */
    public static <C extends ParserContext> Parser<C> transform(final Parser<C> parser,
                                                                final BiFunction<ParserToken, C, ParserToken> transformer) {
        return TransformingParser.with(parser, transformer);
    }

    /**
     * {@see UnicodeEscapeCharacterParser}
     */
    public static <C extends ParserContext> Parser<C> unicodeEscapeCharacter() {
        return UnicodeEscapeCharacterParser.get();
    }

    /**
     * {@see DateTimeFormatterParserZonedDateTime}
     */
    public static <C extends ParserContext> Parser<C> zonedDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserZonedDateTime.with(formatter);
    }

    /**
     * Stop creation.
     */
    private Parsers() {
        throw new UnsupportedOperationException();
    }
}
