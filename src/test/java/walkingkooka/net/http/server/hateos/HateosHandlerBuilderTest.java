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
import walkingkooka.build.BuilderTesting;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.Router;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosHandlerBuilderTest implements ClassTesting2<HateosHandlerBuilder<JsonNode>>,
        BuilderTesting<HateosHandlerBuilder<JsonNode>, Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

    private final static Function<String, BigInteger> ID_PARSER = BigInteger::new;

    // creation ..........................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder(null);
        });
    }

    @Test
    public void testWithQueryStringFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createBuilder("http://example/api?param1=value1");
        });
    }

    @Test
    public void testWithFragmentFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createBuilder("http://example/api#fragment-1");
        });
    }

    @Test
    public void testWithNullContentTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder("http://example/api", null);
        });
    }

    // get ..........................................................................................

    @Test
    public void testGetNullResourceNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().get(null, ID_PARSER, LinkRelation.SELF, this.getHandler());
        });
    }

    @Test
    public void testGetNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().get(this.resourceName1(), null, LinkRelation.SELF, this.getHandler());
        });
    }

    @Test
    public void testGetNullRelationFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().get(this.resourceName1(), ID_PARSER,null, this.getHandler());
        });
    }

    @Test
    public void testGetNullHandlerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().get(this.resourceName1(), ID_PARSER, LinkRelation.SELF, null);
        });
    }

    @Test
    public void testGetDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.get(resourceName, ID_PARSER, relation, this.getHandler());

        assertThrows(IllegalArgumentException.class, () -> {
            builder.get(resourceName, ID_PARSER, relation, this.getHandler());
        });
    }

    @Test
    public void testGetRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosGetHandler<BigInteger, JsonNode> handler = this.getHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.get(resourceName, ID_PARSER, relation, handler);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.get(resourceName, ID_PARSER, relation, handler);
        });
    }

    @Test
    public void testGet() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.get(this.resourceName1(), ID_PARSER, relation, this.getHandler()));
        assertSame(builder, builder.get(this.resourceName2(), ID_PARSER, relation, this.getHandler()));
        assertSame(builder, builder.get(this.resourceName1(), ID_PARSER, LinkRelation.ITEM, this.getHandler()));
    }

    private HateosGetHandler<BigInteger, JsonNode> getHandler() {
        return new FakeHateosGetHandler<BigInteger, JsonNode>() {
            @Override
            public String toString() {
                return "get123";
            }
        };
    }

    // post ..........................................................................................

    @Test
    public void testPostNullResourceNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().post(null, ID_PARSER, LinkRelation.SELF, this.postHandler());
        });
    }

    @Test
    public void testPostNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().post(this.resourceName1(), null,LinkRelation.SELF, this.postHandler());
        });
    }

    @Test
    public void testPostNullRelationFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().post(this.resourceName1(), ID_PARSER,null, this.postHandler());
        });
    }

    @Test
    public void testPostNullHandlerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().post(this.resourceName1(), ID_PARSER, LinkRelation.SELF, null);
        });
    }

    @Test
    public void testPostDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.post(resourceName, ID_PARSER, relation, this.postHandler());

        assertThrows(IllegalArgumentException.class, () -> {
            builder.post(resourceName, ID_PARSER, relation, this.postHandler());
        });
    }

    @Test
    public void testPostRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosPostHandler<BigInteger, JsonNode> handler = this.postHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.post(resourceName, ID_PARSER, relation, handler);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.post(resourceName, ID_PARSER, relation, handler);
        });
    }

    @Test
    public void testPost() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.post(this.resourceName1(), ID_PARSER, relation, this.postHandler()));
        assertSame(builder, builder.post(this.resourceName2(), ID_PARSER, relation, this.postHandler()));
        assertSame(builder, builder.post(this.resourceName1(), ID_PARSER, LinkRelation.ITEM, this.postHandler()));
    }

    private HateosPostHandler<BigInteger, JsonNode> postHandler() {
        return new FakeHateosPostHandler<BigInteger, JsonNode>() {
            @Override
            public String toString() {
                return "post234";
            }
        };
    }

    // put ..........................................................................................

    @Test
    public void testPutNullResourceNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().put(null, ID_PARSER, LinkRelation.SELF, this.putHandler());
        });
    }

    @Test
    public void testPutNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().put(this.resourceName1(), null,LinkRelation.SELF, this.putHandler());
        });
    }
    
    @Test
    public void testPutNullRelationFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().put(this.resourceName1(), ID_PARSER,null, this.putHandler());
        });
    }

    @Test
    public void testPutNullHandlerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().put(this.resourceName1(), ID_PARSER, LinkRelation.SELF, null);
        });
    }

    @Test
    public void testPutDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.put(resourceName, ID_PARSER, relation, this.putHandler());
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.put(resourceName, ID_PARSER, relation, this.putHandler());
        });
    }

    @Test
    public void testPutRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosPutHandler<BigInteger, JsonNode> handler = this.putHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.put(resourceName, ID_PARSER, relation, handler);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.put(resourceName, ID_PARSER, relation, handler);
        });
    }

    @Test
    public void testPut() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.put(this.resourceName1(), ID_PARSER, relation, this.putHandler()));
        assertSame(builder, builder.put(this.resourceName2(), ID_PARSER, relation, this.putHandler()));
        assertSame(builder, builder.put(this.resourceName1(), ID_PARSER, LinkRelation.ITEM, this.putHandler()));
    }

    private HateosPutHandler<BigInteger, JsonNode> putHandler() {
        return new FakeHateosPutHandler<BigInteger, JsonNode>() {
            @Override
            public String toString() {
                return "put345";
            }
        };
    }
    // delete ..........................................................................................

    @Test
    public void testDeleteNullResourceNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().delete(null, ID_PARSER, LinkRelation.SELF, this.deleteHandler());
        });
    }

    @Test
    public void testDeleteNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().delete(this.resourceName1(), null, LinkRelation.SELF, this.deleteHandler());
        });
    }

    @Test
    public void testDeleteNullRelationFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().delete(this.resourceName1(), ID_PARSER,null, this.deleteHandler());
        });
    }

    @Test
    public void testDeleteNullHandlerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().delete(this.resourceName1(), ID_PARSER, LinkRelation.SELF, null);
        });
    }

    @Test
    public void testDeleteDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.delete(resourceName, ID_PARSER, relation, this.deleteHandler());

        assertThrows(IllegalArgumentException.class, () -> {
            builder.delete(resourceName, ID_PARSER, relation, this.deleteHandler());
        });
    }

    @Test
    public void testDeleteRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosDeleteHandler<BigInteger, JsonNode> handler = this.deleteHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.delete(resourceName, ID_PARSER, relation, handler);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.delete(resourceName, ID_PARSER, relation, handler);
        });
    }

    @Test
    public void testDelete() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.delete(this.resourceName1(), ID_PARSER, relation, this.deleteHandler()));
        assertSame(builder, builder.delete(this.resourceName2(), ID_PARSER, relation, this.deleteHandler()));
        assertSame(builder, builder.delete(this.resourceName1(), ID_PARSER, LinkRelation.ITEM, this.deleteHandler()));
    }

    private HateosDeleteHandler<BigInteger, JsonNode> deleteHandler() {
        return new FakeHateosDeleteHandler<BigInteger, JsonNode>() {
            @Override
            public String toString() {
                return "delete456";
            }
        };
    }

    // toString....................................................................................................

    @Test
    public void testToString() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.get(this.resourceName1(), ID_PARSER, LinkRelation.SELF, this.getHandler());
        builder.post(this.resourceName1(), ID_PARSER, LinkRelation.SELF, this.postHandler());
        builder.put(this.resourceName1(), ID_PARSER, LinkRelation.SELF, this.putHandler());
        builder.delete(this.resourceName1(), ID_PARSER, LinkRelation.SELF, this.deleteHandler());

        this.toStringAndCheck(builder,
                "http://example.com/api JSON {resource1 self=GET=get123 POST=post234 PUT=put345 DELETE=delete456}");
    }

    // helpers ..........................................................................................

    private HateosResourceName resourceName1() {
        return HateosResourceName.with("resource1");
    }

    private HateosResourceName resourceName2() {
        return HateosResourceName.with("resource2");
    }

    // helpers ..........................................................................................

    @Override
    public HateosHandlerBuilder<JsonNode> createBuilder() {
        return this.createBuilder("http://example.com/api");
    }

    private HateosHandlerBuilder<JsonNode> createBuilder(final String url) {
        return this.createBuilder(url, this.contentType());
    }

    private HateosHandlerBuilder<JsonNode> createBuilder(final String url, final HateosContentType<JsonNode> contentType) {
        return HateosHandlerBuilder.with(AbsoluteUrl.parse(url), contentType);
    }

    private HateosContentType<JsonNode> contentType() {
        return HateosContentType.JSON;
    }

    @Override
    public Class<HateosHandlerBuilder<JsonNode>> type() {
        return Cast.to(HateosHandlerBuilder.class);
    }

    @Override
    public Class<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> builderProductType() {
        return Cast.to(Router.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
