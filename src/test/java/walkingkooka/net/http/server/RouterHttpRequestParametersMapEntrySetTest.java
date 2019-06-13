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

package walkingkooka.net.http.server;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SetTesting;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.Cookie;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouterHttpRequestParametersMapEntrySetTest implements ClassTesting2<RouterHttpRequestParametersMapEntrySet>,
        SetTesting<RouterHttpRequestParametersMapEntrySet,
                Entry<HttpRequestAttribute<?>, Object>> {

    private final static HttpTransport TRANSPORT = HttpTransport.SECURED;
    private final static HttpMethod METHOD = HttpMethod.with("CUSTOMHTTPMETHOD");
    private final static HttpProtocolVersion PROTOCOL = HttpProtocolVersion.VERSION_1_1;
    private final static RelativeUrl URL = Url.parseRelative("/path1/file2.html?parameter1=parameter-value-1&parameter-2=parameter-value-2");
    private final static List<ClientCookie> COOKIES = Cookie.parseClientHeader("cookie1=cookievalue1;cookie2=cookievalue2");

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.of(HttpHeaderName.CONTENT_LENGTH, "1",
            HttpHeaderName.COOKIE, COOKIES);

    // tests............................................................................................................

    @Test
    public void testAddFails() {
        this.addFails(this.createSet(),
                Maps.entry(HttpRequestAttributes.TRANSPORT, HttpTransport.SECURED));
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createSet(),
                Maps.entry(HttpRequestAttributes.TRANSPORT, HttpTransport.SECURED));
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createSet(), 3 + 3 + 2 + 2 + HEADERS.size() + COOKIES.size());
    }

    @Test
    public void testIteratorWithParametersHeadersAndCookies() {
        this.iteratorAndCheck(TRANSPORT, METHOD, PROTOCOL, URL, HEADERS);
    }

    @Test
    public void testIteratorHeadersAndCookies() {
        this.iteratorAndCheck(TRANSPORT, METHOD, PROTOCOL, "/path1/file2.html", HEADERS);
    }

    @Test
    public void testIteratorNoPath() {
        this.iteratorAndCheck(TRANSPORT, METHOD, PROTOCOL, "/", HEADERS);
    }

    @Test
    public void testIteratorNoPath2() {
        this.iteratorAndCheck(TRANSPORT, METHOD, PROTOCOL, "", HEADERS);
    }

    @Test
    public void testIteratorNoHeaders() {
        this.iteratorAndCheck(TRANSPORT, METHOD, PROTOCOL, URL, HttpRequest.NO_HEADERS);
    }

    private void iteratorAndCheck(final HttpTransport transport,
                                  final HttpMethod method,
                                  final HttpProtocolVersion version,
                                  final String url,
                                  final Map<HttpHeaderName<?>, Object> headers) {
        this.iteratorAndCheck(transport, method, version, Url.parseRelative(url), headers);
    }

    private void iteratorAndCheck(final HttpTransport transport,
                                  final HttpMethod method,
                                  final HttpProtocolVersion version,
                                  final RelativeUrl url,
                                  final Map<HttpHeaderName<?>, Object> headers) {
        final Set<Entry<HttpRequestAttribute<?>, Object>> set = this.createSet(transport,
                method,
                version,
                url,
                headers);
        final Iterator<Entry<HttpRequestAttribute<?>, Object>> iterator = set.iterator();
        this.checkEntry(iterator, HttpRequestAttributes.TRANSPORT, transport);
        this.checkEntry(iterator, HttpRequestAttributes.METHOD, method);
        this.checkEntry(iterator, HttpRequestAttributes.HTTP_PROTOCOL_VERSION, version);

        // paths
        int i = 0;
        for (UrlPathName name : url.path()) {
            this.checkEntry(iterator, HttpRequestAttributes.pathComponent(i), name);
            i++;
        }
        int entryCount = 3 + i; // 3 = transport, method, protocolversion

        final Map<UrlParameterName, List<String>> urlParameters = url.query().parameters();
        for (UrlParameterName name : urlParameters.keySet()) {
            this.checkEntry(iterator, name, urlParameters.get(name));
            entryCount++;
        }

        // headers
        for (Entry<HttpHeaderName<?>, Object> nameAndValue : headers.entrySet()) {
            final HttpHeaderName<?> header = nameAndValue.getKey();
            this.checkEntry(iterator,
                    header,
                    nameAndValue.getValue());
            entryCount++;
        }

        // cookies
        for (ClientCookie cookie : HttpHeaderName.COOKIE.headerValue(headers).orElse(ClientCookie.NO_COOKIES)) {
            this.checkEntry(iterator,
                    cookie.name(),
                    cookie);
            entryCount++;
        }

        // parameters
        for (Entry<UrlParameterName, List<String>> nameAndValue : url.query().parameters().entrySet()) {
            this.checkEntry(iterator,
                    HttpRequestParameterName.with(nameAndValue.getKey().value()),
                    nameAndValue.getValue());
            entryCount++;
        }

        this.sizeAndCheck(set, entryCount);
    }

    private void checkEntry(final Iterator<Entry<HttpRequestAttribute<?>, Object>> iterator,
                            final HttpRequestAttribute<?> key,
                            final Object value) {
        assertTrue(iterator.hasNext(), "has next");
        this.checkEntry0(iterator.next(), key, value);
    }

    private void checkEntry0(final Entry<HttpRequestAttribute<?>, Object> entry,
                             final HttpRequestAttribute<?> key,
                             final Object value) {
        assertEquals(key, entry.getKey(), "entry key");
        assertEquals(value, entry.getValue(), "entry value");
    }

    // helpers ...........................................................................................

    @Override
    public RouterHttpRequestParametersMapEntrySet createSet() {
        return this.createSet(TRANSPORT, METHOD, PROTOCOL, URL, HEADERS);
    }

    private RouterHttpRequestParametersMapEntrySet createSet(final HttpTransport transport,
                                                             final HttpMethod method,
                                                             final HttpProtocolVersion version,
                                                             final RelativeUrl url,
                                                             final Map<HttpHeaderName<?>, Object> headers) {
        return RouterHttpRequestParametersMapEntrySet.with(RouterHttpRequestParametersMap.with(this.request(transport,
                method,
                version,
                url,
                headers)));
    }

    private HttpRequest request(final HttpTransport transport,
                                final HttpMethod method,
                                final HttpProtocolVersion version,
                                final RelativeUrl url,
                                final Map<HttpHeaderName<?>, Object> headers) {
        return new FakeHttpRequest() {

            @Override
            public HttpTransport transport() {
                return transport;
            }

            @Override
            public HttpMethod method() {
                return method;
            }

            @Override
            public HttpProtocolVersion protocolVersion() {
                return version;
            }

            @Override
            public RelativeUrl url() {
                return url;
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return headers;
            }

            public Map<HttpRequestParameterName, List<String>> parameters() {
                final Map<HttpRequestParameterName, List<String>> parameters = Maps.ordered();

                for (Entry<UrlParameterName, List<String>> nameAndValue : url.query().parameters().entrySet()) {
                    parameters.put(HttpRequestParameterName.with(nameAndValue.getKey().value()), nameAndValue.getValue());
                }

                return parameters;
            }

            @Override
            public String toString() {
                return transport + " " + version + " " + url + " " + method + " " + headers;
            }
        };
    }

    @Override
    public Class<RouterHttpRequestParametersMapEntrySet> type() {
        return RouterHttpRequestParametersMapEntrySet.class;
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
