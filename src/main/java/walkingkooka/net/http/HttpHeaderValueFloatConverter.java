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
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link Float}
 */
final class HttpHeaderValueFloatConverter extends HttpHeaderValueConverter<Float> {

    /**
     * Singleton
     */
    final static HttpHeaderValueFloatConverter INSTANCE = new HttpHeaderValueFloatConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueFloatConverter() {
        super();
    }

    @Override
    Float parse0(final String value, final Name name) {
        return Float.parseFloat(value.trim());
    }

    @Override
    public String toString() {
        return toStringType(Float.class);
    }
}
