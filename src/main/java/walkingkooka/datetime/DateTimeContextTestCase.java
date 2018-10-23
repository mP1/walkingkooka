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

import walkingkooka.ContextTestCase;

import static org.junit.Assert.assertEquals;

public abstract class DateTimeContextTestCase<C extends DateTimeContext> extends ContextTestCase<C> {

    @Override
    protected String requiredNameSuffix() {
        return DateTimeContext.class.getSimpleName();
    }

    protected void checkAmPm(final DateTimeContext context, final int hourOfDay, final String ampm) {
        assertEquals("ampm hourOfDay=" + hourOfDay, ampm, context.ampm(hourOfDay));
    }

    protected void checkMonthName(final DateTimeContext context, final int month, final String monthName) {
        assertEquals("monthName month=" + month, monthName, context.monthName(month));
    }

    protected void checkMonthNameAbbreviation(final DateTimeContext context, final int month, final String monthName) {
        assertEquals("monthNameAbbreviation month=" + month, monthName, context.monthNameAbbreviation(month));
    }

    protected void checkWeekDayName(final DateTimeContext context, final int day, final String dayName) {
        assertEquals("weekDayName day=" + day, dayName, context.weekDayName(day));
    }

    protected void checkWeekDayNameAbbreviation(final DateTimeContext context, final int day, final String dayName) {
        assertEquals("weekDayNameAbbreviation day=" + day, dayName, context.weekDayNameAbbreviation(day));
    }
}
