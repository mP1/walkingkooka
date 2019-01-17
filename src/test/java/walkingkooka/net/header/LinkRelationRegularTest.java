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
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class LinkRelationRegularTest extends LinkRelationTestCase<LinkRelationRegular, String> {

    private final static String TEXT = "abc123";

    @Test
    public void testWith() {
        this.createAndCheck(TEXT);
    }

    @Test
    public void testWithUpperCase() {
        this.createAndCheck("AB");
    }

    @Test
    public void testWithDots() {
        this.createAndCheck("A.b");
    }

    @Test
    public void testWithDash() {
        this.createAndCheck("A-b");
    }

    private void createAndCheck(final String text) {
        final LinkRelationRegular linkRelation = LinkRelationRegular.regular(text);
        assertEquals("value", text, linkRelation.value());
    }

    @Test
    public void testHeaderText() {
        this.toHeaderTextAndCheck(TEXT);
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(LinkRelation.with("different"));
    }

    @Test
    public void testEqualsUpperCaseText() {
        this.checkNotEquals(LinkRelation.with("ABC123"));
    }

    @Test
    public void testEqualsUrl() {
        this.checkNotEquals(LinkRelation.with("http://example.com"));
    }

    @Override
    boolean url() {
        return false;
    }

    @Override
    LinkRelationRegular createLinkRelation() {
        return LinkRelationRegular.regular(TEXT);
    }

    @Override
    protected Class<LinkRelationRegular> type() {
        return LinkRelationRegular.class;
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
