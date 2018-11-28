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

/**
 * Holds a ETAG with a value.
 */
final class HttpETagNonWildcard extends HttpETag {

    /**
     * Factory that creates a new {@link HttpETagNonWildcard}
     */
    static HttpETagNonWildcard with0(final String value, final HttpETagValidator validator) {
        checkValue(value);
        checkValidator(validator);

        return new HttpETagNonWildcard(value, validator);
    }

    /**
     * Private ctor use factory
     */
    private HttpETagNonWildcard(final String value, final HttpETagValidator validator) {
        super();
        this.value = value;
        this.validator = validator;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // validator.........................................................................................

    /**
     * The optional validator attribute
     */
    @Override
    public HttpETagValidator validator() {
        return this.validator;
    }

    private final HttpETagValidator validator;

    @Override
    void checkValidator0(final HttpETagValidator validator) {
        checkValidator(validator);
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return false;
    }

    // Object.......................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.validator);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpETagNonWildcard &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpETagNonWildcard other) {
        return this.value.equals(other.value) &&
                this.validator.equals(other.validator);
    }

    // toString........................................................................................................

    @Override
    public final String toString() {
        return this.validator().prefix + CharSequences.quote(this.value());
    }
}
