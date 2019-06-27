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
import walkingkooka.ContextTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing interface that provides methods to test a {@link DateTimeContext}
 */
public interface DateTimeContextTesting<C extends DateTimeContext> extends ContextTesting<C> {

    @Test
    default void testAmpmNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().ampm(-1);
        });
    }

    @Test
    default void testAmpmInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().ampm(24);
        });
    }

    @Test
    default void testMonthNameNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().monthName(-1);
        });
    }

    @Test
    default void testMonthNameInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().monthName(12);
        });
    }

    @Test
    default void testMonthNameAbbrevationNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().monthNameAbbreviation(-1);
        });
    }

    @Test
    default void testMonthNameAbbreviationInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().monthNameAbbreviation(12);
        });
    }

    @Test
    default void testWeekDayNameNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayName(-1);
        });
    }

    @Test
    default void testWeekDayNameZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayName(0);
        });
    }

    @Test
    default void testWeekDayNameInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayName(8);
        });
    }

    @Test
    default void testWeekDayNameAbbrevationNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayNameAbbreviation(-1);
        });
    }

    @Test
    default void testWeekDayNameAbbrevationZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayNameAbbreviation(0);
        });
    }

    @Test
    default void testWeekDayNameAbbreviationInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createContext().weekDayNameAbbreviation(8);
        });
    }


    @Override
    default String typeNameSuffix() {
        return DateTimeContext.class.getSimpleName();
    }

    default void amPmAndCheck(final int hourOfDay, final String ampm) {
        this.amPmAndCheck(this.createContext(), hourOfDay, ampm);
    }

    default void amPmAndCheck(final DateTimeContext context, final int hourOfDay, final String ampm) {
        assertEquals(ampm, context.ampm(hourOfDay), () -> "ampm hourOfDay=" + hourOfDay);
    }

    default void monthNameAndCheck(final int month, final String monthName) {
        this.monthNameAndCheck(this.createContext(), month, monthName);
    }

    default void monthNameAndCheck(final DateTimeContext context, final int month, final String monthName) {
        assertEquals(monthName, context.monthName(month), () -> "monthName month=" + month);
    }

    default void monthNameAbbreviationAndCheck(final int month, final String monthName) {
        this.monthNameAbbreviationAndCheck(this.createContext(), month, monthName);
    }

    default void monthNameAbbreviationAndCheck(final DateTimeContext context, final int month, final String monthName) {
        assertEquals(monthName, context.monthNameAbbreviation(month), () -> "monthNameAbbreviation month=" + month);
    }

    default void weekDayNameAndCheck(final int day, final String dayName) {
        this.weekDayNameAndCheck(this.createContext(), day, dayName);
    }

    default void weekDayNameAndCheck(final DateTimeContext context, final int day, final String dayName) {
        assertEquals(dayName, context.weekDayName(day), () -> "weekDayName day=" + day);
    }

    default void weekDayNameAbbreviationAndCheck(final int day, final String dayName) {
        this.weekDayNameAbbreviationAndCheck(this.createContext(), day, dayName);
    }

    default void weekDayNameAbbreviationAndCheck(final DateTimeContext context, final int day, final String dayName) {
        assertEquals(dayName, context.weekDayNameAbbreviation(day), () -> "weekDayNameAbbreviation day=" + day);
    }
}
