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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

final public class ComparatorsTest implements PublicStaticHelperTesting<Comparators>,
        ComparatorTesting {

    @Test
    public void testNormalizeIntZero() {
        this.normalizeIntAndCheck(0, 0);
    }

    @Test
    public void testNormalizeIntPositive() {
        this.normalizeIntAndCheck(2, 1);
    }

    @Test
    public void testNormalizeIntNegative() {
        this.normalizeIntAndCheck(-3, -1);
    }

    private void normalizeIntAndCheck(final int value, final int expected) {
        this.checkEquals(expected,
                Comparators.normalize(value),
                "Normalize (int)" + value);
    }

    @Test
    public void testNormalizeLongZero() {
        this.normalizeLongAndCheck(0L, 0L);
    }

    @Test
    public void testNormalizeLongPositive() {
        this.normalizeLongAndCheck(2L, 1L);
    }

    @Test
    public void testNormalizeLongNegative() {
        this.normalizeLongAndCheck(-3L, -1L);
    }

    private void normalizeLongAndCheck(final long value, final long expected) {
        this.checkEquals(
                expected,
                (long) Comparators.normalize(value),
                "Normalize (long)" + value
        );
    }

    // comparators......................................................................................................

    @Test
    public void testCompareDayOfMonth() {
        this.compareAndCheckLess(
                Comparators.dayOfMonth(),
                LocalDate.of(2000, 12, 1),
                LocalDateTime.of(2000, 12, 12, 12, 58, 59)
        );
    }

    @Test
    public void testCompareHourOfAmpm() {
        this.compareAndCheckLess(
                Comparators.hourOfAmPm(),
                LocalTime.of(13, 59, 58),
                LocalDateTime.of(2000, 12, 12, 11, 58, 59)
        );
    }

    @Test
    public void testCompareHourOfDay() {
        this.compareAndCheckLess(
                Comparators.hourOfDay(),
                LocalTime.of(1, 59, 58),
                LocalDateTime.of(2000, 12, 12, 12, 58, 59)
        );
    }

    @Test
    public void testCompareMinuteOfHour() {
        this.compareAndCheckLess(
                Comparators.minuteOfHour(),
                LocalTime.of(12, 1, 58),
                LocalDateTime.of(2000, 12, 12, 12, 58, 59)
        );
    }

    @Test
    public void testCompareMonthOfYear() {
        this.compareAndCheckLess(
                Comparators.monthOfYear(),
                LocalDate.of(2000, 1, 31),
                LocalDateTime.of(2000, 12, 12, 12, 58, 59)
        );
    }

    @Test
    public void testCompareSecondOfMinute() {
        this.compareAndCheckLess(
                Comparators.secondOfMinute(),
                LocalTime.of(12, 58, 1),
                LocalDateTime.of(2000, 12, 12, 12, 58, 59)
        );
    }

    @Test
    public void testCompareYear() {
        this.compareAndCheckLess(
                Comparators.year(),
                LocalDate.of(1999, 1, 31),
                LocalDateTime.of(2000, 1, 31, 12, 58, 59)
        );
    }

    // PublicStaticHelperTesting........................................................................................

    @Override
    public Class<Comparators> type() {
        return Comparators.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
