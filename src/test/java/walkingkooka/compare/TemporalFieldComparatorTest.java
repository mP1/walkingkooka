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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TemporalFieldComparatorTest implements ComparatorTesting2<TemporalFieldComparator<LocalDate>, LocalDate>,
        HashCodeEqualsDefinedTesting2<TemporalFieldComparator<LocalDate>> {

    @Test
    public void testWithNullFieldFails() {
        assertThrows(
                NullPointerException.class,
                () -> TemporalFieldComparator.with(null)
        );
    }

    @Test
    public void testCompareDayEqual() {
        this.compareAndCheckEquals(
                TemporalFieldComparator.with(ChronoField.DAY_OF_MONTH),
                LocalDate.of(1999, 1, 31),
                LocalDate.of(2000, 12, 31)
        );
    }

    @Test
    public void testCompareDayLess() {
        this.compareAndCheckLess(
                TemporalFieldComparator.with(ChronoField.DAY_OF_MONTH),
                LocalDate.of(1999, 1, 1),
                LocalDate.of(2000, 12, 22)
        );
    }

    @Test
    public void testCompareMonthEqual() {
        this.compareAndCheckEquals(
                LocalDate.of(1999, 12, 31),
                LocalDate.of(2000, 12, 1)
        );
    }

    @Test
    public void testCompareMonthLess() {
        this.compareAndCheckEquals(
                LocalDate.of(1999, 12, 1),
                LocalDate.of(2000, 12, 22)
        );
    }

    @Test
    public void testCompareTimeMinutesLess() {
        this.compareAndCheckLess(
                TemporalFieldComparator.with(ChronoField.MINUTE_OF_HOUR),
                LocalTime.of(12, 1, 11),
                LocalTime.of(13, 22, 9)
        );
    }

    @Test
    public void testCompareTimeAndDateTimeMinutesLess() {
        this.compareAndCheckLess(
                TemporalFieldComparator.with(ChronoField.MINUTE_OF_HOUR),
                LocalTime.of(12, 1, 11),
                LocalDateTime.of(2000, 12, 31, 13, 22, 9)
        );
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
                TemporalFieldComparator.with(ChronoField.MONTH_OF_YEAR),
                TemporalFieldComparator.with(ChronoField.YEAR)
        );
    }

    @Test
    public void testToString() {
        final TemporalField field = ChronoField.DAY_OF_MONTH;

        this.toStringAndCheck(
                TemporalFieldComparator.with(field),
                field.toString()
        );
    }

    @Override
    public TemporalFieldComparator<LocalDate> createComparator() {
        return TemporalFieldComparator.with(ChronoField.MONTH_OF_YEAR);
    }

    @Override
    public TemporalFieldComparator<LocalDate> createObject() {
        return this.createComparator();
    }

    @Override
    public Class<TemporalFieldComparator<LocalDate>> type() {
        return Cast.to(TemporalFieldComparator.class);
    }
}
