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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.http.server.HttpRequestAttribute;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HateosIdResourceValueHandlerTestingTest extends HateosHandlerTestingTestCase<FakeHateosIdResourceValueHandler<BigInteger, TestHateosResource, TestHateosResource2>>
        implements HateosIdResourceValueHandlerTesting<FakeHateosIdResourceValueHandler<BigInteger, TestHateosResource, TestHateosResource2>, BigInteger, TestHateosResource, TestHateosResource2> {

    @Test
    public void testHandleAndCheck() {
        final BigInteger id = this.id();
        final Optional<TestHateosResource> resources = this.resource();
        final Map<HttpRequestAttribute<?>, Object> parameters = this.parameters();

        final int value = 999;

        this.handleAndCheck(new FakeHateosIdResourceValueHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                public Object handle(final BigInteger id,
                                                     final Optional<TestHateosResource> resource,
                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                    Objects.requireNonNull(id, "id");
                                    Objects.requireNonNull(resource, "resource");
                                    Objects.requireNonNull(parameters, "parameters");

                                    return value;
                                }
                            },
                id,
                resources,
                parameters,
                value);
    }

    @Override
    public FakeHateosIdResourceValueHandler<BigInteger, TestHateosResource, TestHateosResource2> createHandler() {
        return new FakeHateosIdResourceValueHandler<>();
    }

    @Override
    public Optional<TestHateosResource> resource() {
        return Optional.of(TestHateosResource.with(BigInteger.valueOf(111)));
    }

    @Override
    public Class<FakeHateosIdResourceValueHandler<BigInteger, TestHateosResource, TestHateosResource2>> type() {
        return Cast.to(FakeHateosIdResourceValueHandler.class);
    }
}
