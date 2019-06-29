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

package walkingkooka.convert;

import walkingkooka.Value;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.type.PublicStaticHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

/**
 * Factory methods for numerous {@link Converter converters}.
 */
public final class Converters implements PublicStaticHelper {

    /**
     * Parameter to use as the offset for java date/datetimes
     */
    public final static long JAVA_EPOCH_OFFSET = 0;

    /**
     * Parameter to use to convert date/datetimes to an excel/spreadsheet number.
     * A numeric value of 0 when formatted as a date/datetime shows a date component of 1899/12/30.
     */
    public final static long EXCEL_OFFSET = LocalDate.of(1899, 12, 30).toEpochDay();

    /**
     * Hours per day.
     */
    private static final long HOURS_PER_DAY = 24;

    /**
     * Mins per day.
     */
    private static final long MINUTES_PER_HOUR = 60;

    /**
     * Seconds per day.
     */
    private static final long SECONDS_PER_MINUTE = 60;

    /**
     * Nanos per second.
     */
    static final long NANOS_PER_SECOND = 1000_000_000L;

    /**
     * Nanos per minute.
     */
    private static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE;

    /**
     * Nanos per hour.
     */
    private static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * MINUTES_PER_HOUR;

    /**
     * Nanos per day.
     */
    static final long NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY;

    /**
     * [@see BigDecimalBooleanConverter}
     */
    public static Converter bigDecimalBoolean() {
        return BigDecimalBooleanConverter.INSTANCE;
    }

    /**
     * [@see BooleanConverter}
     */
    public static <S, T> Converter booleanConverter(final Class<S> sourceType, final S trueValue, final Class<T> targetType, final T trueAnswer, final T falseAnswer) {
        return BooleanConverter.with(sourceType, trueValue, targetType, trueAnswer, falseAnswer);
    }

    /**
     * {@see ChainConverter}
     */
    public static Converter chain(final Converter first,
                                  final Class<?> intermediateTargetType,
                                  final Converter last) {
        return ChainConverter.with(first, intermediateTargetType, last);
    }

    /**
     * {@see ConverterCollection}
     */
    public static Converter collection(final List<Converter> converters) {
        return ConverterCollection.with(converters);
    }

    /**
     * {@see CustomToStringConverter}
     */
    public static Converter customToString(final Converter converter, final String toString) {
        return CustomToStringConverter.wrap(converter, toString);
    }

    /**
     * {@see DecimalFormatStringConverter}
     */
    public static Converter decimalFormatString(final String pattern) {
        return DecimalFormatStringConverter.with(pattern);
    }

    /**
     * {@see FailConverter}
     */
    public static <S, T> Converter fail(final Class<S> source, final Class<T> target) {
        return FailConverter.with(source, target);
    }

    /**
     * {@see FakeConverter}
     */
    public static Converter fake() {
        return new FakeConverter();
    }

    /**
     * {@see ForwardingConverter}
     */
    public static <S, T> Converter forward(final Converter converter, final Class<S> sourceType, final Class<T> targetType) {
        return ForwardingConverter.with(converter, sourceType, targetType);
    }

    /**
     * [@see FunctionConverter}
     */
    public static <S, D> Converter function(final Class<S> sourceType,
                                            final Class<D> targetType,
                                            final Function<S, D> converter) {
        return FunctionConverter.with(sourceType, targetType, converter);
    }

    /**
     * {@see LocalDateBigDecimalConverter}
     */
    public static Converter localDateBigDecimal(final long offset) {
        return LocalDateBigDecimalConverter.with(offset);
    }

    /**
     * {@see LocalDateBigIntegerConverter}
     */
    public static Converter localDateBigInteger(final long offset) {
        return LocalDateBigIntegerConverter.with(offset);
    }

    /**
     * {@see LocalDateDoubleConverter}
     */
    public static Converter localDateDouble(final long offset) {
        return LocalDateDoubleConverter.with(offset);
    }

    /**
     * {@see LocalDateLongConverter}
     */
    public static Converter localDateLong(final long offset) {
        return LocalDateLongConverter.with(offset);
    }

    /**
     * {@see LocalDateLocalDateTimeConverter}
     */
    public static Converter localDateLocalDateTime() {
        return LocalDateLocalDateTimeConverter.INSTANCE;
    }

    /**
     * {@see LocalDateStringDateTimeFormatterConverter}
     */
    public static Converter localDateString(final DateTimeFormatter formatter) {
        return LocalDateStringDateTimeFormatterConverter.with(formatter);
    }

    /**
     * {@see LocalDateTimeBigDecimalConverter}
     */
    public static Converter localDateTimeBigDecimal(final long offset) {
        return LocalDateTimeBigDecimalConverter.with(offset);
    }

    /**
     * {@see LocalDateTimeBigIntegerConverter}
     */
    public static Converter localDateTimeBigInteger(final long offset) {
        return LocalDateTimeBigIntegerConverter.with(offset);
    }

    /**
     * {@see LocalDateTimeDoubleConverter}
     */
    public static Converter localDateTimeDouble(final long offset) {
        return LocalDateTimeDoubleConverter.with(offset);
    }

