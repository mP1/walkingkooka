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

import java.util.Optional;

final class AbsoluteUrlSerializationProxy implements SerializationProxy {

    AbsoluteUrlSerializationProxy(final AbsoluteUrl url) {
        this.scheme = url.scheme;
        this.credentials = unwrap(url.credentials);
        this.host = url.host;
        this.port = unwrap(url.port);
        this.path = url.path;
        this.query = url.query;
        this.fragment = url.fragment;
    }

    private static <T> T unwrap(final Optional<T> value) {
        return value.isPresent() ? value.get() : null;
    }

    final UrlScheme scheme;
    final UrlCredentials credentials;
    final HostAddress host;
    final IpPort port;
    final UrlPath path;
    final UrlQueryString query;
    final UrlFragment fragment;

    private Object readResolve() {
        return AbsoluteUrl.with(this.scheme,
                Optional.ofNullable(this.credentials),
                this.host,
                Optional.of(this.port),
                this.path,
                this.query,
                this.fragment);
    }

    @Override
    public String toString() {
        return this.readResolve().toString();
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
