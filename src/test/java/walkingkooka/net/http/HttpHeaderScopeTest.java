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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.EnumTestCase;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class HttpHeaderScopeTest extends EnumTestCase<HttpHeaderScope> {

    // checkRequest .....................................................

    @Test
    public void testRequestAcceptEncodingRequestScope() {
        this.checkRequest(HttpHeaderName.ACCEPT_ENCODING, HttpHeaderScope.REQUEST);
    }

    @Test
    public void testRequestContentLengthRequestResponseScope() {
        this.checkRequest(HttpHeaderName.CONTENT_LENGTH, HttpHeaderScope.REQUEST_RESPONSE);
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testRequestSetCookieResponseScopeFails() {
        this.checkRequest(HttpHeaderName.SET_COOKIE, HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testRequestXCustomUnknownScope() {
        this.checkRequest(HttpHeaderName.with("x-custom"), HttpHeaderScope.UNKNOWN);
    }

    private void checkRequest(final HttpHeaderName<?> header, final HttpHeaderScope scope) {
        HttpHeaderScope.REQUEST.check(scope(header, scope));
    }

    // checkResponse .....................................................

    @Test(expected = NotAcceptableHeaderException.class)
    public void testResponseFromRequestScopeFails() {
        this.checkResponse(scope(HttpHeaderName.FROM, HttpHeaderScope.REQUEST),
                EmailAddress.with("user@example.com"));
    }

    @Test
    public void testResponseContentLengthRequestResponseScope() {
        this.checkResponse(scope(HttpHeaderName.CONTENT_LENGTH, HttpHeaderScope.REQUEST_RESPONSE),
                123L);
    }

    @Test
    public void testResponseServerResponseScope() {
        this.checkResponse(scope(HttpHeaderName.SERVER, HttpHeaderScope.RESPONSE),
                "My server");
    }

    @Test
    public void testResponseXCustomUnknownScope() {
        this.checkResponse(Cast.to(HttpHeaderName.with("x-custom")), "value456");
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testResponseCacheControlOnlyIfCachedValueRequestScope() {
        this.checkResponseCacheControl(
                scope(CacheControlDirective.ONLY_IF_CACHED, HttpHeaderScope.REQUEST));
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testResponseCacheControlMustRevalidateOnlyIfCachedValueRequestScope2() {
        this.checkResponseCacheControl(
                scope(CacheControlDirective.MUST_REVALIDATE, HttpHeaderScope.RESPONSE),
                scope(CacheControlDirective.ONLY_IF_CACHED, HttpHeaderScope.REQUEST));
    }

    @Test
    public void testResponseCacheControlMustRevalidateValueResponseScope() {
        this.checkResponseCacheControl(
                scope(CacheControlDirective.MUST_REVALIDATE, HttpHeaderScope.RESPONSE));
    }

    @Test
    public void testResponseContentEncodingGzipValueRequestResponseScope() {
        this.checkResponse(
                scope(HttpHeaderName.CONTENT_ENCODING, HttpHeaderScope.RESPONSE),
                scope(HeaderValueToken.with("gzip"), HttpHeaderScope.REQUEST_RESPONSE));
    }

    @Test
    public void testResponseCacheControlMaxAge123ValueRequestResponseScope() {
        this.checkResponseCacheControl(scope(
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                HttpHeaderScope.REQUEST_RESPONSE));
    }

    @Test
    public void testResponseContentTypeTextPlainValueRequestResponseScope2() {
        this.checkResponse(
                scope(HttpHeaderName.CONTENT_TYPE, HttpHeaderScope.REQUEST_RESPONSE),
                scope(MediaType.TEXT_PLAIN, HttpHeaderScope.REQUEST_RESPONSE));
    }

    private void checkResponseCacheControl(final CacheControlDirective<?>... directives) {
        this.checkResponse(HttpHeaderName.CACHE_CONTROL, Lists.of(directives));
    }

    private <T> void checkResponse(final HttpHeaderName<T> header, final T value) {
        HttpHeaderScope.RESPONSE.check(header, value);
    }

    private <T extends HasHttpHeaderScope> T scope(final T has, final HttpHeaderScope scope) {
        assertEquals(has + " scope", scope, has.httpHeaderScope());
        return has;
    }

    @Override
    protected Class<HttpHeaderScope> type() {
        return HttpHeaderScope.class;
    }
}
