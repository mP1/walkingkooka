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

import java.util.function.Function;
import java.util.stream.Stream;

public final class FlatMapPushableStreamStreamIntermediatePushableStreamConsumerTest extends NonLimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumerTestCase<FlatMapPushableStreamStreamIntermediatePushableStreamConsumer<String, Long>> {

    @Test
    public void testDifferentMapper() {
        this.checkNotEquals(FlatMapPushableStreamStreamIntermediatePushableStreamConsumer.with((s) -> Stream.of(Long.parseLong(s)), NEXT));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), "flatmap " + this.mapper + " " + NEXT_TOSTRING);
    }

    @Override
    FlatMapPushableStreamStreamIntermediatePushableStreamConsumer<String, Long> createPushableStreamConsumer(final PushableStreamConsumer<String> next) {
        return FlatMapPushableStreamStreamIntermediatePushableStreamConsumer.with(this.mapper, next);
    }

    private final Function<String, Stream<Long>> mapper = (s) -> Stream.of(Long.parseLong(s));

    @Override
    public Class<FlatMapPushableStreamStreamIntermediatePushableStreamConsumer<String, Long>> type() {
        return Cast.to(FlatMapPushableStreamStreamIntermediatePushableStreamConsumer.class);
    }
}
