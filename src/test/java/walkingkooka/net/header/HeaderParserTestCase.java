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

package walkingkooka.net.header;

import org.junit.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class HeaderParserTestCase<P extends HeaderParser, V>
        extends PackagePrivateClassTestCase<P> {

    HeaderParserTestCase() {
        super();
    }

    // parse ...........................................................................................

    @Test(expected = NullPointerException.class)
    public final void testNullFails() {
        this.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmpty() {
        this.parse("");
    }

    @Test
    public final void testInvalidInitialFails() {
        this.parseInvalidCharacterFails("\0");
    }

    final void parseAndCheck(final String text, final V expected) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(text),
                expected,
                this.parse(text));
    }

    final void parseInvalidCharacterFails(final String text) {
        this.parseInvalidCharacterFails(text, text.length() - 1);
    }

    final void parseInvalidCharacterFails(final String text, final char c) {
        this.parseInvalidCharacterFails(text, text.indexOf(c));
    }

    final void parseInvalidCharacterFails(final String text, final int pos) {
        parseFails(text, new InvalidCharacterException(text, pos).getMessage());
    }

    final void parseFails(final String text, final String message) {
        try {
            this.parse(text);
            fail();
        } catch (final HeaderValueException expected) {
            assertEquals("Incorrect failure message for " + CharSequences.quoteIfChars(text),
                    message,
                    expected.getMessage());
        }
    }

    abstract V parse(final String text);
}
