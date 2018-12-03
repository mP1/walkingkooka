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
import walkingkooka.test.PublicClassTestCase;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CharsetNameTest extends PublicClassTestCase<CharsetName> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        CharsetName.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        CharsetName.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialCharFails() {
        CharsetName.with("+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialCharFails2() {
        CharsetName.with("\0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartCharFails() {
        CharsetName.with("A\u0100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartCharFails2() {
        CharsetName.with("A\0");
    }

    @Test
    public void testWithUtf8() {
        final String text = "utf-8";
        this.check(CharsetName.with(text), text, text, false);
    }

    /**
     * Content-Type: text/html; charset=ISO-8859-4
     */
    @Test
    public void testWithIso8859_4() {
        final String text = "ISO-8859-4";
        this.check(CharsetName.with(text), text, text, false);
    }

    @Test
    public void testWildcard() {
        this.check(CharsetName.WILDCARD,
                "*",
                CharsetName.NO_CHARSET,
                true);
    }

    private void check(final CharsetName charsetName,
                       final String value,
                       final String charset,
                       final boolean wildcard) {
        this.check(charsetName, value, Charset.forName(charset), wildcard);
    }

    private void check(final CharsetName charsetName,
                       final String value,
                       final Charset charset,
                       final boolean wildcard) {
        this.check(charsetName, value, Optional.of(charset), wildcard);
    }

    private void check(final CharsetName charsetName,
                       final String value,
                       final Optional<Charset> charset,
                       final boolean wildcard) {
        this.checkValue(charsetName, value);
        assertEquals("charset", charset, charsetName.charset());
        assertEquals("wildcard", wildcard, charsetName.isWildcard());
    }

    private void checkValue(final CharsetName charsetName, final String value) {
        assertTrue("value charsetName=" + charsetName + " value=" + value,
                value.equalsIgnoreCase(charsetName.value()));
    }

    @Test
    public void testSetParameters() {
        final CharsetHeaderValue headerValue = CharsetName.UTF_8
                .setParameters(CharsetHeaderValue.NO_PARAMETERS);
        assertEquals("charset", CharsetName.UTF_8, headerValue.value());
        assertEquals("parameters", CharsetHeaderValue.NO_PARAMETERS, headerValue.parameters());
    }

    @Test
    public void testToString() {
        final String charsetName = "utf-8";
        assertEquals(charsetName, CharsetName.with(charsetName).toString());
    }

    @Override
    protected Class<CharsetName> type() {
        return CharsetName.class;
    }
}
