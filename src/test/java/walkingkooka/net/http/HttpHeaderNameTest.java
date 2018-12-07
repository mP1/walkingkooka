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
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.server.FakeHttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
                headerName.headerValue(this.hasHeaders(headerName, headerValue)));
    }

    // headerValueOrFail..............................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderValueOrFailNullFails() {
        HttpHeaderName.ALLOW.headerValueOrFail(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testHeaderValueOrFailAbsent() {
        HttpHeaderName.ALLOW.headerValueOrFail(this.hasHeaders(HttpHeaderName.CONTENT_LENGTH, 123L));
    }

    @Test
    public void testHeaderValueOrFail() {
        this.headerValueOrFailAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L);
    }

    private <T> void headerValueOrFailAndCheck(final HttpHeaderName<T> headerName,
                                               final T headerValue) {
        assertEquals(headerName + "=" + headerValue,
                headerValue,
                headerName.headerValueOrFail(this.hasHeaders(headerName, headerValue)));
    }

    private <T> HasHeaders hasHeaders(final HttpHeaderName<T> name, final T value) {
        return new HasHeaders() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.one(name, value);
            }
        };
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
    public void testToValueScopeContentLengthString() {
        this.toValueAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                123L);
    }

    // addHeaderValue.........................................................................

    @Test(expected = NullPointerException.class)
    public void testAddHeaderValueNullValueFails() {
        HttpHeaderName.CONNECTION.addHeaderValue(null, HttpResponses.fake());
    }

    @Test(expected = NullPointerException.class)
    public void testAddHeaderValueNullResponseFails() {
        HttpHeaderName.CONNECTION.addHeaderValue("*", null);
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testAddHeaderScopeFails() {
        HttpHeaderName.USER_AGENT.addHeaderValue("xyz", HttpResponses.fake());
    }

    @Test
    public void testAddHeaderValueScopeResponse() {
        this.addHeaderValueAndCheck(HttpHeaderName.SERVER, "Server xyz");
    }

    @Test
    public void testAddHeaderValueScopeRequest() {
        this.addHeaderValueAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L);
    }

    @Test
    public void testAddHeaderValueScopeUnknown() {
        this.addHeaderValueAndCheck(Cast.to(HttpHeaderName.with("xyz")), "abc");
    }

    private <T> void addHeaderValueAndCheck(final HttpHeaderName<T> header, final T value) {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();

        header.addHeaderValue(value,
                new FakeHttpResponse() {
                    @Override
                    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
                        headers.put(name, value);
                    }
                });
        assertEquals("set headers", Maps.one(header, value), headers);
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
