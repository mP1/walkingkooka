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
 */

package walkingkooka.text.cursor;

import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * A {@link TestCase} that includes no actual tests except for those in {@link PackagePrivateClassTestCase} but adds helpers for various
 * {@link TextCursor} operations. Unfortunately JUNIT requires this class to be public but its constructor is package private to limit sub classes.
 */
abstract public class TextCursorPackageTestCase<T> extends PackagePrivateClassTestCase<T> {

    /**
     * Package private to limit sub classing.
     */
    TextCursorPackageTestCase() {
        super();
    }

    final protected void checkEmpty(final TextCursor cursor) {
        if (false == cursor.isEmpty()) {
            fail("expected cursor.checkEmpty() to be TRUE but was FALSE=" + cursor);
        }
    }

    final protected void checkEmpty(final TextCursor cursor, final String message) {
        if (false == cursor.isEmpty()) {
            fail(message + '=' + cursor);
        }
    }

    final protected void checkNotEmpty(final TextCursor cursor) {
        if (cursor.isEmpty()) {
            fail("expected cursor.checkEmpty() to be FALSE but was TRUE=" + cursor);
        }
    }

    final protected void checkNotEmpty(final TextCursor cursor, final String message) {
        if (cursor.isEmpty()) {
            fail(message + '=' + cursor);
        }
    }

    final protected void moveNextFails(final TextCursor cursor) {
        try {
            cursor.next();
            fail("cursor.next() should have failed=" + cursor);
        } catch (final TextCursorException expected) {
        }
    }

    final protected void moveNextFails(final TextCursor cursor, final String message) {
        try {
            cursor.next();
            fail(message + "=" + cursor);
        } catch (final TextCursorException expected) {
        }
    }

    final protected void atAndCheck(final TextCursor cursor, final char expected) {
        this.atAndCheck(cursor, expected, null);
    }

    final protected void atAndCheck(final TextCursor cursor, final char expected, final String message) {
        assertNotNull("cursor", cursor);

        final char at = cursor.at();
        if (at != expected) {
            this.failNotEquals(message + "=" + cursor, expected, at);
        }
    }

    final protected void atAndCheck(final char at, final char expected) {
        this.atAndCheck(at, expected, (String) null);
    }

    final protected void atAndCheck(final char at, final char expected, final TextCursor cursor) {
        if (at != expected) {
            this.failNotEquals("wrong character=" + cursor, expected, at);
        }
    }

    final protected void atAndCheck(final char at, final char expected, final String message) {
        if (at != expected) {
            this.failNotEquals(message, expected, at);
        }
    }

    final protected void atFails(final TextCursor cursor) {
        try {
            cursor.at();
            fail("cursor.at() should have failed=" + cursor);
        } catch (final TextCursorException expected) {
        }
    }

    final protected void atFails(final TextCursor cursor, final String message) {
        try {
            cursor.at();
            fail(message + '=' + cursor);
        } catch (final TextCursorException expected) {
        }
    }

    /**
     * Escapes the both characters before failing.
     */
    private void failNotEquals(final String message, final char expected, final char actual) {
        TestCase.failNotEquals(message, CharSequences.quoteAndEscape(expected), CharSequences.quoteAndEscape(actual));
    }
}
