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

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * A {@link HeaderValueConverter} that converts a {@link String} into one {@link ServerCookie}.
 */
final class ServerCookieHeaderValueConverter extends NonStringHeaderValueConverter<ServerCookie> {

    /**
     * Singleton
     */
    final static ServerCookieHeaderValueConverter INSTANCE = new ServerCookieHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private ServerCookieHeaderValueConverter() {
        super();
    }

    @Override
    ServerCookie parse0(final String text, final Name name) {
        return Cookie.parseServerHeader(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, ServerCookie.class, name);
    }

    @Override
    String toText0(final ServerCookie cookie, final Name name) {
        return cookie.toHeaderText();
    }

    @Override
    public String toString() {
        return this.toStringType(ServerCookie.class);
    }
}
