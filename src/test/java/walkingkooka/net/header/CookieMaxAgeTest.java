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


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class CookieMaxAgeTest extends CookieDeletionTestCase<CookieMaxAge> {

    // constants

    private final int SECONDS = 123;

    // tests

    @Test
    public void testDeleted() {
        assertSame(CookieDeletion.DELETED, CookieMaxAge.with(0));
    }

    @Test
    public void testSessionOnly() {
        assertSame(CookieDeletion.SESSION_ONLY, CookieMaxAge.with(-1));
    }

    @Test
    public void testWith() {
        final CookieMaxAge age = this.createDeletion();
        assertEquals("seconds", this.SECONDS, age.seconds());
    }

    @Test
    public void testIsExpires() {
        final CookieMaxAge maxAge = this.createDeletion();
        assertFalse(maxAge.toString(), maxAge.isExpires());
    }

    @Test
    public void testIsMaxAge() {
        final CookieMaxAge maxAge = this.createDeletion();
        assertTrue(maxAge.toString(), maxAge.isMaxAge());
    }

    @Test
    public void testEqualsDifferentSeconds() {
        this.checkNotEquals(CookieMaxAge.with(1 + SECONDS));
    }

    @Test
    public void testToString() {
        assertEquals("max-age=123", this.createDeletion().toString());
    }

    @Override
    CookieMaxAge createDeletion() {
        return CookieMaxAge.with(this.SECONDS);
    }

    @Override
    protected Class<CookieMaxAge> type() {
        return CookieMaxAge.class;
    }
}
