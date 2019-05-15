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

import org.junit.jupiter.api.Test;
import walkingkooka.build.tostring.ToStringBuilder;

public abstract class PushableStreamStreamIntermediatePushableStreamConsumerTestCase<P extends PushableStreamStreamIntermediatePushableStreamConsumer<String>>
extends PushableStreamStreamPushableStreamConsumerTestCase2<P> {

    PushableStreamStreamIntermediatePushableStreamConsumerTestCase() {
        super();
    }

    @Test
    public final void testDifferentNext() {
        this.checkNotEquals(DIFFERENT);
    }

    @Override
    public final P createPushableStreamConsumer() {
        return this.createPushableStreamConsumer(NEXT);
    }

    final static String NEXT_TOSTRING = "Next123";

    static PushableStreamStreamPushableStreamConsumer<String> NEXT = new PushableStreamStreamPushableStreamConsumer<String>() {

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
        boolean canBeEqual(final Object other) {
            return other == this;
        }

        @Override
        boolean equals0(final PushableStreamStreamPushableStreamConsumer<?> other) {
            return true;
        }

        @Override
        void buildToString0(final ToStringBuilder builder) {
            builder.value(NEXT_TOSTRING);
        }
    };

    private static PushableStreamStreamPushableStreamConsumer<String> DIFFERENT = new PushableStreamStreamPushableStreamConsumer<String>() {

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
        boolean canBeEqual(final Object other) {
            return other == this;
        }

        @Override
        boolean equals0(final PushableStreamStreamPushableStreamConsumer<?> other) {
            return true;
        }

        @Override
        void buildToString0(final ToStringBuilder builder) {
            builder.value("different");
        }
    };

    abstract P createPushableStreamConsumer(final PushableStreamConsumer<String> next);

    @Override
    public final String typeNameSuffix() {
        return PushableStreamStreamIntermediatePushableStreamConsumer.class.getSimpleName();
    }
}
