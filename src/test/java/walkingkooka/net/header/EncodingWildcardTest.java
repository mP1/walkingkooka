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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EncodingWildcardTest extends EncodingTestCase<EncodingWildcard> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Encoding.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Encoding.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Encoding.with("\u001f");
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    // predicate........................................................................................................

    @Test
    public void testTestContentEncodingGzip() {
        this.testTrue(ContentEncoding.GZIP);
    }

    @Test
    public void testTestContentEncodingDeflate() {
        this.testTrue(ContentEncoding.DEFLATE);
    }

    @Test
    public void testTestContentEncoding() {
        this.testTrue(ContentEncoding.with("xyz"));
    }

    // parse............................................................................................................

    @Test
    public void testParseWildcard() {
        this.parseAndCheck("*",
                Encoding.WILDCARD_ENCODING);
    }

    @Test
    public void testParseWhitespaceTokenWhitespace() {
        this.parseAndCheck(" * ",
                Encoding.WILDCARD_ENCODING);
    }

    @Override
    public EncodingWildcard createHeaderValueWithParameters() {
        return EncodingWildcard.INSTANCE;
    }

    @Override
    String value() {
        return "*";
    }

    @Override
    Class<EncodingWildcard> encodingType() {
        return EncodingWildcard.class;
    }
}
