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
import walkingkooka.tree.xml.HasXmlNode;
import walkingkooka.tree.xml.XmlNode;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigInteger;

public final class HateosContentTypeXmlNodeTest extends HateosContentTypeTestCase<HateosContentTypeXmlNode<HasXmlNode>, XmlNode, HasXmlNode> {

    @Test
    public void testAddLinkSelf() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "<entity><value>1</value></entity>",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.SELF),
                "<entity><value>1</value><links><link href=\"http://example.com/base/entity/123\" method=\"GET\" rel=\"self\" type=\"application/hal+xml\"/></links></entity>");
    }

    @Test
    public void testAddLinkNonSelf() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "<entity><value>1</value></entity>",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.ITEM),
                "<entity><value>1</value><links><link href=\"http://example.com/base/entity/123/item\" method=\"GET\" rel=\"item\" type=\"application/hal+xml\"/></links></entity>");
    }

    @Test
    public void testAddLinkManyRelations() throws Exception {
        this.addLinksAndCheck(BigInteger.valueOf(123),
                "<entity><value>1</value></entity>",
                HttpMethod.GET,
                Url.parseAbsolute("http://example.com/base"),
                HateosResourceName.with("entity"),
                Sets.of(LinkRelation.SELF, LinkRelation.ITEM),
                "<entity><value>1</value><links><link href=\"http://example.com/base/entity/123/item\" method=\"GET\" rel=\"item\" type=\"application/hal+xml\"/><link href=\"http://example.com/base/entity/123\" method=\"GET\" rel=\"self\" type=\"application/hal+xml\"/></links></entity>");
    }

    @Test
    public void testToNode() {
        final Link link = Link.with(Url.parse("http://example.com"));

        this.toNodeAndCheck(link, link.toXmlNode());
    }

    @Override
    HateosContentTypeXmlNode<HasXmlNode> constant() {
        return HateosContentTypeXmlNode.instance();
    }

    @Override
    XmlNode parse(final String text) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);
        return XmlNode.fromXml(factory.newDocumentBuilder(),
                new StringReader(text))
                .element()
                .get()
                .normalize();
    }

    @Override
    public Class<HateosContentTypeXmlNode<HasXmlNode>> type() {
        return Cast.to(HateosContentTypeXmlNode.class);
    }
}
