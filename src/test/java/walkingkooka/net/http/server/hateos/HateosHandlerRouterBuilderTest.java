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
import walkingkooka.build.BuilderTesting;
import walkingkooka.net.Url;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.Router;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import java.math.BigInteger;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosHandlerRouterBuilderTest extends HateosHandlerRouterTestCase<HateosHandlerRouterBuilder<JsonNode>>
        implements BuilderTesting<HateosHandlerRouterBuilder<JsonNode>, Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

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

    // add ..........................................................................................

    @Test
    public void testAddNullResourceNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().add(null, LinkRelation.SELF, this.mapper());
        });
    }

    @Test
    public void testAddNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().add(this.resourceName1(), null, this.mapper());
        });
    }

    @Test
    public void testAddNullRelationFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().add(this.resourceName1(), LinkRelation.SELF, null);
        });
    }

    @Test
    public void testAddDuplicateResourceNameAndRelationFails() {
        final HateosResourceName resourceName = this.resourceName1();
        final LinkRelation<?> relation = LinkRelation.SELF;

        final HateosHandlerRouterBuilder<JsonNode> builder = this.createBuilder();
        builder.add(resourceName, relation, this.mapper());

        assertThrows(IllegalArgumentException.class, () -> {
            builder.add(resourceName, relation, this.mapper());
        });
    }

    @Test
    public void testAddMany() {
        final HateosHandlerRouterBuilder<JsonNode> builder = this.createBuilder();

        assertSame(builder, builder.add(this.resourceName1(), LinkRelation.SELF, this.mapper()));
        assertSame(builder, builder.add(this.resourceName2(), LinkRelation.SELF, this.mapper()));
        assertSame(builder, builder.add(this.resourceName1(), LinkRelation.ITEM, this.mapper()));
    }


    // toString....................................................................................................

    @Test
    public void testToString() {
        final HateosHandlerRouterBuilder<JsonNode> builder = this.createBuilder();
        builder.add(this.resourceName1(), LinkRelation.SELF, this.mapper().get(this.handler("get123")).post(this.handler("post123")));
        builder.add(this.resourceName2(), LinkRelation.SELF, this.mapper().post(this.handler("post234")));
        builder.add(this.resourceName1(), LinkRelation.ITEM, this.mapper().put(this.handler("put345")));
        builder.add(this.resourceName2(), LinkRelation.ITEM, this.mapper().delete(this.handler("delete456")));

        this.toStringAndCheck(builder,
                "http://example.com/api JSON {resource1 item=PUT=put345, resource1 self=GET=get123 POST=post123, resource2 item=DELETE=delete456, resource2 self=POST=post234}");
    }

    private HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler(final String toString) {
        return new FakeHateosHandler<>() {
            @Override
            public String toString() {
                return toString;
            }
        };
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
    public HateosHandlerRouterBuilder<JsonNode> createBuilder() {
        return this.createBuilder("http://example.com/api");
    }

    private HateosHandlerRouterBuilder<JsonNode> createBuilder(final String url) {
        return this.createBuilder(url, this.contentType());
    }

    private HateosHandlerRouterBuilder<JsonNode> createBuilder(final String url,
                                                               final HateosContentType<JsonNode> contentType) {
        return HateosHandlerRouterBuilder.with(Url.parseAbsolute(url), contentType);
    }

    private HateosContentType<JsonNode> contentType() {
        return HateosContentType.json();
    }

    private HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper() {
        return HateosHandlerRouterMapper.with(BigInteger::new,
                TestHateosResource.class,
                TestHateosResource2.class);
    }

    @Override
    public Class<HateosHandlerRouterBuilder<JsonNode>> type() {
        return Cast.to(HateosHandlerRouterBuilder.class);
    }

    @Override
    public Class<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> builderProductType() {
        return Cast.to(Router.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    String typeNamePrefix2() {
        return "";
    }
}
