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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosHandlerMapperTest implements ClassTesting2<HateosHandlerMapper<BigInteger, TestHateosResource>>,
        ToStringTesting<HateosHandlerMapper<BigInteger, TestHateosResource>> {

    private final static Function<String, BigInteger> STRING_TO_ID = BigInteger::new;
    private final static Class<TestHateosResource> RESOURCE_TYPE = TestHateosResource.class;

    @Test
    public void testWithNullStringToIdFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerMapper.with(null,
                    RESOURCE_TYPE);
        });
    }

    @Test
    public void testWithNullResourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerMapper.with(STRING_TO_ID,
                    null);
        });
    }

    @Test
    public void testCopy() {
        final HateosHandlerMapper<BigInteger, TestHateosResource> mappers = this.mapper();

        final FakeHateosHandler<BigInteger, TestHateosResource> get = new FakeHateosHandler<>();
        final FakeHateosHandler<BigInteger, TestHateosResource> post = new FakeHateosHandler<>();
        final FakeHateosHandler<BigInteger, TestHateosResource> put = new FakeHateosHandler<>();
        final FakeHateosHandler<BigInteger, TestHateosResource> delete = new FakeHateosHandler<>();

        mappers.get = get;
        mappers.post = post;
        mappers.put = put;
        mappers.delete = delete;

        final HateosHandlerMapper<BigInteger, TestHateosResource> copy = mappers.copy();

        assertSame(get, copy.get, "GET");
        assertSame(post, copy.post, "POST");
        assertSame(put, copy.put, "PUT");
        assertSame(delete, copy.delete, "DELETE");
    }

    @Test
    public void testToString() {
        final HateosHandlerMapper<BigInteger, TestHateosResource> mappers = this.mapper();

        mappers.get = this.handler("G1");
        mappers.post = this.handler("P2");
        mappers.put = this.handler("P3");
        mappers.delete = this.handler("D4");
        ;

        this.toStringAndCheck(mappers, "GET=G1 POST=P2 PUT=P3 DELETE=D4");
    }

    private FakeHateosHandler<BigInteger, TestHateosResource> handler(final String toString) {
        return new FakeHateosHandler<BigInteger, TestHateosResource>() {
            public String toString() {
                return toString;
            }
        };
    }

    private HateosHandlerMapper<BigInteger, TestHateosResource> mapper() {
        return HateosHandlerMapper.with(STRING_TO_ID,
                RESOURCE_TYPE);
    }

    @Override
    public Class<HateosHandlerMapper<BigInteger, TestHateosResource>> type() {
        return Cast.to(HateosHandlerMapper.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
