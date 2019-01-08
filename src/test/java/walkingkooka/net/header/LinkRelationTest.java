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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class LinkRelationTest extends LinkRelationTestCase<LinkRelation<Object>, Object> {

    private final static String TEXT = "abc123";
    private final static String URL = "http://example.com";

    @Test
    public void testWithText() {
        final LinkRelation<?> linkRelation = LinkRelation.with(TEXT);
        assertEquals("value", TEXT, linkRelation.value());
    }

    @Test
    public void testWithUrl() {
        final LinkRelation<?> linkRelation = LinkRelation.with(URL);
        assertEquals("value", AbsoluteUrl.parse(URL), linkRelation.value());
    }

    @Test
    public void testHeaderTextText() {
        this.toHeaderTextAndCheck(LinkRelation.with(TEXT), TEXT);
    }

    @Test
    public void testHeaderTextUrl() {
        this.toHeaderTextAndCheck(LinkRelation.with(URL), URL);
    }

    @Override
    boolean url() {
        return true;
    }

    @Override
    LinkRelation<Object> createLinkRelation() {
        return Cast.to(LinkRelation.with(URL));
    }

    @Override
    protected Class<LinkRelation<Object>> type() {
        return Cast.to(LinkRelation.class);
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
