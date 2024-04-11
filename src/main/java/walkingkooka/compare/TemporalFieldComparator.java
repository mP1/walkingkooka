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

import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that may be used to support java.time objects using a particular field. Examples include supporting
 * times by minutes.
 */
final class TemporalFieldComparator<T extends Temporal> implements Comparator<T> {

    static <T extends Temporal> TemporalFieldComparator<T> with(final TemporalField field) {
        return new TemporalFieldComparator<>(
                Objects.requireNonNull(field, "field")
        );
    }

    public TemporalFieldComparator(final TemporalField field) {
        this.field = field;
    }

    @Override
    public int compare(final T left,
                       final T right) {
        return left.get(this.field) - right.get(this.field);
    }

    private TemporalField field;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.field.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof TemporalFieldComparator && this.equals0(Cast.to(other));
    }

    private boolean equals0(final TemporalFieldComparator<?> other) {
        return this.field.equals(other.field);
    }

    @Override
    public String toString() {
        return this.field.toString();
    }
}
