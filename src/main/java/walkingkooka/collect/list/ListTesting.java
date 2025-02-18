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

import walkingkooka.collect.CollectionTesting;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ListTesting extends CollectionTesting {

    default <E> void getAndCheck(final List<E> list,
                                 final int index,
                                 final E element) {
        this.checkEquals(
            element,
            list.get(index),
            () -> "get " + index + " from " + list
        );
    }

    default <E> void getFails(final List<E> list,
                              final int index) {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> list.get(index)
        );
    }

    default <E> void setFails(final List<E> list,
                              final int index,
                              final E element) {
        assertThrows(
            RuntimeException.class,
            () -> list.set(
                index,
                element
            )
        );
    }

    default <E> void removeIndexFails(final List<E> list,
                                      final int index) {
        assertThrows(
            UnsupportedOperationException.class,
            () -> list.remove(index)
        );
    }

    default <E> void removeIndexAndCheck(final List<E> list,
                                         final int index,
                                         final E removed) {
        this.checkEquals(
            removed,
            list.remove(
                index
            ),
            "remove " + index
        );
    }

    default <E> void removeElementAndCheck(final List<E> list,
                                           final Object element,
                                           final boolean removed) {
        this.checkEquals(
            removed,
            list.remove(
                element
            ),
            "remove " + CharSequences.quoteIfChars(element)
        );
    }

    default <E> void setAndCheck(final List<E> list,
                                 final int index,
                                 final E element,
                                 final E replaced) {
        this.checkEquals(
            replaced,
            list.set(
                index,
                element
            ),
            "set " + index + " with " + element
        );
    }

    default <E> void setAndGetCheck(final List<E> list,
                                    final int index,
                                    final E element,
                                    final E replaced) {
        this.setAndCheck(
            list,
            index,
            element,
            replaced
        );
        this.getAndCheck(
            list,
            index,
            element
        );
    }
}
