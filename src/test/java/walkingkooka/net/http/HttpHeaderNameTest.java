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
import walkingkooka.naming.NameTestCase;
import walkingkooka.net.media.MediaType;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

final public class HttpHeaderNameTest extends NameTestCase<HttpHeaderName<?>> {

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
        assertSame("scope", scope, header.scope());
    }

    @Test
    public void testStringHeaderValueNotString() {
        assertNotSame(HttpHeaderName.ACCEPT, HttpHeaderName.ACCEPT.stringHeaderValues());
    }

    @Test
    public void testStringHeaderValueInitiallyString() {
        assertSame(HttpHeaderName.CONNECTION, HttpHeaderName.CONNECTION.stringHeaderValues());
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(HttpHeaderName.ACCEPT, HttpHeaderName.with(HttpHeaderName.ACCEPT.value()));
    }

    // HeaderValue.........................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderValueNullRequestFails() {
        HttpHeaderName.ALLOW.headerValue((HttpRequest)null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHeaderValueScopeResponseFails() {
        HttpHeaderName.SERVER.headerValue(HttpRequests.fake());
    }

    @Test
    public void testHeaderValueScopeRequestAcceptRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.ACCEPT,
                "text/html, application/xhtml+xml",
                Optional.of(Lists.of(
                        MediaType.with("text", "html"),
                        MediaType.with("application", "xhtml+xml"))));
    }

    @Test
    public void testHeaderValueScopeRequestContentLengthRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                Optional.of(123L));
    }

    @Test
    public void testHeaderValueScopeRequestResponseContentLengthAbsentRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.CONTENT_LENGTH,
                null,
                Optional.empty());
    }

    @Test
    public void testHeaderValueScopeRequestUnknown() {
        this.headerValueRequestAndCheck(HttpHeaderName.with("xyz").stringHeaderValues(),
                "xyz",
                Optional.of("xyz"));
    }

    private <T> void headerValueRequestAndCheck(final HttpHeaderName<T> headerName,
                                                final String headerValue,
                                                final Optional<T> value) {
        assertEquals(headerName + "=" + headerValue,
                value,
                headerName.headerValue(new FakeHttpRequest() {
                    @Override
                    public Map<HttpHeaderName<?>, String> headers() {
                        return Maps.one(headerName, headerValue);
                    }
                }));
    }

    @Test(expected = NullPointerException.class)
    public void testHeaderValueNullStringFails() {
        HttpHeaderName.ALLOW.headerValue((String) null);
    }

    @Test
    public void testHeaderValueAcceptString() {
        this.headerValueStringAndCheck(HttpHeaderName.ACCEPT,
                "text/html, application/xhtml+xml",
                Lists.of(
                        MediaType.with("text", "html"),
                        MediaType.with("application", "xhtml+xml")));
    }

    @Test
    public void testHeaderValueScopeContentLengthString() {
        this.headerValueStringAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                123L);
    }

    private <T> void headerValueStringAndCheck(final HttpHeaderName<T> headerName,
                                               final String headerValue,
                                               final T value) {
        assertEquals(headerName + "=" + headerValue,
                value,
                headerName.headerValue(headerValue));
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

    @Test(expected = IllegalArgumentException.class)
    public void testAddHeaderScopeRequestFails() {
        HttpHeaderName.USER_AGENT.addHeaderValue("xyz", HttpResponses.fake());
    }

    @Test
    public void testAddHeaderValueScopeResponse() {
        this.addHeaderValueAndCheck(HttpHeaderName.SERVER, "Server xyz");
    }

    @Test
    public void testAddHeaderValueScopeRequestResponse() {
        this.addHeaderValueAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L);
    }

    @Test
    public void testAddHeaderValueScopeUnknown() {
        this.addHeaderValueAndCheck(HttpHeaderName.with("xyz").stringHeaderValues(), "abc");
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

    // headerValueFormat.........................................................................

    @Test(expected = NullPointerException.class)
    public void testHeaderValueFormatNullFails() {
        HttpHeaderName.CONNECTION.headerValueFormat(null);
    }

    @Test
    public void testHeaderValueFormatString() {
        final String text = "Close";
        this.headerValueFormatAndCheck(HttpHeaderName.CONNECTION, text, text);
    }

    @Test
    public void testHeaderValueFormatLong() {
        this.headerValueFormatAndCheck(HttpHeaderName.CONTENT_LENGTH, 123L, "123");
    }

    private <T> void headerValueFormatAndCheck(final HttpHeaderName<T> header,
                                               final T value,
                                               final String formatted) {
        assertEquals(header + ".headerValueFormat " + CharSequences.quoteIfChars(value),
                formatted,
                header.headerValueFormat(value));
    }

    // toString.................................................................................

    @Test
    public void testToString() {
        final String name = "X-custom";
        assertEquals(name, HttpHeaderName.with(name).toString());
    }

    @Override
    protected HttpHeaderName createName(final String name) {
        return HttpHeaderName.with(name);
    }

    @Override
    protected Class<HttpHeaderName<?>> type() {
        return Cast.to(HttpHeaderName.class);
    }
}
