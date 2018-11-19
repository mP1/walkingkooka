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

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a inclusive value within a {@link Range}
 */
final class RangeBoundInclusive<C extends Comparable<C>> extends RangeBound<C> {

    /**
     * Creates a {@link RangeBoundInclusive}.
     */
    static <C extends Comparable<C>> RangeBoundInclusive<C> with(final C value) {
        Objects.requireNonNull(value, "value");
        return new RangeBoundInclusive<C>(value);
    }

    /**
     * Private ctor use factory
     */
    private RangeBoundInclusive(final C value) {
        super();
        this.value = value;
    }

    /**
     * Always false
     */
    @Override
    public boolean isAll() {
        return false;
    }

    /**
     * Always false
     */
    @Override
    public boolean isExclusive() {
        return false;
    }

    /**
     * Always false
     */
    public boolean isInclusive() {
        return true;
    }

    /**
     * ALways nothing.
     */
    @Override
    public Optional<C> value() {
        return Optional.of(this.value);
    }

    // Range.predicate...........................................

    @Override
    boolean lowerTest(final C value) {
        return this.value.compareTo(value) <= 0;
    }

    @Override
    boolean upperTest(final C value) {
        return this.value.compareTo(value) >= 0;
    }

    // Range.intersection.......................................................

    @Override
    RangeBound<C> min(final RangeBound<C> other) {
        return other.min0(this);
    }

    @Override
    RangeBound<C> min0(final RangeBoundExclusive<C> other) {
        return this.value.compareTo(other.value) <= 0 ?
                this :
                other;
    }

    @Override
    RangeBound<C> min0(final RangeBoundInclusive<C> other) {
        return this.value.compareTo(other.value) <= 0 ?
                this :
                other;
    }

    @Override
    RangeBound<C> max(final RangeBound<C> other) {
        return other.max0(this);
    }

    @Override
    RangeBound<C> max0(final RangeBoundExclusive<C> other) {
        return this.value.compareTo(other.value) >= 0 ?
                this :
                other;
    }

    @Override
    RangeBound<C> max0(final RangeBoundInclusive<C> other) {
        return this.value.compareTo(other.value) >= 0 ?
                this :
                other;
    }

    // Range.toString.......................................................

    @Override
    String rangeToString(final Range<C> range) {
        return range.upper.rangeToString0(this);
    }

    @Override
    String rangeToString0(final RangeBoundAll<C> lower) {
        return "<=" + this.value;
    }

    @Override
    String rangeToString0(final RangeBoundExclusive<C> lower) {
        return EXCLUSIVE_OPEN + lower.value + ".." + this.value + INCLUSIVE_CLOSE;
    }

    @Override
    String rangeToString0(final RangeBoundInclusive<C> lower) {
        return this.value.equals(lower.value) ?
                String.valueOf(this.value) :
                INCLUSIVE_OPEN + lower.value + BETWEEN + this.value + INCLUSIVE_CLOSE;
    }

    // Object........................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof RangeBoundInclusive &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final RangeBoundInclusive<?> other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return "Inclusive: " + this.value;
    }

    final C value;
}
