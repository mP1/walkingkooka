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

import org.junit.Test;

import java.util.Optional;

public final class AbsoluteUrlEqualityTest extends UrlEqualityTestCase<AbsoluteUrl> {

    // constants

    private final static UrlScheme SCHEME = UrlScheme.HTTP;
    private final static Optional<UrlCredentials> CREDENTIALS = Optional.of(UrlCredentials.with("user", "pass"));
    private final static HostAddress HOST = HostAddress.with("host");
    private final static Optional<IpPort> PORT = Optional.of(IpPort.HTTP);
    private final static UrlPath PATH = UrlPath.parse("/path");
    private final static UrlQueryString QUERY = UrlQueryString.with("query=value");
    private final static UrlFragment FRAGMENT = UrlFragment.with("fragment123");

    // tests

    @Test
    public void testDifferentScheme() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT));
    }

    @Test
    public void testDifferentHost() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT));
    }

    @Test
    public void testDifferentPort() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                Optional.of(IpPort.with(9999)),
                PATH,
                QUERY,
                FRAGMENT));
    }

    // factory

    @Override
    protected AbsoluteUrl createObject(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return Url.absolute(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                path,
                query,
                fragment);
    }
}
