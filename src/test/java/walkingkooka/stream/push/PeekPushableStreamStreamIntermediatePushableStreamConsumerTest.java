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

import java.util.function.Consumer;

public final class PeekPushableStreamStreamIntermediatePushableStreamConsumerTest extends NonLimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumerTestCase<PeekPushableStreamStreamIntermediatePushableStreamConsumer<String>> {

    @Test
    public void testDifferentPredicate() {
        this.checkNotEquals(PeekPushableStreamStreamIntermediatePushableStreamConsumer.with((ignored) -> {}, NEXT));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), "peek " + action + " " + NEXT_TOSTRING);
    }

    @Override
    PeekPushableStreamStreamIntermediatePushableStreamConsumer<String> createPushableStreamConsumer(final PushableStreamConsumer<String> next) {
        return PeekPushableStreamStreamIntermediatePushableStreamConsumer.with(this.action, next);
    }

    private final Consumer<String> action = (ignored) -> {};

    @Override
    public Class<PeekPushableStreamStreamIntermediatePushableStreamConsumer<String>> type() {
        return Cast.to(PeekPushableStreamStreamIntermediatePushableStreamConsumer.class);
    }
}
