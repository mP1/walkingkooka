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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.tree.xml.XmlNode;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HateosContentTypeXmlNodeTest extends HateosContentTypeTestCase<HateosContentTypeXmlNode, XmlNode> {

    @Test
    public void testFromNodeFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.contentType().fromNode("<test1/>", documentBuilder(), TestHateosResource.class);
        });
    }

    @Test
    public void testFromNodeListFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.contentType().fromNodeList("<test1/>", documentBuilder(), TestHateosResource.class);
        });
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(TestHateosResource.with(BigInteger.valueOf(123)),
                Lists.of(LinkRelation.SELF),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test1><id>123</id><links><link href=\"http://example.com/api/test/123\" method=\"PUT\" rel=\"self\" type=\"application/hal+xml\"/></links></test1>");
    }

    @Test
    public void testToTextNonSelfLinkRelation() {
        this.toTextAndCheck(TestHateosResource.with(BigInteger.valueOf(123)),
                Lists.of(LinkRelation.ITEM),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test1><id>123</id><links><link href=\"http://example.com/api/test/123/item\" method=\"PUT\" rel=\"item\" type=\"application/hal+xml\"/></links></test1>");
    }

    @Test
    public void testToTextSeveralLinks() {
        this.toTextAndCheck(TestHateosResource.with(BigInteger.valueOf(123)),
                Lists.of(LinkRelation.ITEM, LinkRelation.ABOUT),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test1><id>123</id><links><link href=\"http://example.com/api/test/123/item\" method=\"PUT\" rel=\"item\" type=\"application/hal+xml\"/><link href=\"http://example.com/api/test/123/about\" method=\"PUT\" rel=\"about\" type=\"application/hal+xml\"/></links></test1>");
    }

    @Test
    public void testToTextList() {
        this.toTextListAndCheck(Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222))),
                Lists.of(LinkRelation.SELF, LinkRelation.ABOUT),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><list><test1><id>111</id><links><link href=\"http://example.com/api/test/111\" method=\"PUT\" rel=\"self\" type=\"application/hal+xml\"/><link href=\"http://example.com/api/test/111/about\" method=\"PUT\" rel=\"about\" type=\"application/hal+xml\"/></links></test1><test1><id>222</id><links><link href=\"http://example.com/api/test/222\" method=\"PUT\" rel=\"self\" type=\"application/hal+xml\"/><link href=\"http://example.com/api/test/222/about\" method=\"PUT\" rel=\"about\" type=\"application/hal+xml\"/></links></test1></list>");
    }

    @Override
    HateosContentTypeXmlNode contentType() {
        return HateosContentTypeXmlNode.INSTANCE;
    }

    @Override
    public Class<HateosContentTypeXmlNode> type() {
        return Cast.to(HateosContentTypeXmlNode.class);
    }
}
