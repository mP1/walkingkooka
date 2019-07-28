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

import walkingkooka.collect.list.Lists;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Converter} that tries each {@link DateTimeFormatter} with the {@link Locale} from the {@link ConverterContext} until success.
 */
abstract class StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverter<T> extends FixedTargetTypeConverter<T> {

    /**
     * Checks the given {@link DateTimeFormatter formatters}
     */
    static List<DateTimeFormatter> checkFormatters(final List<DateTimeFormatter> formatters) {
        Objects.requireNonNull(formatters, "formatters");

        final List<DateTimeFormatter> copy = Lists.immutable(formatters);
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("Require at least 1 DateTimeFormatter");
        }

        return copy;
    }

    /**
     * Package private to limit sub classing.
     */
    StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverter(final List<DateTimeFormatter> formatters) {
        super();
        this.formatters = formatters;
    }

    /**
     * Can only convert {@link String} to target type.
     */
    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return value instanceof String && this.targetType() == type;
    }

    @Override
    final T convert1(final Object value,
                     final Class<T> type,
                     final ConverterContext context) {
        final Locale locale = context.locale();
        final String string = String.class.cast(value);

        return this.formatters.stream()
                .map(formatter -> {
                    T result;

                    try {
                        result = this.parse(string, formatter.withLocale(locale));
                    } catch (final DateTimeParseException failed) {
                        result = null;
                    }

                    return result;
                })
                .filter(v -> null != v)
                .findFirst()
                .orElseThrow(() -> new FailedConversionException(value, this.targetType()));
    }

    /**
     * One or more {@link DateTimeFormatter styles} to try, order shouldnt really matter.
     */
    private final List<DateTimeFormatter> formatters;

    /**
     * Calls the appropriate parse method on the target type class such as {@link java.time.LocalDate#parse(CharSequence, DateTimeFormatter)}.
     */
    abstract T parse(final String value, final DateTimeFormatter formatter) throws DateTimeParseException;

    @Override
    public final String toString() {
        return "String ->" +
                this.targetType().getSimpleName() +
                " Locale " + this.formatters.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
}
