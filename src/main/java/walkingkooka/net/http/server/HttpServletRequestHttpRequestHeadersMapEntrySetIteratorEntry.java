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

import walkingkooka.Cast;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import javax.servlet.http.HttpServletRequest;
import java.util.Map.Entry;

/**
 * The entry for an iterator of entries from a headers map.
 */
final class HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry implements Entry<HttpHeaderName<?>, Object>,
        HashCodeEqualsDefined {

    static HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry with(final String headerName,
                                                                             final HttpServletRequest request) {
        return new HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry(headerName, request);
    }

    private HttpServletRequestHttpRequestHeadersMapEntrySetIteratorEntry(final String headerName,
                                                                         final HttpServletRequest request) {
        super();
        this.headerName = headerName;
        this.request = request;
    }

    @Override
    public HttpHeaderName<?> getKey() {
        if (null == this.key) {
            this.key = HttpHeaderName.with(this.headerName);
        }
        return this.key;
    }

    private HttpHeaderName<?> key;

    @Override
    public Object getValue() {
        if (null == this.value) {
            this.value = this.getKey().toValue(this.request.getHeader(this.headerName));
        }
        return this.value;
    }

    private Object value;

    @Override
    public Object setValue(final Object value) {
        throw new UnsupportedOperationException();
    }

    private final String headerName;
    private final HttpServletRequest request;

    // Object .............................................................................

    @Override
    public int hashCode() {
        return CaseSensitivity.INSENSITIVE.hash(this.headerName);
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof Entry &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final Entry<Object, Object> other) {
        return this.getKey().equals(other.getKey()) &&
                this.getValue().equals(other.getValue());
    }

    @Override
    public String toString() {
        return this.getKey() + ": " + CharSequences.quoteIfChars(this.getValue());
    }
}
