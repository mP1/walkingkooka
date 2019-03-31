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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class HateosIdResourceResourceHandlerTestingTest extends HateosHandlerTestingTestCase<FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource2>>
        implements HateosIdResourceResourceHandlerTesting<FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource2>, BigInteger, TestHateosResource, TestHateosResource2> {

    @Test
    public void testHandleIdAndCheck() {
        final BigInteger id = this.id();
        final Optional<TestHateosResource> resource = this.resource();
        final Map<HttpRequestAttribute<?>, Object> parameters = this.parameters();

        final TestHateosResource2 resource2 = TestHateosResource2.with(BigInteger.valueOf(222));

        this.handleAndCheck(new FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                @Override
                                public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                            final Optional<TestHateosResource> r,
                                                                            final Map<HttpRequestAttribute<?>, Object> p) {
                                    assertSame(id, i);
                                    assertSame(resource, r);
                                    assertSame(parameters, p);

                                    return Optional.of(resource2);
                                }
                            },
                id,
                resource,
                parameters,
                Optional.of(resource2));
    }

    @Override
    public FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource2> createHandler() {
        return new FakeHateosIdResourceResourceHandler<>();
    }

    @Override
    public Optional<TestHateosResource> resource() {
        return Optional.of(TestHateosResource.with(BigInteger.valueOf(999)));
    }

    @Override
    public Class<FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource2>> type() {
        return Cast.to(FakeHateosIdResourceResourceHandler.class);
    }
}
