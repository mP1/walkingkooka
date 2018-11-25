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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link HttpHeaderValueConverter} that expects comma separated {@link HttpMethod methods}.
 */
final class HttpHeaderValueHttpMethodListConverter extends HttpHeaderValueConverter<List<HttpMethod>> {

    /**
     * Singleton
     */
    final static HttpHeaderValueHttpMethodListConverter INSTANCE = new HttpHeaderValueHttpMethodListConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueHttpMethodListConverter() {
        super();
    }

    @Override
    List<HttpMethod> parse0(final String value, final Name name) {
        return Arrays.stream(value.split(","))
                .map(m -> HttpMethod.with(m.trim()))
                .collect(Collectors.toList());
    }

    @Override
    boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringListOf(HttpMethod.class);
    }
}
