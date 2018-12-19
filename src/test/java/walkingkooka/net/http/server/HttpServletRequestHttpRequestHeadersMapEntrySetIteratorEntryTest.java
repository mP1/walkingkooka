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
import walkingkooka.collect.map.EntryTestCase;
import walkingkooka.net.http.HttpHeaderName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntryTest extends
        EntryTestCase<HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry,
                HttpHeaderName<?>,
                Object> {

    private final static String HEADER_NAME = "content-length";
    private final static Long CONTENT_LENGTH = 123L;

    @Test
    public void testGetKey() {
        this.getKeyAndCheck(this.createEntry(), HttpHeaderName.CONTENT_LENGTH);
    }

    @Test
    public void testGetKeyCached() {
        final HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry entry = this.createEntry();
        assertSame(entry.getKey(), entry.getKey());
    }

    @Test
    public void testGetValue() {
        this.getValueAndCheck(this.createEntry(), CONTENT_LENGTH);
    }

    @Test
    public void testGetValueCached() {
        final HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry entry = this.createEntry();
        assertSame(entry.getValue(), entry.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueFails() {
        this.createEntry().setValue(99L);
    }

    @Test
    public void testToString() {
        assertEquals("Content-Length: 123", this.createEntry().toString());
    }

    @Override
    protected HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry createEntry() {
        return HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.with(HEADER_NAME,
                new FakeHttpServletRequest() {
                    @Override
                    public String getHeader(final String header) {
                        assertEquals("header", HEADER_NAME, header);
                        return "" + CONTENT_LENGTH;
                    }
                });
    }

    @Override
    protected Class<HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry> type() {
        return HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry.class;
    }
}
