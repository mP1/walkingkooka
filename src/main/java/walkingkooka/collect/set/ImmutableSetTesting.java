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
import walkingkooka.predicate.Predicates;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableSetTesting<S extends ImmutableSet<E>, E> extends SetTesting2<S, E>,
        CanBeEmptyTesting {

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

    @Test
    default void testConcatAllWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createSet()
                        .concatAll(null)
        );
    }

    default void concatAllAndCheck(final ImmutableSet<E> set,
                                   final Collection<E> appended,
                                   final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterConcat = set.concatAll(appended);

        assertNotSame(
                afterConcat,
                set
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> set + " concatAll " + appended
        );

        final Set<E> toSet = set.toSet();
        toSet.addAll(appended);
        this.checkEquals(
                toSet,
                afterConcat,
                () -> set + " concatAll " + appended
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

    @Test
    default void testDeleteAllWithNullCollectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createSet()
                        .deleteAll(null)
        );
    }

    default void deleteAllAndCheck(final ImmutableSet<E> set,
                                   final Collection<E> remove,
                                   final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterRemove = set.deleteAll(remove);

        assertNotSame(
                afterRemove,
                set
        );
        this.checkEquals(
                expected,
                afterRemove,
                () -> set + " deleteAll " + remove
        );

        final Set<E> toSet = set.toSet();
        toSet.removeAll(remove);
        this.checkEquals(
                toSet,
                afterRemove,
                () -> set + " deleteAll " + remove
        );
    }

    // deleteIf.........................................................................................................

    @Test
    default void testDeleteIfWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createSet()
                        .deleteIf(null)
        );
    }

    @Test
    default void testDeleteIfWithNeverPredicate() {
        final S set = this.createSet();

        assertSame(
                set,
                set.deleteIf(Predicates.never())
        );
    }

    default void deleteIfAndCheck(final ImmutableSet<E> set,
                                  final Predicate<? super E> predicate,
                                  final ImmutableSet<E> expected) {
        final ImmutableSet<E> afterConcat = set.deleteIf(predicate);

        assertNotSame(
                afterConcat,
                set
        );
        this.checkEquals(
                expected,
                afterConcat,
                () -> set + " deleteIf " + predicate
        );

        final Set<E> toSet = set.toSet();
        toSet.removeIf(predicate);
        this.checkEquals(
                toSet,
                afterConcat,
                () -> set + " deleteIf " + predicate
        );
    }

    // replace..........................................................................................................

    default void replaceAndCheck(final ImmutableSet<E> set,
                                 final E oldElement,
                                 final E newElement) {
        assertSame(
                set,
                set.replace(
                        oldElement,
                        newElement
                )
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
