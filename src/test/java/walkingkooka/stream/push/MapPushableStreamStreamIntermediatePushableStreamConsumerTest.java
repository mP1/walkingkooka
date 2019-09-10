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

public final class MapPushableStreamStreamIntermediatePushableStreamConsumerTest extends NonLimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumerTestCase<MapPushableStreamStreamIntermediatePushableStreamConsumer<String>> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "map " + this.mapper + " " + NEXT_TOSTRING);
    }

    @Override
    MapPushableStreamStreamIntermediatePushableStreamConsumer<String> createPushableStreamConsumer(final PushableStreamConsumer<String> next) {
        return MapPushableStreamStreamIntermediatePushableStreamConsumer.with(this.mapper, next);
    }

    private final Function<String, String> mapper = Function.identity();

    @Override
    public Class<MapPushableStreamStreamIntermediatePushableStreamConsumer<String>> type() {
        return Cast.to(MapPushableStreamStreamIntermediatePushableStreamConsumer.class);
    }
}
