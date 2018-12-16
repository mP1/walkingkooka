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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HeaderNameTestCase;
import walkingkooka.net.header.HeaderValueConverters;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.MediaType;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

final public class HttpHeaderNameTest extends HeaderNameTestCase<HttpHeaderName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        HttpHeaderName.with("header\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        HttpHeaderName.with("header ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        HttpHeaderName.with("header\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        HttpHeaderName.with("header\u0100;");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testCustomHeaderIsContent() {
        final HttpHeaderName<?> header = HttpHeaderName.with("X-custom");
        assertEquals(header + ".isContent", false, header.isContent());
    }

    @Test
    public void testScopeRequest() {
        this.checkScope(HttpHeaderName.ACCEPT, HttpHeaderScope.REQUEST);
    }

    @Test
    public void testScopeResponse() {
        this.checkScope(HttpHeaderName.SERVER, HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testScopeRequestResponse() {
        this.checkScope(HttpHeaderName.CONTENT_LENGTH, HttpHeaderScope.REQUEST_RESPONSE);
    }

    @Test
    public void testScopeRequestUnknown() {
        this.checkScope(HttpHeaderName.with("xyz"), HttpHeaderScope.UNKNOWN);
    }

    private void checkScope(final HttpHeaderName<?> header, final HttpHeaderScope scope) {
        assertSame("scope", scope, header.httpHeaderScope());
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(HttpHeaderName.ACCEPT, HttpHeaderName.with(HttpHeaderName.ACCEPT.value()));
    }

    @Test
    public void testContentTypeConstants() {
        final List<HttpHeaderName<?>> headers = HttpHeaderName.CONSTANTS.values()
                .stream()
                .filter(h -> h.value().startsWith("content-"))
                .filter(h -> false == h.isContent())
                .collect(Collectors.toList());
        assertEquals("Several HttpHeaderName.isContent() returns false when it should return true",
                Lists.empty(),
                headers);
    }

    // headerValue.........................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderValueNullFails() {
        HttpHeaderName.ALLOW.headerValue(null);
    }

    @Test
    public void testHeaderValueScopeAccept() {
        this.headerValueAndCheck(HttpHeaderName.ACCEPT,
                MediaType.parseList("text/html, application/xhtml+xml"));
    }

    @Test
    public void testHeaderValueScopeContentLength() {
        this.headerValueAndCheck(HttpHeaderName.CONTENT_LENGTH,
                123L);
    }

    @Test
    public void testHeaderValueScopeResponseContentLengthAbsent() {
        this.headerValueAndCheck(HttpHeaderName.CONTENT_LENGTH,
                null);
    }

    @Test
    public void testHeaderValueScopeUnknown() {
        this.headerValueAndCheck(Cast.to(HttpHeaderName.with("xyz")),
                "xyz");
    }

    private <T> void headerValueAndCheck(final HttpHeaderName<T> headerName,
                                         final T headerValue) {
        assertEquals(headerName + "=" + headerValue,
                Optional.ofNullable(headerValue),
                headerName.headerValue(this.headers(headerName, headerValue)));
    }

    // headerValueOrFail..............................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderValueOrFailNullFails() {
        HttpHeaderName.ALLOW.headerValueOrFail(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testHeaderValueOrFailAbsent() {
        HttpHeaderName.ALLOW.headerValueOrFail(this.headers(HttpHeaderName.CONTENT_LENGTH, 123L));
    }

    @Test
    public void testHeaderValueOrFail() {
        this.headerValueOrFailAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L);
    }

    private <T> void headerValueOrFailAndCheck(final HttpHeaderName<T> headerName,
                                               final T headerValue) {
        assertEquals(headerName + "=" + headerValue,
                headerValue,
                headerName.headerValueOrFail(this.headers(headerName, headerValue)));
    }

    private <T> Map<HttpHeaderName<?>, Object> headers(final HttpHeaderName<T> name, final T value) {
        assertNotNull("header name", name);
        return Maps.one(name, value);
    }

    // toValue ...............................................................................................

    @Test
    public void testToValueAcceptString() {
        this.toValueAndCheck(HttpHeaderName.ACCEPT,
                "text/html, application/xhtml+xml",
                Lists.of(
                        MediaType.with("text", "html"),
                        MediaType.with("application", "xhtml+xml")));
    }

    @Test
    public void testToValueContentLengthString() {
        this.toValueAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                123L);
    }

    @Test
    public void testToValueIfRangeETag() {
        this.toValueAndCheck(HttpHeaderName.IF_RANGE,
                "W/\"etag-1234567890\"",
                IfRange.with(ETagValidator.WEAK.setValue("etag-1234567890")));
    }

    @Test
    public void testToValueIfRangeLastModified() {
        final LocalDateTime lastModified = LocalDateTime.of(2000, 12, 31, 6, 28, 29);

        this.toValueAndCheck(HttpHeaderName.IF_RANGE,
                HeaderValueConverters.localDateTime().toText(lastModified, HttpHeaderName.LAST_MODIFIED),
                IfRange.with(lastModified));
    }

    // headerText.........................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderTextNullFails() {
        HttpHeaderName.CONNECTION.headerText(null);
    }

    @Test
    public void testHeaderTextString() {
        final String text = "Close";
        this.headerTextAndCheck(HttpHeaderName.CONNECTION, text, text);
    }

    @Test
    public void testHeaderTextLong() {
        this.headerTextAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L, "123");
    }

    private <T> void headerTextAndCheck(final HttpHeaderName<T> header,
                                               final T value,
                                               final String formatted) {
        assertEquals(header + ".headerText " + CharSequences.quoteIfChars(value),
                formatted,
                header.headerText(value));
    }

    // toString.................................................................................

    @Test
    public void testToString() {
        final String name = "X-custom";
        assertEquals(name, HttpHeaderName.with(name).toString());
    }

    @Override
    protected HttpHeaderName<Object> createName(final String name) {
        return Cast.to(HttpHeaderName.with(name));
    }

    @Override
    protected String nameText() {
        return "X-Custom";
    }

    @Override
    protected Class<HttpHeaderName<?>> type() {
        return Cast.to(HttpHeaderName.class);
    }
}
