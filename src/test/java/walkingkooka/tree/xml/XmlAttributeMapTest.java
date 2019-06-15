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

package walkingkooka.tree.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import walkingkooka.Cast;

import java.util.Optional;

public final class XmlAttributeMapTest extends XmlMapTestCase<XmlAttributeMap, XmlAttributeName, String> {

    private final static String ATTRIBUTE1 = "A1";
    private final static String VALUE1 = "V1";
    private final static String ATTRIBUTE2 = "A2";
    private final static String VALUE2 = "V2";

    private final static String NAMESPACEURI = "http://www.example.com";
    private final static String PREFIX = "P";

    @Test
    public void testKeyAndValue() {
        this.getAndCheck(XmlAttributeName.with(ATTRIBUTE1, XmlAttributeName.NO_PREFIX),
                VALUE1);
    }

    @Test
    public void testKeyAndValue2() {
        this.getAndCheck(XmlAttributeName.with(ATTRIBUTE1, XmlAttributeName.NO_PREFIX),
                VALUE1);
    }

    @Test
    public void testKeyAndValueWithNameSpaces() {
        this.getAndCheck(this.createMapWithNamespaces(),
                XmlAttributeName.with(ATTRIBUTE1, prefixWithNamespace()),
                VALUE1);
    }

    @Test
    public void testKeyAndValueWithNameSpaces2() {
        this.getAndCheck(this.createMapWithNamespaces(),
                XmlAttributeName.with(ATTRIBUTE2, prefixWithNamespace()),
                VALUE2);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createMap(), 2);
    }

    @Override
    public XmlAttributeMap createMap() {
        final Element element = this.document(false)
                .createElement("element");
        element.setAttribute(ATTRIBUTE1, VALUE1);
        element.setAttribute(ATTRIBUTE2, VALUE2);
        return Cast.to(XmlAttributeMap.from(element.getAttributes()));
    }

    private XmlAttributeMap createMapWithNamespaces() {
        final Document document = this.document(true);

        final Element element = document.createElementNS("xmlns:n=" + NAMESPACEURI, "element");
        final Element child = document.createElementNS("xmlns:o=http://example2.com", "element2");
        element.appendChild(child);

        child.setAttributeNode(this.createAttr(document, ATTRIBUTE1, VALUE1));
        child.setAttributeNode(this.createAttr(document, ATTRIBUTE2, VALUE2));

        return Cast.to(XmlAttributeMap.from(child.getAttributes()));
    }

    private Attr createAttr(final Document document, final String name, final String value) {
        final Attr attr = document.createAttributeNS(NAMESPACEURI, name);
        attr.setPrefix(PREFIX);
        attr.setValue(value);
        return attr;
    }

    private Document document(final boolean namespaces) {
        return this.documentBuilder(namespaces, false)
                .newDocument();
    }

    private Optional<XmlNameSpacePrefix> prefixWithNamespace() {
        return Optional.of(XmlNameSpacePrefix.with(PREFIX));
    }

    @Override
    public Class<XmlAttributeMap> type() {
        return XmlAttributeMap.class;
    }
}

