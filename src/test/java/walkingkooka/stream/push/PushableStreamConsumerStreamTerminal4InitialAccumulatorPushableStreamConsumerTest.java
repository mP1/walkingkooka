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

import java.util.function.BiFunction;

public final class PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumerTest extends PushableStreamConsumerStreamTerminal4PushableStreamConsumerTestCase<PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer<String>, String> {

    private final static String INITIAL = "a1";

    @Test
    public void testWithNullInitial() {
        PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer.with(null, REDUCER, closeables);
    }

    @Test
    public void testAcceptWithoutValues() {
        this.acceptAndCheck("", INITIAL);
    }

    @Test
    public void testAcceptManyValues() {
        this.acceptAndCheck("b2,c3", INITIAL + "b2c3");
    }

    private void acceptAndCheck(final String values,
                                final String result) {
        final PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer<String> reducer = this.createPushableStreamConsumer();
        for (String value : this.commaSeparated(values)) {
            this.accept(reducer, value);
        }
        this.checkResult(reducer, result);
        this.checkResult(reducer, this.commaSeparated(values).stream().reduce(INITIAL, REDUCER));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "reduce " + REDUCER + " \"a1\" closeables: " + this.closeables);
    }

    @Override
    PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer<String> createPushableStreamConsumer(final BiFunction<String, String, String> reducer,
                                                                                                                       final PushableStreamConsumerCloseableCollection closeables) {
        return PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer.with(INITIAL, reducer, closeables);
    }

    @Override
    public Class<PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer<String>> type() {
        return Cast.to(PushableStreamConsumerStreamTerminal4InitialAccumulatorPushableStreamConsumer.class);
    }
}
