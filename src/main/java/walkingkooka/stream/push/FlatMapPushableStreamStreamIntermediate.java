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

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The FLATMAP {@link PushableStreamStream}
 */
final class FlatMapPushableStreamStreamIntermediate extends NonLimitOrSkipPushableStreamStreamIntermediate {

    static FlatMapPushableStreamStreamIntermediate with(final Function<?, Stream<?>> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return new FlatMapPushableStreamStreamIntermediate(mapper);
    }

    private FlatMapPushableStreamStreamIntermediate(final Function<?, Stream<?>> mapper) {
        super();
        this.mapper = mapper;
    }

    @Override
    PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(final PushableStreamConsumer<?> next) {
        return PushableStreamStreamIntermediatePushableStreamConsumer.flatMap(Cast.to(this.mapper), next);
    }

    private final Function<?, Stream<?>> mapper;

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.mapper.hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof FlatMapPushableStreamStreamIntermediate;
    }

    @Override
    boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final FlatMapPushableStreamStreamIntermediate other) {
        return this.mapper.equals(other.mapper);
    }

    @Override
    public String toString() {
        return "flatmap " + this.mapper;
    }
}