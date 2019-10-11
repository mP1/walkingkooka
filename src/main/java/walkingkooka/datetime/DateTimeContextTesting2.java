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
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing interface that provides methods to test a {@link DateTimeContext}
 */
public interface DateTimeContextTesting2<C extends DateTimeContext> extends ContextTesting<C>,
        DateTimeContextTesting {

    @Test
    default void testAmpms() {
        assertNotEquals(Lists.empty(), this.createContext().ampms());
    }

    @Test
    default void testAmpmNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().ampm(-1));
    }

    @Test
    default void testAmpmInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().ampm(24));
    }

    @Test
    default void testMonthNames() {
        assertNotEquals(Lists.empty(), this.createContext().monthNames());
    }

    @Test
    default void testMonthNames2() {
        this.monthNamesCheck(this.createContext());
    }

    @Test
    default void testMonthNameNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().monthName(-1));
    }

    @Test
    default void testMonthNameInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().monthName(12));
    }

    @Test
    default void testMonthNameAbbreviations() {
        assertNotEquals(Lists.empty(), this.createContext().monthNameAbbreviations());
    }

    @Test
    default void testMonthNamesAbbreviation2() {
        this.monthNameAbbreviationsCheck(this.createContext());
    }

    @Test
    default void testMonthNameAbbrevationNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().monthNameAbbreviation(-1));
    }

    @Test
    default void testMonthNameAbbreviationInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().monthNameAbbreviation(12));
    }

    @Test
    default void testTwoDigitYear() {
        final C context = this.createContext();
        this.twoDigitYearAndCheck(context, context.twoDigitYear());
    }

    @Test
    default void testWeekDayNames() {
        assertNotEquals(Lists.empty(), this.createContext().weekDayNames());
    }

    @Test
    default void testWeekDayNames2() {
        this.weekDayNamesCheck(this.createContext());
    }

    @Test
    default void testWeekDayNameNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().weekDayName(-1));
    }

    @Test
    default void testWeekDayNameInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().weekDayName(8));
    }

    @Test
    default void testWeekDayNameAbbreviations() {
        assertNotEquals(Lists.empty(), this.createContext().weekDayNameAbbreviations());
    }

    @Test
    default void testWeekDayNameAbbreviations2() {
        this.weekDayNameAbbreviationCheck(this.createContext());
    }

    @Test
    default void testWeekDayNameAbbrevationNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().weekDayNameAbbreviation(-1));
    }

    @Test
    default void testWeekDayNameAbbreviationInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createContext().weekDayNameAbbreviation(8));
    }

    @Override
    default String typeNameSuffix() {
        return DateTimeContext.class.getSimpleName();
    }

    default void amPmAndCheck(final int hourOfDay, final String ampm) {
        this.amPmAndCheck(this.createContext(), hourOfDay, ampm);
    }

    default void monthNameAndCheck(final int month, final String monthName) {
        this.monthNameAndCheck(this.createContext(), month, monthName);
    }

    default void monthNameAbbreviationAndCheck(final int month, final String monthName) {
        this.monthNameAbbreviationAndCheck(this.createContext(), month, monthName);
    }

    default void weekDayNameAndCheck(final int day, final String dayName) {
        this.weekDayNameAndCheck(this.createContext(), day, dayName);
    }

    default void weekDayNameAbbreviationAndCheck(final int day, final String dayName) {
        this.weekDayNameAbbreviationAndCheck(this.createContext(), day, dayName);
    }
}
