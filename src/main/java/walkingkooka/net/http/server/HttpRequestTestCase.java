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

package walkingkooka.net.http.server;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.Assert.assertNotEquals;

public abstract class HttpRequestTestCase<R extends HttpRequest> extends ClassTestCase<R> {

    @Test
    public void testRoutingParameters() {
        final Map<HttpRequestAttribute, Object> routingParameters = this.createRequest().routingParameters();
        assertNotEquals("method absent", null, routingParameters.get(HttpRequestAttributes.METHOD));
        assertNotEquals("transport absent", null, routingParameters.get(HttpRequestAttributes.TRANSPORT));
        assertNotEquals("protocol absent", null, routingParameters.get(HttpRequestAttributes.HTTP_PROTOCOL_VERSION));
    }


    protected abstract R createRequest();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
