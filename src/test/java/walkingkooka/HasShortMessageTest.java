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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public final class HasShortMessageTest implements ClassTesting2<HasShortMessage> {

    private final static String SHORT_MESSAGE = "ShortMessage222";

    @Test
    public void testGetShortMessageOrMessageHasShortMessage() {
        this.getShortMessageOrMessageAndCheck(
            new TestExceptionHasShortMessage(),
            SHORT_MESSAGE
        );
    }

    @Test
    public void testGetShortMessageOrMessage() {
        final String message = "Message 123";

        this.getShortMessageOrMessageAndCheck(
            new RuntimeException(message),
            message
        );
    }

    final static class TestExceptionHasShortMessage extends Exception implements HasShortMessage {

        private static final long serialVersionUID = 1L;

        public TestExceptionHasShortMessage() {
            super();
        }

        @Override
        public String getMessage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getShortMessage() {
            return SHORT_MESSAGE;
        }
    }

    public void getShortMessageOrMessageAndCheck(final Throwable throwable,
                                                 final String expected) {
        this.checkEquals(
            expected,
            HasShortMessage.getShortMessageOrMessage(throwable)
        );
    }

    // class............................................................................................................

    @Override
    public Class<HasShortMessage> type() {
        return HasShortMessage.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
