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

package walkingkooka;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.PublicThrowableTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class InvalidCharacterExceptionTest extends PublicThrowableTestCase<InvalidCharacterException> {

    private final static String TEXT = "abc!123";
    private final static int POSITION = 3;

    @Test
    @Ignore
    public void testAllConstructorsVisibility() throws Throwable {

    }

    @Test
    @Ignore
    public void testCreateOnlyNullMessageFails() throws Throwable {
    }

    @Test
    @Ignore
    public void testCreateOnlyEmptyMessageFails() throws Throwable {
    }

    @Test
    @Ignore
    public void testCreateOnlyMessage() throws Throwable {
    }

    @Test
    @Ignore
    public void testCreateNullMessageAndCauseExceptionFails() throws Throwable {
    }

    @Test
    @Ignore
    public void testCreateEmptyMessageAndNonNullCauseFails() throws Throwable {
    }

    @Test
    @Ignore
    public void testMessageAndNullCauseFails() throws Throwable {
    }

    @Test
    @Ignore
    public void testMessageAndCause() throws Throwable {

    }

    @Test
    @Ignore
    public void testNoArgumentsConstructorItNotPublic() throws Throwable {
    }


    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        new InvalidCharacterException(null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyTextFails() {
        new InvalidCharacterException("", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPositionFails() {
        new InvalidCharacterException(TEXT, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPositionFails2() {
        new InvalidCharacterException(TEXT, TEXT.length());
    }

    @Test
    public void testWith() {
        final InvalidCharacterException cause = this.create();
        check(cause, TEXT, POSITION);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTextAndPositionNullTextFails() {
        this.create().setTextAndPosition(null, POSITION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextAndPositionEmptyTextFails() {
        this.create().setTextAndPosition("", POSITION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextAndPositionInvalidPositionFails() {
        this.create().setTextAndPosition(TEXT, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextAndPositionInvalidPositionFails2() {
        this.create().setTextAndPosition(TEXT, TEXT.length() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextAndPositionInvalidPositionFails3() {
        final String text = "abc";
        this.create().setTextAndPosition(text, text.length() + 1);
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
        assertEquals("text", text, exception.text());
        assertEquals("position", position, exception.position());
    }

    private void checkCause(final InvalidCharacterException exception, final InvalidCharacterException cause) {
        assertEquals("cause", cause, exception.getCause());
    }

    @Override
    protected Class<InvalidCharacterException> type() {
        return InvalidCharacterException.class;
    }
}
