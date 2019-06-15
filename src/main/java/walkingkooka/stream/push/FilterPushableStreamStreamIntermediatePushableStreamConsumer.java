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

import java.util.function.Predicate;

final class FilterPushableStreamStreamIntermediatePushableStreamConsumer<T> extends NonLimitOrSkipPushableStreamStreamIntermediatePushableStreamConsumer<T> {

    static <T> FilterPushableStreamStreamIntermediatePushableStreamConsumer<T> with(final Predicate<T> predicate,
                                                                                    final PushableStreamConsumer<T> next) {
        return new FilterPushableStreamStreamIntermediatePushableStreamConsumer<>(predicate, next);
    }

    private FilterPushableStreamStreamIntermediatePushableStreamConsumer(final Predicate<T> predicate,
                                                                         final PushableStreamConsumer<T> next) {
        super(next);
        this.predicate = predicate;
    }

    @Override
    public boolean isFinished() {
        return this.next.isFinished();
    }

    @Override
    public void accept(final T value) {
        if (this.predicate.test(value)) {
            this.next.accept(value);
        }
    }

    private final Predicate<T> predicate;

    // Object............................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof FilterPushableStreamStreamIntermediatePushableStreamConsumer;
    }

    @Override
    boolean equals2(final PushableStreamStreamIntermediatePushableStreamConsumer<?> other) {
        return this.equals3(Cast.to(other));
    }

    private boolean equals3(final FilterPushableStreamStreamIntermediatePushableStreamConsumer<?> other) {
        return this.predicate.equals(other.predicate);
    }

    @Override
    final void buildToString1(final ToStringBuilder builder) {
        builder.label("filter");
        builder.value(this.predicate);
    }
}
