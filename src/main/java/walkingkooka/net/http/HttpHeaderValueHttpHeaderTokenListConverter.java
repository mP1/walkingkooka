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

import java.util.List;

/**
 * A {@link HttpHeaderValueConverter} that expects comma separated {@link HttpHeaderToken tokens}.
 */
final class HttpHeaderValueHttpHeaderTokenListConverter extends HttpHeaderValueConverter2<List<HttpHeaderToken>> {

    /**
     * Singleton
     */
    final static HttpHeaderValueHttpHeaderTokenListConverter INSTANCE = new HttpHeaderValueHttpHeaderTokenListConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueHttpHeaderTokenListConverter() {
        super();
    }

    @Override
    List<HttpHeaderToken> parse0(final String value, final Name name) {
        return HttpHeaderToken.parse(value);
    }

    @Override
    boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringListOf(HttpHeaderToken.class);
    }
}
