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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.tree.json.JsonNode;

import java.math.BigInteger;

public final class HateosContentTypeJsonNodeTest extends HateosContentTypeTestCase<HateosContentTypeJsonNode, JsonNode> {

    @Test
    public void testFromNode() {
        final TestHateosResource resource = TestHateosResource.with(BigInteger.valueOf(123));
        this.fromNodeAndCheck(resource.toJsonNode().toString(),
                TestHateosResource.class,
                resource);
    }

    @Test
    public void testFromNodeList() {
        final TestHateosResource resource1 = TestHateosResource.with(BigInteger.valueOf(123));
        final TestHateosResource resource2 = TestHateosResource.with(BigInteger.valueOf(234));

        this.fromNodeListAndCheck("[" + resource1.toJsonNode() + "," + resource2.toJsonNode() + "]",
                TestHateosResource.class,
                resource1, resource2);
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(TestHateosResource.with(BigInteger.valueOf(123)),
                Lists.of(LinkRelation.SELF),
                "{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/api/test/123\",\n" +
                        "    \"method\": \"PUT\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testToTextList() {
        this.toTextListAndCheck(Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222))),
                Lists.of(LinkRelation.SELF, LinkRelation.ABOUT),
                "[{\n" +
                        "  \"id\": \"111\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/api/test/111\",\n" +
                        "    \"method\": \"PUT\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }, {\n" +
                        "    \"href\": \"http://example.com/api/test/111/about\",\n" +
                        "    \"method\": \"PUT\",\n" +
                        "    \"rel\": \"about\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}, {\n" +
                        "  \"id\": \"222\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://example.com/api/test/222\",\n" +
                        "    \"method\": \"PUT\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }, {\n" +
                        "    \"href\": \"http://example.com/api/test/222/about\",\n" +
                        "    \"method\": \"PUT\",\n" +
                        "    \"rel\": \"about\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Override
    HateosContentTypeJsonNode contentType() {
        return HateosContentTypeJsonNode.INSTANCE;
    }

    @Override
    public Class<HateosContentTypeJsonNode> type() {
        return HateosContentTypeJsonNode.class;
    }
}
