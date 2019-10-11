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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;

import java.util.Collection;

public interface CollectionTesting2<C extends Collection<E>, E> extends CollectionTesting, ToStringTesting<C> {

    @Test
    default void testContainsAbsent() {
        this.containsAndCheckAbsent(this.createCollection(), new Object());
    }

    @Test
    default void testIteratorContainsAndCollection() {
        int size = 0;
        final C collection = this.createCollection();

        for (final E element : collection) {
            containsAndCheck(collection, element);
            size++;
        }

        this.sizeAndCheck(collection, size);
    }

    @Test
    default void testIsEmptyAndSize() {
        final C collection = this.createCollection();
        final int size = collection.size();
        this.isEmptyAndCheck(collection, 0 == size);
    }

    C createCollection();
}
