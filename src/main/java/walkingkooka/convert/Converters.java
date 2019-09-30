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

import walkingkooka.datetime.DateTimeContext;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.type.PublicStaticHelper;

import java.text.DecimalFormat;
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
    public static <S, T> Converter booleanConverter(final Class<S> sourceType,
                                                    final S falseValue,
                                                    final Class<T> targetType,
                                                    final T trueAnswer,
                                                    final T falseAnswer) {
        return BooleanConverter.with(sourceType,
                falseValue,
                targetType,
                trueAnswer,
                falseAnswer);
    }

    /**
     * [@see BooleanConverterNumber}
     */
    public static Converter booleanNumber() {
        return BooleanConverterNumber.INSTANCE;
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
     * {@see FakeConverter}
     */
    public static Converter fake() {
        return new FakeConverter();
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
     * {@see LocalDateLocalDateTimeConverter}
     */
    public static Converter localDateLocalDateTime() {
        return LocalDateConverterLocalDateTime.INSTANCE;
    }

    /**
     * {@see LocalDateConverterNumber}
     */
    public static Converter localDateNumber(final long offset) {
        return LocalDateConverterNumber.with(offset);
    }

    /**
     * {@see DateTimeFormatterConverterLocalDateString}
     */
    public static Converter localDateString(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterLocalDateString.with(formatter);
    }

    /**
     * {@see LocalDateTimeConverterLocalDate}
     */
    public static Converter localDateTimeLocalDate() {
        return LocalDateTimeConverterLocalDate.INSTANCE;
    }

    /**
     * {@see LocalDateTimeConverterLocalTime}
     */
    public static Converter localDateTimeLocalTime() {
        return LocalDateTimeConverterLocalTime.INSTANCE;
    }

    /**
     * {@see LocalDateTimeConverterNumber}
     */
    public static Converter localDateTimeNumber(final long offset) {
        return LocalDateTimeConverterNumber.with(offset);
    }

    /**
     * {@see DateTimeFormatterConverterLocalDateTimeString}
     */
    public static Converter localDateTimeString(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterLocalDateTimeString.with(formatter);
    }

    /**
     * {@see LocalTimeConverterLocalDateTime}
     */
    public static Converter localTimeLocalDateTime() {
        return LocalTimeConverterLocalDateTime.INSTANCE;
    }   

    /**
     * {@see LocalTimeConverterNumber}
     */
    public static Converter localTimeNumber() {
        return LocalTimeConverterNumber.INSTANCE;
    }

    /**
     * {@see DateTimeFormatterConverterLocalTimeString}
     */
    public static Converter localTimeString(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterLocalTimeString.with(formatter);
    }

    /**
     * {@see ConverterNumberLocalDate}
     */
    public static Converter numberLocalDate(final long offset) {
        return ConverterNumberLocalDate.with(offset);
    }

    /**
     * {@see ConverterNumberLocalDateTime}
     */
    public static Converter numberLocalDateTime(final long offset) {
        return ConverterNumberLocalDateTime.with(offset);
    }

    /**
     * {@see ConverterNumberLocalTime}
     */
    public static Converter numberLocalTime() {
        return ConverterNumberLocalTime.INSTANCE;
    }

    /**
     * {@see ConverterNumberNumber}
     */
    public static Converter numberNumber() {
        return ConverterNumberNumber.INSTANCE;
    }

    /**
     * {@see DecimalFormatConverterNumberString}
     */
    public static Converter numberString(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        return DecimalFormatConverterNumberString.with(decimalFormat);
    }

    /**
     * [@see ConverterObjectString}
     */
    public static Converter objectString() {
        return ConverterObjectString.INSTANCE;
    }
    
    /**
     * {@see ParserConverter}
     */
    public static <V, C extends ParserContext> Converter parser(final Class<V> type,
                                                                final Parser<C> parser,
                                                                final Function<ConverterContext, C> context) {
        return ParserConverter.with(type, parser, context);
    }

    /**
     * {@see SimpleConverter}
     */
    public static Converter simple() {
        return SimpleConverter.INSTANCE;
    }

    /**
     * {@see DateTimeFormatterConverterStringLocalDate}
     */
    public static Converter stringLocalDate(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterStringLocalDate.with(formatter);
    }

    /**
     * {@see DateTimeFormatterConverterStringLocalDateTime}
     */
    public static Converter stringLocalDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterStringLocalDateTime.with(formatter);
    }

    /**
     * {@see DateTimeFormatterConverterStringLocalTime}
     */
    public static Converter stringLocalTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterStringLocalTime.with(formatter);
    }

    /**
     * {@see DecimalFormatConverterStringNumber}
     */
    public static Converter stringNumber(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        return DecimalFormatConverterStringNumber.with(decimalFormat);
    }

    /**
     * {@see TryConverter}
     */
    public static Converter tryConverter(final List<Converter> converters) {
        return TryConverter.with(converters);
    }

    /**
     * {@see ConverterNumberBoolean}
     */
    public static Converter truthyNumberBoolean() {
        return ConverterNumberBoolean.INSTANCE;
    }

    /**
     * Stop creation
     */
    private Converters() {
        throw new UnsupportedOperationException();
    }
}
