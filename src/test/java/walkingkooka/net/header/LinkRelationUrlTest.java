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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.type.JavaVisibility;

public final class LinkRelationUrlTest extends LinkRelationTestCase<LinkRelationUrl, AbsoluteUrl> {

    private final static String TEXT = "http://example.com";

    @Test
    public void testHeaderText() {
        this.toHeaderTextAndCheck(TEXT);
    }

    @Test
    public void testEqualsDifferentUrl() {
        this.checkNotEquals(LinkRelation.with("http://example2.com"));
    }

    @Override
    boolean url() {
        return true;
    }

    @Override
    LinkRelationUrl createLinkRelation(final AbsoluteUrl value) {
        return LinkRelationUrl.url(value.toString());
    }

    @Override
    AbsoluteUrl value() {
        return Url.parseAbsolute(TEXT);
    }

    @Override
    AbsoluteUrl differentValue() {
        return Url.parseAbsolute("http://example.com/different");
    }

    @Override
    public Class<LinkRelationUrl> type() {
        return LinkRelationUrl.class;
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
