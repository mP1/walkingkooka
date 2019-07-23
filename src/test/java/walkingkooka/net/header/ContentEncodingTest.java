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
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentEncodingTest extends HeaderValueTestCase<ContentEncoding>
        implements ParseStringTesting<ContentEncoding> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentEncoding.with(null);
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue(), this.value());
    }

    @Test
    public void testWith2() {
        final List<Encoding> encodings = Lists.of(Encoding.with("unknown"));
        this.checkValue(ContentEncoding.with(encodings), encodings);
    }

    private void checkValue(final ContentEncoding contentEncoding,
                            final List<Encoding> encodings) {
        assertEquals(encodings, contentEncoding.value(), "value");
    }

    // constants .......................................................................................................

    @Test
    public void testWithGzipConstant() {
        final ContentEncoding constant = ContentEncoding.GZIP;
        assertSame(constant, ContentEncoding.parse("gzip"));
    }

    @Test
    public void testWithBrConstant() {
        final ContentEncoding constant = ContentEncoding.BR;
        assertSame(constant, ContentEncoding.parse("BR"));
    }

    @Test
    public void testWithCompressConstantCaseInsignificant() {
        final ContentEncoding constant = ContentEncoding.COMPRESS;
        assertSame(constant, ContentEncoding.parse("compRESS"));
    }

    @Test
    public void testParse() {
        this.parseAndCheck("gzip, deflate, br",
                this.createHeaderValue(Encoding.parse("gzip"),
                        Encoding.parse("deflate"),
                        Encoding.parse("br")));
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("gzip,  deflate,  br",
                this.createHeaderValue(Encoding.parse("gzip"),
                        Encoding.with("deflate"),
                        Encoding.with("br")));
    }

    @Test
    public void testHeaderText() {
        final String text = "compress";
        this.toHeaderTextAndCheck(ContentEncoding.parse(text), text);
    }

    @Test
    public void testHeaderText2() {
        final String text = "identity";
        this.toHeaderTextAndCheck(ContentEncoding.with(Lists.of(Encoding.with(text))), text);
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ContentEncoding.with(Lists.of(Encoding.with("different"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), HeaderValue.toHeaderTextList(this.value(), ContentEncoding.SEPARATOR));
    }

    @Test
    public void testToString2() {
        final String text = "compress";
        this.toStringAndCheck(this.createHeaderValue(Encoding.with(text)), text);
    }

    @Override
    public ContentEncoding createHeaderValue() {
        return ContentEncoding.with(this.value());
    }

    private ContentEncoding createHeaderValue(final Encoding...encodings) {
        return ContentEncoding.with(Lists.of(encodings));
    }

    private List<Encoding> value() {
        return Lists.of(Encoding.GZIP);
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ContentEncoding> type() {
        return ContentEncoding.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public ContentEncoding parse(final String text) {
        return ContentEncoding.parse(text);
    }
}
