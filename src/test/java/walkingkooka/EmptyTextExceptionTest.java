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

public final class EmptyTextExceptionTest implements ThrowableTesting2<EmptyTextException>,
    HashCodeEqualsDefinedTesting2<EmptyTextException> {

    private final static String LABEL = "label123";

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullLabelFails() {
        assertThrows(
            NullPointerException.class,
            () -> new EmptyTextException(null)
        );
    }

    @Test
    public void testWith() {
        final EmptyTextException cause = this.create();
        this.labelCheck(
            cause,
            LABEL
        );
    }

    @Test
    public void testWithCause() {
        final Throwable cause = new Exception();
        final EmptyTextException thrown = new EmptyTextException(LABEL, cause);
        this.labelCheck(
            thrown,
            LABEL
        );
        this.checkCause(thrown, cause);
    }

    @Test
    public void testGetMessage() {
        checkMessage(
            this.create(),
            "Empty \"label123\""
        );
    }

    // setLabel.........................................................................................................

    @Test
    public void testSetLabelWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> new EmptyTextException(LABEL)
                .setLabel(null)
        );
    }

    @Test
    public void testSetLabelWithEmptyFails() {
        assertThrows(
            NullPointerException.class,
            () -> new EmptyTextException(LABEL)
                .setLabel(Optional.empty())
        );
    }

    @Test
    public void testSetLabelWithEmptyStringFails() {
        assertThrows(
            EmptyTextException.class,
            () -> new EmptyTextException(LABEL)
                .setLabel(Optional.of(""))
        );
    }

    private EmptyTextException create() {
        return new EmptyTextException(LABEL);
    }

    private void labelCheck(final EmptyTextException exception,
                            final String label) {
        this.checkEquals(
            Optional.of(label),
            exception.label(),
            "label"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
            new EmptyTextException("different")
        );
    }

    @Override
    public EmptyTextException createObject() {
        return new EmptyTextException(LABEL);
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<EmptyTextException> type() {
        return EmptyTextException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
