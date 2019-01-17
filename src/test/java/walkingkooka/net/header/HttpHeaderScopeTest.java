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

package walkingkooka.net.header;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;
import java.util.Set;

public final class HttpHeaderScopeTest extends ClassTestCase<HttpHeaderScope> {

    // checkRequest .....................................................

    @Test
    public void testRequestAcceptEncoding() {
        this.checkScope(HttpHeaderName.ACCEPT_ENCODING, HttpHeaderScope.REQUEST);
    }

    @Test
    public void testRequestContentLength() {
        this.checkScope(HttpHeaderName.CONTENT_LENGTH, HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testSetCookie() {
        this.checkScope(HttpHeaderName.SET_COOKIE, HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testCustomUnknown() {
        this.checkScope(HttpHeaderName.with("x-custom"),
                HttpHeaderScope.MULTIPART,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testFrom() {
        this.checkScope(HttpHeaderName.FROM,
                EmailAddress.with("user@example.com"),
                HttpHeaderScope.REQUEST);
    }

    @Test
    public void testContentLength() {
        this.checkScope(HttpHeaderName.CONTENT_LENGTH,
                123L,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testServer() {
        this.checkScope(HttpHeaderName.SERVER,
                "My server",
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testXCustomUnknownScope() {
        this.checkScope(Cast.to(HttpHeaderName.with("x-custom")),
                "value456",
                HttpHeaderScope.MULTIPART,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testCacheControlMustRevalidate() {
        this.checkScopeCacheControlScope(CacheControlDirective.MUST_REVALIDATE,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testCacheControlOnlyIfCachedValue() {
        this.checkScopeCacheControlScope(CacheControlDirective.ONLY_IF_CACHED,
                HttpHeaderScope.REQUEST);
    }

    @Test
    public void testContentEncodingGzipValue() {
        this.checkScope(HttpHeaderName.CONTENT_ENCODING,
                TokenHeaderValue.with("gzip"),
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testCacheControlMaxAge123Value() {
        this.checkScopeCacheControlScope(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testContentTypeTextPlainValue() {
        this.checkScope(HttpHeaderName.CONTENT_TYPE,
                MediaType.TEXT_PLAIN,
                HttpHeaderScope.MULTIPART, HttpHeaderScope.REQUEST, HttpHeaderScope.RESPONSE);
    }

    private void checkScopeCacheControlScope(final CacheControlDirective<?> directive,
                                             final HttpHeaderScope... scopes) {
        this.checkScope(HttpHeaderName.CACHE_CONTROL,
                Lists.of(directive),
                scopes);
    }

    private <T> void checkScope(final HttpHeaderName<T> header,
                                final T value,
                                final HttpHeaderScope... scopes) {
        final Set<HttpHeaderScope> scopesSet = Sets.of(scopes);

        for (HttpHeaderScope scope : HttpHeaderScope.values()) {
            if(scopesSet.contains(scope)) {
                scope.check(header, value);
            } else {
                try {
                    scope.check(header, value);
                    Assert.fail(header + " check " + CharSequences.quoteIfChars(value) + " should have failed");
                } catch (final NotAcceptableHeaderException cause) {
                }
            }
        }
    }

    private void checkScope(final HttpHeaderName<?> header,
                            final HttpHeaderScope... scopes) {

        final Set<HttpHeaderScope> scopesSet = Sets.of(scopes);

        for (HttpHeaderScope scope : HttpHeaderScope.values()) {
            if(scopesSet.contains(scope)) {
                scope.check(header);
            } else {
                try {
                    scope.check(header);
                    Assert.fail(header + " checked should have failed");
                } catch (final NotAcceptableHeaderException cause) {
                }
            }
        }
    }

    @Override
    protected Class<HttpHeaderScope> type() {
        return HttpHeaderScope.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
