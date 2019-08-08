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

import walkingkooka.Context;
import walkingkooka.locale.HasLocale;

import java.util.List;

/**
 * Context that typically accompanies another stateless component such as a number parser or formatter that involves decimals.
 */
public interface DateTimeContext extends Context, HasLocale {

    /**
     * Returns all the AM/PM strings.
     */
    List<String> ampms();

    /**
     * Returns the selected AM or PM given the hour of the day (24 hour time).
     */
    default String ampm(final int hourOfDay) {
        if (hourOfDay < 0 || hourOfDay >= 24) {
            throw new IllegalArgumentException("Invalid hourOrDay " + hourOfDay + " not between 0 and 24");
        }
        return this.ampms().get(hourOfDay / 12);
    }

    /**
     * Returns all the month names in long form.
     */
    List<String> monthNames();

    /**
     * Returns the requested month in full. The first month like January is zero index.
     */
    default String monthName(final int month) {
        return DateTimeContextGetter.get(month, this.monthNames(), "month");
    }

    /**
     * Returns all the month names in short form.
     */
    List<String> monthNameAbbreviations();

    /**
     * Returns the requested month in abbreviated form. The month is zero index.
     */
    default String monthNameAbbreviation(final int month) {
        return DateTimeContextGetter.get(month, this.monthNameAbbreviations(), "month");
    }

    /**
     * Returns a two digit value, values under should be 2000 years, while those under should be 1900s.
     */
    int twoDigitYear();

    /**
     * Returns all the week day names in long form.
     */
    List<String> weekDayNames();

    /**
     * Returns the requested week day in full. Sunday is 0.
     */
    default String weekDayName(final int day) {
        return DateTimeContextGetter.get(day, this.weekDayNames(), "day");
    }

    /**
     * Returns all the week day names in short form.
     */
    List<String> weekDayNameAbbreviations();

    /**
     * Returns the requested week day in abbreviated form. Sunday is 0.
     */
    default String weekDayNameAbbreviation(final int day) {
        return DateTimeContextGetter.get(day, this.weekDayNameAbbreviations(), "day");
    }
}
