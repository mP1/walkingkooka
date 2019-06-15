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
 * The SKIP {@link PushableStreamStream}
 */
final class SkipPushableStreamStreamIntermediate extends LimitOrSkipPushableStreamStreamIntermediate {

    static SkipPushableStreamStreamIntermediate with(final long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("Skip " + skip + " < 0");
        }
        return new SkipPushableStreamStreamIntermediate(skip);
    }

    private SkipPushableStreamStreamIntermediate(final long skip) {
        super(skip);
    }

    @Override
    PushableStreamStreamIntermediatePushableStreamConsumer<?> createWithNext(final PushableStreamConsumer<?> next) {
        return PushableStreamStreamIntermediatePushableStreamConsumer.skip(this.value, next);
    }

    @Override
    long limit() {
        return NOT_LIMIT_OR_SKIP;
    }

    @Override
    long skip() {
        return this.value;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SkipPushableStreamStreamIntermediate;
    }

    @Override
    String label() {
        return "skip";
    }
}