    /**
     * {@see LocalDateTimeLocalDateConverter}
     */
    public static Converter localDateTimeLocalDate(final long offset) {
        return LocalDateTimeLocalDateConverter.INSTANCE;
    }

    /**
     * {@see LocalDateTimeLongConverter}
     */
    public static Converter localDateTimeLong(final long offset) {
        return LocalDateTimeLongConverter.with(offset);
    }

    /**
     * {@see LocalDateTimeLocalTimeConverter}
     */
    public static Converter localDateTimeLocalTime() {
        return LocalDateTimeLocalTimeConverter.INSTANCE;
    }

    /**
     * {@see LocalDateTimeStringDateTimeFormatterConverter}
     */
    public static Converter localDateTimeString(final DateTimeFormatter formatter) {
        return LocalDateTimeStringDateTimeFormatterConverter.with(formatter);
    }


    /**
     * {@see LocalTimeBigDecimalConverter}
     */
    public static Converter localTimeBigDecimal() {
        return LocalTimeBigDecimalConverter.INSTANCE;
    }

    /**
     * {@see LocalTimeBigIntegerConverter}
     */
    public static Converter localTimeBigInteger() {
        return LocalTimeBigIntegerConverter.INSTANCE;
    }

    /**
     * {@see LocalTimeDoubleConverter}
     */
    public static Converter localTimeDouble() {
        return LocalTimeDoubleConverter.INSTANCE;
    }

    /**
     * {@see LocalTimeLocalDateTimeConverter}
     */
    public static Converter localTimeLocalDateTime() {
        return LocalTimeLocalDateTimeConverter.INSTANCE;
    }

    /**
     * {@see LocalTimeLongConverter}
     */
    public static Converter localTimeLong() {
        return LocalTimeLongConverter.INSTANCE;
    }

    /**
     * {@see LocalTimeStringDateTimeFormatterConverter}
     */
    public static Converter localTimeString(final DateTimeFormatter formatter) {
        return LocalTimeStringDateTimeFormatterConverter.with(formatter);
    }

    /**
     * [@see NumberBigDecimalConverter}
     */
    public static Converter numberBigDecimal() {
        return NumberBigDecimalConverter.INSTANCE;
    }

    /**
     * [@see NumberBigIntegerConverter}
     */
    public static Converter numberBigInteger() {
        return NumberBigIntegerConverter.INSTANCE;
    }

    /**
     * [@see NumberDoubleConverter}
     */
    public static Converter numberDouble() {
        return NumberDoubleConverter.INSTANCE;
    }

    /**
     * {@see NumberLocalDateConverter}
     */
    public static Converter numberLocalDate(final long offset) {
        return NumberLocalDateConverter.with(offset);
    }

    /**
     * {@see NumberLocalDateTimeConverter}
     */
    public static Converter numberLocalDateTime(final long offset) {
        return NumberLocalDateTimeConverter.with(offset);
    }

    /**
     * {@see NumberLocalTimeConverter}
     */
    public static Converter numberLocalTime() {
        return NumberLocalTimeConverter.INSTANCE;
    }

    /**
     * {@see NumberIntegerConverter}
     */
    public static Converter numberInteger() {
        return NumberIntegerConverter.INSTANCE;
    }

    /**
     * {@see NumberLongConverter}
     */
    public static Converter numberLong() {
        return NumberLongConverter.INSTANCE;
    }

    /**
     * {@see NumberNumberConverter}
     */
    public static Converter numberNumber(final Converter bigDecimal,
                                         final Converter bigInteger,
                                         final Converter doubleConverter,
                                         final Converter longConverter) {
        return NumberNumberConverter.with(bigDecimal, bigInteger, doubleConverter, longConverter);
    }

    /**
     * {@see ParserConverter}
     */
    public static <V, PT extends ParserToken & Value<V>, PC extends ParserContext> Converter parser(final Class<V> type,
                                                                                                    final Parser<PC> parser,
                                                                                                    final Function<ConverterContext, PC> context) {
        return ParserConverter.with(type, parser, context);
    }

    /**
     * {@see SimpleConverter}
     */
    public static Converter simple() {
        return SimpleConverter.INSTANCE;
    }

    /**
     * [@see StringConverter}
     */
    public static Converter string() {
        return StringConverter.INSTANCE;
    }

    /**
     * {@see StringLocalDateDateTimeFormatterConverter}
     */
    public static Converter stringLocalDate(final DateTimeFormatter formatter) {
        return StringLocalDateDateTimeFormatterConverter.with(formatter);
    }

    /**
     * {@see StringLocalDateTimeDateTimeFormatterConverter}
     */
    public static Converter stringLocalDateTime(final DateTimeFormatter formatter) {
        return StringLocalDateTimeDateTimeFormatterConverter.with(formatter);
    }

    /**
     * {@see StringLocalTimeDateTimeFormatterConverter}
     */
    public static Converter stringLocalTime(final DateTimeFormatter formatter) {
        return StringLocalTimeDateTimeFormatterConverter.with(formatter);
    }

    /**
     * {@see TruthyNumberBooleanConverter}
     */
    public static Converter truthyNumberBoolean() {
        return TruthyNumberBooleanConverter.INSTANCE;
    }

    /**
     * Stop creation
     */
    private Converters() {
        throw new UnsupportedOperationException();
    }
}
