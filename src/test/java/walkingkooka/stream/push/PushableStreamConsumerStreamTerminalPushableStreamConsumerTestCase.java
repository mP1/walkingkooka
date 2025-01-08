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

public abstract class PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase<P extends PushableStreamConsumerStreamTerminalPushableStreamConsumer<String, R>, R> extends
    PushableStreamConsumerStreamPushableStreamConsumerTestCase2<P> {

    PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase() {
        super();
    }

    @Test
    public final void testEqualsDifferentCloseables() {
        this.createPushableStreamConsumer(PushableStreamConsumerCloseableCollection.empty().add(TestCloseableRunnable.with("different")));
    }

    @Override
    public final P createPushableStreamConsumer() {
        return this.createPushableStreamConsumer(this.closeables);
    }

    abstract P createPushableStreamConsumer(final PushableStreamConsumerCloseableCollection closeables);

    final PushableStreamConsumerCloseableCollection closeables = PushableStreamConsumerCloseableCollection.empty().add(TestCloseableRunnable.with("first-closeable"));

    final void checkResult(final PushableStreamConsumerStreamTerminalPushableStreamConsumer<String, R> consumer,
                           final R expected) {
        this.checkEquals(expected, consumer.result(), consumer::toString);
    }

    @Override
    public final String typeNamePrefix() {
        return PushableStreamConsumerStream.class.getSimpleName() + "Terminal";
    }
}
