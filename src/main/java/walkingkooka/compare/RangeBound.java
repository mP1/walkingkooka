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

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Optional;

/**
 * One of three bounds of a {@link Range}
 */
abstract public class RangeBound<C extends Comparable<C>> implements HashCodeEqualsDefined, Value<Optional<C>> {

    /**
     * {@see RangeBoundAll}
     */
    static <C extends Comparable<C>> RangeBoundAll<C> all() {
        return RangeBoundAll.instance();
    }

    /**
     * {@see RangeBoundExclusive}
     */
    static <C extends Comparable<C>> RangeBoundExclusive<C> exclusive(final C value) {
        return RangeBoundExclusive.with(value);
    }

    /**
     * {@see RangeBoundInclusive}
     */
    static <C extends Comparable<C>> RangeBoundInclusive<C> inclusive(final C value) {
        return RangeBoundInclusive.with(value);
    }

    /**
     * Package private to limit sub classing.
     */
    RangeBound() {
        super();
    }

    /**
     * Only bounds without a value return true.
     */
    public abstract boolean isAll();

    /**
     * Only bounds with an exclusive value return true.
     */
    public abstract boolean isExclusive();

    /**
     * Only bounds with an inclusive value return true.
     */
    public abstract boolean isInclusive();

    /**
     * Inclusive and exclusive will also include a value.
     */
    @Override
    public abstract Optional<C> value();

    // Range.predicate...........................................

    /**
     * Tests if the value.
     */
    abstract boolean lowerTest(final C value);

    /**
     * Tests if the value.
     */
    abstract boolean upperTest(final C value);

    // Range.and...........................................

    abstract RangeBound<C> min(final RangeBound<C> other);

    final RangeBound<C> min0(final RangeBoundAll<C> other) {
        return this;
    }

    abstract RangeBound<C> min0(final RangeBoundExclusive<C> other);

    abstract RangeBound<C> min0(final RangeBoundInclusive<C> other);

    abstract RangeBound<C> max(final RangeBound<C> other);

    final RangeBound<C> max0(final RangeBoundAll<C> other) {
        return this;
    }

    abstract RangeBound<C> max0(final RangeBoundExclusive<C> other);

    abstract RangeBound<C> max0(final RangeBoundInclusive<C> other);

    // toString...............................................

    final static String BETWEEN = "..";

    final static String INCLUSIVE_OPEN = "(";
    final static char INCLUSIVE_CLOSE = ')';

    final static String EXCLUSIVE_OPEN = "[";
    final static char EXCLUSIVE_CLOSE = ']';

    abstract String rangeToString(final Range<C> range);

    abstract String rangeToString0(final RangeBoundAll<C> lower);

    abstract String rangeToString0(final RangeBoundExclusive<C> lower);

    abstract String rangeToString0(final RangeBoundInclusive<C> lower);

    // Object................................

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(final Object other);
}
