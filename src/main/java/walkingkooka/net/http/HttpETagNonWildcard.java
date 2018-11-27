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

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Holds a ETAG with a value.
 */
final class HttpETagNonWildcard extends HttpETag {

    /**
     * Factory that creates a new {@link HttpETagNonWildcard}
     */
    static HttpETagNonWildcard with0(final String value, final Optional<HttpETagWeak> weak) {
        checkValue(value);
        checkWeak(weak);

        return new HttpETagNonWildcard(value, weak);
    }

    /**
     * Private ctor use factory
     */
    private HttpETagNonWildcard(final String value, final Optional<HttpETagWeak> weak) {
        super();
        this.value = value;
        this.weak = weak;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // weak.........................................................................................

    /**
     * The optional weak attribute
     */
    @Override
    public Optional<HttpETagWeak> weak() {
        return this.weak;
    }

    private final Optional<HttpETagWeak> weak;

    @Override
    void checkWeak0(final Optional<HttpETagWeak> weak) {
        checkWeak(weak);
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return false;
    }

    // Object.......................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.weak);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpETagNonWildcard &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpETagNonWildcard other) {
        return this.value.equals(other.value) &&
                this.weak.equals(other.weak);
    }

    /**
     * Dumps the ETAG as a header value.
     */
    @Override
    public String toString() {
        return (this.weak.isPresent() ? "W/" : "") + CharSequences.quote(this.value);
    }
}
