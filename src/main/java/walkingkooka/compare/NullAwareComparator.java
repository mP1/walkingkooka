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

import java.util.Comparator;

/**
 * Base class for a {@link Comparator} that sorts nulls before or after other comparisons.
 */
abstract class NullAwareComparator<T> implements Comparator<T> {

    NullAwareComparator(final Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public final int compare(final T left,
                             final T right) {
        final boolean leftIsNull = null == left;
        final boolean rightIsNull = null == right;

        return leftIsNull || rightIsNull ?
            leftIsNull && rightIsNull ?
                Comparators.EQUAL :
                leftIsNull ?
                    this.leftIsNull() :
                    this.rightIsNull() :
            this.comparator.compare(left, right);
    }

    abstract int leftIsNull();

    abstract int rightIsNull();

    final Comparator<T> comparator;

    public abstract int hashCode();

    public abstract boolean equals(final Object other);

    public abstract String toString();
}
