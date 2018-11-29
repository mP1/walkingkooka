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
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpHeaderName;

/**
 * A {@link HeaderValueConverter2} that parses a header value into a {@link Url}.
 * This is useful for headers such as {@link HttpHeaderName#LOCATION}.
 */
final class HeaderValueUrlConverter extends HeaderValueConverter2<Url> {

    /**
     * Singleton
     */
    final static HeaderValueUrlConverter INSTANCE = new HeaderValueUrlConverter();

    /**
     * Private ctor use singleton.
     */
    private HeaderValueUrlConverter() {
        super();
    }

    @Override
    Url parse0(final String value, final Name name) {
        return Url.parse(value);
    }

    @Override
    String format0(final Url value, final Name name) {
        return value.toString();
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringType(Url.class);
    }
}
