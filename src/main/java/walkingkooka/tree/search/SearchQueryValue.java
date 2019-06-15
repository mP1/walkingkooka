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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Holds a query value and provides factory methods to create {@link SearchQuery}.
 */
public abstract class SearchQueryValue implements HashCodeEqualsDefined {

    /**
     * {@see SearchBigDecimalQueryValue}
     */
    public static SearchBigDecimalQueryValue bigDecimal(final BigDecimal value) {
        return SearchBigDecimalQueryValue.with(value);
    }

    /**
     * {@see SearchBigIntegerQueryValue}
     */
    public static SearchBigIntegerQueryValue bigInteger(final BigInteger value) {
        return SearchBigIntegerQueryValue.with(value);
    }

    /**
     * {@see SearchDoubleQueryValue}
     */
    public static SearchDoubleQueryValue doubleValue(final double value) {
        return SearchDoubleQueryValue.with(value);
    }

    /**
     * {@see SearchLocalDateQueryValue}
     */
    public static SearchLocalDateQueryValue localDate(final LocalDate value) {
        return SearchLocalDateQueryValue.with(value);
    }

    /**
     * {@see SearchLocalDateTimeQueryValue}
     */
    public static SearchLocalDateTimeQueryValue localDateTime(final LocalDateTime value) {
        return SearchLocalDateTimeQueryValue.with(value);
    }

    /**
     * {@see SearchLocalTimeQueryValue}
     */
    public static SearchLocalTimeQueryValue localTime(final LocalTime value) {
        return SearchLocalTimeQueryValue.with(value);
    }

    /**
     * {@see SearchLongQueryValue}
     */
    public static SearchLongQueryValue longValue(final long value) {
        return SearchLongQueryValue.with(value);
    }

    /**
     * {@see SearchStringQueryValue}
     */
    public static SearchTextQueryValue text(final String value) {
        return SearchTextQueryValue.with(value);
    }

    /**
     * Used in static factory methods to verify non nullness of values.
     */
    static <T> void check(final T value) {
        Objects.requireNonNull(value, "value");
    }

    /**
     * Package private to limit sub classing.
     */
    SearchQueryValue() {
    }

    /**
     * This method is only implemented by {@link SearchTextQueryValue}
     */
    abstract String text();

    abstract Object value();

    @Override
    public final int hashCode() {
        return this.value().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final SearchQueryValue other) {
        return this.value().equals(other.value());
    }
}
