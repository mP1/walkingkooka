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
import walkingkooka.collect.list.Lists;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverterTestCase<C extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverter<T>, T>
        extends ConverterTestCase2<C> {

    final static Locale LOCALE = Locale.FRANCE;

    StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverterTestCase() {
        super();
    }

    @Test
    public final void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createConverter(null);
        });
    }

    @Test
    public final void testInvalidStringFails() {
        this.convertFails("!", this.targetType());
    }

    @Test
    public final void testUnknownFormat() {
        this.convertFails(this.createConverter(Lists.of(this.formatter1())),
                this.format(this.formatter2(), Locale.ENGLISH),
                this.targetType());
    }

    @Test
    public final void testUnknownFormat2() {
        this.convertFails(this.createConverter(Lists.of(this.formatter2())),
                this.format(this.formatter1(), Locale.FRENCH),
                this.targetType());
    }

    @Test
    public final void testOnlyFormatterLocaleEnglish() {
        final DateTimeFormatter formatter = this.formatter1();

        this.convertAndCheck(this.createConverter(Lists.of(formatter)),
                this.format(formatter, Locale.ENGLISH),
                this.targetType(),
                this.value());
    }

    @Test
    public final void testOnlyFormatter2LocaleFrench() {
        final DateTimeFormatter formatter = this.formatter2();
        final T value = this.value();

        this.convertAndCheck(this.createConverter(Lists.of(formatter)),
                this.format(formatter, Locale.FRENCH),
                this.targetType(),
                this.value());
    }

    @Test
    public final void testFirstFormatterLocaleEnglish() {
        final DateTimeFormatter formatter = this.formatter1();

        this.convertAndCheck(this.createConverter(Lists.of(formatter, this.formatter2())),
                this.format(formatter, Locale.ENGLISH),
                this.targetType(),
                this.value());
    }

    @Test
    public final void testLastFormatterLocaleGerman() {
        final DateTimeFormatter formatter = this.formatter2();

        this.convertAndCheck(this.createConverter(Lists.of(this.formatter2(), formatter)),
                this.format(formatter, Locale.GERMAN),
                this.targetType(),
                this.value());
    }

    @Test
    public final void testToString() {
        final DateTimeFormatter formatter = this.formatter1();

        this.toStringAndCheck(this.createConverter(Lists.of(formatter)),
                "String ->" + this.targetType().getSimpleName() + " Locale " + formatter);
    }

    @Test
    public final void testToString2() {
        final DateTimeFormatter formatter1 = this.formatter1();
        final DateTimeFormatter formatter2 = this.formatter2();

        this.toStringAndCheck(this.createConverter(Lists.of(formatter1, formatter2)),
                "String ->" + this.targetType().getSimpleName() + " Locale " + formatter1 + ", " + formatter2);
    }

    @Override
    public final C createConverter() {
        return this.createConverter(Lists.of(this.formatter1(), this.formatter2()));
    }

    abstract C createConverter(final List<DateTimeFormatter> formatters);

    @Override
    public final ConverterContext createContext() {
        return new FakeConverterContext() {
            @Override
            public Locale locale() {
                return LOCALE;
            }
        };
    }

    abstract Class<T> targetType();

    abstract T value();

    abstract DateTimeFormatter formatter1();

    abstract DateTimeFormatter formatter2();

    abstract String format(final DateTimeFormatter formatter, final Locale locale);
}
