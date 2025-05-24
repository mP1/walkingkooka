
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

public final class InvalidTextExceptionTest implements ThrowableTesting2<InvalidTextException>,
    HashCodeEqualsDefinedTesting2<InvalidTextException> {

    private final static Optional<String> LABEL = Optional.of("label123");

    private final static String MESSAGE = "message123";

    @Test
    public void testWithNullMessageFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidTextException(
                null, // message
                null // cause
            )
        );
    }

    @Test
    public void testWith() {
        final InvalidTextException thrown = new InvalidTextException(
            MESSAGE
        );

        this.checkMessage(
            thrown,
            MESSAGE
        );
    }

    @Test
    public void testWithCause() {
        final Throwable cause = new Exception();
        final InvalidTextException thrown = new InvalidTextException(
            MESSAGE,
            cause
        );
        this.labelCheck(
            thrown,
            InvalidTextException.NO_LABEL
        );
        this.checkMessage(
            thrown,
            MESSAGE
        );
        this.checkCause(
            thrown,
            cause
        );
    }

    // setLabel.........................................................................................................

    @Test
    public void testSetLabelWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidTextException(MESSAGE)
                .setLabel(null)
        );
    }

    @Test
    public void testSetLabel() {
        final InvalidTextException thrown = new InvalidTextException(MESSAGE)
            .setLabel(LABEL);
        this.labelCheck(
            thrown,
            LABEL
        );
        this.checkMessage(
            thrown,
            MESSAGE
        );
    }

    private void labelCheck(final InvalidTextException exception,
                            final Optional<String> label) {
        this.checkEquals(
            label,
            exception.label(),
            "label"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentMessage() {
        this.checkNotEquals(
            new InvalidTextException("differentMessage")
        );
    }

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
            new InvalidTextException(MESSAGE)
                .setLabel(LABEL),
            new InvalidTextException(MESSAGE)
                .setLabel(
                    Optional.of("differentLabel")
                )
        );
    }

    @Override
    public InvalidTextException createObject() {
        return new InvalidTextException(MESSAGE);
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<InvalidTextException> type() {
        return InvalidTextException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
