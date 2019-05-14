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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;

/**
 * The COUNT TERMINAL {@link PushableStreamConsumer}.
 */
final class CountPushableStreamStreamTerminalPushableStreamConsumer<T> extends PushableStreamStreamTerminalPushableStreamConsumer<T, Long> {

    static <T> CountPushableStreamStreamTerminalPushableStreamConsumer<T> with(final CloseableCollection closeables) {
        return new CountPushableStreamStreamTerminalPushableStreamConsumer<>(closeables);
    }

    /**
     * Package private to limit sub classing.
     */
    private CountPushableStreamStreamTerminalPushableStreamConsumer(final CloseableCollection closeables) {
        super(closeables);
        this.counter = 0;
    }

    /**
     * Count wants to count everything.
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void accept(final T t) {
        this.counter++;
    }

    /**
     * Returns the final count.
     * @return
     */
    @Override
    Long result() {
        return Long.valueOf(this.counter);
    }

    private long counter;

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CountPushableStreamStreamTerminalPushableStreamConsumer;
    }

    @Override
    boolean equals2(final PushableStreamStreamTerminalPushableStreamConsumer<?, ?> other) {
        return this.equals3(Cast.to(other));
    }

    private boolean equals3(final CountPushableStreamStreamTerminalPushableStreamConsumer<?> other) {
        return this.counter == other.counter;
    }

    @Override
    void buildToString1(final ToStringBuilder builder) {
        builder.value("count");
    }
}
