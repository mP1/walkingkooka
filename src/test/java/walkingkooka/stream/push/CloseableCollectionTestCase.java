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

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CloseableCollectionTestCase<C extends CloseableCollection>
        extends PushTestCase<C>
        implements ToStringTesting<C>,
        TypeNameTesting<C> {

    CloseableCollectionTestCase() {
        super();
    }

    @Test
    public final void testAddNullCloseableRunnableFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createCloseableCollection().add(null);
        });
    }

    final void addAndCheck(final CloseableCollection collection,
                           final Runnable add,
                           final Runnable...expected) {
        final NonEmptyCloseableCollection added = collection.add(add);
        assertNotSame(added, collection);
        assertEquals(Lists.of(expected),
                added.closeables,
                () -> collection + " add " + add);
    }

    abstract C createCloseableCollection();

    @Override
    public final String typeNameSuffix() {
        return CloseableCollection.class.getSimpleName();
    }
}
