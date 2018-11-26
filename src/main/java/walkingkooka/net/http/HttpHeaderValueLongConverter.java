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
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link Long}
 */
final class HttpHeaderValueLongConverter extends HttpHeaderValueConverter<Long> {

    /**
     * Singleton
     */
    final static HttpHeaderValueLongConverter INSTANCE = new HttpHeaderValueLongConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueLongConverter() {
        super();
    }

    @Override
    Long parse0(final String value, final Name name) {
        return Long.parseLong(value.trim());
    }

    @Override
    String format0(final Long value, final Name name) {
        return Long.toString(value);
    }

    @Override
    boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringType(Long.class);
    }
}
