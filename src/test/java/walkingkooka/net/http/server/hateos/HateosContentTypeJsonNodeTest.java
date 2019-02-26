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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.header.Link;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.math.BigInteger;

public final class HateosContentTypeJsonNodeTest extends HateosContentTypeTestCase<HateosContentTypeJsonNode<HasJsonNode>, JsonNode, HasJsonNode> {

    @Test
    public void testAddLinkSelf() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "{ \"prop1\":\"value1\"}",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.SELF),
                "{\n" +
                        "  \"prop1\": \"value1\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/base/entity/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testAddLinkNonSelf() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "{ \"prop1\":\"value1\"}",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.ITEM),
                "{\n" +
                        "  \"prop1\": \"value1\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/base/entity/123/item\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"item\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testAddLinkManyRelations() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "{ \"prop1\":\"value1\"}",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.SELF, LinkRelation.ITEM),
                "{\n" +
                        "  \"prop1\": \"value1\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/base/entity/123/item\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"item\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }, {\n" +
                        "    \"href\": \"http://example.com/base/entity/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testToNode() {
        final Link link = Link.with(Url.parse("http://example.com"));

        this.toNodeAndCheck(link, link.toJsonNode());
    }

    @Override
    HateosContentTypeJsonNode<HasJsonNode> constant() {
        return HateosContentTypeJsonNode.instance();
    }

    @Override
    JsonNode parse(final String text) {
        return JsonNode.parse(text);
    }

    @Override
    public Class<HateosContentTypeJsonNode<HasJsonNode>> type() {
        return Cast.to(HateosContentTypeJsonNode.class);
    }
}
