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

/**
 * Holds a Wildcard ETAG.
 */
final class HttpETagWildcard extends HttpETag {

    /**
     * Instance getter.
     */
    static HttpETagWildcard instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static HttpETagWildcard INSTANCE = new HttpETagWildcard();

    /**
     * Private ctor use factory
     */
    private HttpETagWildcard() {
        super();
    }

    // value.........................................................................................

    @Override
    public String value() {
        return WILDCARD_VALUE.string();
    }

    // weak.........................................................................................

    /**
     * Always returns {@link HttpETagValidator#STRONG}
     */
    public HttpETagValidator validator() {
        return HttpETagValidator.STRONG;
    }

    @Override
    void checkValidator0(final HttpETagValidator validator) {
        checkValidator(validator);
        validator.wildcardValidatorCheck();
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return true;
    }

    // toString........................................................................................................

    @Override
    public final String toString() {
        return this.value();
    }
}
