/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.type.JavaVisibility;

import java.math.BigInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosHandlerRouterMapperTest extends HateosHandlerRouterTestCase<HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2>> {

    private final static Function<String, BigInteger> STRING_TO_ID = BigInteger::new;
    private final static Class<TestHateosResource> RESOURCE_TYPE = TestHateosResource.class;
    private final static Class<TestHateosResource2> COLLECTION_RESOURCE_TYPE = TestHateosResource2.class;

    @Test
    public void testWithNullStringToIdFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(null,
                    RESOURCE_TYPE,
                    COLLECTION_RESOURCE_TYPE);
        });
    }

    @Test
    public void testWithNullInputResourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(STRING_TO_ID,
                    null,
                    COLLECTION_RESOURCE_TYPE);
        });
    }

    @Test
    public void testWithNullOutputResourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(STRING_TO_ID,
                    RESOURCE_TYPE,
                    null);
        });
    }

    @Test
    public void testCopy() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mappers = this.mapper();

        final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> get = this.handler();
        final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> post = this.handler();
        final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> put = this.handler();
        final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> delete = this.handler();

        mappers.get = get;
        mappers.post = post;
        mappers.put = put;
        mappers.delete = delete;

        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> copy = mappers.copy();

        assertSame(get, copy.get, "GET");
        assertSame(post, copy.post, "POST");
        assertSame(put, copy.put, "PUT");
        assertSame(delete, copy.delete, "DELETE");
    }

    private HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler() {
        return new FakeHateosHandler<>();
    }

    @Test
    public void testToString() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mappers = this.mapper();

        mappers.get = this.handler("G1");
        mappers.post = this.handler("P2");
        mappers.put = this.handler("P3");
        mappers.delete = this.handler("D4");

        this.toStringAndCheck(mappers, "GET=G1 POST=P2 PUT=P3 DELETE=D4");
    }

    private HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler(final String toString) {
        return new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
            public String toString() {
                return toString;
            }
        };
    }

    private HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper() {
        return HateosHandlerRouterMapper.with(STRING_TO_ID,
                RESOURCE_TYPE,
                COLLECTION_RESOURCE_TYPE);
    }

    @Override
    public Class<HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2>> type() {
        return Cast.to(HateosHandlerRouterMapper.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    String typeNamePrefix2() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return "Mapper";
    }
}
