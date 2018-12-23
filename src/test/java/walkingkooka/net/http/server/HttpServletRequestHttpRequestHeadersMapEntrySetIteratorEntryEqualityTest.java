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
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import static org.junit.Assert.assertEquals;

public final class HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntryEqualityTest extends
        HashCodeEqualsDefinedEqualityTestCase<HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry> {

    private final static String HEADER_NAME = "content-length";
    private final static long CONTENT_LENGTH = 123L;

    @Test
    public void testDifferentHeaderName() {
        final String value = "abc123";

        this.checkNotEquals(this.createEntry(HttpHeaderName.SERVER.value(), value),
                this.createEntry(HttpHeaderName.USER_AGENT.value(), value));
    }

    @Test
    public void testDifferentHeaderValue() {
        this.checkNotEquals(this.createEntry(HEADER_NAME, "" + (CONTENT_LENGTH + 999L)));
    }

    @Override
    protected HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry createObject() {
        return this.createEntry(HEADER_NAME, CONTENT_LENGTH + "");
    }

    private HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry createEntry(final String headerName,
                                                                                     final String value) {
        return HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(headerName,
                new FakeHttpServletRequest() {
                    @Override
                    public String getHeader(final String header) {
                        assertEquals("header", headerName, header);
                        return value;
                    }
                });
    }
}
