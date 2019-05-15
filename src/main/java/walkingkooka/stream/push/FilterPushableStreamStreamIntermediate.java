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
import java.util.function.Predicate;

/**
 * The FILTER intermediate op {@link PushableStreamStream}.
 */
final class FilterPushableStreamStreamIntermediate extends NonLimitOrSkipPushableStreamStreamIntermediate {

    static FilterPushableStreamStreamIntermediate with(final Predicate<?> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new FilterPushableStreamStreamIntermediate(predicate);
    }

    private FilterPushableStreamStreamIntermediate(final Predicate<?> predicate) {
        super();
        this.predicate = predicate;
    }

    @Override
    PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(final PushableStreamConsumer<?> next) {
        return PushableStreamStreamIntermediatePushableStreamConsumer.filter(Cast.to(this.predicate), next);
    }

    private final Predicate<?> predicate;

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.predicate.hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof FilterPushableStreamStreamIntermediate;
    }

    @Override
    boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final FilterPushableStreamStreamIntermediate other) {
        return this.predicate.equals(other.predicate);
    }

    @Override
    public String toString() {
        return "filter " + this.predicate;
    }
}
