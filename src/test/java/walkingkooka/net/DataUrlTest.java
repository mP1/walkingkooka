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
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.MediaType;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DataUrlTest extends UrlTestCase<DataUrl> {

    @Test
    public void testWithNullMediaTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            DataUrl.with(null, this.binary());
        });
    }

    @Test
    public void testWithMediaTypeWithParametersFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            DataUrl.with(Optional.of(this.mediaType().setCharset(CharsetName.UTF_8)), this.binary());
        });
    }

    @Test
    public void testWithNullBinaryFails() {
        assertThrows(NullPointerException.class, () -> {
            DataUrl.with(Optional.of(this.mediaType()), null);
        });
    }

    // ParseTesting.....................................................................................................

    @Test
    public void testParseNonDataUrlFails() {
        this.parseFails("http://example.com", IllegalArgumentException.class);
    }

    @Test
    public void testParseWithContentTypeBase64AndEncoded() {
        this.parseAndCheck("data:text/plain;base64,YWJjMTIz", this.createUrl());
    }

    @Test
    public void testParseWithContentTypeAndEncoded() {
        this.parseAndCheck("data:text/plain,YWJjMTIz", this.createUrl());
    }

    @Test
    public void testParseWithoutContentType() {
        this.parseAndCheck("data:;base64,YWJjMTIz", DataUrl.with(Optional.empty(), this.binary()));
    }

    // UrlVisitor......................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final DataUrl url = this.createUrl();

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
            protected void visit(final DataUrl u) {
                assertSame(url, u);
                b.append("5");
            }
        }.accept(url);
        assertEquals("152", b.toString());
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createUrl(), "data:text/plain;base64,YWJjMTIz");
    }

    @Override
    DataUrl createUrl() {
        return DataUrl.with(Optional.of(this.mediaType()), this.binary());
    }

    private MediaType mediaType() {
        return MediaType.TEXT_PLAIN;
    }

    private Binary binary() {
        return Binary.with("abc123".getBytes(Charset.defaultCharset()));
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public DataUrl fromJsonNode(final JsonNode node) {
        return Url.fromJsonNodeData(node);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public DataUrl parse(final String text) {
        return DataUrl.parseData0(text);
    }

    // SerializableTesting...............................................................................................

    @Override
    public DataUrl serializableInstance() {
        return this.createUrl();
    }

    @Override
    public Class<DataUrl> type() {
        return DataUrl.class;
    }
}
