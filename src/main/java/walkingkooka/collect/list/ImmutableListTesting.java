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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ImmutableListTesting<L extends ImmutableList<E>, E> extends ListTesting2<L, E> {

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
}
