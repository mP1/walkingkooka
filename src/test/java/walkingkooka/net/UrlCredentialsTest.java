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

package walkingkooka.net;


import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import static org.junit.Assert.assertEquals;

public final class UrlCredentialsTest extends HashCodeEqualsDefinedTestCase<UrlCredentials> {

    private final static String USER = "user123";
    private final static String PASSWORD = "password456";

    @Test(expected = NullPointerException.class)
    public void testNullUserFails() {
        UrlCredentials.with(null, PASSWORD);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPasswordFails() {
        UrlCredentials.with(USER, null);
    }

    @Test
    public void testWith() {
        final UrlCredentials credentials = this.credentials();
        assertEquals("user", USER, credentials.user());
        assertEquals("password", PASSWORD, credentials.password());
    }

    public void testToString() {
        assertEquals(USER + ":" + PASSWORD, this.credentials().toString());
    }

    private UrlCredentials credentials() {
        return UrlCredentials.with(USER, PASSWORD);
    }

    @Override
    protected Class<UrlCredentials> type() {
        return UrlCredentials.class;
    }
}
