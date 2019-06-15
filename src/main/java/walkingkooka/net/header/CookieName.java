/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import walkingkooka.naming.Name;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Optional;

/**
 * The {@link Name} of {@link Cookie}. Note that cookie names must start with an ASCII letter and be composed of only ASCII letters, digits and dash.
 * Though more characters are valid, in the interests of capability and simplicity this limited sub set is enforced.
 */
final public class CookieName extends HeaderNameValue
        implements HttpRequestAttribute<ClientCookie>, Comparable<CookieName> {

    /**
     * A <cookie-name> can be any US-ASCII characters except control characters (CTLs), spaces, or tabs.
     * It also must not contain a separator character like the following: ( ) < > @ , ; : \ " /  [ ] ? = { }.
     */
    private final static CharPredicate PREDICATE = CharPredicates.builder()//
            .or(CharPredicates.asciiPrintable())//
            .andNot(CharPredicates.rfc2045TokenSpecial())
            .toString("cookie name")//
            .build();

    /**
     * Factory that creates a {@link CookieName}
     */
    public static CookieName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", PREDICATE);
        return new CookieName(name);
    }

    /**
     * Private constructor use factory
     */
    private CookieName(final String name) {
        super(name);
    }

    // HttpRequestAttribute..............................................................................................

    /**
     * A typed getter that retrieves a value from a {@link HttpRequest}
     */
    @Override
    public Optional<ClientCookie> parameterValue(final HttpRequest request) {
        return HttpHeaderName.COOKIE.headerValue(request.headers())
                .orElse(ClientCookie.NO_COOKIES)
                .stream()
                .filter(c -> c.name().equals(this))
                .findFirst();
    }

    // Comparable..............................................................................................

    @Override
    public int compareTo(final CookieName other) {
        return this.compareTo0(other);
    }

    // HeaderNameValue..............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CookieName;
    }

    // Object..................................................................................................

    /**
     * Dumps the cookie name
     */
    @Override
    public String toString() {
        return this.name;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
