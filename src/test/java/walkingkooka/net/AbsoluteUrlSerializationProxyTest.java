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
import walkingkooka.io.serialize.SerializationProxyTestCase;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class AbsoluteUrlSerializationProxyTest extends SerializationProxyTestCase<AbsoluteUrlSerializationProxy> {

    @Test
    public void testToString() {
        final AbsoluteUrl url = UrlScheme.HTTPS.andHost(HostAddress.with("example.com"))
                .setPort(Optional.of(IpPort.with(8080)))
                .setPath(UrlPath.parse("/abc/def"))
                .setQuery(UrlQueryString.with("ghi=jkl"));
        assertEquals(url.toString(), url.writeReplace().toString());
    }

    @Override
    public Class<AbsoluteUrlSerializationProxy> type() {
        return AbsoluteUrlSerializationProxy.class;
    }
}
