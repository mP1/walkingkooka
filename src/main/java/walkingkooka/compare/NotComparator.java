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

import walkingkooka.predicate.Notable;
import walkingkooka.predicate.Notables;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that inverts the result of another wrapped {@link Comparator}.
 */
final class NotComparator<T>
        implements Comparator<T>, Notable<Comparator<T>>, HashCodeEqualsDefined, Serializable {
    private final static long serialVersionUID = 1L;

    static <T> Comparator<T> wrap(final Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "comparator");

        final Comparator<T> not = Notables.maybeNotable(comparator);
        return null != not ? not : new NotComparator<T>(comparator);
    }

    /**
     * Private constructor use factory.
     */
    private NotComparator(final Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    // Notable

    @Override
    public Comparator<T> negate() {
        return this.comparator;
    }

    // Comparator

    @Override
    public int compare(final T o1, final T o2) {
        return -this.comparator.compare(o1, o2);
    }

    private final Comparator<T> comparator;

    // Object

    @Override
    public int hashCode() {
        return this.comparator.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof NotComparator)
                && this.equals((NotComparator<?>) other));
    }

    private boolean equals(final NotComparator<?> other) {
        return this.comparator.equals(other.comparator);
    }

    @Override
    public String toString() {
        return "NOT (" + this.comparator + ")";
    }
}
