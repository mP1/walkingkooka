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

package walkingkooka.xml;

import org.junit.Test;
import walkingkooka.test.PublicStaticHelperTestCase;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

final public class XmlsTest extends PublicStaticHelperTestCase<Xmls> {

    // decode

    @Test
    public void testDecodeText() {
        this.decodeAndCheck("abc123", "abc123");
    }

    @Test
    public void testDecodeLessThan() {
        this.decodeAndCheck("before &lt; after", "before < after");
    }

    @Test
    public void testDecodeGreaterThan() {
        this.decodeAndCheck("before &gt; after", "before > after");
    }

    @Test
    public void testDecodeQuote() {
        this.decodeAndCheck("before &quot; after", "before \" after");
    }

    @Test
    public void testDecodeApostrophe() {
        this.decodeAndCheck("before &apos; after", "before ' after");
    }

    @Test
    public void testDecodeAmpersand() {
        this.decodeAndCheck("before &amp; after", "before & after");
    }

    @Test(expected = DomException.class)
    public void testDecodeUnknownEntityFails() {
        Xmls.decode("before &unknown; after");
    }

    private void decodeAndCheck(final String in, final String expected) {
        assertEquals("Decode " + CharSequences.quote(in), expected, Xmls.decode(in));
    }

    // encode

    @Test
    public void testEncodeText() {
        this.encodeAndCheck("abc123", "abc123");
    }

    @Test
    public void testEncodeLessThan() {
        this.encodeAndCheck("before < after", "before &lt; after");
    }

    @Test
    public void testEncodeGreaterThan() {
        this.encodeAndCheck("before > after", "before &gt; after");
    }

    @Test
    public void testEncodeQuote() {
        this.encodeAndCheck("before \" after", "before &quot; after");
    }

    @Test
    public void testEncodeApostrophe() {
        this.encodeAndCheck("before ' after", "before &apos; after");
    }

    @Test
    public void testEncodeAmpersand() {
        this.encodeAndCheck("before & after", "before &amp; after");
    }

    private void encodeAndCheck(final String in, final String expected) {
        assertEquals("Decode " + CharSequences.quote(in), expected, Xmls.encode(in));
    }
    // misc

    @Override protected Class<Xmls> type() {
        return Xmls.class;
    }

    @Override protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
