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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class HttpRequestAttributeTest extends ClassTestCase<HttpRequestAttribute<?>> {

    @Test
    public void testParameterValueHttpMethodl() {
        this.parameterValueAndCheck(HttpRequestAttributes.METHOD, HttpMethod.GET);
        this.parameterValueAndCheck(HttpRequestAttributes.METHOD, HttpMethod.POST);
    }

    @Test
    public void testParameterValueHttpProtocol() {
        this.parameterValueAndCheck(HttpRequestAttributes.HTTP_PROTOCOL_VERSION, HttpProtocolVersion.VERSION_1_0);
    }

    @Test
    public void testParameterValueTransport() {
        this.parameterValueAndCheck(HttpRequestAttributes.TRANSPORT, HttpTransport.SECURED);
        this.parameterValueAndCheck(HttpRequestAttributes.TRANSPORT, HttpTransport.UNSECURED);
    }

    private <T> void parameterValueAndCheck(final HttpRequestAttribute<T> parameter, final T value) {
        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.one(parameter, value);
        assertEquals(parameters.toString(), Optional.of(value), parameter.parameterValue(parameters));
    }

    @Override
    protected Class<HttpRequestAttribute<?>> type() {
        return Cast.to(HttpRequestAttribute.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
