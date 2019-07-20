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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing interface that provides methods to test a {@link DateTimeContext}
 */
public interface DateTimeContextTesting {

    default void amPmAndCheck(final DateTimeContext context, final int hourOfDay, final String ampm) {
        assertEquals(ampm, context.ampm(hourOfDay), () -> "ampm hourOfDay=" + hourOfDay);
    }

    default void monthNameAndCheck(final DateTimeContext context, final int month, final String monthName) {
        assertEquals(monthName, context.monthName(month), () -> "monthName month=" + month);
    }

    default void monthNameAbbreviationAndCheck(final DateTimeContext context, final int month, final String monthName) {
        assertEquals(monthName, context.monthNameAbbreviation(month), () -> "monthNameAbbreviation month=" + month);
    }

    default void weekDayNameAndCheck(final DateTimeContext context, final int day, final String dayName) {
        assertEquals(dayName, context.weekDayName(day), () -> "weekDayName day=" + day);
    }

    default void weekDayNameAbbreviationAndCheck(final DateTimeContext context, final int day, final String dayName) {
        assertEquals(dayName, context.weekDayNameAbbreviation(day), () -> "weekDayNameAbbreviation day=" + day);
    }
}
