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

final public class CookieNameEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<CookieName> {

    @Test
    public void testDifferentName() {
        this.checkNotEquals(CookieName.with("different"));
    }

    @Test
    public void testDifferentCase() {
        this.checkNotEquals(CookieName.with("COOKIE"));
    }

    @Override
    protected CookieName createObject() {
        return CookieName.with("cookie");
    }
}
