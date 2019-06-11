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

package walkingkooka.collect.list;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all immutable {@link List} returned by {@link Lists}.
 */
abstract class ImmutableList<T> extends AbstractList<T> {

    /**
     * Special case checking for empty lists created by {@link Collections#emptyList()}.
     */
    private final static Class<?> EMPTY = Lists.empty().getClass();

    /**
     * Returns true if the {@link List} is immutable. This may not detect all but tries to attempt a few known to {@link List}.
     */
    static boolean isImmutable(final List<?> list) {
        final Class<?> type = list.getClass();
        return EMPTY == type || list instanceof ImmutableList;
    }

    /**
     * Returns a {@link List} which is immutable including copying elements if necessary.
     */
    static <T> List<T> with(final List<T> list) {
        Objects.requireNonNull(list, "list");

        return isImmutable(list) ?
                list :
                copy(list);
    }

    /**
     * Copy to an ordered {@link List} keeping the original order for sorted or unsorted {@link List lists}.
     */
    private static <T> List<T> copy(final List<T> from) {
        final List<T> to = Lists.array();
        to.addAll(from);
        return select(to);
    }

    static <T> List<T> select(final List<T> from) {
        List<T> immutable;
        switch (from.size()) {
            case 0:
                immutable = Lists.empty();
                break;
            case 1:
                immutable = singleton(from.iterator().next());
                break;
            default:
                immutable = wrap(from);
                break;
        }

        return immutable;
    }


    /**
     * {@see SingletonImmutableList}.
     */
    static <T> List<T> singleton(final T element) {
        return SingletonImmutableList.with(element);
    }

    /**
     * Creates a {@link NonSingletonImmutableList} with the given {@link List} which is not defensively copied.
     */
    static <T> ImmutableList<T> wrap(final List<T> wrap) {
        return NonSingletonImmutableList.with(wrap);
    }
}
