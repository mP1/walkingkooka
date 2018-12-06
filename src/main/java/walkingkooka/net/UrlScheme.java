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
    final static Map<String, UrlScheme> CONSTANTS = Maps.sorted();

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

    private final static CharPredicate INITIAL = CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z'));

    /**
     * <pre>
     * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
     * other = "+" | "-" | "."
     * </pre>
     */
    private final static CharPredicate PART = CharPredicateBuilder.create()
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
        this.nameWithSlashes = name + "://";
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
        return this.name.compareToIgnoreCase(scheme.name);
    }

    // Object

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof UrlScheme && this.equals0((UrlScheme) other);
    }

    private boolean equals0(final UrlScheme other) {
        return this.name.equalsIgnoreCase(other.name);
    }

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
