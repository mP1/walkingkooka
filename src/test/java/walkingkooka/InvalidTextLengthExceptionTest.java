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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class InvalidTextLengthExceptionTest implements ThrowableTesting2<InvalidTextLengthException>,
    HashCodeEqualsDefinedTesting2<InvalidTextLengthException> {

    private final static String LABEL = "label123";
    private final static String TEXT = "abc!456";
    private final static int MIN = 2;
    private final static int MAX = 5;

    // throwIfFail......................................................................................................

    @Test
    public void testThrowIfFailWhenTextLengthMin() {
        final String text = "Hello";

        assertSame(
            text,
            InvalidTextLengthException.throwIfFail(
                "Label123",
                text,
                5,
                20
            )
        );
    }

    @Test
    public void testThrowIfFailWhenTextLengthMax() {
        final String text = "Hello";

        assertSame(
            text,
            InvalidTextLengthException.throwIfFail(
                "Label123",
                text,
                0,
                5
            )
        );
    }

    @Test
    public void testThrowIfFailWhenTextLengthBetween() {
        final String text = "Hello";

        assertSame(
            text,
            InvalidTextLengthException.throwIfFail(
                "Label123",
                text,
                1,
                10
            )
        );
    }

    @Test
    public void testThrowIfFailWhenLessThanMinAndEmpty() {
        final EmptyTextException thrown = assertThrows(
            EmptyTextException.class,
            () -> InvalidTextLengthException.throwIfFail(
                "Label123",
                "",
                10,
                20
            )
        );

        this.checkMessage(
            thrown,
            "Empty \"Label123\""
        );
    }

    @Test
    public void testThrowIfFailWhenLessThanMin() {
        final InvalidTextLengthException thrown = assertThrows(
            InvalidTextLengthException.class,
            () -> InvalidTextLengthException.throwIfFail(
                "Label123",
                "Hello",
                10,
                20
            )
        );

        this.checkMessage(
            thrown,
            "Length 5 of \"Label123\" not between 10..20 = \"Hello\""
        );
    }

    @Test
    public void testThrowIfFailWhenGreaterThanMax() {
        final InvalidTextLengthException thrown = assertThrows(
            InvalidTextLengthException.class,
            () -> InvalidTextLengthException.throwIfFail(
                "Label123",
                "Hello",
                1,
                2
            )
        );

        this.checkMessage(
            thrown,
            "Length 5 of \"Label123\" not between 1..2 = \"Hello\""
        );
    }

    // with.............................................................................................................

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullLabelFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidTextLengthException(null, TEXT, MIN, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidTextLengthException("", TEXT, MIN, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithWhitespaceLabelFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidTextLengthException(" ", TEXT, MIN, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidTextLengthException(LABEL, null, MIN, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidMinFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidTextLengthException(LABEL, TEXT, -1, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidMinGreaterThanTextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidTextLengthException(LABEL, TEXT, 6, MAX)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidMaxFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidTextLengthException(LABEL, TEXT, MIN, MIN - 1)
        );
    }

    @Test
    public void testWith() {
        final InvalidTextLengthException cause = this.create();
        check(
            cause,
            LABEL,
            TEXT,
            MIN,
            MAX
        );
    }

    @Test
    public void testWithEmptyText() {
        final InvalidTextLengthException cause = new InvalidTextLengthException(LABEL, "", 0, MAX);
        check(
            cause,
            LABEL,
            "",
            0,
            MAX
        );
    }

    @Test
    public void testWith2() {
        final InvalidTextLengthException cause = new InvalidTextLengthException(LABEL, "abc", 0, 4);
        check(
            cause,
            LABEL,
            "abc",
            0,
            4
        );
    }

    @Test
    public void testWithCause() {
        final Throwable cause = new Exception();
        final InvalidTextLengthException thrown = new InvalidTextLengthException(LABEL, "abc", 0, 4, cause);
        check(
            thrown,
            LABEL,
            "abc",
            0,
            4
        );
        this.checkCause(thrown, cause);
    }

    @Test
    public void testWithMinEqualsMax() {
        final InvalidTextLengthException cause = new InvalidTextLengthException(LABEL, "abcd", 4, 4);
        check(
            cause,
            LABEL,
            "abcd",
            4,
            4
        );
    }

    @Test
    public void testGetMessage() {
        this.checkMessage(
            this.create(),
            "Length 7 of \"label123\" not between 2..5 = \"abc!456\""
        );
    }

    @Test
    public void testGetMessageEscapedCharacter() {
        this.checkMessage(
            new InvalidTextLengthException(
                LABEL,
                "abc'xy",
                MIN,
                MAX
            ),
            "Length 6 of \"label123\" not between 2..5 = \"abc\'xy\""
        );
    }

    @Test
    public void testGetMessageAfterSetNonEmptyLabel() {
        this.checkMessage(
            new InvalidTextLengthException(
                LABEL,
                "abc'xy",
                MIN,
                MAX
            ).setLabel(
                Optional.of("Hello")
            ),
            "Length 6 of \"Hello\" not between 2..5 = \"abc\'xy\""
        );
    }

    private InvalidTextLengthException create() {
        return new InvalidTextLengthException(LABEL, TEXT, MIN, MAX);
    }

    private void check(final InvalidTextLengthException exception,
                       final String label,
                       final String text,
                       final int min,
                       final int max) {
        this.checkEquals(
            Optional.of(label),
            exception.label(),
            "label"
        );
        this.checkEquals(
            text,
            exception.text(),
            "text"
        );
        this.checkEquals(
            min,
            exception.min(),
            () -> "min: " + min
        );
        this.checkEquals(
            max,
            exception.max(),
            () -> "max: " + max
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
            new InvalidTextLengthException(
                "differentLabel",
                TEXT,
                MIN,
                MAX
            )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
            new InvalidTextLengthException(
                LABEL,
                "differentText",
                MIN,
                MAX
            )
        );
    }

    @Test
    public void testEqualsDifferentMin() {
        this.checkNotEquals(
            new InvalidTextLengthException(
                LABEL,
                TEXT,
                MIN + 1,
                MAX
            )
        );
    }

    @Test
    public void testEqualsDifferentMax() {
        this.checkNotEquals(
            new InvalidTextLengthException(
                LABEL,
                TEXT,
                MIN,
                Integer.MAX_VALUE
            )
        );
    }

    @Override
    public InvalidTextLengthException createObject() {
        return new InvalidTextLengthException(LABEL, TEXT, MIN, MAX);
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<InvalidTextLengthException> type() {
        return InvalidTextLengthException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
