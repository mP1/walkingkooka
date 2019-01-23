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
import walkingkooka.net.http.HttpMethod;

/**
 * A {@link HeaderValueConverter} that handles a single {@link HttpMethod method}.
 */
final class HttpMethodHeaderValueConverter extends HeaderValueConverter2<HttpMethod> {

    /**
     * Singleton
     */
    final static HttpMethodHeaderValueConverter INSTANCE = new HttpMethodHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private HttpMethodHeaderValueConverter() {
        super();
    }

    @Override
    HttpMethod parse0(final String text, final Name name) {
        return HttpMethod.with(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, HttpMethod.class, name);
    }

    @Override
    String toText0(final HttpMethod method, final Name name) {
        return method.toHeaderText();
    }

    @Override
    public String toString() {
        return this.toStringType(HttpMethod.class);
    }
}
