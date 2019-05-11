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

import java.util.function.Consumer;

/**
 * The PEEK {@link PushableStreamStream}
 */
final class PeekPushableStreamStreamIntermediate extends NonLimitOrSkipPushableStreamStreamIntermediate {

    static PeekPushableStreamStreamIntermediate with(final Consumer<?> action) {
        return new PeekPushableStreamStreamIntermediate(action);
    }

    private PeekPushableStreamStreamIntermediate(final Consumer<?> action) {
        super();
        this.action = action;
    }

    @Override
    PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(final PushableStreamConsumer<?> next) {
        return PushableStreamStreamIntermediatePushableStreamConsumer.peek(Cast.to(this.action), next);
    }

    private final Consumer<?> action;

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.action.hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PeekPushableStreamStreamIntermediate;
    }

    @Override
    boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final PeekPushableStreamStreamIntermediate other) {
        return this.action.equals(other.action);
    }

    @Override
    public String toString() {
        return "peek " + this.action;
    }
}
