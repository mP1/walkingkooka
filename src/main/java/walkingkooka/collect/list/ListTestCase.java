/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.CollectionTestCase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ListTestCase<L extends List<E>, E> extends CollectionTestCase<L, E> {

    @Test
    public final void testGetNegativeIndexFails() {
        this.getFails(this.createList(), -1);
    }

    @Test
    public final void testGetAndIterator() {
        final L list = this.createList();
        int i = 0;

        for (E element : list) {
            this.getAndCheck(list, i, element);
            i++;
        }

        this.sizeAndCheck(list, i);
    }

    protected final L createCollection() {
        return this.createList();
    }

    protected abstract L createList();

    protected void getAndCheck(final List<E> list, final int index, final E element) {
        assertEquals(element, list.get(index), () -> "get " + index + " from " + list);
    }

    protected void getFails(final List<E> list, final int index) {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(index);
        });
    }
}
