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

import walkingkooka.io.serialize.SerializationProxy;
import walkingkooka.text.CharSequences;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * Note that equality is based by comparing all components, with only the scheme being compared while ignoring case.
 */
public final class AbsoluteUrl extends Url {

    public final static Optional<UrlCredentials> NO_CREDENTIALS = Optional.empty();
    public final static Optional<IpPort> NO_PORT = Optional.empty();

    /**
     * Parses a {@link String url} into a {@link AbsoluteUrl}
     */
    public static AbsoluteUrl parse(final String url) {
        return parseAbsolute0(url);
    }

    /**
     * Parses a {@link String url} into a {@link AbsoluteUrl}
     */
    private static AbsoluteUrl parseAbsolute0(final String url) {
        Objects.requireNonNull(url, "url");

        try {
            return parseAbsolute1(new URL(url));
        } catch (final MalformedURLException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static AbsoluteUrl parseAbsolute1(final URL url) {
        return AbsoluteUrl.with(UrlScheme.with(url.getProtocol()),
                credentials(url),
                HostAddress.with(url.getHost()),
                ipPort(url),
                UrlPath.parse(url.getPath()),
                UrlQueryString.with(nullToEmpty(url.getQuery())),
                UrlFragment.with(nullToEmpty(url.getRef())));
    }

    private static Optional<UrlCredentials> credentials(final URL url) {
        final String userInfo = url.getUserInfo();
        return CharSequences.isNullOrEmpty(userInfo) ?
                NO_CREDENTIALS :
                credentials0(userInfo);
    }

    private static Optional<UrlCredentials> credentials0(final String userInfo) {
        final int separator = userInfo.indexOf(":");
        if (-1 == separator) {
            throw new IllegalArgumentException("Invalid user credentials " + userInfo);
        }
        return Optional.of(UrlCredentials.with(
                userInfo.substring(0, separator),
                userInfo.substring(separator + 1)));
    }

    private static Optional<IpPort> ipPort(final URL url) {
        final int value = url.getPort();
        return -1 != value ?
                Optional.of(IpPort.with(value)) :
                NO_PORT;
    }

    /**
     * Factory that creates an {@link AbsoluteUrl}
     */
    static AbsoluteUrl with(final UrlScheme scheme,
                            final Optional<UrlCredentials> credentials,
                            final HostAddress host,
                            final Optional<IpPort> port,
                            final UrlPath path,
                            final UrlQueryString query,
                            final UrlFragment fragment) {
        Objects.requireNonNull(scheme, "scheme");
        Objects.requireNonNull(credentials, "credentials");
        Objects.requireNonNull(host, "host");
        Objects.requireNonNull(port, "port");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(query, "query");
        Objects.requireNonNull(fragment, "fragment");

        return new AbsoluteUrl(scheme, credentials, host, port, path, query, fragment);
    }

    /**
     * Private constructor.
     */
    private AbsoluteUrl(final UrlScheme scheme,
                        final Optional<UrlCredentials> credentials,
                        final HostAddress host,
                        final Optional<IpPort> port,
                        final UrlPath path,
                        final UrlQueryString query,
                        final UrlFragment fragment) {
        super(path, query, fragment);

        this.scheme = scheme;
        this.credentials = credentials;
        this.host = host;
        this.port = port;
    }

    // Url

    @Override
    public AbsoluteUrl setPath(final UrlPath path) {
        return this.setPath0(path).cast();
    }

    @Override
    public AbsoluteUrl setQuery(final UrlQueryString query) {
        return this.setQuery0(query).cast();
    }

    @Override
    public AbsoluteUrl setFragment(final UrlFragment fragment) {
        return this.setFragment0(fragment).cast();
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }

    /**
     * Unconditionally creates a new {@link AbsoluteUrl}
     */
    @Override
    final AbsoluteUrl replace(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return new AbsoluteUrl(this.scheme, this.credentials, this.host, this.port, path, query, fragment);
    }

    /**
     * Returns the scheme with this URL.
     */
    public UrlScheme scheme() {
        return this.scheme;
    }

    public AbsoluteUrl setScheme(final UrlScheme scheme) {
        Objects.requireNonNull(scheme, "scheme");

        return this.scheme.equals(scheme) ?
                this :
                new AbsoluteUrl(scheme, this.credentials, this.host, this.port, this.path, this.query, this.fragment);
    }

    final UrlScheme scheme;

    /**
     * Returns the credentials with this URL.
     */
    public Optional<UrlCredentials> credentials() {
        return this.credentials;
    }

    public AbsoluteUrl setCredentials(final Optional<UrlCredentials> credentials) {
        Objects.requireNonNull(credentials, "credentials");

        return this.credentials.equals(credentials) ?
                this :
                new AbsoluteUrl(this.scheme, credentials, this.host, this.port, this.path, this.query, this.fragment);
    }

    final Optional<UrlCredentials> credentials;

    /**
     * Retrieves the {@link HostAddress} embedded within this URL.
     */
    public HostAddress host() {
        return this.host;
    }

    public AbsoluteUrl setHost(final HostAddress host) {
        Objects.requireNonNull(host, "host");

        return this.host.equals(host) ?
               this :
               new AbsoluteUrl(this.scheme, this.credentials, host, this.port, this.path, this.query, this.fragment);
    }

    final HostAddress host;

    /**
     * Retrieves the {@link IpPort} within this URL. Note this value is never null and if not present in the URL will have the default for the scheme.
     */
    public Optional<IpPort> port() {
        return this.port;
    }

    public AbsoluteUrl setPort(final Optional<IpPort> port) {
        Objects.requireNonNull(port, "port");

        return this.port.equals(port) ?
                this :
                new AbsoluteUrl(this.scheme, this.credentials, this.host, port, this.path, this.query, this.fragment);
    }

    final Optional<IpPort> port;

    @Override
    public AbsoluteUrl set(final UrlScheme scheme,
                                    final Optional<UrlCredentials> credentials,
                                    final HostAddress host,
                                    final Optional<IpPort> port){
        return this.setScheme(scheme)
               .setCredentials(credentials)
               .setHost(host)
               .setPort(port);
    }

    /**
     * Returns a {@link RelativeUrl} built from the path, query and fragment components.
     */
    public RelativeUrl relativeUrl() {
        return RelativeUrl.with(this.path, this.query, this.fragment);
    }

    // Object ......................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.scheme,
                this.credentials,
                this.host,
                this.port,
                this.path,
                this.query,
                this.fragment);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof AbsoluteUrl &&
                this.equals0((AbsoluteUrl) other);
    }

    private boolean equals0(final AbsoluteUrl other) {
        return this.scheme.equals(other.scheme) && //
                this.credentials.equals(other.credentials) && //
                this.host.equals(other.host) && //
                this.port.equals(other.port) && //
                this.path.equals(other.path) && //
                this.query.equals(other.query) && //
                this.fragment.equals(other.fragment);
    }

    @Override
    void toString0(final StringBuilder b) {
        this.scheme.toString0(b);
        if(this.credentials.isPresent()) {
            this.credentials.get().toString0(b);
        }
        this.host.toString0(b);
        if(this.port.isPresent()) {
            this.port.get().toString0(b);
        }
    }

    // Serializable

    /**
     * Returns either of the two {@link SerializationProxy}
     */
    // @VisibleForTesting
    final Object writeReplace() {
        return new AbsoluteUrlSerializationProxy(this);
    }

    /**
     * Should never be called expect a serialization proxy
     */
    private Object readResolve() {
        throw new UnsupportedOperationException();
    }

    private final static long serialVersionUID = 1L;
}
