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
import walkingkooka.collect.set.Sets;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CollectorPushableStreamStreamTerminalPushableStreamConsumerTest extends PushableStreamStreamTerminalPushableStreamConsumerTestCase<CollectorPushableStreamStreamTerminalPushableStreamConsumer<String, List<String>, List<String>>, List<String>> {

    @Test
    public void testWithNullCollectorFails() {
        assertThrows(NullPointerException.class, () -> {
            CollectorPushableStreamStreamTerminalPushableStreamConsumer.with(null, CloseableCollection.empty());
        });
    }

    @Test
    public void testDifferentCollector() {
        this.checkNotEquals(CollectorPushableStreamStreamTerminalPushableStreamConsumer.with(Collectors.toCollection(Sets::ordered), this.closeables));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), "collect " + this.collector + "  closeables: " + this.closeables);
    }

    @Test
    public void testToStringContainerNotEmpty() {
        final CollectorPushableStreamStreamTerminalPushableStreamConsumer<String, List<String>, List<String>> collector = this.createObject();
        collector.container.add("1a");
        collector.container.add("2b");
        collector.container.add("3c");

        this.toStringAndCheck(collector, "collect " + this.collector + " \"1a\", \"2b\", \"3c\" closeables: " + this.closeables);
    }

    @Override
    CollectorPushableStreamStreamTerminalPushableStreamConsumer<String, List<String>, List<String>> createPushableStreamConsumer(final CloseableCollection closeables) {
        return CollectorPushableStreamStreamTerminalPushableStreamConsumer.with(this.collector, closeables);
    }

    private final Collector<String, List<String>, List<String>> collector = Cast.to(Collectors.toList());

    @Override
    public Class<CollectorPushableStreamStreamTerminalPushableStreamConsumer<String, List<String>, List<String>>> type() {
        return Cast.to(CollectorPushableStreamStreamTerminalPushableStreamConsumer.class);
    }
}
