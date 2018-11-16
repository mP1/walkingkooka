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
 */

package walkingkooka.collect.list;

import walkingkooka.collect.iterable.Iterables;
import walkingkooka.type.PublicStaticHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;

final public class Lists implements PublicStaticHelper {
    /**
     * {@see ArrayList}
     */
    public static <T> List<T> array() {
        return new ArrayList<T>();
    }

    /**
     * {@see CopyOnWriteArrayList}
     */
    public static <T> CopyOnWriteArrayList<T> copyOnWrite() {
        return new CopyOnWriteArrayList<T>();
    }

    /**
     * {@see Collections#emptyList()}
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }

    /**
     * Tests if both {@link List} are equal using the {@link BiPredicate} to test each and every element.
     */
    public static <T> boolean equals(final List<T> list, final List<T> other, final BiPredicate<? super T, ? super T> equivalency) {
        return Iterables.equals(list, other, equivalency);
    }

    /**
     * {@see LinkedList}
     */
    public static <T> List<T> linkedList() {
        return new LinkedList<T>();
    }

    /**
     * {@see Collections#singletonList(Object)}
     */
    public static <T> List<T> of(final T item) {
        return Collections.singletonList(item);
    }

    /**
     * {@see Arrays#asList}
     */
    @SafeVarargs
    public static <T> List<T> of(final T... items) {
        return Arrays.asList(items);
    }

    /**
     * {@see Collections#unmodifiableList(List)}
     */
    public static <T> List<T> readOnly(final List<T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * {@see Vector}
     */
    public static <T> Vector<T> vector() {
        return new Vector<T>();
    }

    /**
     * Stop creation
     */
    private Lists() {
        throw new UnsupportedOperationException();
    }
}
