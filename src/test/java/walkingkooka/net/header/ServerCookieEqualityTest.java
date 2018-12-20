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

import java.util.Optional;

import static org.junit.Assert.assertNotSame;

final public class ServerCookieEqualityTest extends CookieEqualityTestCase<ServerCookie> {

    // constants

    private final static Optional<String> DOMAIN = Optional.of("host");
    private final static Optional<String> PATH = Optional.of("/path");
    private final static Optional<String> COMMENT = Optional.of("// comment");
    private final static Optional<CookieDeletion> DELETION = CookieDeletion.maxAge(123);
    private final static CookieSecure SECURE = CookieSecure.PRESENT;
    private final static CookieHttpOnly HTTP_ONLY = CookieHttpOnly.PRESENT;
    private final static CookieVersion VERSION = CookieVersion.VERSION_0;

    // tests

    @Test
    public void testDifferentDomain() {
        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                Optional.of("different"),
                PATH,
                COMMENT,
                DELETION,
                SECURE,
                HTTP_ONLY,
                VERSION));
    }

    @Test
    public void testDifferentPath() {
        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                Optional.of("/different"),
                COMMENT,
                DELETION,
                SECURE,
                HTTP_ONLY,
                VERSION));
    }

    @Test
    public void testDifferentComment() {
        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                Optional.of("different"),
                DELETION,
                SECURE,
                HTTP_ONLY,
                VERSION));
    }

    @Test
    public void testDifferentDeletion() {
        assertNotSame(DELETION, ServerCookie.NO_DELETION);

        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                ServerCookie.NO_DELETION,
                SECURE,
                HTTP_ONLY,
                VERSION));
    }


    @Test
    public void testDifferentSecure() {
        assertNotSame(SECURE, CookieSecure.ABSENT);

        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                DELETION,
                CookieSecure.ABSENT,
                HTTP_ONLY,
                VERSION));
    }

    @Test
    public void testDifferentHttpOnly() {
        assertNotSame(HTTP_ONLY, CookieHttpOnly.ABSENT);

        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                DELETION,
                SECURE,
                CookieHttpOnly.ABSENT,
                VERSION));
    }

    @Test
    public void testDifferentVersion() {
        assertNotSame(VERSION, CookieVersion.VERSION_1);

        this.checkNotEquals(ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                DELETION,
                SECURE,
                HTTP_ONLY,
                CookieVersion.VERSION_1));
    }

    @Override
    ServerCookie createObject(final CookieName name, final String value) {
        return ServerCookie.with(name,
                value,
                DOMAIN,
                PATH,
                COMMENT,
                DELETION,
                SECURE,
                HTTP_ONLY,
                VERSION);
    }
}
