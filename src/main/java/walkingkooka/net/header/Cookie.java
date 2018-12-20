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

import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a cookie including all its properties. Note that values are verified to contain correct characters but no attempt is made to encode values
 * with incorrect values.
 */
abstract public class Cookie implements HeaderValue, UsesToStringBuilder {

    /**
     * The <code>domain</code> attribute
     */
    public final static String DOMAIN = "domain";

    /**
     * The <code>path</code> attribute
     */
    public final static String PATH = "path";

    /**
     * The <code>expires</code> attribute
     */
    public final static String EXPIRES = "expires";

    /**
     * The <code>max-age</code> attribute expects a integer value.
     */
    public final static String MAX_AGE = "max-age";

    /**
     * The <code>secure</code> attribute
     */
    public final static String SECURE = "secure";

    /**
     * The <code>httpOnly</code> attribute
     */
    public final static String HTTP_ONLY = "httpOnly";
    
    /**
     * {@see ClientCookie#parseHeader(String)}.
     */
    public static List<ClientCookie> parseClientHeader(final String header) {
        return ClientCookie.parseHeader(header);
    }

    /**
     * {@see ServerCookie#parseHeader(String)}.
     */
    public static ServerCookie parseServerHeader(final String header) {
        return ServerCookie.parseHeader(header);
    }

    /**
     * {@see ClientCookie#from(java.servlet.http.Cookie } .
     */
    public static ClientCookie clientFrom(final javax.servlet.http.Cookie cookie) {
        return ClientCookie.from(cookie);
    }

    /**
     * {@see ServerCookie#from(java.servlet.http.Cookie } .
     */
    public static ServerCookie serverFrom(final javax.servlet.http.Cookie cookie) {
        return ServerCookie.from(cookie);
    }

    /**
     * Extracts the value portion of a token.
     */
    static String extractValue(final String source) {
        return extractValue0(source, "");
    }

    /**
     * Extracts the value portion of a token.
     */
    static String extractValueOrNull(final String source) {
        return extractValue0(source, null);
    }

    /**
     * Extracts the value portion of a token.
     */
    private static String extractValue0(final String source, final String absent) {
        final int equals = source.indexOf(PARAMETER_NAME_VALUE_SEPARATOR.character());
        return -1 == equals ? absent : source.substring(equals + 1);
    }

    /**
     * Converts empty to nulls.
     */
    static String convertEmptyToNull(final String value) {
        return CharSequences.isNullOrEmpty(value) ? null : value;
    }

    /**
     * {@see ClientCookie}
     */
    public static ClientCookie client(final CookieName name, final String value) {
        return ClientCookie.with(name, value);
    }

    /**
     * {@see ServerCookie}
     */
    public static ServerCookie server(final CookieName name,
                                      final String value,
                                      final Optional<String> domain,
                                      final Optional<String> path,
                                      final Optional<String> comment,
                                      final Optional<CookieDeletion> deletion,
                                      final CookieSecure secure,
                                      final CookieHttpOnly httpOnly,
                                      final CookieVersion version) {
        return ServerCookie.with(name, value, domain, path, comment, deletion, secure, httpOnly, version);
    }

    static void checkName(final CookieName name) {
        Objects.requireNonNull(name, "name");
    }

    static void checkValue(final String value) {
        Objects.requireNonNull(value, "value");

        final int length = value.length();

        // empty nothing to check... empty is ok.
        if (0 != length) {
            int start = 0;
            int end = value.length();

            final char first = value.charAt(0);
            if ('"' == first) {
                if (length == 1) {
                    throw new InvalidCharacterException(value, 0);
                }

                // check characters within quotes
                final int secondLast = length - 1;
                checkValue0(value, 1, secondLast);

                // verify closing double quote present
                final char last = value.charAt(secondLast);
                if ('"' != last) {
                    throw new InvalidCharacterException(value, secondLast);
                }

                start = 1;
                end = secondLast;
            }
            // check the content of the string.
            checkValue0(value, start, end);
        }
    }

    /**
     * Verifies the characters in the range. This is used to check all characters in the {@link String} as well as the content of a {@link String quoted
     * value}.
     */
    private static void checkValue0(final String value, final int from, final int last) {
        for (int i = from; i < last; i++) {
            final char c = value.charAt(i);
            if (0x21 == c) {
                continue;
            }
            if (c >= 0x2d && c <= 0x3a) {
                continue;
            }
            if (c >= 0x3c && c <= 0x5b) {
                continue;
            }
            if (c >= 0x5d && c <= 0x7e) {
                continue;
            }
            throw new InvalidCharacterException(value, i);
        }
    }

    /**
     * Package private constructor
     */
    Cookie(final CookieName name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    // name............................................................................................

    /**
     * Returns the name of this cookie.
     */
    final public CookieName name() {
        return this.name;
    }

    final CookieName name;

    /**
     * Would be setter that returns a cookie with the given name creating a new instance if necessary.
     */
    abstract public Cookie setName(final CookieName name);

    final Cookie setName0(final CookieName name) {
        checkName(name);
        return this.name.equals(name) ?
                this :
                this.replace(name, this.value);
    }

    // value............................................................................................

    /**
     * Getter that returns the value for this cookie.
     */
    final public String value() {
        return this.value;
    }

    /**
     * Would be setter that returns a cookie with the given value creating a new instance if necessary.
     */
    abstract public Cookie setValue(final String value);

    final Cookie setValue0(final String value) {
        checkValue(value);
        return this.value.equals(value) ?
                this :
                this.replace(this.name, value);
    }

    final String value;

    // replace............................................................................................

    abstract Cookie replace(final CookieName name, final String value);

    /**
     * Cast method used by set name / set value.
     */
    final <T extends Cookie> T cast() {
        return Cast.to(this);
    }

    // misc............................................................................................

    /**
     * Only {@link ClientCookie} returns true.
     */
    abstract public boolean isClient();

    /**
     * Only {@link ServerCookie} returns true.
     */
    abstract public boolean isServer();

    /**
     * The name of the {@link HttpHeaderName} used to encode this cookie.
     */
    abstract HttpHeaderName header();

    /**
     * Creates a new {@link javax.servlet.http.Cookie}
     */
    final javax.servlet.http.Cookie createJavaxServletCookieWithNameAndValue() {
        return new javax.servlet.http.Cookie(this.name.value(), this.value);
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public final boolean isMultipart() {
        return false;
    }

    // Object .............................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final Cookie other) {
        return this.name.equals(other.name()) &&
                this.value.equals(other.value()) &&
                this.equals1(other);
    }

    abstract boolean equals1(final Cookie other);

    /**
     * Dumps all attributes.
     */
    @Override
    final public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public final void buildToString(final ToStringBuilder builder) {
        builder.separator(PARAMETER_SEPARATOR.string());
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        builder.label(this.name.value());
        builder.value(CharSequences.quoteIfNecessary(this.value));

        this.appendAttributes(builder);
        builder.append(PARAMETER_SEPARATOR);
    }

    abstract void appendAttributes(final ToStringBuilder builder);
}
