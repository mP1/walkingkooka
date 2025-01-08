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

import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;

public interface PushableStreamConsumerTesting<C extends PushableStreamConsumer<T>, T> extends ClassTesting2<C>,
    ToStringTesting<C> {

    /**
     * Factory that creates a {@link PushableStreamConsumer}
     */
    C createPushableStreamConsumer();

    default void checkIsFinished(final PushableStreamConsumer<?> consumer,
                                 final boolean finished) {
        this.checkEquals(finished,
            consumer.isFinished(),
            consumer::toString);
    }

    default void accept(final PushableStreamConsumer<T> consumer,
                        final T value) {
        this.checkIsFinished(consumer, false);
        consumer.accept(value);
    }
}
