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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that invokes {@link Comparator#compare(Object, Object)} on its inputs.
 */
final class NaturalOrderingComparator<T extends Comparable<T>>
        implements Comparator<T>, Serializable {

    private static final long serialVersionUID = -3279592093936918816L;

    /**
     * Singleton
     */
    @SuppressWarnings("rawtypes")
    private final static NaturalOrderingComparator INSTANCE = new NaturalOrderingComparator();

    /**
     * Type safe getter.
     */
    static <T extends Comparable<T>> NaturalOrderingComparator<T> instance() {
        return Cast.to(NaturalOrderingComparator.INSTANCE);
    }

    /**
     * Private constructor use instance getter
     */
    private NaturalOrderingComparator() {
        super();
    }

    @Override
    public int compare(final T object1, final T object2) {
        Objects.requireNonNull(object1, "first");
        Objects.requireNonNull(object2, "second");

        return object1.compareTo(object2);
    }

    private Object readResolve() {
        return NaturalOrderingComparator.INSTANCE;
    }

    @Override
    public String toString() {
        return "natural";
    }
}
