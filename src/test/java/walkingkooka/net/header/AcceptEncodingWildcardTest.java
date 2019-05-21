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
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptEncodingWildcardTest extends AcceptEncodingTestCase<AcceptEncodingWildcard> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            AcceptEncoding.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncoding.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncoding.with("\u001f");
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
                Lists.of(AcceptEncoding.WILDCARD_ACCEPT_ENCODING));
    }

    @Test
    public void testParseWhitespaceTokenWhitespace() {
        this.parseAndCheck(" * ",
                Lists.of(AcceptEncoding.WILDCARD_ACCEPT_ENCODING));
    }

    @Override
    public AcceptEncodingWildcard createHeaderValueWithParameters() {
        return AcceptEncodingWildcard.INSTANCE;
    }

    @Override
    String value() {
        return "*";
    }

    @Override
    Class<AcceptEncodingWildcard> acceptEncodingType() {
        return AcceptEncodingWildcard.class;
    }
}
