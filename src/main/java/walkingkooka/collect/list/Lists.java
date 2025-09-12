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

import walkingkooka.collect.iterable.Iterables;
import walkingkooka.reflect.PublicStaticHelper;

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
        return new ArrayList<>();
    }

    /**
     * {@see AutoExpandArrayList}
     */
    public static <T> List<T> autoExpandArray() {
        return AutoExpandArrayList.empty();
    }

    /**
     * {@see CopyOnWriteArrayList}
     */
    public static <T> CopyOnWriteArrayList<T> copyOnWrite() {
        return new CopyOnWriteArrayList<>();
    }

    /**
     * {@see ImmutableListImplEmpty.empty()}
     */
    public static <T> ImmutableList<T> empty() {
        return ImmutableListImplEmpty.empty();
    }

    /**
     * Tests if both {@link List} are equal using the {@link BiPredicate} to test each and every element.
     */
    public static <T> boolean equals(final List<T> list, final List<T> other, final BiPredicate<? super T, ? super T> equivalency) {
        return Iterables.equals(list, other, equivalency);
    }

    /**
     * Returns a {@link ImmutableList} that is immutable, making a defensive copy if necessary.
     */
    public static <E> ImmutableList<E> immutable(final List<E> list) {
        return ImmutableListImpl.with(list);
    }

    /**
     * {@see LinkedList}
     */
    public static <T> List<T> linkedList() {
        return new LinkedList<>();
    }

    /**
     * {@see ImmutableListImpl#singleton}.
     */
    public static <T> List<T> of(final T element) {
        return ImmutableListImpl.singleton(element);
    }

    /**
     * {@see ImmutableListImpl#prepare}
     */
    @SafeVarargs
    public static <T> List<T> of(final T... elements) {
        return ImmutableListImpl.prepare(
            Arrays.copyOf(
                elements,
                elements.length
            )
        );
    }

    /**
     * Returns a read only view of the given list. If the list is modified then the read only view will also contain different elements.
     */
    public static <T> List<T> readOnly(final List<T> list) {
        return list instanceof ImmutableList ?
            list :
            Collections.unmodifiableList(list);
    }

    /**
     * {@see StringList}
     */
    public static StringList stringList() {
        return StringList.EMPTY;
    }

    /**
     * {@see Vector}
     */
    public static <T> Vector<T> vector() {
        return new Vector<>();
    }

    /**
     * Stop creation
     */
    private Lists() {
        throw new UnsupportedOperationException();
    }
}
