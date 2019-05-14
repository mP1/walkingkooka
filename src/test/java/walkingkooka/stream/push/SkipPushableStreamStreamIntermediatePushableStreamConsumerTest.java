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

public final class SkipPushableStreamStreamIntermediatePushableStreamConsumerTest extends
        LimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumerTestCase<SkipPushableStreamStreamIntermediatePushableStreamConsumer<String>> {

    @Test
    public void testSkip0() {
        this.acceptAndCheck(0, "a1,b2,c3", "a1,b2,c3", 3);
    }

    @Test
    public void testSkip1() {
        this.acceptAndCheck(1, "a1,b2,c3", "b2,c3", 3);
    }

    @Test
    public void testSkip2() {
        this.acceptAndCheck(2, "a1,b2,c3", "c3", 3);
    }

    @Test
    public void testSkip3() {
        this.acceptAndCheck(3, "a1,b2,c3", "", 3);
    }

    @Test
    public void testSkipExceedsActualValuesCount() {
        this.acceptAndCheck(99, "a1,b2,c3", "", 3);
    }

    @Test
    public void testSkipEarlyFinished() {
        this.acceptAndCheck(1, "a1,b2,c3", 0, "", 0);
    }

    @Test
    public void testToString() {
        final SkipPushableStreamStreamIntermediatePushableStreamConsumer<String> consumer = this.createPushableStreamStreamPushableStreamConsumer();
        consumer.counter = 1;
        this.toStringAndCheck(consumer, "skip 1/2 " + NEXT_TOSTRING);
    }

    @Override
    SkipPushableStreamStreamIntermediatePushableStreamConsumer<String> createPushableStreamStreamPushableStreamConsumer(final long value,
                                                                                                                        final PushableStreamConsumer<String> next) {
        return SkipPushableStreamStreamIntermediatePushableStreamConsumer.with(value, next);
    }

    @Override
    public Class<SkipPushableStreamStreamIntermediatePushableStreamConsumer<String>> type() {
        return Cast.to(SkipPushableStreamStreamIntermediatePushableStreamConsumer.class);
    }
}
