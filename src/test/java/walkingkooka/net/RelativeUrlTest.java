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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RelativeUrlTest extends AbsoluteOrRelativeUrlTestCase<RelativeUrl> {

    // parseRelative..........................................................................................

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            RelativeUrl.parseRelative0(null);
        });
    }

    @Override
    public void testParseEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseAbsoluteUrlFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelativeUrl.parseRelative0("http://example.com");
        });
    }

    @Test
    public void testParseEmptyPath() {
        final RelativeUrl url = RelativeUrl.parseRelative0("");
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSlash() {
        final RelativeUrl url = RelativeUrl.parseRelative0("/");
        this.checkPath(url, UrlPath.parse("/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParsePathEndingSlash() {
        final RelativeUrl url = RelativeUrl.parseRelative0("/path/file/");
        this.checkPath(url, UrlPath.parse("/path/file/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParsePathQueryStringFragment() {
        final RelativeUrl url = RelativeUrl.parseRelative0("/path123?query456#fragment789");
        this.checkPath(url, UrlPath.parse("path123"));
        this.checkQueryString(url, UrlQueryString.with("query456"));
        this.checkFragment(url, UrlFragment.with("fragment789"));
    }

    // UrlVisitor......................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final RelativeUrl url = this.createUrl();

        new FakeUrlVisitor() {
            @Override
            protected Visiting startVisit(final Url u) {
                assertSame(url, u);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Url u) {
                assertSame(url, u);
                b.append("2");
            }

            @Override
            protected void visit(final RelativeUrl u) {
                assertSame(url, u);
                b.append("5");
            }
        }.accept(url);
        assertEquals("152", b.toString());
    }

    // toString.........................................................................................................

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

    // ClassTesting ....................................................................................................

    @Override
    public Class<RelativeUrl> type() {
        return RelativeUrl.class;
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public RelativeUrl fromJsonNode(final JsonNode node) {
        return Url.fromJsonNodeRelative(node);
    }

    // ParseStringTesting ..............................................................................................

    @Override
    public RelativeUrl parse(final String text) {
        return RelativeUrl.parseRelative0(text);
    }

    // SerializationTesting ............................................................................................

    @Override
    public RelativeUrl serializableInstance() {
        return Url.relative(UrlPath.parse("/path"), UrlQueryString.with("query"), UrlFragment.with("fragment123"));
    }
}
