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

import java.util.function.Function;
import java.util.stream.Stream;

public final class PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumerTest extends PushableStreamConsumerStreamIntermediate2PushableStreamConsumerTestCase<PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumer<String, Long>> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "flatmap " + this.mapper + " " + NEXT_TOSTRING);
    }

    @Override
    PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumer<String, Long> createPushableStreamConsumer(final PushableStreamConsumer<String> next) {
        return PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumer.with(this.mapper, next);
    }

    private final Function<String, Stream<Long>> mapper = (s) -> Stream.of(Long.parseLong(s));

    @Override
    public Class<PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumer<String, Long>> type() {
        return Cast.to(PushableStreamConsumerStreamIntermediate2FlatMapPushableStreamConsumer.class);
    }
}
