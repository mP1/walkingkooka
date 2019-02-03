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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class CookieMaxAgeTest extends CookieDeletionTestCase<CookieMaxAge> {

    // constants

    private final static int SECONDS = 123;

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
        assertEquals(SECONDS, age.seconds(), "seconds");
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
        return CookieMaxAge.with(SECONDS);
    }

    @Override
    protected Class<CookieMaxAge> type() {
        return CookieMaxAge.class;
    }
}
