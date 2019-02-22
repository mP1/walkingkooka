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

package walkingkooka.net.http;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;

import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpEntityTest implements ClassTesting2<HttpEntity>,
        HashCodeEqualsDefinedTesting<HttpEntity>,
        ToStringTesting<HttpEntity> {

    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 26L;

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.one(HEADER, HEADER_VALUE);
    private final static Map<HttpHeaderName<?>, Object> INVALID_HEADERS = Maps.one(HttpHeaderName.SERVER, 999L);
    private final static byte[] BODY = "abcdefghijklmnopqrstuvwxyz".getBytes(Charset.forName("utf8"));

    private final static HttpHeaderName<?> DIFFERENT_HEADER = HttpHeaderName.SERVER;

    // with ....................................................................................................

    @Test
    public void testWithNullHeadersFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpEntity.with(null, BODY);
        });
    }

    @Test
    public void testWithInvalidHeaderFails() {
        assertThrows(HeaderValueException.class, () -> {
            HttpEntity.with(INVALID_HEADERS, BODY);
        });
    }

    @Test
    public void testWithNullBodyFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpEntity.with(HEADERS, null);
        });
    }

    @Test
    public void testWith() {
        this.check(HttpEntity.with(HEADERS, BODY));
    }

    // setHeaders ....................................................................................................

    @Test
    public void testSetHeadersNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().setHeaders(null);
        });
    }

    @Test
    public void testSetHeaderInvalidFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.create().setHeaders(INVALID_HEADERS);
        });
    }

    @Test
    public void testSetHeadersSame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.setHeaders(HEADERS));
    }

    @Test
    public void testSetHeadersDifferent() {
        final HttpEntity entity = this.create();
        final Map<HttpHeaderName<?>, Object> headers = Maps.one(HttpHeaderName.CONTENT_LENGTH, 456L);
        final HttpEntity different = entity.setHeaders(headers);
        this.check(different, headers, BODY);
    }

    // addHeader ....................................................................................................

    @Test
    public void testAddHeaderNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().addHeader(null, "*value*");
        });
    }

    @Test
    public void testAddHeaderNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().addHeader(HttpHeaderName.SERVER, null);
        });
    }

    @Test
    public void testAddHeaderInvalidValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.create().addHeader(Cast.to(HttpHeaderName.CONTENT_LENGTH), "*invalid*");
        });
    }

    @Test
    public void testAddHeaderExisting() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.addHeader(HEADER, HEADER_VALUE));
    }

    @Test
    public void testAddHeaderReplaceValue() {
        final HttpEntity entity = this.create();

        final Long headerValue = 456L;

        this.check(entity.addHeader(HEADER, headerValue),
                Maps.one(HEADER, headerValue),
                BODY);
    }

    @Test
    public void testAddHeader() {
        final HttpEntity entity = this.create();

        final HttpHeaderName<String> header = HttpHeaderName.SERVER;
        final String headerValue = "*Server*";

        final HttpEntity different = entity.addHeader(header, headerValue);

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.putAll(HEADERS);
        headers.put(header, headerValue);

        this.check(different, headers, BODY);
    }

    // removeHeader ....................................................................................................

    @Test
    public void testRemoveHeaderNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().removeHeader(null);
        });
    }

    @Test
    public void testRemoveHeaderSame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.removeHeader(DIFFERENT_HEADER));
    }

    @Test
    public void testRemoveHeader() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.putAll(HEADERS);
        headers.put(DIFFERENT_HEADER, "Server123");

        final HttpEntity entity = HttpEntity.with(headers, BODY);
        this.check(entity.removeHeader(DIFFERENT_HEADER),
                HEADERS,
                BODY);
    }

    // setBody ....................................................................................................

    @Test
    public void testSetBodyNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().setBody(null);
        });
    }

    @Test
    public void testSetBodySame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.setBody(BODY));
    }

    @Test
    public void testSetBodyDifferent() {
        final HttpEntity entity = this.create();
        final byte[] body = new byte[456];
        final HttpEntity different = entity.setBody(body);
        this.check(different, HEADERS, body);
    }

    // toString ....................................................................................................

    @Test
    public void testToStringText() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN.setCharset(CharsetName.UTF_8));
        headers.put(HttpHeaderName.SERVER, "Server 123");

        this.toStringAndCheck(HttpEntity.with(headers, new byte[]{'A', 'B', '\n', 'C'}),
                "Content-Length: 257\r\nContent-Type: text/plain; charset=UTF-8\r\nServer: Server 123\r\n\r\nAB\nC");
    }

    @Test
    public void testToStringBinary() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "a";
        this.toStringAndCheck(HttpEntity.with(headers, letters.getBytes("utf-8")),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 61                                              a               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryUnprintable() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "\0";
        this.toStringAndCheck(HttpEntity.with(headers, letters.getBytes("utf-8")),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 00                                              .               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryMultiLine() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "abcdefghijklmnopq";
        this.toStringAndCheck(HttpEntity.with(headers, letters.getBytes("utf-8")),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 61 62 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 abcdefghijklmnop" + LineEnding.SYSTEM +
                        "00000010 71                                              q               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryMultiLine2() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "\n\0cdefghijklmnopq";
        this.toStringAndCheck(HttpEntity.with(headers, letters.getBytes("utf-8")),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 0a 00 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 ..cdefghijklmnop" + LineEnding.SYSTEM +
                        "00000010 71                                              q               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringMultipleHeadersBinary() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.BINARY);
        headers.put(HttpHeaderName.SERVER, "Server 123");

        final String letters = "\n\0cdefghijklmnopq";
        this.toStringAndCheck(HttpEntity.with(headers, letters.getBytes("utf-8")),
                "Content-Length: 257\r\n" +
                        "Content-Type: application/octet-stream\r\n" +
                        "Server: Server 123\r\n\r\n" +
                        "00000000 0a 00 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 ..cdefghijklmnop" + LineEnding.SYSTEM +
                        "00000010 71                                              q               " + LineEnding.SYSTEM);
    }

    // factory text............................................................................................

    @Test
    public void testTextContentTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpEntity.text(null,
                    this.text());
        });
    }

    @Test
    public void testTextBodyNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpEntity.text(this.contentType(),
                    null);
        });
    }

    @Test
    public void testTextContentTypeMissingCharsetFails() {
        assertThrows(HeaderValueException.class, () -> {
            HttpEntity.text(MediaType.TEXT_PLAIN,
                    this.text());
        });
    }

    @Test
    public void testTextContentTypeUnsupportedCharsetFails() {
        assertThrows(NotAcceptableHeaderException.class, () -> {
            HttpEntity.text(this.contentType(CharsetName.with("unsupported")),
                    this.text());
        });
    }

    @Test
    public void testTextContentType() {
        final MediaType contentType = this.contentType();
        final String text = this.text();
        final byte[] body = text.getBytes(CharsetName.UTF_8.charset().get());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(body.length));
        headers.put(HttpHeaderName.CONTENT_TYPE, contentType);

        this.check(HttpEntity.text(contentType, text),
                headers,
                body);
    }

    private MediaType contentType() {
        return this.contentType(CharsetName.UTF_8);
    }

    private MediaType contentType(final CharsetName charsetName) {
        return MediaType.TEXT_PLAIN.setCharset(charsetName);
    }

    private String text() {
        return "abc123";
    }

    // extractRange................................................................................................

    @Test
    public void testExtractRangeRangeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.create().extractRange(null);
        });
    }

    @Test
    public void testExtractRangeRangeNegativeLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().extractRange(Range.greaterThanEquals(-1L));
        });
    }

    @Test
    public void testExtractRangeRangeExclusiveLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().extractRange(Range.greaterThan(0L));
        });
    }

    @Test
    public void testExtractRangeRangeExclusiveUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().extractRange(Range.greaterThan(0L).and(Range.lessThan(1L)));
        });
    }

    @Test
    public void testExtractRangeUpperBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.create().extractRange(Range.greaterThan(0L).and(Range.lessThan(0L + BODY.length)));
        });
    }

    @Test
    public void testExtractRangeSame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.extractRange(Range.greaterThanEquals(0L).and(Range.lessThanEquals(0L + BODY.length -1))));
    }

    @Test
    public void testExtractRangeSameUpperWildcard() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.extractRange(Range.greaterThanEquals(0L).and(Range.all())));
    }

    @Test
    public void testExtractRangeSameWildcard() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.extractRange(Range.all()));
    }

    @Test
    public void testExtractRange() {
        this.extractRangeAndCheck(0,
                0,
                new byte[]{'a'});
    }

    @Test
    public void testExtractRange2() {
        this.extractRangeAndCheck(1,
                2,
                new byte[]{'b', 'c'});
    }

    @Test
    public void testExtractRange3() {
        this.extractRangeAndCheck(22,
                25,
                new byte[]{'w', 'x', 'y', 'z'});
    }

    @Test
    public void testExtractRange4() {
        this.extractRangeAndCheck(Range.greaterThanEquals(22L),
                new byte[]{'w', 'x', 'y', 'z'});
    }

    private void extractRangeAndCheck(final long lower, final long upper, final byte[] expected) {
        this.extractRangeAndCheck(Range.greaterThanEquals(lower).and(Range.lessThanEquals(upper)),
                expected);
    }

    private void extractRangeAndCheck(final Range<Long> range, final byte[] expected) {
        final HttpEntity entity = this.create();
        assertEquals(HttpEntity.with(HEADERS, expected),
                entity.extractRange(range),
                ()-> entity + " extractRange " + range + " failed");
    }

    @Test
    public void testEqualsDifferentHeaders() {
        this.checkNotEquals(HttpEntity.with(Maps.one(HttpHeaderName.CONTENT_LENGTH, 456L), BODY));
    }

    @Test
    public void testEqualsDifferentBody() {
        this.checkNotEquals(HttpEntity.with(HEADERS, new byte[456]));
    }

    // helpers................................................................................................

    private HttpEntity create() {
        return HttpEntity.with(HEADERS, BODY);
    }

    private void check(final HttpEntity entity) {
        check(entity, HEADERS, BODY);
    }

    private void check(final HttpEntity entity, final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        assertEquals(headers, entity.headers(), "headers");
        assertArrayEquals(body, entity.body(), "body");
    }

    @Override
    public Class<HttpEntity> type() {
        return HttpEntity.class;
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public HttpEntity createObject() {
        return HttpEntity.with(HEADERS, BODY);
    }
}
