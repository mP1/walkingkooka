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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;

public abstract class PushableStreamConsumerStreamIntermediate1PushableStreamConsumerTestCase<P extends PushableStreamConsumerStreamIntermediate1PushableStreamConsumer<String>>
    extends PushableStreamConsumerStreamIntermediatePushableStreamConsumerTestCase<P> {

    PushableStreamConsumerStreamIntermediate1PushableStreamConsumerTestCase() {
        super();
    }

    final static long VALUE = 2;

    @Test
    public final void testToStringExhausted() {
        final P consumer = this.createPushableStreamConsumer();
        consumer.counter = 999;
        this.toStringAndCheck(consumer, NEXT_TOSTRING);
    }

    @Override final P createPushableStreamConsumer(final PushableStreamConsumer<String> next) {
        return this.createPushableStreamConsumer(VALUE, next);
    }

    abstract P createPushableStreamConsumer(final long value,
                                            final PushableStreamConsumer<String> next);

    final void acceptAndCheck(final long skipOrLimit,
                              final String commaSeperated,
                              final String expected,
                              final int consumedCount) {
        this.acceptAndCheck(skipOrLimit,
            commaSeperated,
            Integer.MAX_VALUE,
            expected,
            consumedCount);
    }

    final void acceptAndCheck(final long skipOrLimit,
                              final String commaSeperated,
                              final int finishedCollectedSize,
                              final String expected,
                              final int consumedCount) {
        final List<String> collected = Lists.array();

        final P consumer = this.createPushableStreamConsumer(skipOrLimit,
            new PushableStreamConsumer<>() {
                @Override
                public boolean isFinished() {
                    return collected.size() >= finishedCollectedSize;
                }

                @Override
                public void accept(final String s) {
                    collected.add(s);
                }

                @Override
                public void close() {

                }

                @Override
                public String toString() {
                    return (this.isFinished() ? "FINISHED" : "") + ", collected: " + collected;
                }
            });

        final List<String> values = this.commaSeparated(commaSeperated);

        int i = 0;
        while (i < values.size() && false == consumer.isFinished()) {
            consumer.accept(values.get(i));
            i++;
        }

        this.checkEquals(this.commaSeparated(expected),
            collected,
            () -> "values: " + CharSequences.quoteAndEscape(commaSeperated) + " " + consumer);
        this.checkEquals(consumedCount, i, () -> "consumed values count, values: " + CharSequences.quoteAndEscape(commaSeperated));
    }
}
