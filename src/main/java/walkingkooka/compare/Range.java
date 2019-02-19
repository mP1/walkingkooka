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

package walkingkooka.compare;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Range} contains a lower and upper bound of values that may be used for testing...
 * Two create a {@link Range} with both a lower and upper bound it is necessary to create a range for both
 * and then intersect them.
 */
public final class Range<C extends Comparable> implements Predicate<C>, HashCodeEqualsDefined {

    /**
     * A {@link Range} that matches all values.
     */
    public static <C extends Comparable> Range<C> all() {
        return Cast.to(ALL);
    }

    /**
     * The ALL singleton instance.
     */
    @SuppressWarnings("unchecked")
    private final static Range<?> ALL = new Range(RangeBound.all(), RangeBound.all());

    /**
     * A {@link Range} that holds a single value.
     */
    public static <C extends Comparable<?>> Range<C> singleton(final C value) {
        return new Range<C>(RangeBound.inclusive(value), RangeBound.inclusive(value));
    }

    /**
     * A {@link Range} that matches all values less than but not including the given value.
     */
    public static <C extends Comparable<?>> Range<C> lessThan(final C value) {
        return new Range<C>(RangeBound.all(), RangeBound.exclusive(value));
    }

    /**
     * A {@link Range} that matches all values less than and including the given value.
     */
    public static <C extends Comparable<?>> Range<C> lessThanEquals(final C value) {
        return new Range<C>(RangeBound.all(), RangeBound.inclusive(value));
    }

    /**
     * A {@link Range} that matches all values greater than but not including the given value.
     */
    public static <C extends Comparable<?>> Range<C> greaterThan(final C value) {
        return new Range<C>(RangeBound.exclusive(value), RangeBound.all());
    }

    /**
     * A {@link Range} that matches all values greater than and including the given value.
     */
    public static <C extends Comparable<?>> Range<C> greaterThanEquals(final C value) {
        return new Range<C>(RangeBound.inclusive(value), RangeBound.all());
    }

    /**
     * Checks that the other range is not null.
     */
    private static void checkOther(final Range<?> other) {
        Objects.requireNonNull(other, "other");
    }

    /**
     * Private ctor use factory
     */
    private Range(final RangeBound<C> lower, final RangeBound<C> upper) {
        super();

        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns a {@link Range} that matches true to both {@link Range ranges}.
     */
    public Range<C> and(final Range<C> other) {
        checkOther(other);

        final RangeBound<C> lower = this.lower.max(other.lower);
        final RangeBound<C> upper = this.upper.min(other.upper);
        return this.equals1(lower, upper) ?
                this :
                other.equals1(lower, upper) ?
                        other :
                        lower.equals(upper) ?
                                this.replace(lower, other) :
                                this.replace0(lower, upper);
    }

    /**
     * Creates a new {@link Range} after verifying the lower is less than the  upper bounds.
     */
    private Range<C> replace(final RangeBound<C> lower, final Range<C> other) {
        if (lower.isExclusive()) {
            throw new IllegalArgumentException("Invalid range bounds " + this + " < " + other);
        }

        return new Range<C>(lower, lower);
    }

    private Range<C> replace0(final RangeBound<C> lower, final RangeBound<C> upper) {
        if (lower.equals(upper) && lower.isExclusive()) {
            throw new IllegalArgumentException("Invalid range bounds " + upper + " < " + lower);
        }

        if (lower.min(upper) != lower) {
            throw new IllegalArgumentException("Invalid range bounds " + upper + " < " + lower);
        }

        if (upper.max(lower) != upper) {
            throw new IllegalArgumentException("Invalid range bounds " + lower + " > " + upper);
        }

        return new Range<C>(lower, upper);
    }


    /**
     * Getter that returns the lower bound of the range.
     */
    public RangeBound<C> lowerBound() {
        return this.lower;
    }

    final RangeBound<C> lower;

    /**
     * Getter that returns the upper bound of the range.
     */
    public RangeBound<C> upperBound() {
        return this.upper;
    }

    final RangeBound<C> upper;

    // Predicate .............................................................................................

    /**
     * Returns true if the given value is within this range honouring the lower or upper bounds if either is present.
     */
    @Override
    public boolean test(final C c) {
        return this.lower.lowerTest(c) &&
                this.upper.upperTest(c);
    }

    // isOverlapping.......................................................................................................

    /**
     * Returns true if the two ranges overlap.
     */
    public boolean isOverlapping(final Range<C> other) {
        checkOther(other);

        return this.lower.max(other.lower)
                .lessThanOrEqual(this.upper.min(other.upper));
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.lower, this.upper);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof Range &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final Range<?> other) {
        return this.equals1(other.lower, other.upper);
    }

    private boolean equals1(final RangeBound<?> lower, final RangeBound<?> upper) {
        return this.lower.equals(lower) &&
                this.upper.equals(upper);
    }
    @Override
    public String toString() {
        return this.lower.rangeToString(this);
    }
}
