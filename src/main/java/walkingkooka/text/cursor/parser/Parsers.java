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
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.PublicStaticHelper;

import java.math.MathContext;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

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
    static <T extends ParserToken, C extends ParserContext> Parser<C> andNot(final Parser<C> left, final Parser<C> right) {
        return AndNotParser.with(left, right);
    }

    /**
     * {@see BigDecimalParser}
     */
    public static <C extends ParserContext> Parser<C> bigDecimal(final MathContext context) {
        return BigDecimalParser.with(context);
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
    public static <T extends ParserToken, C extends ParserContext> Parser<C> customToString(final Parser<C> parser, final String toString) {
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
    public static <T extends ParserToken, C extends ParserContext> Parser<C> fake() {
        return FakeParser.create();
    }

    /**
     * {@see FixedParser}
     */
    public static <C extends ParserContext> Parser<C> fixed(final Optional<ParserToken> result) {
        return FixedParser.with(result);
    }

    // hsl..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsl(359, 100%, 99%)
     * </pre>
     * into a {@link HslParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hslFunction() {
        return HSL_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHslFunction(final ParserToken token, final ParserContext context) {
        return HslFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSL_FUNCTION_PARSER = hslFunctionParser();

    private static Parser<ParserContext> hslFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> percentage = Parsers.character(CharPredicates.is('%'));
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsl(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(Parsers::transformHslFunction)
                .setToString("hsl()");
    }

    // hsv..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsv(359, 1.0, 1.0)
     * </pre>
     * into a {@link HsvParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hsvFunction() {
        return HSV_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHsvFunction(final ParserToken token, final ParserContext context) {
        return HsvFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSV_FUNCTION_PARSER = hsvFunctionParser();

    private static Parser<ParserContext> hsvFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsv(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(Parsers::transformHsvFunction)
                .setToString("hsv()");
    }

    // ................................................................................................................

    /**
     * {@see LocalDateDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> localDate(final DateTimeFormatter formatter, final String pattern) {
        return LocalDateDateTimeFormatterParser.with(formatter, pattern);
    }

    /**
     * {@see LocalDateTimeDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> localDateTime(final DateTimeFormatter formatter, final String pattern) {
        return LocalDateTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    /**
     * {@see LocalTimeDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> localTime(final DateTimeFormatter formatter, final String pattern) {
        return LocalTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    /**
     * {@see LongParser}
     */
    public static <C extends ParserContext> Parser<C> longParser(final int radix) {
        return LongParser.with(radix);
    }

    /**
     * {@see OffsetDateTimeDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> offsetDateTime(final DateTimeFormatter formatter, final String pattern) {
        return OffsetDateTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    /**
     * {@see OffsetTimeDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> offsetTime(final DateTimeFormatter formatter, final String pattern) {
        return OffsetTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    // ................................................................................................................

    /**
     * A parser that handles
     * <pre>
     * rgb(RR,GG,BB)
     * </pre>
     * into a {@link ColorParserToken}.
     */
    public static <C extends ParserContext> Parser<C> rgbFunction() {
        return RGB_FUNCTION_PARSER.cast();
    }

    /**
     * This method should only be called to init {@link #RGB_FUNCTION_PARSER}
     */
    private static Parser<ParserContext> rgbFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.longParser(10);
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("rgb(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // red
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // green
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // blue
                .required(component)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(Parsers::transformRgbFunction)
                .setToString("rgb()");
    }

    private static ParserToken transformRgbFunction(final ParserToken token, final ParserContext context) {
        return transformRgbFunction0(token.cast(), context);
    }

    private static ColorParserToken transformRgbFunction0(final SequenceParserToken token, final ParserContext context) {
        return RgbFunctionParserTokenVisitor.parseSequenceParserToken(token);
    }

    private final static Parser<ParserContext> RGB_FUNCTION_PARSER = rgbFunctionParser();

    // ................................................................................................................

    /**
     * A parser that handles
     * <pre>
     * rgba(RR,GG,BB,1.0)
     * </pre>
     * into a {@link ColorParserToken}.
     */
    public static <C extends ParserContext> Parser<C> rgbaFunction() {
        return RGBA_FUNCTION_PARSER.cast();
    }

    /**
     * This method should only be called to init {@link #RGBA_FUNCTION_PARSER}
     */
    private static Parser<ParserContext> rgbaFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.longParser(10);
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("rgba(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // red
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // green
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // blue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // alpha
                .required(Parsers.doubleParser())
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(Parsers::transformRgbAFunction)
                .setToString("rgba()");
    }

    private static ParserToken transformRgbAFunction(final ParserToken token, final ParserContext context) {
        return transformArgbFunction0(token.cast(), context);
    }

    private static ColorParserToken transformArgbFunction0(final SequenceParserToken token, final ParserContext context) {
        return RgbaFunctionParserTokenVisitor.parseSequenceParserToken(token);
    }

    private final static Parser<ParserContext> RGBA_FUNCTION_PARSER = rgbaFunctionParser();

    // ................................................................................................................

    /**
     * {@see RepeatedParser}
     */
    public static <C extends ParserContext> Parser<C> repeated(final Parser<C> parser) {
        return RepeatedParser.with(parser);
    }

    /**
     * {@see ReportingParser}
     */
    public static <T extends ParserToken, C extends ParserContext> Parser<C> report(final ParserReporterCondition condition,
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
     * {@see ZonedDateTimeDateTimeFormatterParser}
     */
    public static <C extends ParserContext> Parser<C> zonedDateTime(final DateTimeFormatter formatter, final String pattern) {
        return ZonedDateTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    /**
     * Stop creation.
     */
    private Parsers() {
        throw new UnsupportedOperationException();
    }
}
