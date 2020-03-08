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

import walkingkooka.locale.HasLocaleTesting;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing interface that provides methods to test a {@link DateTimeContext}
 */
public interface DateTimeContextTesting extends HasLocaleTesting {

    default void amPmAndCheck(final DateTimeContext context,
                              final int hourOfDay,
                              final String ampm) {
        assertEquals(ampm, context.ampm(hourOfDay), () -> "ampm hourOfDay=" + hourOfDay);
    }

    default void monthNameAndCheck(final DateTimeContext context,
                                   final int month,
                                   final String monthName) {
        assertEquals(monthName, context.monthName(month), () -> "monthName month=" + month);
    }

    default void monthNamesCheck(final DateTimeContext context) {
        final List<String> names = context.monthNames();
        IntStream.range(0, names.size())
                .forEach(i -> this.monthNameAndCheck(context, i, names.get(i)));
    }

    default void monthNameAbbreviationAndCheck(final DateTimeContext context,
                                               final int month,
                                               final String monthName) {
        assertEquals(monthName, context.monthNameAbbreviation(month), () -> "monthNameAbbreviation month=" + month);
    }

    default void monthNameAbbreviationsCheck(final DateTimeContext context) {
        final List<String> names = context.monthNameAbbreviations();
        IntStream.range(0, names.size())
                .forEach(i -> this.monthNameAbbreviationAndCheck(context, i, names.get(i)));
    }

    default void twoDigitYearAndCheck(final DateTimeContext context,
                                      final int twoDigitYear) {
        assertEquals(true,
                twoDigitYear >= 0 && twoDigitYear <= 100,
                () -> "twoDigitYear " + twoDigitYear + " " + context);
    }

    default void weekDayNameAndCheck(final DateTimeContext context,
                                     final int day,
                                     final String dayName) {
        assertEquals(dayName, context.weekDayName(day), () -> "weekDayName day=" + day);
    }

    default void weekDayNamesCheck(final DateTimeContext context) {
        final List<String> names = context.weekDayNames();
        IntStream.range(0, names.size())
                .forEach(i -> this.weekDayNameAndCheck(context, i, names.get(i)));
    }

    default void weekDayNameAbbreviationAndCheck(final DateTimeContext context,
                                                 final int day,
                                                 final String dayName) {
        assertEquals(dayName, context.weekDayNameAbbreviation(day), () -> "weekDayNameAbbreviation day=" + day);
    }

    default void weekDayNameAbbreviationCheck(final DateTimeContext context) {
        final List<String> names = context.weekDayNameAbbreviations();
        IntStream.range(0, names.size())
                .forEach(i -> this.weekDayNameAbbreviationAndCheck(context, i, names.get(i)));
    }
}
