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

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ImmutableListImplNotEmptyTestCase<S extends ImmutableListImpl<String>> extends ImmutableListImplTestCase2<S> {

    ImmutableListImplNotEmptyTestCase() {
        super();
    }

    @Test
    public final void testDeleteAtIndexNew() {
        final ImmutableListImpl<String> immutableList = this.createList();
        final String removed = immutableList.get(0);

        final List<String> list = immutableList.toList();
        list.remove(0);

        this.deleteAtIndexAndCheck(
                immutableList,
                0,
                (ImmutableList<String>)
                        ImmutableListImpl.with(list)
        );
    }

    @Test
    public final void testDeleteNew() {
        final ImmutableListImpl<String> immutableList = this.createList();
        final String removed = immutableList.get(0);

        final List<String> list = immutableList.toList();
        list.remove(0);

        this.deleteAndCheck(
                immutableList,
                removed,
                (ImmutableList<String>)
                        ImmutableListImpl.with(list)
        );
    }
    
    @Test
    public final void testIteratorRemoveFails() {
        final Iterator<String> iterator = this.createList().iterator();
        iterator.next();

        assertThrows(
                UnsupportedOperationException.class,
                iterator::remove
        );
    }
}
