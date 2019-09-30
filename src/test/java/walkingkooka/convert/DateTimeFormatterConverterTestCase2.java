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

import org.junit.jupiter.api.Test;
import walkingkooka.datetime.DateTimeContext;

import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class DateTimeFormatterConverterTestCase2<C extends DateTimeFormatterConverter<S, T>, S, T> extends ConverterTestCase2<C> {

    DateTimeFormatterConverterTestCase2() {
        super();
    }

    @Test
    public final void testWithNullFormatterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createConverter((Function<DateTimeContext, DateTimeFormatter>) null);
        });
    }

    @Test
    public final void testConvert() {
        this.convertAndCheck2(this.source(), this.converted());
    }

    @Test
    public final void testConvertTwice() {
        final C converter = this.createConverter();
        final S source = this.source();
        final Class<T> targetType = this.targetType();
        final ConverterContext context = this.createContext();
        final T converted = this.converted();

        this.convertAndCheck(converter, source, targetType, context, converted);
        this.convertAndCheck(converter, source, targetType, context, converted);
    }

    @Override
    public final C createConverter() {
        return this.createConverter(this.formatter());
    }

    final C createConverter(final DateTimeFormatter formatter) {
        this.create = 0;

        return this.createConverter((context) -> {
            final Locale locale = context.locale();
            switch (this.create++) {
                case 0:
                case 2:
                    assertEquals(LOCALE1, locale, "locale");
                    assertEquals(TWO_DIGIT_YEARS1, context.twoDigitYear(), "twoDigitYears");
                    break;
                case 1:
                    assertEquals(LOCALE2, locale, "locale");
                    assertEquals(TWO_DIGIT_YEARS2, context.twoDigitYear(), "twoDigitYears");
                    break;
                default:
                    fail();
            }

            return formatter.withLocale(locale);
        });
    }

    private int create = 0;

    final static Locale LOCALE1 = Locale.ENGLISH;
    final static Locale LOCALE2 = Locale.GERMAN;

    final static int TWO_DIGIT_YEARS1 = 20;
    final static int TWO_DIGIT_YEARS2 = 40;

    abstract C createConverter(final Function<DateTimeContext, DateTimeFormatter> formatter);

    @Override
    public final ConverterContext createContext() {
        return this.createContext(LOCALE1, TWO_DIGIT_YEARS1);
    }

    final ConverterContext createContext2() {
        return this.createContext(LOCALE2, TWO_DIGIT_YEARS2);
    }

    final ConverterContext createContext(final Locale locale, final int twoDigitYears) {
        final DecimalStyle decimalStyle = DecimalStyle.of(locale);

        return new FakeConverterContext() {

            @Override
            public char decimalSeparator() {
                return decimalStyle.getDecimalSeparator();
            }

            @Override
            public char negativeSign() {
                return decimalStyle.getNegativeSign();
            }

            @Override
            public char positiveSign() {
                return decimalStyle.getPositiveSign();
            }

            @Override
            public Locale locale() {
                return locale;
            }

            @Override
            public int twoDigitYear() {
                return twoDigitYears;
            }

            @Override
            public String toString() {
                return this.locale() + " " + this.twoDigitYear();
            }
        };
    }

    abstract DateTimeFormatter formatter();

    abstract S source();

    abstract T converted();

    final void convertAndCheck2(final S source,
                                final T converted) {
        this.convertAndCheck2(this.createConverter(),
                source,
                this.createContext(),
                converted);
    }

    final void convertAndCheck2(final C converter,
                                final S source,
                                final ConverterContext context,
                                final T converted) {
        this.convertAndCheck(converter,
                source,
                this.targetType(),
                context,
                converted);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return DateTimeFormatter.class.getSimpleName() + Converter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return this.targetType().getSimpleName();
    }

    abstract Class<T> targetType();
}
