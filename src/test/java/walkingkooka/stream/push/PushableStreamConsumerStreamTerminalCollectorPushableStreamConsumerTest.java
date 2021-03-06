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
import walkingkooka.Cast;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumerTest extends PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase<PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer<String, List<String>, List<String>>, List<String>> {

    @Test
    public void testWithNullCollectorFails() {
        assertThrows(NullPointerException.class, () -> PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer.with(null, PushableStreamConsumerCloseableCollection.empty()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "collect " + this.collector + " closeables: " + this.closeables);
    }

    @Test
    public void testToStringContainerNotEmpty() {
        final PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer<String, List<String>, List<String>> collector = this.createPushableStreamConsumer();
        collector.container.add("1a");
        collector.container.add("2b");
        collector.container.add("3c");

        this.toStringAndCheck(collector, "collect " + this.collector + " \"1a\", \"2b\", \"3c\" closeables: " + this.closeables);
    }

    @Override
    PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer<String, List<String>, List<String>> createPushableStreamConsumer(final PushableStreamConsumerCloseableCollection closeables) {
        return PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer.with(this.collector, closeables);
    }

    private final Collector<String, List<String>, List<String>> collector = Cast.to(Collectors.toList());

    @Override
    public Class<PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer<String, List<String>, List<String>>> type() {
        return Cast.to(PushableStreamConsumerStreamTerminalCollectorPushableStreamConsumer.class);
    }
}
