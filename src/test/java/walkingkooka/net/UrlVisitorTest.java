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
import walkingkooka.Binary;
import walkingkooka.net.header.MediaType;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class UrlVisitorTest implements UrlVisitorTesting<UrlVisitor> {

    @Test
    public void testStartVisitSkip() {
        final StringBuilder b = new StringBuilder();
        final AbsoluteUrl url = Url.parseAbsolute("http://example.com");

        new FakeUrlVisitor() {
            @Override
            protected Visiting startVisit(final Url u) {
                assertSame(url, u);
                b.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final Url u) {
                assertSame(url, u);
                b.append("2");
            }

        }.accept(url);

        assertEquals("12", b.toString());
    }

    @Test
    public void testAcceptAbsoluteUrl() {
        this.createVisitor().accept(Url.parseAbsolute("http://example.com"));
    }

    @Test
    public void testAcceptDataUrl() {
        this.createVisitor().accept(Url.data(Optional.of(MediaType.TEXT_PLAIN), Binary.EMPTY));
    }

    @Test
    public void testAcceptRelativeUrl() {
        this.createVisitor().accept(Url.parseRelative("/path/to/file"));
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public UrlVisitor createVisitor() {
        return new UrlVisitor() {
        };
    }

    @Override
    public Class<UrlVisitor> type() {
        return UrlVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }
}
