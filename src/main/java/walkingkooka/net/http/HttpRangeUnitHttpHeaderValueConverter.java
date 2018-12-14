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

import walkingkooka.naming.Name;

/**
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link HttpRangeUnit>}.
 * This is useful for headers such as {@link HttpHeaderName#ACCEPT_RANGES}.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Ranges"></a>
 * <pre>
 * Accept-Ranges: bytes
 * Accept-Ranges: none
 * </pre>
 */
final class HttpRangeUnitHttpHeaderValueConverter extends HttpHeaderValueConverter<HttpRangeUnit> {

    /**
     * Singleton
     */
    final static HttpRangeUnitHttpHeaderValueConverter INSTANCE = new HttpRangeUnitHttpHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpRangeUnitHttpHeaderValueConverter() {
        super();
    }

    @Override
    HttpRangeUnit parse0(final String value, final Name name) {
        return HttpRangeUnit.fromHeaderText(value);
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, HttpRangeUnit.class);
    }

    @Override
    String toText0(final HttpRangeUnit value, final Name name) {
        return value.toHeaderText();
    }

    @Override
    public String toString() {
        return this.toStringType(HttpRangeUnit.class);
    }
}
