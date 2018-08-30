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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

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

    @Test
    public final void testNaming() {
        this.checkNaming(Url.class);
    }

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
        this.checkPath(url,PATH);
        this.checkQueryString(url, QUERY);
        this.checkFragment(url, FRAGMENT);
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
        this.checkFragment(url, FRAGMENT);
    }

    @Test
    public void testWithoutFragment() {
        final U url = this.createUrl(PATH, QUERY, UrlFragment.EMPTY);
        assertSame("path", PATH, url.path());
        this.checkQueryString(url, QUERY);
        assertSame("fragment", UrlFragment.EMPTY, url.fragment());
    }

    // would be setters
    
    // setPath .......................................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetPathNullFails() {
        this.createUrl().setPath(null);
    }

    @Test
    public final void testSetPathSame() {
        final U url = this.createUrl();
        assertSame(url, url.setPath(PATH));
    }

    @Test
    public final void testSetPathDifferent() {
        final U url = this.createUrl();

        final UrlPath differentPath = UrlPath.parse("/different-path");
        final Url different = url.setPath(differentPath);
        assertNotSame(url, different);
        assertEquals(this.createUrl(differentPath, QUERY, FRAGMENT), different);
    }

    // setQuery .......................................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetQueryNullFails() {
        this.createUrl().setQuery(null);
    }

    @Test
    public final void testSetQuerySame() {
        final U url = this.createUrl();
        assertSame(url, url.setQuery(QUERY));
    }

    @Test
    public final void testSetQueryDifferent() {
        final U url = this.createUrl();

        final UrlQueryString differentQueryString = UrlQueryString.with("different=value");
        final Url different = url.setQuery(differentQueryString);
        assertNotSame(url, different);
        assertEquals(this.createUrl(PATH, differentQueryString, FRAGMENT), different);
    }

    // setFragment .......................................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetFragmentNullFails() {
        this.createUrl().setFragment(null);
    }

    @Test
    public final void testSetFragmentSame() {
        final U url = this.createUrl();
        assertSame(url, url.setFragment(FRAGMENT));
    }

    @Test
    public final void testSetFragmentDifferent() {
        final U url = this.createUrl();

        final UrlFragment differentFragment = UrlFragment.with("different-anchor");
        final Url different = url.setFragment(differentFragment);
        assertNotSame(url, different);
        assertEquals(this.createUrl(PATH, QUERY, differentFragment), different);
    }

    // toString........................................................................
    
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
        assertEquals("Url.value", expected, url.toString());
    }
    
    final void checkPath(final Url url, final UrlPath path) {
        assertSame("path", path, url.path());
    }

    final void checkQueryString(final Url url, final UrlQueryString queryString) {
        assertSame("queryString", queryString, url.query());
    }

    final void checkFragment(final Url url, final UrlFragment fragment) {
        assertSame("fragment", fragment, url.fragment());
    }
}
