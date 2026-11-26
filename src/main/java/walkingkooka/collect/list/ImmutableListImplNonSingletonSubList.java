
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

package walkingkooka.collect.list;

import walkingkooka.Cast;

import java.util.List;

/**
 * A NON empty sub list view of a {@link ImmutableListImplNonSingleton}
 */
final class ImmutableListImplNonSingletonSubList<T> extends ImmutableListImpl<T> {

    /**
     * Returns a {@link List} which is immutable assumng the elements are from a {@link ImmutableListImplNonSingleton}
     */
    static <T> ImmutableListImplNonSingletonSubList<T> with(final Object[] elements,
                                                            final int from,
                                                            final int to) {
        return new ImmutableListImplNonSingletonSubList<>(
            elements,
            from,
            to
        );
    }

    ImmutableListImplNonSingletonSubList(final Object[] elements,
                                         final int from,
                                         final int to) {
        super();
        this.elements = elements;
        this.from = from;
        this.to = to;
    }

    @Override
    public T get(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index " + index + " < 0");
        }

        final int from = this.from;
        final int size = this.to - from;

        if (index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " > " + size);
        }

        return Cast.to(
            this.elements[index + from]
        );
    }

    @Override
    public int size() {
        return this.to - this.from;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    Object[] elements() {
        return this.elements;
    }

    private final Object[] elements;

    @Override
    int offset() {
        return this.from;
    }

    private final int from;

    private final int to;
}
