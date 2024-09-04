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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableSetTesting<L extends ImmutableSet<E>, E> extends SetTesting2<L, E> {

    default void concatAndCheck(final ImmutableSet<E> set,
                                final E appended,
                                final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterConcat = set.concat(appended);

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

    default void deleteAndCheck(final ImmutableSet<E> set,
                                final E remove,
                                final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterRemove = set.delete(remove);

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

    default void replaceAndCheck(final ImmutableSet<E> set,
                                 final E oldElement,
                                 final E newElement,
                                 final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterReplace = set.replace(
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
        final ImmutableSet<E> immutableSet = this.createSet();

        assertThrows(
                NullPointerException.class,
                () -> immutableSet.setElements(null)
        );
    }

    @Test
    default void testSetElementsSame() {
        final ImmutableSet<E> immutableSet = this.createSet();

        assertSame(
                immutableSet,
                immutableSet.setElements(
                        immutableSet.toSet()
                )
        );
    }

    default void toSetAndCheck(final ImmutableSet<E> set,
                               final E... expected) {
        this.toSetAndCheck(
                set,
                Sets.of(expected)
        );
    }

    default void toSetAndCheck(final ImmutableSet<E> set,
                               final Set<E> expected) {
        this.checkEquals(
                expected,
                set.toSet(),
                () -> set.toString()
        );
    }
}
