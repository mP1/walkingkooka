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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RelativeUrlTest extends UrlTestCase<RelativeUrl> {

    // tests

    @Test
    @Override
    public void testToString() {
        checkToString(Url.relative(PATH, QUERY, FRAGMENT), "/path?query=value#fragment");
    }

    @Test
    @Override
    public void testToStringWithoutQuery() {
        checkToString(Url.relative(PATH, UrlQueryString.EMPTY, FRAGMENT), "/path#fragment");
    }

    @Test
    @Override
    public void testToStringWithoutFragment() {
        checkToString(Url.relative(PATH, QUERY, UrlFragment.EMPTY), "/path?query=value");
    }

    @Test
    @Override
    public void testToStringWithoutQueryAndFragment() {
        checkToString(Url.relative(PATH, UrlQueryString.EMPTY, UrlFragment.EMPTY), "/path");
    }

    @Test
    @Override
    public void testIsAbsolute() {
        assertFalse(Url.relative(PATH, QUERY, FRAGMENT).isAbsolute());
    }

    @Test
    @Override
    public void testIsRelative() {
        assertTrue(this.createUrl().isRelative());
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
    protected Class<RelativeUrl> type() {
        return RelativeUrl.class;
    }
}
