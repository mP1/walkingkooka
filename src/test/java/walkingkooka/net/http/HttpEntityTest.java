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
import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.ContentRange;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpEntityTest implements ClassTesting2<HttpEntity>,
        HashCodeEqualsDefinedTesting<HttpEntity>,
        ToStringTesting<HttpEntity> {

    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 26L;

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.of(HEADER, HEADER_VALUE);
    private final static Map<HttpHeaderName<?>, Object> INVALID_HEADERS = Maps.of(HttpHeaderName.SERVER, 999L);
    
    private final static HttpHeaderName<?> DIFFERENT_HEADER = HttpHeaderName.SERVER;

    // with ....................................................................................................

    @Test
    public void testWithNullHeadersFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpEntity.with(null, this.body());
        });
    }

    @Test
    public void testWithInvalidHeaderFails() {
        assertThrows(HeaderValueException.class, () -> {
            HttpEntity.with(INVALID_HEADERS, this.body());
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
        this.check(HttpEntity.with(HEADERS, this.body()));
    }

    // setHeaders ....................................................................................................

    @Test
    public void testSetHeadersNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHttpEntity().setHeaders(null);
        });
    }

    @Test
    public void testSetHeaderInvalidFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHttpEntity().setHeaders(INVALID_HEADERS);
        });
    }

    @Test
    public void testSetHeadersSame() {
        final HttpEntity entity = this.createHttpEntity();
        assertSame(entity, entity.setHeaders(HEADERS));
    }

    @Test
    public void testSetHeadersDifferent() {
        final HttpEntity entity = this.createHttpEntity();
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 456L);
        final HttpEntity different = entity.setHeaders(headers);
        this.check(different, headers, this.body());
    }

    // addHeader ....................................................................................................

    @Test
    public void testAddHeaderNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHttpEntity().addHeader(null, "*value*");
        });
    }

    @Test
    public void testAddHeaderNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHttpEntity().addHeader(HttpHeaderName.SERVER, null);
        });
    }

    @Test
    public void testAddHeaderInvalidValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHttpEntity().addHeader(Cast.to(HttpHeaderName.CONTENT_LENGTH), "*invalid*");
        });
    }

    @Test
    public void testAddHeaderExisting() {
        final HttpEntity entity = this.createHttpEntity();
        assertSame(entity, entity.addHeader(HEADER, HEADER_VALUE));
    }

    @Test
    public void testAddHeaderReplaceValue() {
        final HttpEntity entity = this.createHttpEntity();

        final Long headerValue = 456L;

        this.check(entity.addHeader(HEADER, headerValue),
                Maps.of(HEADER, headerValue),
                this.body());
    }

    @Test
    public void testAddHeader() {
        final HttpEntity entity = this.createHttpEntity();

        final HttpHeaderName<String> header = HttpHeaderName.SERVER;
        final String headerValue = "*Server*";

        final HttpEntity different = entity.addHeader(header, headerValue);

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.putAll(HEADERS);
        headers.put(header, headerValue);

        this.check(different, headers, this.body());
    }

    // removeHeader ....................................................................................................

    @Test
    public void testRemoveHeaderNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHttpEntity().removeHeader(null);
        });
    }

    @Test
    public void testRemoveHeaderSame() {
        final HttpEntity entity = this.createHttpEntity();
        assertSame(entity, entity.removeHeader(DIFFERENT_HEADER));
    }

    @Test
    public void testRemoveHeader() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.putAll(HEADERS);
        headers.put(DIFFERENT_HEADER, "Server123");

        final HttpEntity entity = HttpEntity.with(headers, this.body());
        this.check(entity.removeHeader(DIFFERENT_HEADER),
                HEADERS,
                this.body());
    }

    // setBody ....................................................................................................

    @Test
    public void testSetBodyNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHttpEntity().setBody(null);
        });
    }

    @Test
    public void testSetBodySame() {
        final HttpEntity entity = this.createHttpEntity();
        assertSame(entity, entity.setBody(this.body()));
    }

    @Test
    public void testSetBodyDifferent() {
        final HttpEntity entity = this.createHttpEntity();
        final Binary body = Binary.with(new byte[456]);
        final HttpEntity different = entity.setBody(body);
        this.check(different, HEADERS, body);
    }

    // headersAndBodyBytes ....................................................................................................

    @Test
    public void testHeadersAndBodyBytesMissingContentTypeContentLengthOrContentRangeFails() {
        this.headersAndBodyBytesFail(HttpEntity.with(HttpEntity.NO_HEADERS, HttpEntity.NO_BODY),
                "Headers missing Content-Type in {}");
    }

    @Test
    public void testHeadersAndBodyBytesMissingContentTypeFails() {
        this.headersAndBodyBytesFail(HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_LENGTH, 0L), HttpEntity.NO_BODY),
                "Headers missing Content-Type in {Content-Length=0}");
    }

    @Test
    public void testHeadersAndBodyBytesMissingContentLengthOrContentRangeFails() {
        this.headersAndBodyBytesFail(HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN), HttpEntity.NO_BODY),
                "Headers missing Content-Length or Content-Range in {Content-Type=text/plain}");
    }

    @Test
    public void testHeadersAndBodyBytesContentLengthBodyLengthMismatchFails() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.CONTENT_LENGTH, 99L);

        this.headersAndBodyBytesFail(this.createHttpEntity(headers, new byte[1]),
                "Content-Length: 99 & actual body length 1 mismatch.");
    }

    @Test
    public void testHeadersAndBodyBytesContentRangeBodyLengthMismatchFails() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.CONTENT_RANGE, ContentRange.parse("bytes 0-99/888"));

        this.headersAndBodyBytesFail(this.createHttpEntity(headers, new byte[1]),
                "Content-Range: bytes 0-99/888 & actual body length 1 mismatch.");
    }

    private void headersAndBodyBytesFail(final HttpEntity entity, final String message) {
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            entity.headersAndBodyBytes();
        });
        assertEquals(message,
                thrown.getMessage(),
                () -> entity.toString());
    }

    @Test
    public void testHeadersAndBodyBytesContentLength() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.CONTENT_LENGTH, 10L);

        this.headersAndBodyBytesAndCheck(headers, "BODY123456", "Content-Type: text/plain\r\nContent-Length: 10\r\n\r\nBODY123456");
    }

    @Test
    public void testHeadersAndBodyBytesContentRange() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.CONTENT_RANGE, ContentRange.parse("bytes 0-9/888"));

        this.headersAndBodyBytesAndCheck(headers, "BODY123456", "Content-Type: text/plain\r\nContent-Range: bytes 0-9/888\r\n\r\nBODY123456");
    }

    @Test
    public void testHeadersAndBodyBytesContentRangeWildcard() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.CONTENT_RANGE, ContentRange.parse("bytes */888"));

        this.headersAndBodyBytesAndCheck(headers, "BODY123456", "Content-Type: text/plain\r\nContent-Range: bytes */888\r\n\r\nBODY123456");
    }

    private void headersAndBodyBytesAndCheck(final Map<HttpHeaderName<?>, Object> headers,
                                             final String body,
                                             final String bytes) throws Exception {
        headersAndBodyBytesAndCheck(HttpEntity.with(headers, Binary.with(body.getBytes("UTF-8"))),
                bytes);
    }

    private void headersAndBodyBytesAndCheck(final HttpEntity entity, final String bytes) throws Exception {
        assertEquals(bytes, new String(entity.headersAndBodyBytes(), "UTF-8"));
    }

    // toString ....................................................................................................

    @Test
    public void testToStringText() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L,
                HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN.setCharset(CharsetName.UTF_8),
                HttpHeaderName.SERVER, "Server 123");

        this.toStringAndCheck(this.createHttpEntity(headers, new byte[]{'A', 'B', '\n', 'C'}),
                "Content-Length: 257\r\nContent-Type: text/plain; charset=UTF-8\r\nServer: Server 123\r\n\r\nAB\nC");
    }

    @Test
    public void testToStringBinary() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "a";
        this.toStringAndCheck(this.createHttpEntity(headers, letters),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 61                                              a               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryUnprintable() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "\0";
        this.toStringAndCheck(this.createHttpEntity(headers, letters),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 00                                              .               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryMultiLine() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "abcdefghijklmnopq";
        this.toStringAndCheck(this.createHttpEntity(headers, letters),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 61 62 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 abcdefghijklmnop" + LineEnding.SYSTEM +
                        "00000010 71                                              q               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringBinaryMultiLine2() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L);

        final String letters = "\n\0cdefghijklmnopq";
        this.toStringAndCheck(this.createHttpEntity(headers, letters),
                "Content-Length: 257\r\n\r\n" +
                        "00000000 0a 00 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 ..cdefghijklmnop" + LineEnding.SYSTEM +
                        "00000010 71                                              q               " + LineEnding.SYSTEM);
    }

    @Test
    public void testToStringMultipleHeadersBinary() throws Exception {
        final Map<HttpHeaderName<?>, Object> headers = Maps.of(HttpHeaderName.CONTENT_LENGTH, 257L,
                HttpHeaderName.CONTENT_TYPE, MediaType.BINARY,
                HttpHeaderName.SERVER, "Server 123");

        final String letters = "\n\0cdefghijklmnopq";
        this.toStringAndCheck(this.createHttpEntity(headers, letters),
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
    public void testExtractRangeSameWildcard() {
        final HttpEntity entity = this.createHttpEntity();
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
                Binary.with(new byte[]{'w', 'x', 'y', 'z'}));
    }

    private void extractRangeAndCheck(final long lower,
                                      final long upper,
                                      final byte[] expected) {
        this.extractRangeAndCheck(Range.greaterThanEquals(lower).and(Range.lessThanEquals(upper)),
                Binary.with(expected));
    }

    private void extractRangeAndCheck(final Range<Long> range, final Binary expected) {
        final HttpEntity entity = this.createHttpEntity();
        assertEquals(HttpEntity.with(HEADERS, expected),
                entity.extractRange(range),
                () -> entity + " extractRange " + range + " failed");
    }

    @Test
    public void testEqualsDifferentHeaders() {
        this.checkNotEquals(HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_LENGTH, 456L), this.body()));
    }

    @Test
    public void testEqualsDifferentBody() {
        this.checkNotEquals(HttpEntity.with(HEADERS, Binary.with(new byte[456])));
    }

    // helpers................................................................................................

    private HttpEntity createHttpEntity() {
        return HttpEntity.with(HEADERS, this.body());
    }

    private HttpEntity createHttpEntity(final Map<HttpHeaderName<?>, Object> headers,
                                        final String body) throws UnsupportedEncodingException {
        return this.createHttpEntity(headers, body.getBytes("UTF-8"));
    }

    private HttpEntity createHttpEntity(final Map<HttpHeaderName<?>, Object> headers,
                                        final byte[] body) {
        return HttpEntity.with(headers, Binary.with(body));
    }

    private Binary body() {
        return Binary.with("abcdefghijklmnopqrstuvwxyz".getBytes(Charset.forName("utf8")));
    }

    private void check(final HttpEntity entity) {
        check(entity, HEADERS, this.body());
    }

    private void check(final HttpEntity entity,
                       final Map<HttpHeaderName<?>, Object> headers,
                       final byte[] body) {
        this.check(entity, headers, Binary.with(body));
    }

    private void check(final HttpEntity entity,
                       final Map<HttpHeaderName<?>, Object> headers,
                       final Binary body) {
        assertEquals(headers, entity.headers(), "headers");
        assertEquals(body, entity.body(), "body");
    }

    @Override
    public Class<HttpEntity> type() {
        return HttpEntity.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public HttpEntity createObject() {
        return HttpEntity.with(HEADERS, this.body());
    }
}
