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

import java.math.MathContext;
import java.util.Locale;
import java.util.Objects;

/**
 * An adaptor for {@link DecimalNumberContext} to {@link ConverterContext}.
 */
final class BasicConverterContext implements ConverterContext {

    /**
     * Creates a new {@link BasicConverterContext}.
     */
    static BasicConverterContext with(final DateTimeContext dateTimeContext,
                                      final DecimalNumberContext decimalNumberContext) {
        Objects.requireNonNull(dateTimeContext, "dateTimeContext");
        Objects.requireNonNull(decimalNumberContext, "decimalNumberContext");

        return new BasicConverterContext(dateTimeContext, decimalNumberContext);
    }

    /**
     * Private ctor use factory
     */
    private BasicConverterContext(final DateTimeContext dateTimeContext,
                                  final DecimalNumberContext decimalNumberContext) {
        super();

        this.dateTimeContext = dateTimeContext;
        this.decimalNumberContext = decimalNumberContext;
    }

    @Override
    public String ampm(final int hourOfDay) {
        return this.dateTimeContext.ampm(hourOfDay);
    }

    @Override
    public String monthName(final int month) {
        return this.dateTimeContext.monthName(month);
    }

    @Override
    public String monthNameAbbreviation(final int month) {
        return this.dateTimeContext.monthNameAbbreviation(month);
    }

    @Override
    public String weekDayName(final int day) {
        return this.dateTimeContext.weekDayName(day);
    }

    @Override
    public String weekDayNameAbbreviation(final int day) {
        return this.dateTimeContext.weekDayNameAbbreviation(day);
    }

    private final DateTimeContext dateTimeContext;

    // DecimalNumberContext.............................................................................................

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext.currencySymbol();
    }

    @Override
    public char decimalPoint() {
        return this.decimalNumberContext.decimalPoint();
    }

    @Override
    public char exponentSymbol() {
        return this.decimalNumberContext.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.decimalNumberContext.groupingSeparator();
    }

    @Override
    public char minusSign() {
        return this.decimalNumberContext.minusSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext.percentageSymbol();
    }

    @Override
    public char plusSign() {
        return this.decimalNumberContext.plusSign();
    }

    @Override
    public Locale locale() {
        return this.decimalNumberContext.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.decimalNumberContext.mathContext();
    }

    private final DecimalNumberContext decimalNumberContext;

    @Override
    public String toString() {
        return this.dateTimeContext + " " + this.decimalNumberContext;
    }
}
