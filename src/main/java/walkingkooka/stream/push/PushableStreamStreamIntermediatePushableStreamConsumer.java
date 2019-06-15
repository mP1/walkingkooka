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

/**
 * Base class for all INTERMEDIATE {@link PushableStreamConsumer} returned by {@link PushableStreamStreamIntermediate#createWithNext(PushableStreamConsumer)}.
 */
abstract class PushableStreamStreamIntermediatePushableStreamConsumer<T> extends PushableStreamStreamPushableStreamConsumer<T> {

    /**
     * Package private to limit sub classing.
     */
    PushableStreamStreamIntermediatePushableStreamConsumer(final PushableStreamConsumer<T> next) {
        super();
        this.next = next;
    }

    /**
     * Intermediates dont need to close they simply eventually as the terminal to close.
     */
    @Override
    public final void close() {
        this.next.close();
    }

    /**
     * Intermediates always have a next, which may be other intermediate or the terminal {@link PushableStreamConsumer}.
     */
    final PushableStreamConsumer<T> next;

    // Object..........................................................................................................

    @Override
    final boolean equals0(final PushableStreamStreamPushableStreamConsumer<?> other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final PushableStreamStreamIntermediatePushableStreamConsumer<?> other) {
        return this.next.equals(other.next) &&
                this.equals2(Cast.to(other));
    }

    abstract boolean equals2(final PushableStreamStreamIntermediatePushableStreamConsumer<?> other);

    @Override
    final void buildToString0(final ToStringBuilder builder) {
        builder.separator(" ");
        this.buildToString1(builder);
        builder.value(this.next);
    }

    abstract void buildToString1(final ToStringBuilder builder);
}
