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
import walkingkooka.naming.NameTestCase;

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie
final public class CookieNameTest extends NameTestCase<CookieName> {

    /**
     * A <cookie-name> can be any US-ASCII characters except control characters (CTLs), spaces, or tabs.
     * It also must not contain a separator character like the following: ( ) < > @ , ; : \ " /  [ ] ? = { }.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncludeParensOpenFails() {
        CookieName.with("cookie(");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeParensCloseFails() {
        CookieName.with("cookie)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeLessThanFails() {
        CookieName.with("cookie<");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeGreaterThanFails() {
        CookieName.with("cookie>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeAtSignFails() {
        CookieName.with("cookie@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeCommaFails() {
        CookieName.with("cookie,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeSemiColonFails() {
        CookieName.with("cookie;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeColonFails() {
        CookieName.with("cookie:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeBackslashFails() {
        CookieName.with("cookie\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeDoubleQuoteFails() {
        CookieName.with("cookie\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeForwardSlashFails() {
        CookieName.with("cookie/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeBracketOpenFails() {
        CookieName.with("cookie[");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeBracketCloseFails() {
        CookieName.with("cookie]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuestionMarkFails() {
        CookieName.with("cookie?");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testEqualsSignFails() {
        CookieName.with("cookie=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBraceOpenFails() {
        CookieName.with("cookie{");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBraceCloseFails() {
        CookieName.with("cookie}");
    }

    @Test
    public void testIncludesDigits() {
        this.createNameAndCheck("cookie123");
    }

    @Test
    public void testIncludesDash() {
        this.createNameAndCheck("cookie-123");
    }

    @Test
    public void testBeginsDollarSign() {
        this.createNameAndCheck("$cookie");
    }

    @Override
    protected CookieName createName(final String name) {
        return CookieName.with(name);
    }

    @Override
    protected Class<CookieName> type() {
        return CookieName.class;
    }
}
