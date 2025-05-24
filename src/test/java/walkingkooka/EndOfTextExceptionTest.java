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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EndOfTextExceptionTest implements ThrowableTesting2<EndOfTextException>,
    HashCodeEqualsDefinedTesting2<EndOfTextException> {

    private final static String MESSAGE = "message123";

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullMessageFails() {
        assertThrows(
            NullPointerException.class,
            () -> new EndOfTextException(null)
        );
    }

    @Test
    public void testWith() {
        final EndOfTextException cause = this.create();
        check(
            cause,
            MESSAGE
        );
    }

    @Test
    public void testWithCause() {
        final Throwable cause = new Exception();
        final EndOfTextException thrown = new EndOfTextException(MESSAGE, cause);
        check(
            thrown,
            MESSAGE
        );
        this.checkCause(thrown, cause);
    }

    @Test
    public void testGetMessage() {
        checkMessage(
            this.create(),
            "message123"
        );
    }

    private EndOfTextException create() {
        return new EndOfTextException(MESSAGE);
    }

    @Test
    public void testGetMessageAfterSetNonEmptyLabel() {
        this.checkMessage(
            this.create()
                .setLabel(Optional.of("Hello")),
            MESSAGE
        );
    }

    private void check(final EndOfTextException exception,
                       final String message) {
        this.checkEquals(
            message,
            exception.getMessage(),
            "message"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentMessage() {
        this.checkNotEquals(
            new EndOfTextException("different")
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<EndOfTextException> type() {
        return EndOfTextException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // equality.........................................................................................................

    @Override
    public EndOfTextException createObject() {
        return new EndOfTextException(MESSAGE);
    }
}
