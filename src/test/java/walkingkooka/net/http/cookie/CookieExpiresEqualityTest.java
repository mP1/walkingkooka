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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.time.LocalDateTime;

final public class CookieExpiresEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<CookieExpires> {

    // constants

    private final static LocalDateTime EXPIRES = LocalDateTime.of(2000, 12, 31, 12, 58, 59);

    // tests

    @Test
    public void testDifferentDateTime() {
        this.checkNotEquals(CookieExpires.with(EXPIRES.plusDays(1)));
    }

    @Override
    protected CookieExpires createObject() {
        return CookieExpires.with(CookieExpiresEqualityTest.EXPIRES);
    }
}
