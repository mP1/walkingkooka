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

public final class PushableStreamConsumerCloseableCollectionEmptyTest extends PushableStreamConsumerCloseableCollectionTestCase<PushableStreamConsumerCloseableCollectionEmpty> {

    @Test
    public void testAdd() {
        final Runnable first = TestCloseableRunnable.with("first-add");
        this.addAndCheck(this.createCloseableCollection(),
            first,
            first);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCloseableCollection(), "");
    }

    @Override
    PushableStreamConsumerCloseableCollectionEmpty createCloseableCollection() {
        return PushableStreamConsumerCloseableCollectionEmpty.INSTANCE;
    }

    @Override
    public String typeNameSuffix() {
        return "Empty";
    }

    @Override
    public Class<PushableStreamConsumerCloseableCollectionEmpty> type() {
        return PushableStreamConsumerCloseableCollectionEmpty.class;
    }
}
