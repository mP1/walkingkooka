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

import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;
import java.util.List;

/**
 * A {@link List} presents a read only view of a defensively copied {@link List} given to it.
 */
final class ImmutableListNonSingleton<T> extends ImmutableList<T> {

    /**
     * Returns a {@link List} which is immutable but does not make a copy of the given list which assumed to already be copied.
     */
    static <T> ImmutableListNonSingleton<T> with(final List<T> notCopied) {
        return new ImmutableListNonSingleton<>(notCopied);
    }

    private ImmutableListNonSingleton(final List<T> list) {
        super();
        this.list = list;
    }

    @Override
    public boolean contains(final Object other) {
        return this.list.contains(other);
    }

    @Override
    public T get(final int index) {
        return this.list.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.readOnly(this.list.iterator());
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private final List<T> list;

    @Override
    public String toString() {
        return this.list.toString();
    }
}
