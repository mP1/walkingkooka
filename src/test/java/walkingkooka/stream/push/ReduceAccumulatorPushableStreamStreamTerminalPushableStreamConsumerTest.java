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

import java.util.Optional;
import java.util.function.BiFunction;

public final class ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumerTest extends ReducePushableStreamStreamTerminalPushableStreamConsumerTestCase<ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<String>, Optional<String>> {

    @Test
    public void testWithoutAccepts() {
        this.acceptAndCheck("", Optional.empty());
    }

    @Test
    public void testAccepts() {
        final String values = "a1b2c3";
        this.acceptAndCheck(values, Optional.of(values));
    }

    private void acceptAndCheck(final String values,
                                final Optional<String> result) {
        final ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<String> reducer = this.createPushableStreamConsumer();
        for (String value : this.commaSeparated(values)) {
            this.accept(reducer, value);
        }
        this.checkResult(reducer, result);
        this.checkResult(reducer, this.commaSeparated(values).stream().reduce(REDUCER));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "reduce " + REDUCER + " closeables: " + this.closeables);
    }

    @Override
    ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<String> createPushableStreamConsumer(final BiFunction<String, String, String> reducer,
                                                                                                             final CloseableCollection closeables) {
        return ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer.with(reducer, closeables);
    }

    @Override
    public Class<ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<String>> type() {
        return Cast.to(ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer.class);
    }
}
