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

/**
 * The LIMIT {@link PushableStreamStream}
 */
final class LimitPushableStreamStreamIntermediate extends LimitOrSkipPushableStreamStreamIntermediate {

    static LimitPushableStreamStreamIntermediate with(final long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit " + limit + " < 0");
        }
        return new LimitPushableStreamStreamIntermediate(limit);
    }

    private LimitPushableStreamStreamIntermediate(final long limit) {
        super(limit);
    }

    @Override
    PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(final PushableStreamConsumer<?> next) {
        return PushableStreamStreamIntermediatePushableStreamConsumer.limit(this.value, next);
    }

    @Override
    long limit() {
        return this.value;
    }

    @Override
    long skip() {
        return NOT_LIMIT_OR_SKIP;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LimitPushableStreamStreamIntermediate;
    }

    @Override
    String label() {
        return "limit";
    }
}
