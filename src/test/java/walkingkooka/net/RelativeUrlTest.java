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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RelativeUrlTest extends UrlTestCase<RelativeUrl>
        implements ParseStringTesting<RelativeUrl>,
        SerializationTesting<RelativeUrl> {

    // parseRelative..........................................................................................

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            RelativeUrl.parse(null);
        });
    }

    @Override
    public void testParseEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseAbsoluteUrlFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelativeUrl.parse("http://example.com");
        });
    }

    @Test
    public void testParseEmptyPath() {
        final RelativeUrl url = RelativeUrl.parse("");
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSlash() {
        final RelativeUrl url = RelativeUrl.parse("/");
        this.checkPath(url, UrlPath.parse("/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParsePathEndingSlash() {
        final RelativeUrl url = RelativeUrl.parse("/path/file/");
        this.checkPath(url, UrlPath.parse("/path/file/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParsePathQueryStringFragment() {
        final RelativeUrl url = RelativeUrl.parse("/path123?query456#fragment789");
        this.checkPath(url, UrlPath.parse("path123"));
        this.checkQueryString(url, UrlQueryString.with("query456"));
        this.checkFragment(url, UrlFragment.with("fragment789"));
    }
    
    // toString........................................................................

    @Test
    public void testToString() {
        toStringAndCheck(Url.relative(PATH, QUERY, FRAGMENT), "/path?query=value#fragment");
    }

    @Test
    @Override
    public void testToStringWithoutQuery() {
        toStringAndCheck(Url.relative(PATH, UrlQueryString.EMPTY, FRAGMENT), "/path#fragment");
    }

    @Test
    @Override
    public void testToStringWithoutFragment() {
        toStringAndCheck(Url.relative(PATH, QUERY, UrlFragment.EMPTY), "/path?query=value");
    }

    @Test
    @Override
    public void testToStringWithoutQueryAndFragment() {
        toStringAndCheck(Url.relative(PATH, UrlQueryString.EMPTY, UrlFragment.EMPTY), "/path");
    }

    @Test
    public void testToRelative() {
        final RelativeUrl url = this.createUrl();
        assertSame(url, url.relativeUrl());
    }

    @Override
    protected RelativeUrl createUrl(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return Url.relative(path, query, fragment);
    }

    @Override
    public Class<RelativeUrl> type() {
        return RelativeUrl.class;
    }

    @Override
    RelativeUrl createObject(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return Url.relative(path, query, fragment);
    }

    // ParseStringTesting ........................................................................................

    @Override
    public RelativeUrl parse(final String text) {
        return RelativeUrl.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializationTesting ........................................................................................

    @Override
    public RelativeUrl serializableInstance() {
        return Url.relative(UrlPath.parse("/path"), UrlQueryString.with("query"), UrlFragment.with("fragment123"));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
