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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;

import java.util.function.Consumer;

final class PeekPushableStreamStreamIntermediatePushableStreamConsumer<T> extends NonLimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumer<T> {

    static <T> PeekPushableStreamStreamIntermediatePushableStreamConsumer<T> with(final Consumer<T> action,
                                                                                  final PushableStreamConsumer<T> next) {
        return new PeekPushableStreamStreamIntermediatePushableStreamConsumer<>(action, next);
    }

    private PeekPushableStreamStreamIntermediatePushableStreamConsumer(final Consumer<T> action,
                                                                       final PushableStreamConsumer<T> next) {
        super(next);
        this.action = action;
    }

    @Override
    public boolean isFinished() {
        return this.next.isFinished();
    }

    @Override
    public void accept(final T value) {
        this.action.accept(value);
        this.next.accept(value);
    }

    private final Consumer<T> action;

    // Object............................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PeekPushableStreamStreamIntermediatePushableStreamConsumer;
    }

    @Override
    boolean equals2(final PushableStreamStreamIntermediatePushableStreamConsumer<?> other) {
        return this.equals3(Cast.to(other));
    }

    private boolean equals3(final PeekPushableStreamStreamIntermediatePushableStreamConsumer<?> other) {
        return this.action.equals(other.action);
    }

    @Override
    final void buildToString1(final ToStringBuilder builder) {
        builder.label("peek");
        builder.value(this.action);
    }
}
