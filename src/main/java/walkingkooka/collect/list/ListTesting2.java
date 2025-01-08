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
import walkingkooka.collect.CollectionTesting2;
import walkingkooka.reflect.TypeNameTesting;

import java.util.List;

public interface ListTesting2<L extends List<E>, E> extends ListTesting, CollectionTesting2<L, E>,
    TypeNameTesting<L> {

    @Test
    default void testGetNegativeIndexFails() {
        this.getFails(this.createList(), -1);
    }

    @Test
    default void testGetAndIterator() {
        final L list = this.createList();
        int i = 0;

        for (E element : list) {
            this.getAndCheck(list, i, element);
            i++;
        }

        this.sizeAndCheck(list, i);
    }

    default L createCollection() {
        return this.createList();
    }

    L createList();

    // TypeNameTesting .................................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return List.class.getSimpleName();
    }
}
