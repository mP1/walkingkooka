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
import walkingkooka.collect.iterator.IteratorTestCase;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;

import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public final class RouterHttpRequestParametersMapHttpRequestAttributeEntryIteratorTest extends
        IteratorTestCase<RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator,
                Entry<HttpRequestAttribute<?>, Object>> {

    private final static HttpTransport TRANSPORT = HttpTransport.UNSECURED;
    private final static HttpMethod METHOD = HttpMethod.HEAD;
    private final static HttpProtocolVersion PROTOCOL = HttpProtocolVersion.VERSION_1_1;

    @Test
    public void testHasNextAndNextConsole() {
        this.iterateAndCheck(TRANSPORT, METHOD, PROTOCOL, true);
    }

    @Test
    public void testHasNextAndNextConsole2() {
        this.iterateAndCheck(HttpTransport.SECURED, HttpMethod.GET, HttpProtocolVersion.VERSION_1_0, true);
    }

    @Test
    public void testNextConsole() {
        this.iterateAndCheck(TRANSPORT, METHOD, PROTOCOL, false);
    }

    @Test
    public void testNextConsole2() {
        this.iterateAndCheck(HttpTransport.SECURED, HttpMethod.GET, HttpProtocolVersion.VERSION_1_0, false);
    }

    private void iterateAndCheck(final HttpTransport transport,
                                 final HttpMethod method,
                                 final HttpProtocolVersion version,
                                 final boolean hasNext) {
        final RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator iterator = this.createIterator(transport, method, version);

        this.checkHasNextTrue("iterator should have 3 entries", iterator);
        this.checkNext(iterator, HttpRequestAttributes.TRANSPORT, transport);

        this.checkHasNextTrue("iterator should have 2 entries", iterator);
        this.checkNext(iterator, HttpRequestAttributes.METHOD, method);

        this.checkHasNextTrue("iterator should have 1 entries", iterator);
        this.checkNext(iterator, HttpRequestAttributes.HTTP_PROTOCOL_VERSION, version);

        this.checkHasNextFalse(iterator);
        this.checkNextFails(iterator);
    }

    private void checkNext(final RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator iterator,
                           final HttpRequestAttributes key,
                           final Object value) {
        final RouterHttpRequestParametersMapEntry entry = iterator.next();
        assertEquals("key", key, entry.getKey());
        assertEquals("value", value, entry.getValue());
    }

    @Test
    public void testToString() {
        assertEquals(HttpRequestAttributes.TRANSPORT + "=" + TRANSPORT,
                this.createIterator().toString());
    }

    @Test
    public void testToString2() {
        final RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator iterator = this.createIterator();
        iterator.next();
        assertEquals(HttpRequestAttributes.METHOD + "=" + METHOD, iterator.toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        assertEquals("TRANSPORT=UNSECURED", this.createIterator().toString());
    }

    @Override
    protected RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator createIterator() {
        return this.createIterator(TRANSPORT, METHOD, PROTOCOL);
    }

    private RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator createIterator(final HttpTransport transport,
                                                                                           final HttpMethod method,
                                                                                           final HttpProtocolVersion version) {
        return RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator.with(this.request(transport, method, version));
    }

    private HttpRequest request(final HttpTransport transport,
                                final HttpMethod method,
                                final HttpProtocolVersion version) {
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
        };
    }

    @Override
    protected Class<RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator> type() {
        return RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator.class;
    }
}
