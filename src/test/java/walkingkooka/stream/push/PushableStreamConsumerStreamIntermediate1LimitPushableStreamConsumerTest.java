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

public final class PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumerTest extends
    PushableStreamConsumerStreamIntermediate1PushableStreamConsumerTestCase<PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer<String>> {

    @Test
    public void testLimit0() {
        this.acceptAndCheck(0, "a1,b2,c3", "", 0);
    }

    @Test
    public void testLimit1() {
        this.acceptAndCheck(1, "a1,b2,c3", "a1", 1);
    }

    @Test
    public void testLimit2() {
        this.acceptAndCheck(2, "a1,b2,c3", "a1,b2", 2);
    }

    @Test
    public void testLimit3() {
        this.acceptAndCheck(3, "a1,b2,c3", "a1,b2,c3", 3);
    }

    @Test
    public void testLimitExceedsActualValuesCount() {
        this.acceptAndCheck(99, "a1,b2,c3", "a1,b2,c3", 3);
    }

    @Test
    public void testLimitEarlyFinish() {
        this.acceptAndCheck(1, "a1,b2,c3", 1, "a1", 1);
    }

    @Test
    public void testToString() {
        final PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer<String> consumer = this.createPushableStreamConsumer();
        consumer.counter = 1;
        this.toStringAndCheck(consumer, "limit 1/2 " + NEXT_TOSTRING);
    }

    @Override
    PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer<String> createPushableStreamConsumer(final long value,
                                                                                                              final PushableStreamConsumer<String> next) {
        return PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer.with(value, next);
    }

    @Override
    public Class<PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer<String>> type() {
        return Cast.to(PushableStreamConsumerStreamIntermediate1LimitPushableStreamConsumer.class);
    }
}
