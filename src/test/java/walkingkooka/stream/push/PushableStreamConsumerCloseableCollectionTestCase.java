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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.TypeNameTesting;

import java.io.Closeable;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PushableStreamConsumerCloseableCollectionTestCase<C extends PushableStreamConsumerCloseableCollection>
    extends PushableStreamConsumerTestCase<C>
    implements ToStringTesting<C>,
    TypeNameTesting<C> {

    PushableStreamConsumerCloseableCollectionTestCase() {
        super();
    }

    @Test
    public final void testAddNullCloseableRunnableFails() {
        assertThrows(NullPointerException.class, () -> this.createCloseableCollection().add(null));
    }

    final void addAndCheck(final PushableStreamConsumerCloseableCollection collection,
                           final Runnable add,
                           final Runnable... expected) {
        final PushableStreamConsumerCloseableCollectionNonEmpty added = collection.add(add);
        assertNotSame(added, collection);
        this.checkEquals(Lists.of(expected),
            added.closeables,
            () -> collection + " add " + add);
    }

    abstract C createCloseableCollection();

    @Override
    public final String typeNamePrefix() {
        return PushableStreamConsumer.class.getSimpleName() + Closeable.class.getSimpleName() + Collection.class.getSimpleName();
    }
}
