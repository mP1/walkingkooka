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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.http.server.HttpRequestAttribute;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class HateosIdResourceCollectionResourceCollectionHandlerTestingTest extends HateosHandlerTestingTestCase<FakeHateosIdResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>>
        implements HateosIdResourceCollectionResourceCollectionHandlerTesting<FakeHateosIdResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>, BigInteger, TestHateosResource, TestHateosResource2> {

    @Test
    public void testHandleAndCheck() {
        final BigInteger id = this.id();
        final List<TestHateosResource> resources = Lists.of(TestHateosResource.with(BigInteger.valueOf(999)));
        final Map<HttpRequestAttribute<?>, Object> parameters = this.parameters();

        final List<TestHateosResource2> resource2 = Lists.of(TestHateosResource2.with(BigInteger.valueOf(222)));

        this.handleAndCheck(new FakeHateosIdResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                @Override
                                public List<TestHateosResource2> handle(final BigInteger i,
                                                                            final List<TestHateosResource> r,
                                                                            final Map<HttpRequestAttribute<?>, Object> p) {
                                    assertSame(id, i);
                                    assertSame(resources, r);
                                    assertSame(parameters, p);

                                    return resource2;
                                }
                            },
                id,
                resources,
                parameters,
                resource2);
    }

    @Override
    public FakeHateosIdResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2> createHandler() {
        return new FakeHateosIdResourceCollectionResourceCollectionHandler<>();
    }

    @Override
    public final List<TestHateosResource> resourceCollection() {
        return Lists.empty();
    }

    @Override
    public Class<FakeHateosIdResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>> type() {
        return Cast.to(FakeHateosIdResourceCollectionResourceCollectionHandler.class);
    }
}
