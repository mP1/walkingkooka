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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

abstract public class UrlEqualityTestCase<U extends Url> extends HashCodeEqualsDefinedEqualityTestCase<Url> {

    // constants

    private final static UrlPath PATH = UrlPath.parse("/path");
    private final static UrlQueryString QUERY = UrlQueryString.with("query=value");
    private final static UrlFragment FRAGMENT = UrlFragment.with("fragment123");

    // tests

    @Ignore
    @Test
    public void testEqualsOnlyOverridesAbstractOrObject() {
        // ignore
    }

    @Test
    public final void testDifferentPath() {
        this.checkNotEquals(
                this.createObject(UrlPath.parse("/different"), QUERY, FRAGMENT));
    }

    @Test
    public final void testDifferentQuery() {
        this.checkNotEquals(this.createObject(PATH, UrlQueryString.with("differentQueryString"), FRAGMENT));
    }

    @Test
    public void testDifferentAnchor() {
        this.checkNotEquals(this.createObject(PATH, QUERY, UrlFragment.with("different")));
    }

    // factory

    @Override
    protected U createObject() {
        return this.createObject(PATH, QUERY, FRAGMENT);
    }

    abstract protected U createObject(final UrlPath path, final UrlQueryString query, final UrlFragment fragment);
}
