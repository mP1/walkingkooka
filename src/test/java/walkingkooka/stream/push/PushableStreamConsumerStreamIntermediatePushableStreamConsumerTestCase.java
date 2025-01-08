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

import walkingkooka.ToStringBuilder;

public abstract class PushableStreamConsumerStreamIntermediatePushableStreamConsumerTestCase<P extends PushableStreamConsumerStreamIntermediatePushableStreamConsumer<String>>
    extends PushableStreamConsumerStreamPushableStreamConsumerTestCase2<P> {

    PushableStreamConsumerStreamIntermediatePushableStreamConsumerTestCase() {
        super();
    }

    @Override
    public final P createPushableStreamConsumer() {
        return this.createPushableStreamConsumer(NEXT);
    }

    final static String NEXT_TOSTRING = "Next123";

    static final PushableStreamConsumerStreamPushableStreamConsumer<String> NEXT = new PushableStreamConsumerStreamPushableStreamConsumer<>() {

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public void accept(final String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {

        }

        @Override
        void buildToString0(final ToStringBuilder builder) {
            builder.value(NEXT_TOSTRING);
        }
    };

    abstract P createPushableStreamConsumer(final PushableStreamConsumer<String> next);

    @Override
    public final String typeNamePrefix() {
        return PushableStreamConsumerStreamIntermediate.class.getSimpleName();
    }
}
