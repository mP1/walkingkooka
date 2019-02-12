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
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;

public final class HttpServletRequestHttpRequestHeadersMapEntrySetIteratorTest extends ClassTestCase<HttpServletRequestHttpRequestHeadersMapEntrySetIterator>
        implements IteratorTesting,
        ToStringTesting<HttpServletRequestHttpRequestHeadersMapEntrySetIterator>,
        TypeNameTesting<HttpServletRequestHttpRequestHeadersMapEntrySetIterator> {

    private final static HttpHeaderName<?> HEADER1 = HttpHeaderName.CONTENT_LENGTH;
    private final static Long VALUE1 = 111L;

    private final static HttpHeaderName<?> HEADER2 = HttpHeaderName.SERVER;
    private final static String VALUE2 = "Server2";

    @Test
    public void testIterate() {
        final HttpServletRequest request = this.request();
        final HttpServletRequestHttpRequestHeadersMapEntrySetIterator iterator = HttpServletRequestHttpRequestHeadersMapEntrySetIterator.with(request);
        this.iterateUsingHasNextAndCheck(iterator,
                HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(HEADER1.value(), request),
                HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(HEADER2.value(), request));
    }

    @Test
    public void testIterateOnlyNext() {
        final HttpServletRequest request = this.request();
        final HttpServletRequestHttpRequestHeadersMapEntrySetIterator iterator = HttpServletRequestHttpRequestHeadersMapEntrySetIterator.with(request);
        this.iterateAndCheck(iterator,
                HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(HEADER1.value(), request),
                HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(HEADER2.value(), request));
    }

    @Test
    public void testRemoveFails() {
        HttpServletRequestHttpRequestHeadersMapEntrySetIterator iterator = this.createIterator();
        iterator.next();
        this.checkRemoveUnsupportedFails(iterator);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterator(), "[Content-Length, Server]");
    }

    private HttpServletRequestHttpRequestHeadersMapEntrySetIterator createIterator() {
        return HttpServletRequestHttpRequestHeadersMapEntrySetIterator.with(this.request());
    }

    private HttpServletRequest request() {
        return new FakeHttpServletRequest() {

            @Override
            public String getHeader(final String header) {
                if (HEADER1.value().equalsIgnoreCase(header)) {
                    return "" + VALUE1;
                }
                if (HEADER2.value().equalsIgnoreCase(header)) {
                    return "" + VALUE2;
                }

                throw new UnsupportedOperationException();
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return Enumerations.array(new Object[]{HEADER1.value(), HEADER2.value()});
            }
        };
    }

    @Override
    public Class<HttpServletRequestHttpRequestHeadersMapEntrySetIterator> type() {
        return HttpServletRequestHttpRequestHeadersMapEntrySetIterator.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return HttpServletRequestHttpRequestHeadersMapEntrySet.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Iterator.class.getSimpleName();
    }
}
