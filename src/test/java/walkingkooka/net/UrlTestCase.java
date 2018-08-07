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
import walkingkooka.test.PublicClassTestCase;

/**
 * Base class for testing a {@link Url} with mostly parameter checking tests.
 */
abstract public class UrlTestCase<U extends Url> extends PublicClassTestCase<U> {

    UrlTestCase() {
        super();
    }

    // constants

    final static UrlPath PATH = UrlPath.parse("/path");
    final static UrlQueryString QUERY = UrlQueryString.with("query=value");
    final static UrlFragment FRAGMENT = UrlFragment.with("fragment");

    // tests

    @Test(expected = NullPointerException.class)
    public void testNullPathFails() {
        this.createUrl(null, QUERY, FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void testNullQueryFails() {
        this.createUrl(PATH, null, FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFragmentFails() {
        this.createUrl(PATH, QUERY, null);
    }

    public void testWith() {
        final U url = this.createUrl(PATH, QUERY, FRAGMENT);
        assertSame("path", PATH, url.path());
        assertSame("query", QUERY, url.query());
        assertSame("fragment", FRAGMENT, url.fragment());
    }

    @Test
    public void testOnlySlash() {
        final UrlPath path = UrlPath.parse("" + Url.PATH_START);
        final U url = this.createUrl(path, QUERY, FRAGMENT);
        assertSame("path", path, url.path());
    }

    @Test
    public void testWithoutQuery() {
        final U url = this.createUrl(PATH, UrlQueryString.EMPTY, FRAGMENT);
        assertSame("path", PATH, url.path());
        assertSame("query", UrlQueryString.EMPTY, url.query());
        assertSame("fragment", FRAGMENT, url.fragment());
    }

    @Test
    public void testWithoutFragment() {
        final U url = this.createUrl(PATH, QUERY, UrlFragment.EMPTY);
        assertSame("path", PATH, url.path());
        assertSame("query", QUERY, url.query());
        assertSame("fragment", UrlFragment.EMPTY, url.fragment());
    }

    @Test
    abstract public void testToString();

    @Test
    abstract public void testToStringWithoutQuery();

    @Test
    abstract public void testToStringWithoutFragment();

    @Test
    abstract public void testToStringWithoutQueryAndFragment();

    @Test
    abstract public void testIsAbsolute();

    @Test
    abstract public void testIsRelative();

    // factory

    final U createUrl() {
        return this.createUrl(PATH, QUERY, FRAGMENT);
    }

    abstract U createUrl(UrlPath path, UrlQueryString query, UrlFragment fragment);

    static void checkToString(final Url url, final String expected) {
        if (false == expected.equals(url.value())) {
            failNotEquals(null, expected, url.value());
        }
    }
}
