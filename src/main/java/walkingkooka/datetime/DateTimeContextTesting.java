/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */
package walkingkooka.datetime;

import walkingkooka.ContextTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing interface that provides methods to test a {@link DateTimeContext}
 */
public interface DateTimeContextTesting<C extends DateTimeContext> extends ContextTesting<C> {

    @Override
    default String typeNameSuffix() {
        return DateTimeContext.class.getSimpleName();
    }

    default void checkAmPm(final DateTimeContext context, final int hourOfDay, final String ampm) {
        assertEquals("ampm hourOfDay=" + hourOfDay, ampm, context.ampm(hourOfDay));
    }

    default void checkMonthName(final DateTimeContext context, final int month, final String monthName) {
        assertEquals("monthName month=" + month, monthName, context.monthName(month));
    }

    default void checkMonthNameAbbreviation(final DateTimeContext context, final int month, final String monthName) {
        assertEquals("monthNameAbbreviation month=" + month, monthName, context.monthNameAbbreviation(month));
    }

    default void checkWeekDayName(final DateTimeContext context, final int day, final String dayName) {
        assertEquals("weekDayName day=" + day, dayName, context.weekDayName(day));
    }

    default void checkWeekDayNameAbbreviation(final DateTimeContext context, final int day, final String dayName) {
        assertEquals("weekDayNameAbbreviation day=" + day, dayName, context.weekDayNameAbbreviation(day));
    }
}
