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
import walkingkooka.test.IsMethodTesting;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class CookieTestCase<C extends Cookie> extends HeaderValueTestCase<C>
        implements IsMethodTesting<C> {

    CookieTestCase() {
        super();
    }

    // constants

    final static CookieName NAME = CookieName.with("cookie123");
    final static String VALUE = "value456";

    // tests

    @Test
    public final void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createCookie(null, CookieTestCase.VALUE);
        });
    }

    @Test
    public final void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createCookie(CookieTestCase.NAME, null);
        });
    }

    @Test
    public final void testWithInvalidValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createCookie(CookieTestCase.NAME, "  ");
        });
    }

    // setName ......................................................................................

    @Test
    public final void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createCookie().setName(null);
        });
    }

    @Test
    public final void testSetNameSame() {
        final C cookie = this.createCookie();
        assertSame(cookie, cookie.setName(NAME));
    }

    // setValue ................................................................................................

    @Test
    public final void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createCookie().setValue(null);
        });
    }

    @Test
    public final void testSetValueSame() {
        final C cookie = this.createCookie();
        assertSame(cookie, cookie.setValue(VALUE));
    }

    // check ..................................................................................................

    @Test
    public final void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    final public void testEqualsDifferentName() {
        this.checkNotEquals(this.createCookie(CookieName.with("different"), VALUE));
    }

    final public void testEqualsDifferentValue() {
        this.checkNotEquals(this.createCookie(NAME, "different"));
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
        assertEquals(name, cookie.name(), "name");
    }

    final void checkValue(final Cookie cookie) {
        checkValue(cookie, VALUE);
    }

    final void checkValue(final Cookie cookie, final String value) {
        assertEquals(value, cookie.value(), "value");
    }

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final C createIsMethodObject() {
        return this.createCookie();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Cookie.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isRequest") || m.equals("isResponse");
    }
}
