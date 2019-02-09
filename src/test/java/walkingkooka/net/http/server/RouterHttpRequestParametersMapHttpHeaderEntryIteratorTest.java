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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class RouterHttpRequestParametersMapHttpHeaderEntryIteratorTest implements IteratorTesting,
        ClassTesting2<RouterHttpRequestParametersMapHttpHeaderEntryIterator>,
        ToStringTesting<RouterHttpRequestParametersMapHttpHeaderEntryIterator>,
        TypeNameTesting<RouterHttpRequestParametersMapHttpHeaderEntryIterator> {

    private final static HttpHeaderName<Long> CONTENT_LENGTH = HttpHeaderName.CONTENT_LENGTH;
    private final static Long CONTENT_LENGTH_VALUE = 123L;
    private final static HttpHeaderName<String> CONNECTION = HttpHeaderName.CONNECTION;
    private final static String CONNECTION_VALUE = "Close";

    @Test
    public void testHasNextAndNextConsumeEmpty() {
        this.iterateAndCheck(true,
                HttpRequest.NO_HEADERS,
                Lists.empty());
    }

    @Test
    public void testHasNextAndNextConsumeStringHeader() {
        this.iterateAndCheck(true,
                Maps.one(CONNECTION, CONNECTION_VALUE),
                CONNECTION,
                CONNECTION_VALUE);
    }

    @Test
    public void testHasNextAndNextConsumeNonStringHeader() {
        this.iterateAndCheck(true,
                Maps.one(CONTENT_LENGTH, CONTENT_LENGTH_VALUE),
                CONTENT_LENGTH,
                CONTENT_LENGTH_VALUE);
    }

    @Test
    public void testHasNextAndNextConsume() {
        final Map<HttpHeaderName<?>, Object> headers = this.headers2();

        this.iterateAndCheck(true,
                headers,
                HttpHeaderName.CONNECTION, HttpHeaderName.CONTENT_LENGTH,
                CONNECTION_VALUE, CONTENT_LENGTH_VALUE);
    }

    @Test
    public void testNextConsume() {
        final Map<HttpHeaderName<?>, Object> headers = this.headers2();

        this.iterateAndCheck(false,
                headers,
                HttpHeaderName.CONNECTION, HttpHeaderName.CONTENT_LENGTH,
                CONNECTION_VALUE, CONTENT_LENGTH_VALUE);
    }

    private void iterateAndCheck(final boolean checkHasNext,
                                 final Map<HttpHeaderName<?>, Object> headers,
                                 final HttpHeaderName<?> headerName,
                                 final Object headerValue) {
        this.iterateAndCheck(checkHasNext, headers, Lists.of(headerName), headerValue);
    }

    private void iterateAndCheck(final boolean checkHasNext,
                                 final Map<HttpHeaderName<?>, Object> headers,
                                 final HttpHeaderName<?> headerName1,
                                 final HttpHeaderName<?> headerName2,
                                 final Object headerValue1,
                                 final Object headerValue2) {
        this.iterateAndCheck(checkHasNext,
                headers,
                Lists.of(headerName1, headerName2),
                headerValue1, headerValue2);
    }

    private void iterateAndCheck(final boolean checkHasNext,
                                 final Map<HttpHeaderName<?>, Object> headers,
                                 final List<HttpHeaderName<?>> headerNames,
                                 final Object... headerValues) {
        assertEquals(headerNames.size(), headerValues.length, "headerNames count != headerValues count");

        final RouterHttpRequestParametersMapHttpHeaderEntryIterator iterator = this.createIterator(headers);

        for (int i = 0; i < headerNames.size(); i++) {
            if (checkHasNext) {
                this.hasNextCheckTrue(iterator, "iterator should have " + (headerValues.length - i) + " entries left");
            }
            this.checkNext(iterator, headerNames.get(i), headerValues[i]);
        }

        this.hasNextCheckFalse(iterator);
        this.nextFails(iterator);
    }

    private void checkNext(final RouterHttpRequestParametersMapHttpHeaderEntryIterator iterator,
                           final HttpHeaderName<?> header,
                           final Object value) {
        final Entry<HttpRequestAttribute<?>, Object> entry = iterator.next();
        assertEquals(header, entry.getKey(), "key");
        assertEquals(value, entry.getValue(), "value");
    }

    @Test
    public void testToString() {
        final Iterator<Entry<HttpHeaderName<?>, Object>> iterator = Iterators.fake();
        this.toStringAndCheck(RouterHttpRequestParametersMapHttpHeaderEntryIterator.with(iterator), iterator.toString());
    }

    private RouterHttpRequestParametersMapHttpHeaderEntryIterator createIterator() {
        return this.createIterator(this.headers2());
    }

    private RouterHttpRequestParametersMapHttpHeaderEntryIterator createIterator(final Map<HttpHeaderName<?>, Object> headers) {
        return RouterHttpRequestParametersMapHttpHeaderEntryIterator.with(headers.entrySet().iterator());
    }

    private Map<HttpHeaderName<?>, Object> headers2() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(CONNECTION, CONNECTION_VALUE);
        headers.put(CONTENT_LENGTH, CONTENT_LENGTH_VALUE);
        return headers;
    }

    @Override
    public Class<RouterHttpRequestParametersMapHttpHeaderEntryIterator> type() {
        return RouterHttpRequestParametersMapHttpHeaderEntryIterator.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return RouterHttpRequestParametersMap.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Iterator.class.getSimpleName();
    }
}
