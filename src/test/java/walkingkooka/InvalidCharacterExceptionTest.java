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
import walkingkooka.test.ThrowableTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class InvalidCharacterExceptionTest implements ThrowableTesting<InvalidCharacterException> {

    private final static String TEXT = "abc!123";
    private final static int POSITION = 3;

    @Test
    public void testWithNullTextFails() {
        assertThrows(NullPointerException.class, () -> {
            new InvalidCharacterException(null, 3);
        });
    }

    @Test
    public void testWithEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InvalidCharacterException("", 3);
        });
    }

    @Test
    public void testWithInvalidPositionFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InvalidCharacterException(TEXT, -1);
        });
    }

    @Test
    public void testWithInvalidPositionFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InvalidCharacterException(TEXT, TEXT.length());
        });
    }

    @Test
    public void testWith() {
        final InvalidCharacterException cause = this.create();
        check(cause, TEXT, POSITION);
    }

    @Test
    public void testSetTextAndPositionNullTextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().setTextAndPosition(null, POSITION);
        });
    }

    @Test
    public void testSetTextAndPositionEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().setTextAndPosition("", POSITION);
        });
    }

    @Test
    public void testSetTextAndPositionInvalidPositionFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().setTextAndPosition(TEXT, -1);
        });
    }

    @Test
    public void testSetTextAndPositionInvalidPositionFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().setTextAndPosition(TEXT, TEXT.length() + 1);
        });
    }

    @Test
    public void testSetTextAndPositionInvalidPositionFails3() {
        final String text = "abc";
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().setTextAndPosition(text, text.length() + 1);
        });
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
        this.check(different, text, POSITION);
        this.check(cause);
    }

    @Test
    public void testSetTextAndPositionDifferentPosition() {
        final InvalidCharacterException cause = this.create();
        final int position = 2;
        final InvalidCharacterException different = cause.setTextAndPosition(TEXT, position);
        assertNotSame(cause, different);
        this.check(different, TEXT, position);
        this.check(cause);
    }

    @Test
    public void testSetTextAndPositionDifferentPositionAndText() {
        final InvalidCharacterException cause = this.create();
        final String text = "different";
        final int position = 1;
        final InvalidCharacterException different = cause.setTextAndPosition(text, position);
        assertNotSame(cause, different);
        this.check(different, text, position);
        this.check(cause);
    }

    @Test
    public void testGetMessage() {
        assertEquals("Invalid character \'!\' at 3 in \"abc!123\"",
                this.create().getMessage());
    }

    @Test
    public void testGetMessageEscapedCharacter() {
        assertEquals("Invalid character \'\\\"\' at 3 in \"abc\"123\"",
                new InvalidCharacterException("abc\"123", 3)
                        .getMessage());
    }

    @Test
    public void testGetMessageAfterSetTextAndPosition() {
        assertEquals("Invalid character \'!\' at 5 in \"@@abc!123\"",
                this.create().setTextAndPosition("@@" + TEXT, 2 + POSITION)
                        .getMessage());
    }

    private InvalidCharacterException create() {
        return new InvalidCharacterException(TEXT, POSITION);
    }

    private void check(final InvalidCharacterException exception) {
        this.check(exception, TEXT, POSITION);
    }

    private void check(final InvalidCharacterException exception, final String text, final int position) {
        assertEquals(text, exception.text(), "text");
        assertEquals(position, exception.position(), "position");
    }

    @Override
    public Class<InvalidCharacterException> type() {
        return InvalidCharacterException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
