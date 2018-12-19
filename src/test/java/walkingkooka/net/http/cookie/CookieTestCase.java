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
import walkingkooka.net.header.HeaderValueTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

abstract public class CookieTestCase<C extends Cookie> extends HeaderValueTestCase<C> {

    CookieTestCase() {
        super();
    }

    // constants

    final static CookieName NAME = CookieName.with("cookie123");
    final static String VALUE = "value456";

    // tests

    @Test(expected = NullPointerException.class)
    public final void testWithNullNameFails() {
        this.createCookie(null, CookieTestCase.VALUE);
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullValueFails() {
        this.createCookie(CookieTestCase.NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithInvalidValueFails() {
        this.createCookie(CookieTestCase.NAME, "  ");
    }

    // setName ......................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetNameNullFails() {
        this.createCookie().setName(null);
    }

    @Test
    public final void testSetNameSame() {
        final C cookie = this.createCookie();
        assertSame(cookie, cookie.setName(NAME));
    }

    // setValue ................................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetValueNullFails() {
        this.createCookie().setValue(null);
    }

    @Test
    public final void testSetValueSame() {
        final C cookie = this.createCookie();
        assertSame(cookie, cookie.setValue(VALUE));
    }

    // helpers ................................................................................................

    final C createCookie() {
        return this.createCookie(NAME, VALUE);
    }

    abstract C createCookie(final CookieName name, final String value);

    final void checkName(final Cookie cookie) {
        checkName(cookie, NAME);
    }

    final void checkName(final Cookie cookie, final CookieName name) {
        assertEquals("name", name, cookie.name());
    }

    final void checkValue(final Cookie cookie) {
        checkValue(cookie, VALUE);
    }

    final void checkValue(final Cookie cookie, final String value) {
        assertEquals("value", value, cookie.value());
    }

    @Override
    protected final boolean isMultipart() {
        return false;
    }
}
