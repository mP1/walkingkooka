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

package walkingkooka.net;

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link Name} that holds a URI/URL scheme and when testing for equality case is insignificant.
 */
public final class UrlScheme
        implements Name,
        Comparable<UrlScheme>,
        Serializable {

    // constants

    /**
     * A read only cache of already prepared {@link UrlScheme schemes}.
     */
    final static Map<String, UrlScheme> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link UrlScheme} to the cache being built.
     */
    static UrlScheme registerConstant(final String scheme) {
        final UrlScheme urlScheme = new UrlScheme(scheme);
        UrlScheme.CONSTANTS.put(scheme, urlScheme);
        return urlScheme;
    }

    /**
     * Holds <code>http</code>
     */
    public final static UrlScheme HTTP = UrlScheme.registerConstant("http");

    /**
     * Holds <code>https</code>
     */
    public final static UrlScheme HTTPS = UrlScheme.registerConstant("https");

    /**
     * <a href="https://tools.ietf.org/html/rfc3986#section-3"></a>
     * <pre>
     * 3.1.  Scheme
     *
     * Each URI begins with a scheme name that refers to a specification for
     * assigning identifiers within that scheme.  As such, the URI syntax is
     * a federated and extensible naming system wherein each scheme's
     * specification may further restrict the syntax and semantics of
     * identifiers using that scheme.
     *
     * Scheme names consist of a sequence of characters beginning with a
     * letter and followed by any combination of letters, digits, plus
     * ("+"), period ("."), or hyphen ("-").  Although schemes are case-
     * insensitive, the canonical form is lowercase and documents that
     * specify schemes must do so with lowercase letters.  An implementation
     * should accept uppercase letters as equivalent to lowercase in scheme
     * names (e.g., allow "HTTP" as well as "http") for the sake of
     * robustness but should only produce lowercase scheme names for
     * consistency.
     *
     *    scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )    
     * </pre>
     */
    private final static CharPredicate INITIAL = CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z'));

    /**
     * <pre>
     * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
     * other = "+" | "-" | "."
     * </pre>
     */
    private final static CharPredicate PART = CharPredicateBuilder.empty()
            .any("0123456789")
            .any("+-.")
            .or(INITIAL)
            .build();

    // schemes

    /**
     * Factory that creates a {@link UrlScheme}.
     * <pre>
     * ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
     * </pre>
     */
    public static UrlScheme with(final String name) {
        UrlScheme scheme = UrlScheme.CONSTANTS.get(name);
        if (null == scheme) {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "UrlScheme", INITIAL, PART);
            scheme = new UrlScheme(name);
        }

        return scheme;
    }

    /**
     * package private constructor.
     */
    UrlScheme(final String name) {
        super();
        this.name = name;
        this.nameWithSlashes = name.toLowerCase() + "://";
    }

    // Name

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Returns the scheme with <code>://</code>
     */
    public String nameWithSlashes() {
        return this.nameWithSlashes;
    }

    private final String nameWithSlashes;


    /**
     * Combines this scheme and a {@link HostAddress} giving an {@link AbsoluteUrl}
     */
    public AbsoluteUrl andHost(final HostAddress address) {
        Objects.requireNonNull(address, "address");

        return Url.absolute(this, UrlCredentials.NO_CREDENTIALS, address, IpPort.WITHOUT_PORT, UrlPath.EMPTY, UrlQueryString.EMPTY, UrlFragment.EMPTY);
    }

    // Comparable

    @Override
    public int compareTo(final UrlScheme scheme) {
        return CASE_SENSITIVITY.comparator().compare(this.name, scheme.name);
    }

    // Object

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof UrlScheme && this.equals0((UrlScheme) other);
    }

    private boolean equals0(final UrlScheme other) {
        return this.compareTo(other) == 0;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Returns the raw {@link String}.
     */
    @Override
    public String toString() {
        return this.name;
    }

    final void toString0(final StringBuilder b) {
        b.append(this.nameWithSlashes);
    }

    // Serializable

    private final static long serialVersionUID = 1L;

    /**
     * Keeps constants singletons
     */
    private Object readResolve() {
        final UrlScheme constant = UrlScheme.CONSTANTS.get(this.name);
        return null != constant ? constant : this;
    }
}
