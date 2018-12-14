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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.net.header.HeaderValueTestCase;

import static org.junit.Assert.assertSame;

public final class HttpHeaderRangeUnitTest extends HeaderValueTestCase<HttpHeaderRangeUnit> {

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        HttpHeaderRangeUnit.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        HttpHeaderRangeUnit.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownFails() {
        HttpHeaderRangeUnit.parse("unknown");
    }

    @Test
    public void testParseBytes() {
        assertSame(HttpHeaderRangeUnit.BYTES, HttpHeaderRangeUnit.parse("bytes"));
    }

    @Test
    public void testParseBytesCaseUnimportant() {
        assertSame(HttpHeaderRangeUnit.BYTES, HttpHeaderRangeUnit.parse("BYtes"));
    }

    @Test
    public void testParseNone() {
        assertSame(HttpHeaderRangeUnit.NONE, HttpHeaderRangeUnit.parse("none"));
    }

    @Override
    protected HttpHeaderRangeUnit createHeaderValue() {
        return HttpHeaderRangeUnit.BYTES;
    }

    @Override
    protected HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    @Override
    protected Class<HttpHeaderRangeUnit> type() {
        return HttpHeaderRangeUnit.class;
    }
}
