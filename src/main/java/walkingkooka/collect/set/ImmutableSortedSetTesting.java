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

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableSortedSetTesting<S extends ImmutableSortedSet<E>, E> extends SetTesting2<S, E>,
        CanBeEmptyTesting {

    default void concatAndCheck(final ImmutableSortedSet<E> set,
                                final E appended,
                                final ImmutableSortedSet<E> expected) {
        final ImmutableSortedSet<E> afterConcat = set.concat(appended);

        assertNotSame(
                afterConcat,
                set
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> set + " concat " + appended
        );

        final Set<E> toSet = set.toSet();
        toSet.add(appended);
        this.checkEquals(
                toSet,
                afterConcat,
                () -> set + " concat " + appended
        );
    }

    default void deleteAndCheck(final ImmutableSortedSet<E> set,
                                final E remove,
                                final ImmutableSortedSet<E> expected) {
        final ImmutableSortedSet<E> afterRemove = set.delete(remove);

        assertNotSame(
                afterRemove,
                set
        );
        this.checkEquals(
                expected,
                afterRemove,
                () -> set + " delete " + remove
        );

        final Set<E> toSet = set.toSet();
        toSet.remove(remove);
        this.checkEquals(
                toSet,
                afterRemove,
                () -> set + " delete " + remove
        );
    }

    default void replaceAndCheck(final ImmutableSortedSet<E> set,
                                 final E oldElement,
                                 final E newElement) {
        assertSame(
                set,
                set.replace(
                        oldElement,
                        newElement
                ),
                () -> set + " replaced " + oldElement + " with " + newElement
        );
    }

    default void replaceAndCheck(final ImmutableSortedSet<E> set,
                                 final E oldElement,
                                 final E newElement,
                                 final ImmutableSortedSet<E> expected) {
        final ImmutableSortedSet<E> afterReplace = set.replace(
                oldElement,
                newElement
        );

        assertNotSame(
                newElement,
                set
        );
        this.checkEquals(
                expected,
                afterReplace,
                () -> set + " replaced " + oldElement + " with " + newElement
        );
    }

    @Test
    default void testSetElementsNullFails() {
        final ImmutableSortedSet<E> ImmutableSortedSet = this.createSet();

        assertThrows(
                NullPointerException.class,
                () -> ImmutableSortedSet.setElements(null)
        );
    }

    @Test
    default void testSetElementsSame() {
        final ImmutableSortedSet<E> ImmutableSortedSet = this.createSet();

        assertSame(
                ImmutableSortedSet,
                ImmutableSortedSet.setElements(
                        ImmutableSortedSet.toSet()
                )
        );
    }

    default void toSetAndCheck(final ImmutableSortedSet<E> set,
                               final E... expected) {
        this.toSetAndCheck(
                set,
                Sets.of(expected)
        );
    }

    default void toSetAndCheck(final ImmutableSortedSet<E> set,
                               final Set<E> expected) {
        this.checkEquals(
                expected,
                set.toSet(),
                () -> set.toString()
        );
    }
}
