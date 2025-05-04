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

import java.util.OptionalInt;

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

    // with.............................................................................................................

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidCharacterException(
                null,
                3
            )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithEmptyTextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidCharacterException(
                "",
                3
            )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidPositionFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidCharacterException(
                TEXT,
                -1
            )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithInvalidPositionFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new InvalidCharacterException(
                TEXT,
                TEXT.length()
            )
        );
    }

    @Test
    public void testWith() {
        final InvalidCharacterException cause = this.create();
        this.check(
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
        this.check(
            thrown,
            TEXT,
            POSITION,
            "" // appendToMessage
        );
        checkCause(thrown, cause);
    }

    // setTextAndPosition...............................................................................................

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.create()
                .setTextAndPosition(
                    null,
                    POSITION
                )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionEmptyTextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.create()
                .setTextAndPosition(
                    "",
                    POSITION
                )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.create()
                .setTextAndPosition(
                    TEXT,
                    -1
                )
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.create()
                .setTextAndPosition(
                    TEXT,
                    TEXT.length() + 1)
        );
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testSetTextAndPositionInvalidPositionFails3() {
        final String text = "abc";
        assertThrows(
            IllegalArgumentException.class,
            () -> this.create()
                .setTextAndPosition(
                    text,
                    text.length() + 1
                )
        );
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
        final InvalidCharacterException different = cause.setTextAndPosition(
            text,
            POSITION
        );
        assertSame(cause, different);

        this.check(
            different,
            text,
            POSITION,
            "" // appendToMessage
        );
    }

    @Test
    public void testSetTextAndPositionDifferentTextWithCause() {
        final Throwable cause = new Exception();

        final InvalidCharacterException thrown = this.create(cause);
        final String text = "different";
        final InvalidCharacterException different = thrown.setTextAndPosition(
            text,
            POSITION
        );

        this.check(
            different,
            text,
            POSITION,
            "" // appendToMessage
        );

        this.checkCause(different, cause);
    }

    @Test
    public void testSetTextAndPositionDifferentPosition() {
        final InvalidCharacterException cause = this.create();
        final int position = 2;
        final InvalidCharacterException different = cause.setTextAndPosition(
            TEXT,
            position
        );
        assertSame(cause, different);

        this.check(
            different,
            TEXT,
            position,
            "" // appendToMessage
        );
    }

    @Test
    public void testSetTextAndPositionDifferentPositionAndText() {
        final InvalidCharacterException cause = this.create();
        final String text = "different";
        final int position = 1;
        final InvalidCharacterException different = cause.setTextAndPosition(
            text,
            position
        );
        assertSame(cause, different);

        this.check(
            different,
            text,
            position,
            "" // appendToMessage
        );
    }

    // setColumnAndLine.................................................................................................

    @Test
    public void testSetColumnAndLineWithSame() {
        final int column = 2;
        final int line = 3;

        final InvalidCharacterException thrown = this.create()
            .setColumnAndLine(
                column,
                line
            );

        this.check(
            thrown,
            TEXT,
            POSITION,
            OptionalInt.of(column),
            OptionalInt.of(line),
            InvalidCharacterException.NO_APPEND_TO_MESSAGE
        );
    }

    @Test
    public void testSetColumnAndLineDifferent() {
        final InvalidCharacterException thrown = this.create();

        final int column = 2;
        final int line = 3;

        final InvalidCharacterException different = thrown.setColumnAndLine(
            column,
            line
        );
        this.check(
            different,
            TEXT,
            POSITION,
            OptionalInt.of(column),
            OptionalInt.of(line),
            InvalidCharacterException.NO_APPEND_TO_MESSAGE
        );
    }

    @Test
    public void testSetColumnAndLineDifferentAndAppendToMessage() {
        final InvalidCharacterException thrown = this.create();

        final int column = 2;
        final int line = 3;
        final String appendToMessage = "appendToMessage123";

        final InvalidCharacterException different = thrown.appendToMessage(appendToMessage)
            .setColumnAndLine(
                column,
                line
            );
        this.check(
            different,
            TEXT,
            POSITION,
            OptionalInt.of(column),
            OptionalInt.of(line),
            appendToMessage
        );
    }

    // clearColumnAndLine...............................................................................................

    @Test
    public void testClearColumnAndLineWhenAbsent() {
        final InvalidCharacterException thrown = this.create()
            .clearColumnAndLine();

        this.check(
            thrown,
            TEXT,
            POSITION,
            InvalidCharacterException.NO_COLUMN,
            InvalidCharacterException.NO_LINE,
            InvalidCharacterException.NO_APPEND_TO_MESSAGE
        );
    }

    @Test
    public void testClearColumnAndLineWhenPresent() {
        final InvalidCharacterException thrown = this.create();

        final int column = 2;
        final int line = 3;
        final String appendToMessage = "appendToMessage123";

        final InvalidCharacterException cleared = thrown.appendToMessage(appendToMessage)
            .setColumnAndLine(
                column,
                line
            ).clearColumnAndLine();
        this.check(
            cleared,
            TEXT,
            POSITION,
            appendToMessage
        );
    }

    // appendToMessage..................................................................................................

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

        this.check(
            different,
            thrown.text(),
            thrown.position(),
            thrown.column(),
            thrown.line(),
            appendToMessage
        );

        this.checkMessage(
            different,
            thrown.getMessage() + ' ' + appendToMessage
        );
    }

    // getMessage.......................................................................................................

    @Test
    public void testGetMessage() {
        checkMessage(
            this.create(),
            "Invalid character \'!\' at 3 in \"abc!123\""
        );
    }

    @Test
    public void testGetMessageWithColumnAndLine() {
        checkMessage(
            this.create()
                .setColumnAndLine(
                    4,
                    5
                ),
            "Invalid character \'!\' at (4,5) in \"abc!123\""
        );
    }

    // getShortMessage..................................................................................................

    @Test
    public void testGetShortMessage() {
        getShortMessageAndCheck(
            this.create(),
            "Invalid character \'!\' at 3"
        );
    }

    @Test
    public void testGetShortMessageWithColumnAndLine() {
        getShortMessageAndCheck(
            this.create()
                .setColumnAndLine(3, 4),
            "Invalid character \'!\' at (3,4)"
        );
    }

    @Test
    public void testGetShortMessageWithAppendMessage() {
        getShortMessageAndCheck(
            this.create()
                .appendToMessage("AppendToMessage333"),
            "Invalid character \'!\' at 3 AppendToMessage333"
        );
    }


    @Test
    public void testGetShortMessageWithAppendMessageAndColumnAndLine() {
        getShortMessageAndCheck(
            this.create()
                .setColumnAndLine(3, 4)
                .appendToMessage("AppendToMessage333"),
            "Invalid character \'!\' at (3,4) AppendToMessage333"
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
        getShortMessageAndCheck(
            this.create()
                .setTextAndPosition(
                    "ABCDEFG",
                    5
                ),
            "Invalid character 'F' at 5"
        );
    }

    private InvalidCharacterException create() {
        return new InvalidCharacterException(
            TEXT,
            POSITION
        );
    }

    private InvalidCharacterException create(final Throwable cause) {
        return new InvalidCharacterException(
            TEXT,
            POSITION,
            cause
        );
    }

    private void check(final InvalidCharacterException exception) {
        this.check(
            exception,
            TEXT, POSITION,
            InvalidCharacterException.NO_COLUMN,
            InvalidCharacterException.NO_LINE,
            InvalidCharacterException.NO_APPEND_TO_MESSAGE
        );
    }

    private void check(final InvalidCharacterException exception,
                       final String text,
                       final int position,
                       final String appendToMessage) {
        this.check(
            exception,
            text,
            position,
            InvalidCharacterException.NO_COLUMN,
            InvalidCharacterException.NO_LINE,
            appendToMessage
        );
    }

    private void check(final InvalidCharacterException exception,
                       final String text,
                       final int position,
                       final OptionalInt column,
                       final OptionalInt line,
                       final String appendToMessage) {
        this.textAndCheck(
            exception,
            text
        );
        this.checkEquals(
            position,
            exception.position(),
            "position"
        );
        this.checkEquals(
            column,
            exception.column(),
            "column"
        );
        this.checkEquals(
            line,
            exception.line(),
            "line"
        );
        this.checkEquals(
            appendToMessage,
            exception.appendToMessage,
            "appendToMessage"
        );
    }

    private void getShortMessageAndCheck(final InvalidCharacterException exception,
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
        this.checkNotEquals(
            new InvalidCharacterException(
                TEXT + " different",
                POSITION
            )
        );
    }

    @Test
    public void testEqualsDifferentPosition() {
        this.checkNotEquals(
            new InvalidCharacterException(
                TEXT,
                POSITION + 1
            )
        );
    }

    @Test
    public void testEqualsDifferentColumnAndRow() {
        this.checkNotEquals(
            new InvalidCharacterException(
                TEXT,
                POSITION + 1
            ).setColumnAndLine(
                1,
                2
            )
        );
    }

    @Test
    public void testEqualsDifferentAppendToMessage() {
        this.checkNotEquals(
            new InvalidCharacterException(
                TEXT,
                POSITION + 1
            ).appendToMessage(
                "DifferentAppendToMessage123"
            )
        );
    }

    @Override
    public InvalidCharacterException createObject() {
        return new InvalidCharacterException(
            TEXT,
            POSITION
        );
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
}
