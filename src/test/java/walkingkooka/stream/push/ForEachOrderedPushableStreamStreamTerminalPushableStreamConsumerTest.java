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
import walkingkooka.Cast;

import java.util.function.Consumer;

public final class ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumerTest extends PushableStreamStreamTerminalPushableStreamConsumerTestCase<ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<String>, Void> {

    @Test
    public void testDifferentAction() {
        this.checkNotEquals(ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer.with((ignored) -> {
        }, this.closeables));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), "forEachOrdered " + this.action + " closeables: " + this.closeables);
    }

    @Override
    ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<String> createPushableStreamConsumer(final CloseableCollection closeables) {
        return ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer.with(this.action, closeables);
    }

    private final Consumer<String> action = (ignored) -> {
    };

    @Override
    public Class<ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<String>> type() {
        return Cast.to(ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer.class);
    }
}
