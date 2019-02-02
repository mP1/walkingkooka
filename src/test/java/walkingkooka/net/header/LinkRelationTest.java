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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LinkRelationTest extends LinkRelationTestCase<LinkRelation<Object>, Object> {

    private final static String TEXT = "abc123";
    private final static String URL_TEXT = "http://example.com";

    @Override
    public void testWith2() {
        // ignored
    }

    @Test
    public void testWithText() {
        final LinkRelation<?> linkRelation = LinkRelation.with(TEXT);
        assertEquals(TEXT, linkRelation.value(), "value");
    }

    @Test
    public void testWithUrl() {
        final LinkRelation<?> linkRelation = LinkRelation.with(URL_TEXT);
        assertEquals(AbsoluteUrl.parse(URL_TEXT), linkRelation.value(), "value");
    }

    @Test
    public void testHeaderTextText() {
        this.toHeaderTextAndCheck(LinkRelation.with(TEXT), TEXT);
    }

    @Test
    public void testHeaderTextUrl() {
        this.toHeaderTextAndCheck(LinkRelation.with(URL_TEXT), URL_TEXT);
    }

    @Override
    boolean url() {
        return false; // TEXT is not a URL_TEXT
    }

    @Override
    LinkRelation<Object> createLinkRelation(final Object value) {
        return Cast.to(LinkRelation.with((String)value));
    }

    @Override
    Object value() {
        return TEXT;
    }

    @Override
    Object differentValue() {
        return URL_TEXT;
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
