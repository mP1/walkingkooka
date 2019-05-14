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

import java.util.function.Consumer;

/**
 * The FOREACH TERMINAL {@link PushableStreamConsumer}.
 */
final class ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<T> extends PushableStreamStreamTerminalPushableStreamConsumer<T, Void> {

    static <T> ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<T> with(final Consumer<? super T> action,
                                                                                        final CloseableCollection closeables) {
        return new ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<T>(action, closeables);
    }

    /**
     * Package private to limit sub classing.
     */
    private ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer(final Consumer<? super T> action,
                                                                             final CloseableCollection closeables) {
        super(closeables);
        this.action = action;
    }

    /**
     * Count wants to count everything.
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void accept(final T value) {
        this.action.accept(value);
    }

    private final Consumer<? super T> action;

    /**
     * For each returns nothing.
     */
    @Override
    Void result() {
        return null;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer;
    }

    @Override
    boolean equals2(final PushableStreamStreamTerminalPushableStreamConsumer<?, ?> other) {
        return this.equals3(Cast.to(other));
    }

    private boolean equals3(final ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<?> other) {
        return this.action.equals(other.action);
    }

    @Override
    final void buildToString1(final ToStringBuilder builder) {
        builder.label("forEachOrdered");
        builder.value(this.action);
    }
}
