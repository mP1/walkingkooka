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

import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

/**
 * An enumeration that represents the Http version
 */
public enum HttpProtocol implements Value<String> {

    /**
     * Version 1.0
     */
    VERSION_1_0("HTTP/1.0"),

    /**
     * Version 1.1
     */
    VERSION_1_1("HTTP/1.1");

    /**
     * Lookups and returns the {@link HttpProtocol} for the given header. If the header is unknown an {@link IllegalArgumentException} will be thrown.
     */
    static public HttpProtocol fromHeader(final String header) {
        Whitespace.failIfNullOrWhitespace(header, "header");

        HttpProtocol protocol = null;
        for (final HttpProtocol possible : HttpProtocol.values()) {
            if (possible.value().equals(header)) {
                protocol = possible;
                break;
            }
        }
        if (null == protocol) {
            throw new IllegalArgumentException("Unknown protocol=" + CharSequences.quote(header));
        }
        return protocol;
    }

    /**
     * Private constructor
     */
    private HttpProtocol(final String value) {
        this.value = value;
    }

    /**
     * Returns the raw form for this protocol as found within a web request.
     */
    @Override
    public String value() {
        return this.value;
    }

    private String value;

    @Override
    public String toString() {
        return this.value;
    }
}
