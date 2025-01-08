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

import walkingkooka.Cast;

import java.util.Comparator;
import java.util.Objects;

final class NullAwareComparatorBefore<T> extends NullAwareComparator<T> {

    static <T> NullAwareComparatorBefore<T> with(final Comparator<T> comparator) {
        return comparator instanceof NullAwareComparatorBefore ?
            Cast.to(comparator) :
            new NullAwareComparatorBefore<>(
                Objects.requireNonNull(comparator, "comparator")
            );
    }

    private NullAwareComparatorBefore(final Comparator<T> comparator) {
        super(comparator);
    }

    @Override
    int leftIsNull() {
        return Comparators.LESS;
    }

    @Override
    int rightIsNull() {
        return Comparators.MORE;
    }

    @Override
    public int hashCode() {
        return this.comparator.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof NullAwareComparatorBefore && this.equals0((NullAwareComparatorBefore<T>) other);
    }

    private boolean equals0(final NullAwareComparatorBefore<?> other) {
        return this.comparator.equals(other.comparator);
    }

    @Override
    public String toString() {
        return "null < " + this.comparator;
    }
}
