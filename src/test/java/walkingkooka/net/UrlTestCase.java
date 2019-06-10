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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Url} with mostly parameter checking tests.
 */
abstract public class UrlTestCase<U extends AbsoluteOrRelativeUrl> implements ClassTesting2<U>,
        HashCodeEqualsDefinedTesting<U>,
        IsMethodTesting<U>,
        ToStringTesting<U>,
        TypeNameTesting<U> {

    UrlTestCase() {
        super();
    }

    // constants

    final static UrlPath PATH = UrlPath.parse("/path");
    final static UrlQueryString QUERY = UrlQueryString.with("query=value");
    final static UrlFragment FRAGMENT = UrlFragment.with("fragment");

    // tests

    @Test
    public void testNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl(null, QUERY, FRAGMENT);
        });
    }

    @Test
    public void testNullQueryFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl(PATH, null, FRAGMENT);
        });
    }

    @Test
    public void testNullFragmentFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl(PATH, QUERY, null);
        });
    }

    public void testWith() {
        final U url = this.createUrl(PATH, QUERY, FRAGMENT);
        this.checkPath(url, PATH);
        this.checkQueryString(url, QUERY);
        this.checkFragment(url, FRAGMENT);
    }

    @Test
    public void testOnlySlash() {
        final UrlPath path = UrlPath.parse("" + Url.PATH_START);
        final U url = this.createUrl(path, QUERY, FRAGMENT);
        assertSame(path, url.path(), "path");
    }

    @Test
    public void testWithoutQuery() {
        final U url = this.createUrl(PATH, UrlQueryString.EMPTY, FRAGMENT);
        assertSame(PATH, url.path(), "path");
        assertSame(UrlQueryString.EMPTY, url.query(), "query");
        this.checkFragment(url, FRAGMENT);
    }

    @Test
    public void testWithoutFragment() {
        final U url = this.createUrl(PATH, QUERY, UrlFragment.EMPTY);
        assertSame(PATH, url.path(), "path");
        this.checkQueryString(url, QUERY);
        assertSame(UrlFragment.EMPTY, url.fragment(), "fragment");
    }

    // would be setters

    // setPath .......................................................................................................

    @Test
    public final void testSetPathNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl().setPath(null);
        });
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

    @Test
    public final void testSetQueryNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl().setQuery(null);
        });
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

    @Test
    public final void testSetFragmentNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createUrl().setFragment(null);
        });
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

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public final void testEqualsDifferentPath() {
        this.checkNotEquals(
                this.createObject(UrlPath.parse("/different"), QUERY, FRAGMENT));
    }

    @Test
    public final void testEqualsDifferentQuery() {
        this.checkNotEquals(this.createObject(PATH, UrlQueryString.with("differentQueryString"), FRAGMENT));
    }

    @Test
    public void testEqualsDifferentAnchor() {
        this.checkNotEquals(this.createObject(PATH, QUERY, UrlFragment.with("different")));
    }

    // toString........................................................................

    @Test
    abstract public void testToStringWithoutQuery();

    @Test
    abstract public void testToStringWithoutFragment();

    @Test
    abstract public void testToStringWithoutQueryAndFragment();

    // factory

    final U createUrl() {
        return this.createUrl(PATH, QUERY, FRAGMENT);
    }

    abstract U createUrl(UrlPath path, UrlQueryString query, UrlFragment fragment);

    final void checkPath(final AbsoluteOrRelativeUrl url, final UrlPath path) {
        assertEquals(path, url.path(), "path");
    }

    final void checkQueryString(final AbsoluteOrRelativeUrl url, final UrlQueryString queryString) {
        assertEquals(queryString, url.query(), "queryString");
    }

    final void checkFragment(final AbsoluteOrRelativeUrl url, final UrlFragment fragment) {
        assertEquals(fragment, url.fragment(), "fragment");
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public U createObject() {
        return this.createObject(PATH, QUERY, FRAGMENT);
    }

    abstract U createObject(final UrlPath path, final UrlQueryString query, final UrlFragment fragment);

    // IsMethodTesting.................................................................................................

    @Override
    public final U createIsMethodObject() {
        return this.createUrl();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return "Url";
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Url.class.getSimpleName();
    }
}
