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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.Router;
import walkingkooka.tree.json.JsonNode;

import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HateosHandlerBuilderTest extends BuilderTestCase<HateosHandlerBuilder<JsonNode>,
        Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

    // creation ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        this.createBuilder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithQueryStringFails() {
        this.createBuilder("http://example/api?param1=value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFragmentFails() {
        this.createBuilder("http://example/api#fragment-1");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullContentTypeFails() {
        this.createBuilder("http://example/api", null);
    }

    // get ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testGetNullResourceNameFails() {
        this.createBuilder().get(null, LinkRelation.SELF, this.getHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullRelationFails() {
        this.createBuilder().get(this.resourceName1(), null, this.getHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullHandlerFails() {
        this.createBuilder().get(this.resourceName1(), LinkRelation.SELF, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.get(resourceName, relation, this.getHandler());
        builder.get(resourceName, relation, this.getHandler());
    }

    @Test
    public void testGetRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosGetHandler<JsonNode> handler = this.getHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.get(resourceName, relation, handler);
        builder.get(resourceName, relation, handler);
    }

    @Test
    public void testGet() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.get(this.resourceName1(), relation, this.getHandler()));
        assertSame(builder, builder.get(this.resourceName2(), relation, this.getHandler()));
        assertSame(builder, builder.get(this.resourceName1(), LinkRelation.ITEM, this.getHandler()));
    }

    private HateosGetHandler<JsonNode> getHandler() {
        return new FakeHateosGetHandler<JsonNode>() {
            @Override
            public String toString() {
                return "get123";
            }
        };
    }

    // post ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testPostNullResourceNameFails() {
        this.createBuilder().post(null, LinkRelation.SELF, this.postHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testPostNullRelationFails() {
        this.createBuilder().post(this.resourceName1(), null, this.postHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testPostNullHandlerFails() {
        this.createBuilder().post(this.resourceName1(), LinkRelation.SELF, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.post(resourceName, relation, this.postHandler());
        builder.post(resourceName, relation, this.postHandler());
    }

    @Test
    public void testPostRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosPostHandler<JsonNode> handler = this.postHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.post(resourceName, relation, handler);
        builder.post(resourceName, relation, handler);
    }

    @Test
    public void testPost() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.post(this.resourceName1(), relation, this.postHandler()));
        assertSame(builder, builder.post(this.resourceName2(), relation, this.postHandler()));
        assertSame(builder, builder.post(this.resourceName1(), LinkRelation.ITEM, this.postHandler()));
    }

    private HateosPostHandler<JsonNode> postHandler() {
        return new FakeHateosPostHandler<JsonNode>() {
            @Override
            public String toString() {
                return "post234";
            }
        };
    }

    // put ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testPutNullResourceNameFails() {
        this.createBuilder().put(null, LinkRelation.SELF, this.putHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullRelationFails() {
        this.createBuilder().put(this.resourceName1(), null, this.putHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullHandlerFails() {
        this.createBuilder().put(this.resourceName1(), LinkRelation.SELF, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.put(resourceName, relation, this.putHandler());
        builder.put(resourceName, relation, this.putHandler());
    }

    @Test
    public void testPutRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosPutHandler<JsonNode> handler = this.putHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.put(resourceName, relation, handler);
        builder.put(resourceName, relation, handler);
    }

    @Test
    public void testPut() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.put(this.resourceName1(), relation, this.putHandler()));
        assertSame(builder, builder.put(this.resourceName2(), relation, this.putHandler()));
        assertSame(builder, builder.put(this.resourceName1(), LinkRelation.ITEM, this.putHandler()));
    }

    private HateosPutHandler<JsonNode> putHandler() {
        return new FakeHateosPutHandler<JsonNode>() {
            @Override
            public String toString() {
                return "put345";
            }
        };
    }
    // delete ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testDeleteNullResourceNameFails() {
        this.createBuilder().delete(null, LinkRelation.SELF, this.deleteHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteNullRelationFails() {
        this.createBuilder().delete(this.resourceName1(), null, this.deleteHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteNullHandlerFails() {
        this.createBuilder().delete(this.resourceName1(), LinkRelation.SELF, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteDuplicateResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.delete(resourceName, relation, this.deleteHandler());
        builder.delete(resourceName, relation, this.deleteHandler());
    }

    @Test
    public void testDeleteRepeatedResourceAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;
        final HateosDeleteHandler<JsonNode> handler = this.deleteHandler();

        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        builder.delete(resourceName, relation, handler);
        builder.delete(resourceName, relation, handler);
    }

    @Test
    public void testDelete() {
        final HateosHandlerBuilder<JsonNode> builder = this.createBuilder();
        final LinkRelation<?> relation = LinkRelation.SELF;
        assertSame(builder, builder.delete(this.resourceName1(), relation, this.deleteHandler()));
        assertSame(builder, builder.delete(this.resourceName2(), relation, this.deleteHandler()));
        assertSame(builder, builder.delete(this.resourceName1(), LinkRelation.ITEM, this.deleteHandler()));
    }

    private HateosDeleteHandler<JsonNode> deleteHandler() {
        return new FakeHateosDeleteHandler<JsonNode>() {
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
        builder.get(this.resourceName1(), LinkRelation.SELF, this.getHandler());
        builder.post(this.resourceName1(), LinkRelation.SELF, this.postHandler());
        builder.put(this.resourceName1(), LinkRelation.SELF, this.putHandler());
        builder.delete(this.resourceName1(), LinkRelation.SELF, this.deleteHandler());
        assertEquals("http://example.com/api JSON {resource1 self=GET=get123 POST=post234 PUT=put345 DELETE=delete456}",
                builder.toString());
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
    protected HateosHandlerBuilder<JsonNode> createBuilder() {
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
    protected Class<HateosHandlerBuilder> type() {
        return Cast.to(HateosHandlerBuilder.class);
    }

    @Override
    protected Class<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> builderProductType() {
        return Cast.to(Router.class);
    }
}
