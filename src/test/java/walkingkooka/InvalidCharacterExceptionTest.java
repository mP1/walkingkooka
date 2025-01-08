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
import walkingkooka.text.HasTextTesting;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class InvalidCharacterExceptionTest implements ThrowableTesting2<InvalidCharacterException>,
    HashCodeEqualsDefinedTesting2<InvalidCharacterException>,
    HasTextTesting {

    private final static String TEXT = "abc!123";
    private final static int POSITION = 3;


    // ICE includes 2 private ctors.
    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullTextFails() {
        assertThrows(NullPointerException.class, () -> new InvalidCharacterException(null, 3));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> new InvalidCharacterException("", 3));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidPositionFails() {
        assertThrows(IllegalArgumentException.class, () -> new InvalidCharacterException(TEXT, -1));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidPositionFails2() {
        assertThrows(IllegalArgumentException.class, () -> new InvalidCharacterException(TEXT, TEXT.length()));
    }

    @Test
    public void testWith() {
        final InvalidCharacterException cause = this.create();
        check(
            cause,
            TEXT,
            POSITION,
            "" // appendToMessage
        );
    }

    @Test
    public void testWithCause() {
        final Exception cause = new Exception();
        final InvalidCharacterException thrown = this.create(cause);
        check(
            thrown,
            TEXT,
            POSITION,
            "" // appendToMessage
        );
        checkCause(thrown, cause);
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionNullTextFails() {
        assertThrows(NullPointerException.class, () -> this.create().setTextAndPosition(null, POSITION));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> this.create().setTextAndPosition("", POSITION));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails() {
        assertThrows(IllegalArgumentException.class, () -> this.create().setTextAndPosition(TEXT, -1));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails2() {
        assertThrows(IllegalArgumentException.class, () -> this.create().setTextAndPosition(TEXT, TEXT.length() + 1));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails3() {
        final String text = "abc";
        assertThrows(IllegalArgumentException.class, () -> this.create().setTextAndPosition(text, text.length() + 1));
    }

    @Test
    public void testSetTextAndPositionSame() {
        final InvalidCharacterException cause = this.create();
        assertSame(cause, cause.setTextAndPosition(TEXT, POSITION));
    }

    @Test
    public void testSetTextAndPositionDifferentText() {
        final InvalidCharacterException cause = this.create();
        final String text = "different";
        final InvalidCharacterException different = cause.setTextAndPosition(text, POSITION);
        assertNotSame(cause, different);

        this.check(
            different,
            text,
            POSITION,
            "" // appendToMessage
        );

        this.check(cause);
    }

    @Test
    public void testSetTextAndPositionDifferentTextWithCause() {
        final Throwable cause = new Exception();

        final InvalidCharacterException thrown = this.create(cause);
        final String text = "different";
        final InvalidCharacterException different = thrown.setTextAndPosition(text, POSITION);
        assertNotSame(cause, different);

        this.check(
            different,
            text,
            POSITION,
            "" // appendToMessage
        );

        this.checkCause(different, cause);

        this.check(thrown);
    }

    @Test
    public void testSetTextAndPositionDifferentPosition() {
        final InvalidCharacterException cause = this.create();
        final int position = 2;
        final InvalidCharacterException different = cause.setTextAndPosition(TEXT, position);
        assertNotSame(cause, different);

        this.check(
            different,
            TEXT,
            position,
            "" // appendToMessage
        );

        this.check(cause);
    }

    @Test
    public void testSetTextAndPositionDifferentPositionAndText() {
        final InvalidCharacterException cause = this.create();
        final String text = "different";
        final int position = 1;
        final InvalidCharacterException different = cause.setTextAndPosition(text, position);
        assertNotSame(cause, different);

        this.check(
            different,
            text,
            position,
            "" // appendToMessage
        );

        this.check(cause);
    }

    @Test
    public void testAppendToMessageNullFails() {
        final InvalidCharacterException thrown = this.create();

        assertThrows(
            NullPointerException.class,
            () -> thrown.appendToMessage(null)
        );
    }

    @Test
    public void testAppendToMessageSame() {
        final InvalidCharacterException thrown = this.create();
        assertSame(
            thrown,
            thrown.appendToMessage("")
        );
    }

    @Test
    public void testAppendToMessageDifferent() {
        final InvalidCharacterException thrown = this.create();

        final String appendToMessage = "AppendToMessage123";
        final InvalidCharacterException different = thrown.appendToMessage(appendToMessage);

        assertNotSame(thrown, different);

        this.check(
            different,
            thrown.text(),
            thrown.position(),
            appendToMessage
        );

        this.checkMessage(
            different,
            thrown.getMessage() + appendToMessage
        );
    }


    @Test
    public void testGetMessage() {
        checkMessage(this.create(), "Invalid character \'!\' at 3 in \"abc!123\"");
    }

    @Test
    public void testGetShortMessage() {
        checkShortMessage(
            this.create(),
            "Invalid character \'!\' at 3"
        );
    }

    @Test
    public void testGetShortMessageWithAppendMessage() {
        checkShortMessage(
            this.create()
                .appendToMessage("AppendToMessage333"),
            "Invalid character \'!\' at 3AppendToMessage333"
        );
    }

    @Test
    public void testGetMessageEscapedCharacter() {
        checkMessage(new InvalidCharacterException("abc\"123", 3),
            "Invalid character \'\\\"\' at 3 in \"abc\"123\"");
    }

    @Test
    public void testGetMessageAfterSetTextAndPosition() {
        checkMessage(this.create().setTextAndPosition("@@" + TEXT, 2 + POSITION),
            "Invalid character \'!\' at 5 in \"@@abc!123\"");
    }

    @Test
    public void testGetShortMessageAfterSetTextAndPosition() {
        checkShortMessage(
            this.create()
                .setTextAndPosition(
                    "ABCDEFG",
                    5
                ),
            "Invalid character 'F' at 5"
        );
    }

    private InvalidCharacterException create() {
        return new InvalidCharacterException(TEXT, POSITION);
    }

    private InvalidCharacterException create(final Throwable cause) {
        return new InvalidCharacterException(TEXT, POSITION, cause);
    }

    private void check(final InvalidCharacterException exception) {
        this.check(
            exception,
            TEXT, POSITION,
            "" // appendToMessage
        );
    }

    private void check(final InvalidCharacterException exception,
                       final String text,
                       final int position,
                       final String appendToMessage) {
        this.checkEquals(text, exception.text(), "text");
        this.checkEquals(position, exception.position(), "position");
        this.checkEquals(appendToMessage, exception.appendToMessage, "appendToMessage");
    }

    private void checkShortMessage(final InvalidCharacterException exception,
                                   final String expected) {
        this.checkEquals(
            expected,
            exception.getShortMessage(),
            () -> "getShortMessage"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(new InvalidCharacterException(TEXT + " different", POSITION));
    }

    @Test
    public void testEqualsDifferentPosition() {
        this.checkNotEquals(new InvalidCharacterException(TEXT, POSITION + 1));
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<InvalidCharacterException> type() {
        return InvalidCharacterException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // equality.........................................................................................................

    @Override
    public InvalidCharacterException createObject() {
        return new InvalidCharacterException(TEXT, POSITION);
    }
}
