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
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class HeaderParserTestCase<P extends HeaderParser<N>,
        N extends HeaderParameterName<?>,
        V> extends PackagePrivateClassTestCase<P> {

    HeaderParserTestCase() {
        super();
    }

    @Test
    public final void testToString() {
        assertEquals("0 in \"123\"", this.createHeaderParser("123").toString());
    }

    abstract P createHeaderParser(final String text);

    final void parseAndCheck(final String headerValue, final ContentDisposition disposition) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                disposition,
                this.parse(headerValue));
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, HeaderParser.invalidCharacter(pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            this.parse(text);
            fail();
        } catch (final HeaderValueException expected) {
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    abstract V parse(final String text);
}
