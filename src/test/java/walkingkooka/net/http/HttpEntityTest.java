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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.header.CharsetHeaderValue;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HttpEntityTest extends ClassTestCase<HttpEntity> {

    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 26L;

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.one(HEADER, HEADER_VALUE);
    private final static Map<HttpHeaderName<?>, Object> INVALID_HEADERS = Maps.one(HttpHeaderName.SERVER, 999L);
    private final static byte[] BODY = "abcdefghijklmnopqrstuvwxyz".getBytes(Charset.forName("utf8"));

    private final static HttpHeaderName<?> DIFFERENT_HEADER = HttpHeaderName.SERVER;

    // with ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullHeadersFails() {
        HttpEntity.with(null, BODY);
    }

    @Test(expected = HeaderValueException.class)
    public void testWithInvalidHeaderFails() {
        HttpEntity.with(INVALID_HEADERS, BODY);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullBodyFails() {
        HttpEntity.with(HEADERS, null);
    }

    @Test
    public void testWith() {
        this.check(HttpEntity.with(HEADERS, BODY));
    }

    // setHeaders ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetHeadersNullFails() {
        this.create().setHeaders(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testSetHeaderInvalidFails() {
        this.create().setHeaders(INVALID_HEADERS);
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

    @Test(expected = NullPointerException.class)
    public void testAddHeaderNullNameFails() {
        this.create().addHeader(null, "*value*");
    }

    @Test(expected = NullPointerException.class)
    public void testAddHeaderNullValueFails() {
        this.create().addHeader(HttpHeaderName.SERVER, null);
    }

    @Test(expected = HeaderValueException.class)
    public void testAddHeaderInvalidValueFails() {
        this.create().addHeader(Cast.to(HttpHeaderName.CONTENT_LENGTH), "*invalid*");
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

    @Test(expected = NullPointerException.class)
    public void testRemoveHeaderNullFails() {
        this.create().removeHeader(null);
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

    @Test(expected = NullPointerException.class)
    public void testSetBodyNullFails() {
        this.create().setBody(null);
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
    public void testToString() {
        assertEquals("Content-Length: 26\r\n\r\n4142",
                HttpEntity.with(HEADERS, new byte[]{'A', 'B'})
                        .toString());
    }

    @Test
    public void testToStringMultipleHeaders() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, 257L);
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);
        headers.put(HttpHeaderName.SERVER, "Server 123");

        assertEquals("Content-Length: 257\r\nContent-Type: text/plain\r\nServer: Server 123\r\n\r\n4142",
                HttpEntity.with(headers, new byte[]{'A', 'B'})
                        .toString());
    }

    // factory text............................................................................................

    @Test(expected = NullPointerException.class)
    public void testTextContentTypeNullFails() {
        HttpEntity.text(null,
                this.acceptCharset(),
                this.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextAcceptCharsetNullFails() {
        HttpEntity.text(this.contentType(),
                null,
                this.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextBodyNullFails() {
        HttpEntity.text(this.contentType(),
                this.acceptCharset(),
                null);
    }

    @Test(expected = HeaderValueException.class)
    public void testTextContentTypeMissingCharsetFails() {
        HttpEntity.text(MediaType.TEXT_PLAIN,
                this.acceptCharset(),
                this.text());
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testTextContentTypeUnsupportedCharsetFails() {
        final Charset utf8 = Charset.forName("utf8");

        final Charset unsupported = Charset.availableCharsets()
                .values()
                .stream()
                .filter(c -> !utf8.contains(c))
                .findFirst()
                .get();

        HttpEntity.text(this.contentType(CharsetName.with(unsupported.name())),
                this.acceptCharset(),
                this.text());
    }

    @Test
    public void testTextContentType() {
        final MediaType contentType = this.contentType();
        final String text = this.text();
        final byte[] body = text.getBytes(CharsetName.UTF_8.charset().get());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(body.length));
        headers.put(HttpHeaderName.CONTENT_TYPE, contentType);

        this.check(HttpEntity.text(contentType, this.acceptCharset(), text),
                headers,
                body);
    }

    private MediaType contentType() {
        return this.contentType(CharsetName.UTF_8);
    }

    private MediaType contentType(final CharsetName charsetName) {
        return MediaType.TEXT_PLAIN.setCharset(charsetName);
    }

    private List<CharsetHeaderValue> acceptCharset() {
        return CharsetHeaderValue.parse("utf-8;");
    }

    private String text() {
        return "abc123";
    }

    // extractRange................................................................................................

    @Test(expected = NullPointerException.class)
    public void testExtractRangeRangeNullFails() {
        this.create().extractRange(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractRangeRangeNegativeLowerBoundsFails() {
        this.create().extractRange(Range.greaterThanEquals(-1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractRangeRangeExclusiveLowerBoundsFails() {
        this.create().extractRange(Range.greaterThan(0L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractRangeRangeExclusiveUpperBoundsFails() {
        this.create().extractRange(Range.greaterThan(0L).and(Range.lessThan(1L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractRangeUpperBoundsFails() {
        this.create().extractRange(Range.greaterThan(0L).and(Range.lessThan(0L + BODY.length)));
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
        assertEquals(entity + " extractRange " + range + " failed",
                HttpEntity.with(HEADERS, expected),
                entity.extractRange(range));
    }

    // helpers................................................................................................

    private HttpEntity create() {
        return HttpEntity.with(HEADERS, BODY);
    }

    private void check(final HttpEntity entity) {
        check(entity, HEADERS, BODY);
    }

    private void check(final HttpEntity entity, final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        assertEquals("headers", headers, entity.headers());
        assertArrayEquals("body", body, entity.body());
    }

    @Override
    protected Class<HttpEntity> type() {
        return HttpEntity.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
