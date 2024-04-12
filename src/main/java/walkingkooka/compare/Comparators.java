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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.reflect.PublicStaticHelper;

import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.Comparator;

final public class Comparators implements PublicStaticHelper {
    // constants
    public final static int LESS = -1;

    public final static int EQUAL = 0;

    public final static int MORE = 1;

    public static Comparator<Temporal> dayOfMonth() {
        return DAY_OF_MONTH;
    }

    private final static Comparator<Temporal> DAY_OF_MONTH = TemporalFieldComparator.with(ChronoField.DAY_OF_MONTH);


    /**
     * {@see FakeComparator}
     */
    public static <T> Comparator<T> fake() {
        return FakeComparator.create();
    }


    public static Comparator<Temporal> hourOfAmPm() {
        return HOUR_OF_AMPM;
    }

    private final static Comparator<Temporal> HOUR_OF_AMPM = TemporalFieldComparator.with(ChronoField.HOUR_OF_AMPM);

    public static Comparator<Temporal> hourOfDay() {
        return HOUR_OF_DAY;
    }

    private final static Comparator<Temporal> HOUR_OF_DAY = TemporalFieldComparator.with(ChronoField.HOUR_OF_DAY);

    public static Comparator<Temporal> minuteOfHour() {
        return MINUTE_OF_HOUR;
    }

    private final static Comparator<Temporal> MINUTE_OF_HOUR = TemporalFieldComparator.with(ChronoField.MINUTE_OF_HOUR);

    public static Comparator<Temporal> monthOfYear() {
        return MONTH_OF_YEAR;
    }

    private final static Comparator<Temporal> MONTH_OF_YEAR = TemporalFieldComparator.with(ChronoField.MONTH_OF_YEAR);

    public static Comparator<Temporal> nanoOfSecond() {
        return NANO_OF_SECOND;
    }

    private final static Comparator<Temporal> NANO_OF_SECOND = TemporalFieldComparator.with(ChronoField.NANO_OF_SECOND);


    /**
     * {@see NormalizingCharSequenceComparator}
     */
    public static <S extends CharSequence> Comparator<S> normalizing(final CharPredicate predicate) {
        return NormalizingCharSequenceComparator.with(predicate);
    }

    /**
     * Returns an integer that contains the sign of the given int value.
     */
    @SuppressWarnings("UseCompareMethod")
    public static int normalize(final int value) {
        return 0 == value ? 0 : value < 0 ? -1 : +1;
    }

    /**
     * Returns an integer that contains the sign of the given long value.
     */
    public static int normalize(final long value) {
        return 0 == value ? 0 : value < 0 ? -1 : +1;
    }

    /**
     * {@see NullAwareComparatorAfter}
     */
    public static <T> Comparator<T> nullAfter(final Comparator<T> comparator) {
        return NullAwareComparatorAfter.with(comparator);
    }

    /**
     * {@see NullAwareComparatorBefore}
     */
    public static <T> Comparator<T> nullBefore(final Comparator<T> comparator) {
        return NullAwareComparatorBefore.with(comparator);
    }

    
    public static Comparator<Temporal> secondOfMinute() {
        return SECOND_OF_MINUTE;
    }

    private final static Comparator<Temporal> SECOND_OF_MINUTE = TemporalFieldComparator.with(ChronoField.SECOND_OF_MINUTE);

    /**
     * {@see TemporalFieldComparator}
     */
    public static Comparator<Temporal> temporalField(final TemporalField field) {
        return TemporalFieldComparator.with(field);
    }
    
    public static Comparator<Temporal> year() {
        return YEAR;
    }
    
    private final static Comparator<Temporal> YEAR = TemporalFieldComparator.with(ChronoField.YEAR);

    /**
     * Stop creation
     */
    private Comparators() {
        throw new UnsupportedOperationException();
    }
}
