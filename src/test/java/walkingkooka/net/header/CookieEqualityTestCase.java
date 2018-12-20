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


import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

abstract public class CookieEqualityTestCase<C extends Cookie> extends HashCodeEqualsDefinedEqualityTestCase<Cookie> {

    CookieEqualityTestCase() {
        super();
    }

    // constants

    final static CookieName NAME = CookieName.with("cookie123");
    final static String VALUE = "value456";

    // tests

    final public void testDifferentName() {
        this.checkNotEquals(this.createObject(CookieName.with("different"), CookieEqualityTestCase.VALUE));
    }

    final public void testDifferentValue() {
        this.checkNotEquals(this.createObject(CookieEqualityTestCase.NAME, "different"));
    }

    @Override
    protected Cookie createObject() {
        return this.createObject(CookieEqualityTestCase.NAME, CookieEqualityTestCase.VALUE);
    }

    abstract C createObject(CookieName name, String value);
}
