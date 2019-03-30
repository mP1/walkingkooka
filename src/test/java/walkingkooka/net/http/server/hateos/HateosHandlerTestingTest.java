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
import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class HateosHandlerTestingTest implements HateosHandlerTesting<HateosHandler<BigInteger, TestHateosResource, TestHateosResource2>,
        BigInteger, TestHateosResource, TestHateosResource2> {

    @Override
    public void testTestNaming() {

    }

    @Test
    public void testHandleIdAndCheck() {
        final BigInteger id = this.id();
        final Optional<TestHateosResource> resource = this.resource();
        final Map<HttpRequestAttribute<?>, Object> parameters = this.parameters();

        final TestHateosResource2 resource2 = TestHateosResource2.with(BigInteger.valueOf(222));

        this.handleAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
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

    @Test
    public void testHandleCollectionAndCheck() {
        final Range<BigInteger> ids = this.collection();
        final List<TestHateosResource> resources = this.resourceCollection();
        final Map<HttpRequestAttribute<?>, Object> parameters = this.parameters();

        final List<TestHateosResource2> resources2 = Lists.of(TestHateosResource2.with(BigInteger.valueOf(222)));

        this.handleCollectionAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                          @Override
                                          public List<TestHateosResource2> handleCollection(final Range<BigInteger> i,
                                                                                            final List<TestHateosResource> r,
                                                                                            final Map<HttpRequestAttribute<?>, Object> p) {
                                              assertSame(ids, i);
                                              assertSame(resources, r);
                                              assertSame(parameters, p);

                                              return resources2;
                                          }
                                      },
                ids,
                resources,
                parameters,
                resources2);
    }

    @Override
    public HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> createHandler() {
        return new FakeHateosHandler<>();
    }

    @Override
    public Map<HttpRequestAttribute<?>, Object> parameters() {
        return HateosHandler.NO_PARAMETERS;
    }

    @Override
    public BigInteger id() {
        return BigInteger.valueOf(111);
    }

    @Override
    public Optional<TestHateosResource> resource() {
        return Optional.empty();
    }

    @Override
    public Range<BigInteger> collection() {
        return Range.all();
    }

    @Override
    public List<TestHateosResource> resourceCollection() {
        return Lists.empty();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Class<HateosHandler<BigInteger, TestHateosResource, TestHateosResource2>> type() {
        return Cast.to(HateosHandler.class);
    }
}
