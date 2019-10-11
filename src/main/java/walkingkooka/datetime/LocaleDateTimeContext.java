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

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link DateTimeContext} that sources its responses from a {@link DateFormatSymbols} taken from a {@link Locale}.
 */
final class LocaleDateTimeContext implements DateTimeContext {

    static LocaleDateTimeContext with(final Locale locale,
                                      final int twoDigitYear) {
        Objects.requireNonNull(locale, "locale");

        return new LocaleDateTimeContext(locale, twoDigitYear);
    }

    private LocaleDateTimeContext(final Locale locale,
                                  final int twoDigitYear) {
        super();

        this.locale = locale;

        final DateFormatSymbols symbols = new DateFormatSymbols(locale);
        this.ampms = Lists.of(symbols.getAmPmStrings());

        this.monthNames = monthNames(symbols.getMonths());
        this.monthNameAbbreviations = monthNames(symbols.getShortMonths());

        this.twoDigitYear = twoDigitYear;

        this.weekDayNames = dayNames(symbols.getWeekdays());
        this.weekDayNameAbbreviations = dayNames(symbols.getShortWeekdays());
    }

    /**
     * {@link DateFormatSymbols} returns arrays of 13 with null occupying the 13th slot for month systems with only 12.
     */
    private static List<String> monthNames(final String[] names) {
        final int last = names.length - 1;

        return CharSequences.isNullOrEmpty(names[last]) ?
                Lists.of(Arrays.copyOfRange(names, 0, last)) :
                Lists.of(names);
    }

    /**
     * {@link DateFormatSymbols} removes the initial empty string lot in a list of day names, so 0 = Sunday.
     */
    private static List<String> dayNames(final String[] names) {
        return Lists.of(Arrays.copyOfRange(names, 0, names.length));
    }

    // DateTimeContext..................................................................................................

    @Override
    public List<String> ampms() {
        return this.ampms;
    }

    private final List<String> ampms;

    @Override
    public Locale locale() {
        return this.locale;
    }

    private final Locale locale;

    @Override
    public List<String> monthNames() {
        return this.monthNames;
    }

    private final List<String> monthNames;

    @Override
    public List<String> monthNameAbbreviations() {
        return this.monthNameAbbreviations;
    }

    private final List<String> monthNameAbbreviations;

    @Override
    public int twoDigitYear() {
        return this.twoDigitYear;
    }

    private final int twoDigitYear;

    @Override
    public List<String> weekDayNames() {
        return this.weekDayNames;
    }

    private final List<String> weekDayNames;

    @Override
    public List<String> weekDayNameAbbreviations() {
        return this.weekDayNameAbbreviations;
    }

    private final List<String> weekDayNameAbbreviations;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("locale").value(this.locale.toLanguageTag())
                .label("twoDigitYear").value(this.twoDigitYear)
                .build();
    }
}
