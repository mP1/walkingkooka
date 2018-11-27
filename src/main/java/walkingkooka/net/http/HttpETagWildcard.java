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

import java.util.Optional;

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
     * The optional weak attribute
     */
    public Optional<HttpETagWeak> weak() {
        return NO_WEAK;
    }

    @Override
    void checkWeak0(final Optional<HttpETagWeak> weak) {
        checkWeak(weak);
        if (weak.isPresent()) {
            throw new IllegalArgumentException("Wildcard cannot have weak");
        }
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return true;
    }

    // Object.......................................................................................................

    /**
     * THe string form of a wildcard etags is "*" without quotes.
     */
    @Override
    public String toString() {
        return this.value();
    }
}
