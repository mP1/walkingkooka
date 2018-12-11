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

package walkingkooka.net.http.cookie;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderScope;
import walkingkooka.text.Whitespace;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Cookie} that is sent by clients to servers and holds the name and value.
 */
final public class ClientCookie extends Cookie {

    /**
     * Converts a {@link javax.servlet.http.Cookie} into a {@link Cookie}.
     */
    public static ClientCookie from(final javax.servlet.http.Cookie cookie) {
        Objects.requireNonNull(cookie, "cookie");

        return ClientCookie.with(CookieName.with(cookie.getName()), Cookie.convertEmptyToNull(cookie.getValue()));
    }

    /**
     * Creates a {@link ClientCookie} from a {@link ServerCookie cookie}. This is only called by {@link ServerCookie#toClient()}
     */
    static ClientCookie from(final ServerCookie cookie) {
        return new ClientCookie(cookie.name(), cookie.value());
    }

    /**
     * Parses a request header return a single {@link Cookie}. <br>
     * <a ref="http://en.wikipedia.org/wiki/HTTP_cookie} Client cookie"></a>
     *
     * <pre>
     * GET /spec.html HTTP/1.1
     * Host: www.example.org
     * Cookie: name=value; name2=value2
     * </pre>
     */
    public static List<ClientCookie> parseHeader(final String header) {
        Whitespace.failIfNullOrEmptyOrWhitespace(header, "header");

        final char nameValueSeparator = PARAMETER_NAME_VALUE_SEPARATOR.character();
        final List<ClientCookie> cookies = Lists.array();
        final String[] tokens = header.split(PARAMETER_SEPARATOR.string());

        for (int i = 0; i < tokens.length; i++) {
            final String token = tokens[i];
            final int nameEnd = token.indexOf(nameValueSeparator);
            cookies.add(Cookie.client(//
                    CookieName.with(token.substring(0, -1 == nameEnd ? token.length() : nameEnd).trim()), // name
                    Cookie.extractValue(token))); // value
        }
        return Lists.readOnly(cookies);
    }

    /**
     * Formats a list of cookies into their header string form. Basically the inverse of {@link #parseHeader(String)}.
     */
    public static String toHeaderTextList(final List<ClientCookie> cookies) {
        Objects.requireNonNull(cookies, "cookies");

        return cookies.stream()
                .map(c -> c.toHeaderText())
                .collect(Collectors.joining(FORMAT_SEPARATOR));
    }

    private final static String FORMAT_SEPARATOR = " ";

    /**
     * Factory that creates a {@link ClientCookie}
     */
    static ClientCookie with(final CookieName name, final String value) {
        checkName(name);
        checkValue(value);

        return new ClientCookie(name, value);
    }

    /**
     * Private constructor use static factory
     */
    private ClientCookie(final CookieName name, final String value) {
        super(name, value);
    }

    @Override
    public ClientCookie setName(final CookieName name) {
        return this.setName0(name).cast();
    }

    @Override
    public ClientCookie setValue(final String value) {
        return this.setValue0(value).cast();
    }

    @Override
    Cookie replace(final CookieName name, final String value) {
        return new ClientCookie(name, value);
    }

    /**
     * Always returns this.
     */
    @Override
    public boolean isClient() {
        return true;
    }

    /**
     * Always returns null.
     */
    @Override
    public boolean isServer() {
        return false;
    }

    /**
     * Always returns {@link HttpHeaderName#COOKIE}.
     */
    @Override
    public HttpHeaderName header() {
        return HttpHeaderName.COOKIE;
    }

    /**
     * Returns this cookie in header value form.
     */
    @Override
    public String toHeaderText() {
        return this.toString();
    }

    // HasHttpHeaderScope ....................................................................................................

    @Override
    public HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST;
    }

    /**
     * Returns a {@link javax.servlet.http.Cookie}
     */
    public javax.servlet.http.Cookie toJavaxServletCookie() {
        return this.createJavaxServletCookieWithNameAndValue();
    }

    /**
     * Does nothing client cookies only have name and value which is already added.
     */
    @Override
    void appendAttributes(final ToStringBuilder builder) {
        //
    }

    // Object .................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ClientCookie;
    }

    @Override
    boolean equals1(final Cookie other) {
        return true;
    }
}
