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
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link HttpETag}.
 * This is useful for headers such as {@link HttpHeaderName#E_TAG}.
 */
final class HttpETagHeaderValueConverter extends HttpHeaderValueConverter<HttpETag> {

    /**
     * Singleton
     */
    final static HttpETagHeaderValueConverter INSTANCE = new HttpETagHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpETagHeaderValueConverter() {
        super();
    }

    @Override
    HttpETag parse0(final String value, final Name name) {
        return HttpETag.parseOne(value);
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, HttpETag.class);
    }

    @Override
    String format0(final HttpETag value, final Name name) {
        return value.toString();
    }

    @Override
    public String toString() {
        return toStringType(HttpETag.class);
    }
}
