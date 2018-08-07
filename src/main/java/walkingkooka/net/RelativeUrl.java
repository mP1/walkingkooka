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

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link RelativeUrl} holds a URL with getters available to retrieve components.
 */
public final class RelativeUrl extends Url {

    /**
     * Factory.
     */
    static RelativeUrl with(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(query, "query");
        Objects.requireNonNull(fragment, "fragment");

        return new RelativeUrl(path, query, fragment);
    }

    /**
     * Private constructor
     */
    private RelativeUrl(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        super(path, query, fragment);
    }

    // Url

    @Override
    final Url replace(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return new RelativeUrl(path, query, fragment);
    }

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    /**
     * Would be setter that attempts to set or replace the absolute url only properties. This has the added benefit
     * of being useful and able to convert a relative url to an absolute url.
     */
    @Override
    public AbsoluteUrl set(final UrlScheme scheme,
                           final Optional<UrlCredentials> credentials,
                           final HostAddress host,
                           final Optional<IpPort> port){
        return AbsoluteUrl.with(scheme, credentials, host, port, this.path, this.query, this.fragment);
    }
    /**
     * Returns this
     */
    @Override
    public RelativeUrl relativeUrl() {
        return this;
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.path,
                this.query,
                this.fragment);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof RelativeUrl &&
                this.equals0((RelativeUrl) other);
    }

    private boolean equals0(final RelativeUrl other) {
        return this.path.equals(other.path) && //
               this.query.equals(other.query) && //
               this.fragment.equals(other.fragment);
    }

    @Override
    void toString0(final StringBuilder b) {
        // no additional properties to add.
    }

    // Serializable

    private final static long serialVersionUID = 1L;
}
