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
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentEncodingTest extends HeaderValueTestCase<ContentEncoding>
        implements ComparableTesting<ContentEncoding>,
        ParseStringTesting<List<ContentEncoding>> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentEncoding.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentEncoding.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            ContentEncoding.with("\u001f");
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue(), this.value());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(ContentEncoding.with(text), text);
    }

    private void checkValue(final ContentEncoding contentEncoding,
                            final String value) {
        assertEquals(value, contentEncoding.value(), "value");
    }

    // constants .........................................................................

    @Test
    public void testWithExistingGzipConstant() {
        final ContentEncoding constant = ContentEncoding.GZIP;
        assertSame(constant, ContentEncoding.with("gzip"));
    }

    @Test
    public void testWithExistingBrConstant() {
        final ContentEncoding constant = ContentEncoding.BR;
        assertSame(constant, ContentEncoding.with("BR"));
    }

    @Test
    public void testWithExistingCompressConstantCaseInsignificant() {
        final ContentEncoding constant = ContentEncoding.COMPRESS;
        assertSame(constant, ContentEncoding.with("compRESS"));
    }

    @Test
    public void testParse() {
        this.parseAndCheck("gzip, deflate, br",
                Lists.of(ContentEncoding.with("gzip"),
                        ContentEncoding.with("deflate"),
                        ContentEncoding.with("br")));
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("gzip,  deflate,  br",
                Lists.of(ContentEncoding.with("gzip"),
                        ContentEncoding.with("deflate"),
                        ContentEncoding.with("br")));
    }

    @Test
    public void testHeaderText() {
        final String text = "compress";
        this.toHeaderTextAndCheck(ContentEncoding.with(text), text);
    }

    @Test
    public void testHeaderText2() {
        final String text = "identity";
        this.toHeaderTextAndCheck(ContentEncoding.with(text), text);
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ContentEncoding.with("different"));
    }

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(ContentEncoding.with("deflate"), ContentEncoding.with("gzip"));
    }

    @Test
    public void testCompareLessCaseInsignificant() {
        this.compareToAndCheckLess(ContentEncoding.with("deflate"), ContentEncoding.with("GZIP"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), this.value());
    }

    @Test
    public void testToString2() {
        final String text = "compress";
        this.toStringAndCheck(ContentEncoding.with(text), text);
    }

    @Override
    public ContentEncoding createHeaderValue() {
        return this.createHeaderValue(this.value());
    }

    private ContentEncoding createHeaderValue(final String value) {
        return ContentEncoding.with(value);
    }

    private String value() {
        return "gzip";
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<ContentEncoding> type() {
        return ContentEncoding.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ComparableTesting................................................................................................

    @Override
    public ContentEncoding createObject() {
        return this.createComparable();
    }

    @Override
    public ContentEncoding createComparable() {
        return ContentEncoding.with(this.value());
    }

    // ParseStringTesting...............................................................................................

    @Override
    public List<ContentEncoding> parse(final String text) {
        return ContentEncoding.parse(text);
    }
}
