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
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public final class HttpETagTest extends PublicClassTestCase<HttpETag> {

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

    // toString ...........................................................................................

    @Test
    public void testToString() {
        assertEquals("\"abc123\"", HttpETag.with("abc123", HttpETagValidator.STRONG).toString());
    }

    @Test
    public void testToStringWeak() {
        assertEquals("W/\"abc123\"", HttpETag.with("abc123", WEAK).toString());
    }

    // toString(List)....................................................................................

    @Test(expected = NullPointerException.class)
    public void testToStringListNullFails() {
        HttpETag.toString(null);
    }

    @Test
    public void testToStringListOfOne() {
        this.toStringAndCheck("\"abc123\"",
                HttpETag.with("abc123", HttpETagValidator.STRONG));
    }

    @Test
    public void testToStringListOfOne2() {
        this.toStringAndCheck("W/\"abc123\"",
                HttpETag.with("abc123", HttpETagValidator.WEAK));
    }

    @Test
    public void testToStringListOfOneWildcard() {
        this.toStringAndCheck("*",
                HttpETag.wildcard());
    }

    @Test
    public void testToStringListOfMany() {
        this.toStringAndCheck("\"1\", \"2\"",
                HttpETag.with("1", HttpETagValidator.STRONG),
                HttpETag.with("2", HttpETagValidator.STRONG));
    }

    @Test
    public void testToStringListOfMany2() {
        this.toStringAndCheck("\"11\", \"22\"",
                HttpETag.with("11", HttpETagValidator.STRONG),
                HttpETag.with("22", HttpETagValidator.STRONG));
    }

    @Test
    public void testToStringListOfMany3() {
        this.toStringAndCheck("W/\"11\", \"22\"",
                HttpETag.with("11", HttpETagValidator.WEAK),
                HttpETag.with("22", HttpETagValidator.STRONG));
    }

    private void toStringAndCheck(final String toString, final HttpETag... tags) {
        assertEquals("HttpETag.toString(List) failed =" + CharSequences.quote(toString),
                toString,
                HttpETag.toString(Lists.of(tags)));
    }

    @Override
    protected Class<HttpETag> type() {
        return HttpETag.class;
    }
}
