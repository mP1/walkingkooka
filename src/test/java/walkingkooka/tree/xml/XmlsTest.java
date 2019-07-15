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

package walkingkooka.tree.xml;

import org.junit.jupiter.api.Test;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class XmlsTest implements PublicStaticHelperTesting<Xmls> {

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

    @Test
    public void testDecodeUnknownEntityFails() {
        assertThrows(XmlException.class, () -> {
            Xmls.decode("before &unknown; after");
        });
    }

    private void decodeAndCheck(final String in, final String expected) {
        assertEquals(expected, Xmls.decode(in), "Decode " + CharSequences.quote(in));
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
        assertEquals(expected, Xmls.encode(in), "encode " + CharSequences.quote(in));
    }
    // misc

    @Override
    public Class<Xmls> type() {
        return Xmls.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
