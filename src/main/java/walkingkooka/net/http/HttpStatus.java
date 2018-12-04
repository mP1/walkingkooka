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
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Whitespace;

import java.util.Objects;

/**
 * Holds a HTTP response status code and message.
 */
public final class HttpStatus implements HashCodeEqualsDefined, Value<HttpStatusCode> {

    /**
     * Factory that creates a {@link HttpStatus}
     */
    static HttpStatus with(final HttpStatusCode value, final String message) {
        return new HttpStatus(value, message);
    }

    /**
     * Private constructor use static factory.
     */
    private HttpStatus(final HttpStatusCode value, final String message) {
        super();
        this.value = value;
        this.message = message;
    }


    /**
     * Getter that returns the code
     */
    @Override
    public HttpStatusCode value() {
        return this.value;
    }

    /**
     * Would be setter that returns a {@link HttpStatus} with the new message. Together with this method any of the constants may be used to set a
     * message inheriting the status code.
     */
    public HttpStatus setCode(final HttpStatusCode code) {
        Objects.requireNonNull(code, "code");

        return this.value.equals(code) ?
                this :
                this.replace(code, this.message);
    }

    private final HttpStatusCode value;

    /**
     * Getter that returns the message accompanying the code
     */
    public String message() {
        return this.message;
    }

    /**
     * Would be setter that returns a {@link HttpStatus} with the new message. Together with this method any of the constants may be used to set a
     * message inheriting the status code.
     */
    public HttpStatus setMessage(final String message) {
        checkMessage(message);

        return this.message.equals(message) ?
                this :
                this.replace(this.value, message);
    }

    private final String message;

    private static void checkMessage(final String message) {
        Whitespace.failIfNullOrEmptyOrWhitespace(message, "message");
    }

    // replace........................................................................

    private HttpStatus replace(final HttpStatusCode value, final String message) {
        return new HttpStatus(value, message);
    }

    // Object........................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.message);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpStatus &&
                        this.equals0(Cast.to(other));
    }

    /**
     * Note only the value and not the message is included in equality tests.
     */
    private boolean equals0(final HttpStatus other) {
        return this.value == other.value &&
                this.message.equals(other.message);
    }

    /**
     * Dumps the code and message in the format of a response.
     */
    @Override
    public String toString() {
        return this.value.code() + " " + this.message;
    }
}
