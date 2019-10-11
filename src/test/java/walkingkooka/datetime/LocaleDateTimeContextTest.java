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

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LocaleDateTimeContextTest implements DateTimeContextTesting2<LocaleDateTimeContext> {

    @Test
    public void testWithNullLocaleFails() {
        assertThrows(NullPointerException.class, () -> LocaleDateTimeContext.with(null, 50));
    }

    // ampm.............................................................................................................

    @Test
    public void testAmpmMidnight() {
        this.amPmAndCheck(0, "AM");
    }

    @Test
    public void testAmpm600() {
        this.amPmAndCheck(6, "AM");
    }

    @Test
    public void testAmpmNoon() {
        this.amPmAndCheck(12, "PM");
    }

    @Test
    public void testAmpm1800() {
        this.amPmAndCheck(18, "PM");
    }

    // locale...........................................................................................................

    @Test
    public void testLocale() {
        this.hasLocaleAndCheck(this.createContext(), this.locale());
    }

    // monthName........................................................................................................

    @Test
    public void testMonthName0() {
        this.monthNameAndCheck(0, "January");
    }

    @Test
    public void testMonthName1() {
        this.monthNameAndCheck(1, "February");
    }

    @Test
    public void testMonthName11() {
        this.monthNameAndCheck(11, "December");
    }

    // monthNameAbbreviation............................................................................................

    @Test
    public void testMonthNameAbbreviation0() {
        this.monthNameAbbreviationAndCheck(0, "Jan");
    }

    @Test
    public void testMonthNameAbbreviation1() {
        this.monthNameAbbreviationAndCheck(1, "Feb");
    }

    @Test
    public void testMonthNameAbbreviation11() {
        this.monthNameAbbreviationAndCheck(11, "Dec");
    }

    // weekDayName......................................................................................................

    @Test
    public void testWeekDayName1() {
        this.weekDayNameAndCheck(1, "Sunday");
    }

    @Test
    public void testWeekDayName2() {
        this.weekDayNameAndCheck(2, "Monday");
    }

    @Test
    public void testWeekDayName6() {
        this.weekDayNameAndCheck(6, "Friday");
    }

    // weekDayNameAbbreviation..........................................................................................

    @Test
    public void testWeekDayNameAbbreviation1() {
        this.weekDayNameAbbreviationAndCheck(1, "Sun");
    }

    @Test
    public void testWeekDayNameAbbreviation2() {
        this.weekDayNameAbbreviationAndCheck(2, "Mon");
    }

    @Test
    public void testWeekDayNameAbbreviation6() {
        this.weekDayNameAbbreviationAndCheck(6, "Fri");
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), "locale=\"en\" twoDigitYear=1");
    }

    @Override
    public LocaleDateTimeContext createContext() {
        return LocaleDateTimeContext.with(this.locale(), 1);
    }

    private Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public Class<LocaleDateTimeContext> type() {
        return LocaleDateTimeContext.class;
    }
}
