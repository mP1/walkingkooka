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

package walkingkooka.net.header;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CookieExpiresTest extends CookieDeletionTestCase<CookieExpires> {

    // constants

    private final static LocalDateTime EXPIRES = LocalDateTime.of(2000, 12, 31, 12, 58, 59);

    // tests

    @Test
    public void testParseFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CookieExpires.parseExpires("invalid!");
        });
    }

    @Test
    public void testWith() {
        final CookieExpires expires = this.createDeletion();
        assertEquals(EXPIRES, expires.dateTime(), "dateTime");
    }

    @Test
    public void testEqualsDifferentDateTime() {
        this.checkNotEquals(CookieExpires.with(EXPIRES.plusDays(1)));
    }

    @Test
    public void testToMaxAgeSeconds() {
        assertEquals(0, this.createDeletion().toMaxAgeSeconds(EXPIRES));
    }

    @Test
    public void testToMaxAgeSeconds2() {
        assertEquals(123, this.createDeletion().toMaxAgeSeconds(EXPIRES.minusSeconds(123)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createDeletion(), "expires=Sun, 31 Dec 2000 12:58:59 GMT");
    }

    @Override
    CookieExpires createDeletion() {
        return CookieExpires.with(EXPIRES);
    }

    @Override
    public Class<CookieExpires> type() {
        return CookieExpires.class;
    }
}
