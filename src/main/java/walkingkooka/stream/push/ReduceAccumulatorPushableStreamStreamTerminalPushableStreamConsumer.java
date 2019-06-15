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

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The REDUCE terminator {@link PushableStreamConsumer}.
 */
final class ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<T> extends ReducePushableStreamStreamTerminalPushableStreamConsumer<T, Optional<T>> {

    /**
     * Creates a new {@link ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer} without a combiner.
     */
    static <T> ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<T> with(final BiFunction<T, ? super T, T> reducer,
                                                                                           final CloseableCollection closeables) {
        checkReducer(reducer);

        return new ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<>(reducer, closeables);
    }

    /**
     * Private use factory
     */
    private ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer(final BiFunction<T, ? super T, T> reducer,
                                                                                final CloseableCollection closeables) {
        super(null, reducer, closeables);
        this.empty = true;
    }


    @Override
    public void accept(final T value) {
        this.value = this.empty ?
                value :
                this.reducer.apply(this.value, value);
        this.empty = false;
    }

    private boolean empty;

    /**
     * Returns the REDUCE result if any is present.
     */
    @Override
    final Optional<T> result() {
        return Optional.ofNullable(this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer;
    }

    @Override
    boolean isValuePresent() {
        return !this.empty;
    }
}
