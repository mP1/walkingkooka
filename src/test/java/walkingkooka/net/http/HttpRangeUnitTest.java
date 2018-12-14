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

public final class HttpRangeUnitTest extends HeaderValueTestCase<HttpRangeUnit> {

    @Test(expected = NullPointerException.class)
    public void testFromHeaderTextNullFails() {
        HttpRangeUnit.fromHeaderText(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromHeaderTextEmptyFails() {
        HttpRangeUnit.fromHeaderText("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromHeaderTextUnkownFails() {
        HttpRangeUnit.fromHeaderText("unknown");
    }

    @Test
    public void testFromHeaderTextBytes() {
        assertSame(HttpRangeUnit.BYTES, HttpRangeUnit.fromHeaderText("bytes"));
    }

    @Test
    public void testFromHeaderTextBytesCaseUnimportant() {
        assertSame(HttpRangeUnit.BYTES, HttpRangeUnit.fromHeaderText("BYtes"));
    }

    @Test
    public void testFromHeaderTextNone() {
        assertSame(HttpRangeUnit.NONE, HttpRangeUnit.fromHeaderText("none"));
    }

    @Override
    protected HttpRangeUnit createHeaderValue() {
        return HttpRangeUnit.BYTES;
    }

    @Override
    protected HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    @Override
    protected Class<HttpRangeUnit> type() {
        return HttpRangeUnit.class;
    }
}
