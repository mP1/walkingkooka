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

package walkingkooka.net.http.cookie;


import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

final public class CookieExpiresTest extends CookieDeletionTestCase<CookieExpires> {

    // constants

    private final static LocalDateTime EXPIRES = LocalDateTime.of(2000, 12, 31, 12, 58, 59);

    // tests

    @Test(expected = IllegalArgumentException.class)
    public void testParseFails() {
        CookieExpires.parseExpires("invalid!");
    }

    @Test
    public void testWith() {
        final CookieExpires expires = this.createCookieExpires();
        assertEquals("dateTime", EXPIRES, expires.dateTime());
    }

    @Test
    public void testIsExpires() {
        final CookieExpires expires = this.createCookieExpires();
        assertTrue(expires.toString(), expires.isExpires());
    }

    @Test
    public void testIsMaxAge() {
        final CookieExpires expires = this.createCookieExpires();
        assertFalse(expires.toString(), expires.isMaxAge());
    }

    @Test
    public void testToMaxAgeSeconds() {
        assertEquals(0, this.createCookieExpires().toMaxAgeSeconds(EXPIRES));
    }

    @Test
    public void testToMaxAgeSeconds2() {
        assertEquals(123, this.createCookieExpires().toMaxAgeSeconds(EXPIRES.minusSeconds(123)));
    }

        @Test
    public void testToString() {
        assertEquals("expires=Sun, 31 Dec 2000 12:58:59 GMT", this.createCookieExpires().toString());
    }

    private CookieExpires createCookieExpires() {
        return CookieExpires.with(EXPIRES);
    }

    @Override
    protected Class<CookieExpires> type() {
        return CookieExpires.class;
    }
}
