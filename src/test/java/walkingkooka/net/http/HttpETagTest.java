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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.HeaderValueTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public final class HttpETagTest extends HeaderValueTestCase<HttpETag> {

    private final static String VALUE = "123";
    private final static HttpETagValidator WEAK = HttpETagValidator.WEAK;

    // with .....................................................................................


    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        HttpETag.with(null, HttpETagValidator.STRONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueCharacterFails() {
        HttpETag.with("abc def", HttpETagValidator.STRONG);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullWeaknessIndicatorFails() {
        HttpETag.with(VALUE, null);
    }

    // toHeaderText...................................................................

    @Test
    public void testToHeaderTextString() {
        this.toHeaderTextAndCheck(HttpETag.with("abc123", HttpETagValidator.STRONG),
                "\"abc123\"");
    }

    @Test
    public void testToHeaderTextWeak() {
        this.toHeaderTextAndCheck(HttpETag.with("abc123", HttpETagValidator.WEAK),
                "W/\"abc123\"");
    }

    // toString ...........................................................................................

    @Test
    public void testToString() {
        assertEquals("\"abc123\"", HttpETag.with("abc123", HttpETagValidator.STRONG).toString());
    }

    @Test
    public void testToStringWeak() {
        assertEquals("W/\"abc123\"", HttpETag.with("abc123", WEAK).toString());
    }

    // toHeaderTextList.......................................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListListNullFails() {
        HttpETag.toHeaderTextList(null);
    }

    @Test
    public void testToHeaderTextListListOfOne() {
        this.toHeaderTextListAndCheck("\"abc123\"",
                HttpETag.with("abc123", HttpETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfOne2() {
        this.toHeaderTextListAndCheck("W/\"abc123\"",
                HttpETag.with("abc123", HttpETagValidator.WEAK));
    }

    @Test
    public void testToHeaderTextListListOfOneWildcard() {
        this.toHeaderTextListAndCheck("*",
                HttpETag.wildcard());
    }

    @Test
    public void testToHeaderTextListListOfMany() {
        this.toHeaderTextListAndCheck("\"1\", \"2\"",
                HttpETag.with("1", HttpETagValidator.STRONG),
                HttpETag.with("2", HttpETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfMany2() {
        this.toHeaderTextListAndCheck("\"11\", \"22\"",
                HttpETag.with("11", HttpETagValidator.STRONG),
                HttpETag.with("22", HttpETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfMany3() {
        this.toHeaderTextListAndCheck("W/\"11\", \"22\"",
                HttpETag.with("11", HttpETagValidator.WEAK),
                HttpETag.with("22", HttpETagValidator.STRONG));
    }

    private void toHeaderTextListAndCheck(final String toString, final HttpETag... tags) {
        assertEquals("HttpETag.toString(List) failed =" + CharSequences.quote(toString),
                toString,
                HttpETag.toHeaderTextList(Lists.of(tags)));
    }

    @Override
    protected HttpETag createHeaderValue() {
        return HttpETag.with("A", HttpETagValidator.WEAK);
    }

    @Override
    protected HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    @Override
    protected Class<HttpETag> type() {
        return HttpETag.class;
    }
}
