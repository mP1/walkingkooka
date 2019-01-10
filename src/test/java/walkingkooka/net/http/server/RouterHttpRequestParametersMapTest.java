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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.MapTestCase;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.Cookie;
import walkingkooka.net.header.CookieName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class RouterHttpRequestParametersMapTest extends MapTestCase<RouterHttpRequestParametersMap, HttpRequestAttribute, Object> {

    @Test
    public void testContainsAndGet() {
        this.getAndCheck(HttpRequestAttributes.TRANSPORT, this.transport());
    }

    @Test
    public void testEntrySet() {
        final Iterator<Entry<HttpRequestAttribute, Object>> iterator = this.createMap().entrySet().iterator();

        this.checkEntry(iterator, HttpRequestAttributes.TRANSPORT, this.transport());
        this.checkEntry(iterator, HttpRequestAttributes.METHOD, this.method());
        this.checkEntry(iterator, HttpRequestAttributes.HTTP_PROTOCOL_VERSION, this.protocolVersion());

        // paths
        int i = 0;
        for (UrlPathName name : this.url().path()) {
            this.checkEntry(iterator, HttpRequestAttributes.pathComponent(i), name);
            i++;
        }

        // headers
        for (Entry<HttpHeaderName<?>, Object> nameAndValue : this.headers().entrySet()) {
            final HttpHeaderName<?> header = nameAndValue.getKey();
            this.checkEntry(iterator,
                    header,
                    nameAndValue.getValue());
        }

        // cookies
        for (ClientCookie cookie : this.cookies()) {
            this.checkEntry(iterator,
                    cookie.name(),
                    cookie);
        }

        // parameters
        for (Entry<HttpRequestParameterName, List<String>> nameAndValue : this.parameters().entrySet()) {
            this.checkEntry(iterator,
                    nameAndValue.getKey(),
                    nameAndValue.getValue());
        }
    }

    private void checkEntry(final Iterator<Entry<HttpRequestAttribute, Object>> iterator,
                            final HttpRequestAttribute key,
                            final Object value) {
        assertTrue("has next", iterator.hasNext());
        this.checkEntry0(iterator.next(), key, value);
    }

    private void checkEntry0(final Entry<HttpRequestAttribute, Object> entry,
                             final HttpRequestAttribute key,
                             final Object value) {
        assertEquals("entry key", key, entry.getKey());
        assertEquals("entry value", value, entry.getValue());
    }

    @Test
    public void testGetPathInvalid() {
        this.getAndCheckAbsent(HttpRequestAttributes.pathComponent(3));
    }

    @Test
    public void testGetPath() {
        this.getAndCheck(HttpRequestAttributes.pathComponent(1), UrlPathName.with("path"));
    }

    @Test
    public void testGetPath2() {
        this.getAndCheck(HttpRequestAttributes.pathComponent(2), UrlPathName.with("file"));
    }

    @Override
    protected RouterHttpRequestParametersMap createMap() {
        return this.createMap(transport(),
                this.method(),
                this.url(),
                this.protocolVersion(),
                this.headers(),
                this.cookies(),
                this.parameters());
    }

    private HttpTransport transport() {
        return HttpTransport.SECURED;
    }

    private HttpMethod method() {
        return HttpMethod.GET;
    }

    private RelativeUrl url() {
        return Url.parseRelative("/path/file");
    }

    private HttpProtocolVersion protocolVersion() {
        return HttpProtocolVersion.VERSION_1_1;
    }

    private Map<HttpHeaderName<?>, Object> headers() {
        return Maps.one(HttpHeaderName.CONNECTION, "Close");
    }

    private List<ClientCookie> cookies() {
        return Lists.of(Cookie.client(CookieName.with("cookie123"), "cookie-value-456"));
    }

    private Map<HttpRequestParameterName, List<String>> parameters() {
        return HttpRequest.NO_PARAMETERS;
    }

    private RouterHttpRequestParametersMap createMap(final HttpTransport transport,
                                                     final HttpMethod method,
                                                     final RelativeUrl url,
                                                     final HttpProtocolVersion protocolVersion,
                                                     final Map<HttpHeaderName<?>, Object> headers,
                                                     final List<ClientCookie> cookies,
                                                     final Map<HttpRequestParameterName, List<String>> parameters) {
        return RouterHttpRequestParametersMap.with(new FakeHttpRequest() {

            @Override
            public HttpTransport transport() {
                return transport;
            }

            @Override
            public HttpMethod method() {
                return method;
            }

            @Override
            public RelativeUrl url() {
                return url;
            }

            @Override
            public HttpProtocolVersion protocolVersion() {
                return protocolVersion;
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return headers;
            }

            @Override
            public List<ClientCookie> cookies() {
                return cookies;
            }

            @Override
            public Map<HttpRequestParameterName, List<String>> parameters() {
                return parameters;
            }

            @Override
            public String toString() {
                return transport + " " + method + " " + url + " " + protocolVersion + " " + headers + " " + cookies + " " + parameters;
            }
        });
    }

    @Override
    protected Class<RouterHttpRequestParametersMap> type() {
        return RouterHttpRequestParametersMap.class;
    }
}
