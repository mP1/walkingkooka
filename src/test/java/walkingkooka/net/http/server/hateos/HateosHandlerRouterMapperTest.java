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
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosHandlerRouterMapperTest extends HateosHandlerRouterTestCase<HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3>> {

    private final static Function<String, BigInteger> STRING_TO_ID = BigInteger::new;
    private final static Class<TestHateosResource> INPUT_RESOURCE_TYPE = TestHateosResource.class;
    private final static Class<TestHateosResource3> OUTPUT_RESOURCE_TYPE = TestHateosResource3.class;

    @Test
    public void testWithNullStringToIdFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(null,
                    INPUT_RESOURCE_TYPE,
                    OUTPUT_RESOURCE_TYPE);
        });
    }

    @Test
    public void testWithNullInputResourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(STRING_TO_ID,
                    null,
                    OUTPUT_RESOURCE_TYPE);
        });
    }

    @Test
    public void testWithNullOutputResourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HateosHandlerRouterMapper.with(STRING_TO_ID,
                    INPUT_RESOURCE_TYPE,
                    null);
        });
    }

    @Test
    public void testCopy() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> mappers = this.mapper();

        final HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> getId = this.idMapping();
        final HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> postId = this.idMapping();
        final HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> putId = this.idMapping();
        final HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> deleteId = this.idMapping();
        
        mappers.getId = getId;
        mappers.postId = postId;
        mappers.putId = putId;
        mappers.deleteId = deleteId;

        final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> getIdRange = this.idRangeMapping();
        final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> postIdRange = this.idRangeMapping();
        final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> putIdRange = this.idRangeMapping();
        final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> deleteIdRange = this.idRangeMapping();

        mappers.getIdRange = getIdRange;
        mappers.postIdRange = postIdRange;
        mappers.putIdRange = putIdRange;
        mappers.deleteIdRange = deleteIdRange;

        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> copy = mappers.copy();

        assertSame(getId, copy.getId, "GET");
        assertSame(postId, copy.postId, "POST");
        assertSame(putId, copy.putId, "PUT");
        assertSame(deleteId, copy.deleteId, "DELETE");

        assertSame(getIdRange, copy.getIdRange, "GET");
        assertSame(postIdRange, copy.postIdRange, "POST");
        assertSame(putIdRange, copy.putIdRange, "PUT");
        assertSame(deleteIdRange, copy.deleteIdRange, "DELETE");
    }

    private HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idMapping() {
        return this.idMapping(new FakeHateosIdResourceResourceHandler<>());
    }

    private HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idRangeMapping() {
        return this.idRangeMapping(new FakeHateosIdRangeResourceCollectionResourceCollectionHandler<>());
    }
    
    @Test
    public void testToString() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> mappers = this.mapper();

        mappers.getId = this.idMapping("G1");
        mappers.postId = this.idMapping("P2");
        mappers.putId = this.idMapping("P3");
        mappers.deleteId = this.idMapping("D4");

        this.toStringAndCheck(mappers, "GET=G1 POST=P2 PUT=P3 DELETE=D4");
    }

    @Test
    public void testToString2() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> mappers = this.mapper();

        mappers.getIdRange = this.idRangeMapping("G1");
        mappers.postIdRange = this.idRangeMapping("P2");
        mappers.putIdRange = this.idRangeMapping("P3");
        mappers.deleteIdRange = this.idRangeMapping("D4");

        this.toStringAndCheck(mappers, "GET=G1 POST=P2 PUT=P3 DELETE=D4");
    }

    @Test
    public void testToString3() {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> mappers = this.mapper();

        mappers.getId = this.idMapping("G1");
        mappers.postId = this.idMapping("P2");
        mappers.putId = this.idMapping("P3");
        mappers.deleteId = this.idMapping("D4");

        mappers.getIdRange = this.idRangeMapping("G5");
        mappers.postIdRange = this.idRangeMapping("P6");
        mappers.putIdRange = this.idRangeMapping("P7");
        mappers.deleteIdRange = this.idRangeMapping("D8");

        this.toStringAndCheck(mappers, "GET=G1, G5 POST=P2, P6 PUT=P3, P7 DELETE=D4, D8");
    }

    private HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idMapping(final String toString) {
        return this.idMapping(new FakeHateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource3>() {
            public String toString() {
                return toString;
            }
        });
    }

    private HateosHandlerRouterMapperHateosIdHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idMapping(final HateosIdResourceResourceHandler<BigInteger, TestHateosResource, TestHateosResource3> handler) {
        return HateosHandlerRouterMapperHateosIdHandlerMapping.idResourceResource(handler);
    }

    private HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idRangeMapping(final String toString) {
        return this.idRangeMapping(new FakeHateosIdRangeResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource3>() {
            public String toString() {
                return toString;
            }
        });
    }

    private HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, BigInteger, TestHateosResource, TestHateosResource3> idRangeMapping(final HateosIdRangeResourceCollectionResourceCollectionHandler<BigInteger, TestHateosResource, TestHateosResource3> handler) {
        return HateosHandlerRouterMapperHateosIdRangeHandlerMapping.idRangeResourceCollectionResourceCollection(handler);
    }

    private HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3> mapper() {
        return HateosHandlerRouterMapper.with(STRING_TO_ID,
                INPUT_RESOURCE_TYPE,
                OUTPUT_RESOURCE_TYPE);
    }

    @Override
    public Class<HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource3>> type() {
        return Cast.to(HateosHandlerRouterMapper.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
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
