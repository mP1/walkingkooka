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

import walkingkooka.test.HashCodeEqualsDefined;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Base class for all intermediate steps while building a {@link Stream} to terminal operation invocation.
 */
abstract class PushableStreamStreamIntermediate implements HashCodeEqualsDefined {

    /**
     * {@see FilterPushableStreamStreamIntermediate}
     */
    static FilterPushableStreamStreamIntermediate filter(final Predicate<?> predicate) {
        return FilterPushableStreamStreamIntermediate.with(predicate);
    }

    /**
     * {@see FlatMapPushableStreamStreamIntermediate}
     */
    static FlatMapPushableStreamStreamIntermediate flatMap(final Function<?, Stream<?>> function) {
        return FlatMapPushableStreamStreamIntermediate.with(function);
    }

    /**
     * {@see LimitPushableStreamStreamIntermediate}
     */
    static LimitPushableStreamStreamIntermediate limit(final long limit) {
        return LimitPushableStreamStreamIntermediate.with(limit);
    }

    /**
     * {@see MapPushableStreamStreamIntermediate}
     */
    static MapPushableStreamStreamIntermediate map(final Function<?, ?> function) {
        return MapPushableStreamStreamIntermediate.with(function);
    }

    /**
     * {@see PeekPushableStreamStreamIntermediate}
     */
    static PeekPushableStreamStreamIntermediate peek(final Consumer<?> consumer) {
        return PeekPushableStreamStreamIntermediate.with(consumer);
    }

    /**
     * {@see SkipPushableStreamStreamIntermediate}
     */
    static SkipPushableStreamStreamIntermediate skip(final long skip) {
        return SkipPushableStreamStreamIntermediate.with(skip);
    }

    PushableStreamStreamIntermediate() {
        super();
    }

    final static long NOT_LIMIT_OR_SKIP = -1;

    /**
     * Only {@link LimitPushableStreamStreamIntermediate} will return a non zero value.
     */
    abstract long limit();

    /**
     * Only {@link SkipPushableStreamStreamIntermediate} will return a non zero value.
     */
    abstract long skip();

    /**
     * Creates the {@link PushableStreamConsumer} with the given next {@link PushableStreamConsumer}.
     */
    abstract PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(PushableStreamConsumer<?> next);

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(other);
    }

    abstract boolean canBeEqual(final Object other);

    abstract boolean equals0(final Object other);
}
