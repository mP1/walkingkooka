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
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.http.cookie.Cookie;

import java.util.List;

/**
 * A {@link HttpHeaderValueConverter} that converts a {@link String} into one {@link ClientCookie}.
 */
final class HttpHeaderValueClientCookieListConverter extends HttpHeaderValueConverter<List<ClientCookie>> {

    /**
     * Singleton
     */
    final static HttpHeaderValueClientCookieListConverter INSTANCE = new HttpHeaderValueClientCookieListConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpHeaderValueClientCookieListConverter() {
        super();
    }

    @Override
    List<ClientCookie> parse0(final String value, final Name name) {
        return Cookie.parseClientHeader(value);
    }

    @Override
    boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringListOf(ClientCookie.class);
    }
}
