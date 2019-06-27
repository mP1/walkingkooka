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

package walkingkooka.datetime;

import java.text.DateFormatSymbols;
import java.util.Objects;

/**
 * A {@link DateTimeContext} that sources its responses from a {@link DateFormatSymbols}.
 */
final class DateFormatSymbolsDateTimeContext implements DateTimeContext {

    static DateFormatSymbolsDateTimeContext with(final DateFormatSymbols symbols) {
        Objects.requireNonNull(symbols, "symbols");

        return new DateFormatSymbolsDateTimeContext(symbols);
    }

    private DateFormatSymbolsDateTimeContext(final DateFormatSymbols symbols) {
        super();

        this.symbols = symbols;
    }

    // DateTimeContext..................................................................................................

    @Override
    public String ampm(final int hourOfDay) {
        if (hourOfDay < 0 || hourOfDay >= 24) {
            throw new IllegalArgumentException("Invalid hourOfDay " + hourOfDay + " not between 0 and 24");
        }
        return this.symbols.getAmPmStrings()[hourOfDay / 12];
    }

    @Override
    public String monthName(final int month) {
        return this.get(this.symbols.getMonths(), month, 0, 12, "month");
    }

    @Override
    public String monthNameAbbreviation(final int month) {
        return this.get(this.symbols.getShortMonths(), month, 0, 12, "month");
    }

    @Override
    public String weekDayName(final int day) {
        return this.get(this.symbols.getWeekdays(), day, 1, 8, "day");
    }

    @Override
    public String weekDayNameAbbreviation(final int day) {
        return this.get(this.symbols.getShortWeekdays(), day, 1, 8, "day");
    }

    private String get(final String[] values,
                       final int index,
                       final int lower,
                       final int upper,
                       final String label) {
        if (index < lower || index >= upper) {
            throw new IllegalArgumentException("Invalid " + label + " " + index + " not between " + lower + " and " + upper);
        }
        return values[index];
    }

    private final DateFormatSymbols symbols;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.symbols.toString();
    }
}
