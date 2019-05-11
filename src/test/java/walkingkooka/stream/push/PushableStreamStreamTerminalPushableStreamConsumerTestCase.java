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

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;

public abstract class PushableStreamStreamTerminalPushableStreamConsumerTestCase<P extends PushableStreamStreamTerminalPushableStreamConsumer<String, R>, R> extends
        PushableStreamStreamPushableStreamConsumerTestCase2<P> {

    PushableStreamStreamTerminalPushableStreamConsumerTestCase() {
        super();
    }

    @Test
    public final void testDifferentCloseables() {
        this.createPushableStreamStreamPushableStreamConsumer(CloseableCollection.empty().add(TestCloseableRunnable.with("different")));
    }

    @Override
    final P createPushableStreamStreamPushableStreamConsumer() {
        return this.createPushableStreamStreamPushableStreamConsumer(this.closeables);
    }

    abstract P createPushableStreamStreamPushableStreamConsumer(final CloseableCollection closeables);

    final CloseableCollection closeables = CloseableCollection.empty().add(TestCloseableRunnable.with("first-closeable"));

    @Override
    public final String typeNameSuffix() {
        return PushableStreamStreamTerminalPushableStreamConsumer.class.getSimpleName();
    }
}
