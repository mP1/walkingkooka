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

package walkingkooka.net;


import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UrlCredentialsTest implements ClassTesting2<UrlCredentials>,
        HashCodeEqualsDefinedTesting<UrlCredentials>,
        SerializationTesting<UrlCredentials>,
        ToStringTesting<UrlCredentials> {

    private final static String USER = "user123";
    private final static String PASSWORD = "password456";

    @Test
    public void testNullUserFails() {
        assertThrows(NullPointerException.class, () -> {
            UrlCredentials.with(null, PASSWORD);
        });
    }

    @Test
    public void testNullPasswordFails() {
        assertThrows(NullPointerException.class, () -> {
            UrlCredentials.with(USER, null);
        });
    }

    @Test
    public void testWith() {
        final UrlCredentials credentials = this.credentials();
        assertEquals(USER, credentials.user(), "user");
        assertEquals(PASSWORD, credentials.password(), "password");
    }

    @Test
    public void testEqualsDifferentUser() {
        this.checkNotEquals(UrlCredentials.with("different", PASSWORD));
    }

    @Test
    public void testEqualsDifferentPassword() {
        this.checkNotEquals(UrlCredentials.with(USER, "different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.credentials(), USER + ":" + PASSWORD);
    }

    private UrlCredentials credentials() {
        return UrlCredentials.with(USER, PASSWORD);
    }

    @Override
    public Class<UrlCredentials> type() {
        return UrlCredentials.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public UrlCredentials createObject() {
        return UrlCredentials.with(USER, PASSWORD);
    }

    // SerializationTesting.................................................................................................

    @Override
    public UrlCredentials serializableInstance() {
        return UrlCredentials.with("user123", "password456");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
