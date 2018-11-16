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

/**
 * Represents a bound within a range that matches everything.
 */
final class RangeBoundAll<C extends Comparable<C>> extends RangeBound<C> {

    /**
     * Type safe getter
     */
    static <C extends Comparable<C>> RangeBoundAll<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    @SuppressWarnings("RawTypes")
    private final static RangeBoundAll INSTANCE = new RangeBoundAll();

    /**
     * Use singleton getter
     */
    private RangeBoundAll() {
        super();
    }

    // Predicate ...............................................................................................

    @Override
    boolean lowerTest(final C value) {
        Objects.requireNonNull(value, "value");
        return true;
    }

    @Override
    boolean upperTest(final C value) {
        return true;
    }

    // Range.intersection...............................................................................................

    @Override
    RangeBound<C> min(final RangeBound<C> other) {
        return other;
    }

    @Override
    RangeBound<C> min0(final RangeBoundExclusive<C> other) {
        return other;
    }

    @Override
    RangeBound<C> min0(final RangeBoundInclusive<C> other) {
        return other;
    }

    @Override
    RangeBound<C> max(final RangeBound<C> other) {
        return other;
    }

    @Override
    RangeBound<C> max0(final RangeBoundExclusive<C> other) {
        return other;
    }

    @Override
    RangeBound<C> max0(final RangeBoundInclusive<C> other) {
        return other;
    }

    // Range.toString...............................................................................................

    @Override
    String rangeToString(final Range<C> range) {
        return range.upper.rangeToString0(this);
    }

    @Override
    String rangeToString0(final RangeBoundAll<C> upper) {
        return "*";
    }

    @Override
    String rangeToString0(final RangeBoundExclusive<C> upper) {
        return ">" + upper.value;
    }

    @Override
    String rangeToString0(final RangeBoundInclusive<C> upper) {
        return ">=" + upper.value;
    }

    // Object.................................................................................................

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other;
    }

    @Override
    public String toString() {
        return "*";
    }
}
