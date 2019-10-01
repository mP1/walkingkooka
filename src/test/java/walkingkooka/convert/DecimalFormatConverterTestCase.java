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
import walkingkooka.Cast;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;

import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class DecimalFormatConverterTestCase<C extends DecimalFormatConverter> extends ConverterTestCase2<C> {

    DecimalFormatConverterTestCase() {
        super();
    }

    @Test
    public final void testWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createConverter((Function<DecimalNumberContext, DecimalFormat>)null);
        });
    }

    // toString.........................................................................................................

    @Test
    public final void testToString() {
        final Function<DecimalNumberContext, DecimalFormat> decimalFormat = this::decimalFormat;
        this.toStringAndCheck(this.createConverter(decimalFormat), decimalFormat.toString());
    }

    @Override
    public final C createConverter() {
        return this.createConverter(this::decimalFormat);
    }

    private DecimalFormat decimalFormat(final DecimalNumberContext context) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.000");
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(context.locale()));
        return decimalFormat;
    }

    final C createConverter(final String pattern) {
        return this.createConverter((c) -> {
            final DecimalFormat decimalFormat = new DecimalFormat(pattern);
            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(c.locale()));
            return decimalFormat;
        });
    }

    abstract C createConverter(final Function<DecimalNumberContext, DecimalFormat> decimalFormat);

    @Override
    public final ConverterContext createContext() {
        return this.createContext(Locale.UK);
    }

    final ConverterContext createContext(final Locale locale) {
        return ConverterContexts.basic(DateTimeContexts.fake(),
                DecimalNumberContexts.decimalFormatSymbols(new DecimalFormatSymbols(locale),
                        'E',
                        '+',
                        locale,
                        MathContext.DECIMAL32));
    }

    final void convertAndCheck2(final String pattern,
                                final Object value,
                                final Object expected) {
        this.convertAndCheck2(pattern,
                value,
                Locale.UK,
                expected);
    }

    final void convertAndCheck2(final String pattern,
                                final Object value,
                                final Locale locale,
                                final Object expected) {
        this.convertAndCheck2(pattern,
                value,
                Cast.to(expected.getClass()),
                locale,
                expected);
    }

    final <T> void convertAndCheck2(final String pattern,
                                    final Object value,
                                    final Class<T> type,
                                    final T expected) {
        this.convertAndCheck2(pattern,
                value,
                type,
                Locale.UK,
                expected);
    }

    final <T> void convertAndCheck2(final String pattern,
                                    final Object value,
                                    final Class<T> type,
                                    final Locale locale,
                                    final T expected) {
        this.convertAndCheck(this.createConverter(pattern),
                value,
                type,
                this.createContext(locale),
                expected);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return DecimalFormat.class.getSimpleName() + Converter.class.getSimpleName();
    }
}
