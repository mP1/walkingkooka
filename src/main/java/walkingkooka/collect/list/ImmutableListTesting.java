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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableListTesting<L extends ImmutableList<E>, E> extends ListTesting2<L, E> {

    default void concatAndCheck(final ImmutableList<E> list,
                                final E appended,
                                final ImmutableList<E> expected) {
        final ImmutableList<E> appendAndNew = list.concat(appended);

        assertNotSame(
                appendAndNew,
                list
        );
        this.checkEquals(
                expected,
                appendAndNew,
                () -> list + " concat " + appended
        );

        final List<E> toList = list.toList();
        toList.add(appended);
        this.checkEquals(
                toList,
                appendAndNew,
                () -> list + " concat " + appended
        );
    }

    default void removeAndNewAndCheck(final ImmutableList<E> list,
                                      final int removed,
                                      final ImmutableList<E> expected) {
        final ImmutableList<E> removedAndNew = list.removeAndNew(removed);

        assertNotSame(
                removedAndNew,
                list
        );
        this.checkEquals(
                expected,
                removedAndNew,
                () -> list + " removedAndNew " + removed
        );

        final List<E> toList = list.toList();
        toList.remove(removed);
        this.checkEquals(
                toList,
                removedAndNew,
                () -> list + " removedAndNew " + removed
        );
    }

    default void removeElementAndNewAndCheck(final ImmutableList<E> list,
                                             final E removed,
                                             final ImmutableList<E> expected) {
        final ImmutableList<E> removedAndNew = list.removeElementAndNew(removed);

        assertNotSame(
                removedAndNew,
                list
        );
        this.checkEquals(
                expected,
                removedAndNew,
                () -> list + " removeElementAndNew " + removed
        );

        final List<E> toList = list.toList();
        toList.remove(removed);
        this.checkEquals(
                toList,
                removedAndNew,
                () -> list + " removeElementAndNew " + removed
        );
    }

    @Test
    default void testSetElementsNullFails() {
        final ImmutableList<E> immutableList = this.createList();

        assertThrows(
                NullPointerException.class,
                () -> immutableList.setElements(null)
        );
    }

    @Test
    default void testSetElementsSame() {
        final ImmutableList<E> immutableList = this.createList();

        assertSame(
                immutableList,
                immutableList.setElements(
                        immutableList.toList()
                )
        );
    }

    @Test
    default void testSwapSameIndices() {
        final ImmutableList<E> immutableList = this.createList();
        assertSame(
                immutableList,
                immutableList.swap(0, 0)
        );
    }

    default void swapAndCheck(final ImmutableList<E> list,
                              final int left,
                              final int right,
                              final ImmutableList<E> expected) {
        final ImmutableList<E> swapped = list.swap(left, right);
        assertNotSame(
                swapped,
                list
        );
        this.checkEquals(
                expected,
                swapped,
                () -> list + " swap " + left + ", " + right
        );
    }

    default void replaceAndCheck(final ImmutableList<E> list,
                                 final int index,
                                 final E replace,
                                 final ImmutableList<E> expected) {
        final ImmutableList<E> replaced = list.replace(index, replace);

        assertNotSame(
                replace,
                list
        );
        this.checkEquals(
                expected,
                replaced,
                () -> list + " replaced " + index + ", " + replace
        );
    }

    default void toListAndCheck(final ImmutableList<E> list,
                                final E... expected) {
        this.toListAndCheck(
                list,
                Lists.of(expected)
        );
    }

    default void toListAndCheck(final ImmutableList<E> list,
                                final List<E> expected) {
        this.checkEquals(
                expected,
                list.toList(),
                () -> list.toString()
        );
    }
}
