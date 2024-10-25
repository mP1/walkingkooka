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
import walkingkooka.CanBeEmptyTesting;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableListTesting<L extends ImmutableList<E>, E> extends ListTesting2<L, E>,
        CanBeEmptyTesting {

    default void concatAndCheck(final ImmutableList<E> list,
                                final E appended,
                                final ImmutableList<E> expected) {
        final ImmutableList<E> afterConcat = list.concat(appended);

        assertNotSame(
                afterConcat,
                list
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> list + " concat " + appended
        );

        final List<E> toList = list.toList();
        toList.add(appended);
        this.checkEquals(
                toList,
                afterConcat,
                () -> list + " concat " + appended
        );
    }

    @Test
    default void testConcatAllWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createList()
                        .concatAll(null)
        );
    }

    default void concatAllAndCheck(final ImmutableList<E> list,
                                   final Collection<E> appended,
                                   final ImmutableList<E> expected) {
        final ImmutableList<E> afterConcat = list.concatAll(appended);

        assertNotSame(
                afterConcat,
                list
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> list + " concat " + appended
        );

        final List<E> toList = list.toList();
        toList.addAll(appended);
        this.checkEquals(
                toList,
                afterConcat,
                () -> list + " concat " + appended
        );
    }

    default void deleteAtIndexAndCheck(final ImmutableList<E> list,
                                       final int removed,
                                       final ImmutableList<E> expected) {
        final ImmutableList<E> afterRemove = list.deleteAtIndex(removed);

        assertNotSame(
                afterRemove,
                list
        );
        this.checkEquals(
                expected,
                afterRemove,
                () -> list + " deleteAtIndex " + removed
        );

        final List<E> toList = list.toList();
        toList.remove(removed);
        this.checkEquals(
                toList,
                afterRemove,
                () -> list + " deleteAtIndex " + removed
        );
    }

    default void deleteAndCheck(final ImmutableList<E> list,
                                final E remove,
                                final ImmutableList<E> expected) {
        final ImmutableList<E> afterRemove = list.delete(remove);

        assertNotSame(
                afterRemove,
                list
        );
        this.checkEquals(
                expected,
                afterRemove,
                () -> list + " delete " + remove
        );

        final List<E> toList = list.toList();
        toList.remove(remove);
        this.checkEquals(
                toList,
                afterRemove,
                () -> list + " delete " + remove
        );
    }

    @Test
    default void testDeleteAllWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createList()
                        .deleteAll(null)
        );
    }

    @Test
    default void testDeleteAllWithEmptyCollection() {
        final L list = this.createList();

        assertSame(
                list,
                list.deleteAll(Collections.emptyList())
        );
    }

    default void deleteAllAndCheck(final ImmutableList<E> list,
                                   final Collection<E> appended,
                                   final ImmutableList<E> expected) {
        final ImmutableList<E> afterConcat = list.deleteAll(appended);

        assertNotSame(
                afterConcat,
                list
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> list + " deleteAll " + appended
        );

        final List<E> toList = list.toList();
        toList.removeAll(appended);
        this.checkEquals(
                toList,
                afterConcat,
                () -> list + " deleteAll " + appended
        );
    }

    default void replaceAndCheck(final ImmutableList<E> list,
                                 final int index,
                                 final E replace,
                                 final ImmutableList<E> expected) {
        final ImmutableList<E> afterReplace = list.replace(index, replace);

        assertNotSame(
                replace,
                list
        );
        this.checkEquals(
                expected,
                afterReplace,
                () -> list + " replaced " + index + ", " + replace
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
        final ImmutableList<E> afterSwap = list.swap(left, right);
        assertNotSame(
                afterSwap,
                list
        );
        this.checkEquals(
                expected,
                afterSwap,
                () -> list + " swap " + left + ", " + right
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
