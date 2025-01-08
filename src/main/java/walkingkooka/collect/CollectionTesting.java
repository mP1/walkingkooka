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

package walkingkooka.collect;

import walkingkooka.collect.list.Lists;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface CollectionTesting extends Testing {

    default <E> void addFails(final Collection<E> collection,
                              final E add) {
        final List<E> before = Lists.array();
        before.addAll(collection);

        assertThrows(UnsupportedOperationException.class, () -> collection.add(add));

        final List<E> after = Lists.array();
        after.addAll(collection);

        this.checkEquals(
            before,
            after,
            () -> "add modified collection " + collection
        );
    }

    default <E> void containsAndCheck(final Collection<E> collection,
                                      final E element) {
        this.checkEquals(
            true,
            collection.contains(element),
            () -> collection + " should contain " + CharSequences.quoteIfChars(element)
        );
        this.checkEquals(
            true,
            collection.containsAll(Lists.of(element)),
            () -> collection + " should NOT contain Collection of " + CharSequences.quoteIfChars(element)
        );
    }

    default void containsAndCheckAbsent(final Collection<?> collection,
                                        final Object element) {
        this.checkEquals(
            false,
            collection.contains(element),
            () -> collection + " should contain " + CharSequences.quoteIfChars(element)
        );
        this.checkEquals(
            false,
            collection.containsAll(Lists.of(element)),
            () -> collection + " should NOT contain Collection of " + CharSequences.quoteIfChars(element)
        );
    }

    default void isEmptyAndCheck(final Collection<?> collection, final boolean empty) {
        this.checkEquals(
            empty,
            collection.isEmpty(),
            () -> "isEmpty of " + collection
        );
    }

    default <E> void removeFails(final Collection<E> collection,
                                 final E remove) {
        final List<E> before = Lists.array();
        before.addAll(collection);

        assertThrows(UnsupportedOperationException.class, () -> collection.remove(remove));

        final List<E> after = Lists.array();
        after.addAll(collection);
        this.checkEquals(
            before,
            after,
            () -> "remove modified collection " + collection
        );
    }

    default void sizeAndCheck(final Collection<?> collection, final int size) {
        this.checkEquals(
            size,
            collection.size(),
            () -> "size of " + collection
        );
        this.isEmptyAndCheck(collection, 0 == size);
    }
}
