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

package walkingkooka.tree.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeAttributeName;
import walkingkooka.tree.search.SearchNodeName;

import javax.xml.parsers.DocumentBuilder;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class XmlElementTest extends XmlParentNodeTestCase<XmlElement> {

    private final static XmlName PARENT = XmlName.element("parent");
    private final static XmlName CHILD = XmlName.element("child");
    private final static XmlName CHILD2 = XmlName.element("child2");
    private final static XmlName GRAND_CHILD = XmlName.element("grand-child");

    private final static String TEXT = "text-1";
    private final static String TEXT2 = "text-2";
    private final static String TEXT3 = "text-3";

    private final static String NAMESPACE_URL1 = "http://example.com/namespace1";
    private final static XmlNameSpacePrefix PREFIX1 = XmlNode.prefix("ns1");

    private final static XmlAttributeName ATTRIBUTE_NAME_DEF1 = XmlNode.XMLNS.attributeName(PREFIX1.value());
    private final static Map<XmlAttributeName, String> ATTRIBUTES_XMLNS1 = Maps.one(ATTRIBUTE_NAME_DEF1, NAMESPACE_URL1);

    private final static String NAMESPACE_URL2 = "http://example.com/namespace2";
    private final static XmlNameSpacePrefix PREFIX2 = XmlNode.prefix("ns2");

    private final static XmlAttributeName ATTRIBUTE_NAME_DEF2 = XmlNode.XMLNS.attributeName(PREFIX2.value());
    private final static Map<XmlAttributeName, String> ATTRIBUTES_XMLNS2 = attributes(ATTRIBUTE_NAME_DEF1,
            NAMESPACE_URL1,
            ATTRIBUTE_NAME_DEF2,
            NAMESPACE_URL2);

    private final static XmlAttributeName ATTRIBUTE_NAME1 = XmlNode.attribute("attribute-1", XmlNode.NO_PREFIX);
    private final static XmlAttributeName ATTRIBUTE_NAME_NS1 = ATTRIBUTE_NAME1.setPrefix(Optional.of(PREFIX1));
    private final static String ATTRIBUTE_VALUE1 = "value-1";

    private final static XmlAttributeName ATTRIBUTE_NAME2 = XmlNode.attribute("attribute-2", XmlNode.NO_PREFIX);
    private final static XmlAttributeName ATTRIBUTE_NAME_NS2 = ATTRIBUTE_NAME2.setPrefix(Optional.of(PREFIX1));
    private final static String ATTRIBUTE_VALUE2 = "value-2";

    private final static Map<XmlAttributeName, String> ATTRIBUTES_1 = Maps.one(ATTRIBUTE_NAME1, ATTRIBUTE_VALUE1);

    private final static Map<XmlAttributeName, String> ATTRIBUTES_2 = attributes(ATTRIBUTE_NAME1,
            ATTRIBUTE_VALUE1,
            ATTRIBUTE_NAME2,
            ATTRIBUTE_VALUE2);

    private final static Map<XmlAttributeName, String> ATTRIBUTES_NS1 = Maps.one(ATTRIBUTE_NAME_NS1, ATTRIBUTE_VALUE1);
    private final static Map<XmlAttributeName, String> ATTRIBUTES_NS2 = attributes(ATTRIBUTE_NAME_NS1,
            ATTRIBUTE_VALUE1,
            ATTRIBUTE_NAME_NS2,
            ATTRIBUTE_VALUE2);

    // tests

    @Test
    public void testCreateWithoutPrefix() {
        final XmlElement element = this.createNode();
        this.checkPrefix(element, null);
    }

    // name ............................................................

    @Test
    public void testName() {
        assertEquals(PARENT, this.createNode().name());
    }

    // children.............................................................

    @Test
    public void testAppendChild() {
        this.appendChildAndCheck();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildListAddReadOnly() {
        this.appendChildAndCheck().children().add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildListSetReadOnly() {
        this.appendChildAndCheck().children().set(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildListRemoveReadOnly() {
        this.appendChildAndCheck().children().remove(0);
    }

    @Test
    public void testAppendTwoChildren() {
        final XmlElement parent = this.appendChildAndCheck();
        final XmlNode child1 = parent.children().get(0);

        final XmlText child2 = parent.createText(TEXT2);
        this.checkNotAdopted("child", child2);
        this.checkChildren("parent", 1, parent);

        final XmlElement parent2 = parent.appendChild(child2);
        assertFalse(parent == parent2);

        this.checkChildren("parent2", 2, parent2);
        assertEquals("text of 2children", TEXT + TEXT2, parent2.text());
    }

    private XmlElement appendChildAndCheck() {
        final XmlElement parent = this.createNode();
        final XmlText child = parent.createText(TEXT);
        this.checkChildren("parent", 0, parent);

        final XmlElement parent2 = parent.appendChild(child);

        assertFalse(parent == parent2);
        this.checkNotAdopted("child", child);
        this.checkChildren("parent", 1, parent2);

        return parent2;
    }

    @Test
    public void testAppendChildWithChild() {
        final XmlElement parent = this.createNode();
        final XmlElement child = parent.createElement(CHILD);
        final XmlText grandChild = parent.createText("grand-child");
        final XmlNode child2 = child.appendChild(grandChild);

        final XmlNode parent2 = parent.appendChild(child2);
        this.checkChildren("parent", 1, parent2);
        this.checkChildren("child", 1, parent2.children().get(0));

        assertEquals("grand-child", parent2.text());
    }

    @Test
    public void testSetManyChildren() {
        final XmlElement parent = this.createNode();
        final XmlText child1 = parent.createText(TEXT);
        final XmlText child2 = parent.createText(TEXT2);

        final XmlElement parent2 = parent.setChildren(Lists.of(child1, child2));
        this.checkChildren("parent of 2 children", 2, parent2);
        this.checkChildren("original parent", 0, parent);
    }

    @Test
    public void testSetSameChildren() {
        final XmlElement parent = this.createNode();
        final XmlText child = parent.createText(TEXT);
        final XmlElement parent2 = parent.appendChild(child);

        final XmlElement parent3 = parent2.setChildren(Lists.of(child));
        assertSame("set of new child would be identical", parent2, parent3);
    }

    @Test
    public void testSetDifferentChildren() {
        final XmlElement parent = this.createNode();
        final XmlText child = parent.createText(TEXT);
        final XmlElement parent2 = parent.appendChild(child);

        final XmlText child2 = parent.createText(TEXT2);
        final XmlElement parent3 = parent2.setChildren(Lists.of(child2));
        assertTrue(parent2 != parent3);

        this.checkChildren("parent after set different child", 1, parent3);
        assertEquals("text after replacing only child", TEXT2, parent3.text());
    }

    @Test
    public void testSetSomeDifferentChildren() {
        final XmlElement parent = this.createNode();
        final XmlText child = parent.createText(TEXT);
        final XmlText child2 = parent.createText(TEXT2);
        final XmlElement parent2 = parent.setChildren(Lists.of(child, child2));

        // replace child2 with child3
        final XmlText child3 = parent.createText(TEXT3);
        final XmlElement parent3 = parent2.setChildren(Lists.of(parent2.children().get(0), child3));
        assertTrue(parent2 != parent3);

        this.checkNotAdopted("child3", child3);

        this.checkChildren("parent after set different child", 2, parent3);
        assertEquals("text after replacing only child", TEXT + TEXT3, parent3.text());
    }

    @Test
    public void testSetGrandChildren() {
        final XmlElement parent1 = this.createNode();
        final XmlElement child1 = parent1.createElement(CHILD);
        final XmlElement grandChild1 = child1.createElement(GRAND_CHILD);

        final XmlElement child2 = child1.appendChild(grandChild1);
        final XmlElement parent2 = parent1.appendChild(child2);

        parent2.children().get(0).children().get(0);
    }

    @Test
    public void testRemoveChild() {
        final XmlElement parent1 = this.createNode();
        final XmlElement child1 = parent1.createElement(CHILD);
        final XmlElement child2 = parent1.createElement(CHILD2);

        final XmlElement parent2 = parent1.setChildren(Lists.of(child1, child2));
        this.checkChildren("parent2", 2, parent2);

        final XmlElement parent3 = parent2.removeChild(0);
        this.checkChildren("parent3", 1, parent3);
    }

    // attributes ................................................................................................

    @Test
    public void testSetAttributes() {
        this.setAttributesAndCheck(this.createNode(), ATTRIBUTES_1);
    }

    @Test
    public void testSetAttributesMany() {
        this.setAttributesAndCheck(this.createNode(), ATTRIBUTES_2);
    }

    @Test
    public void testSetAttributesNamespaceDefinition() {
        this.setAttributesAndCheck(this.createNodeNamespaced(), ATTRIBUTES_XMLNS1);
    }

    @Test
    public void testSetAttributesNamespaced() {
        final XmlElement element = this.createNodeNamespaced().setAttributes(ATTRIBUTES_XMLNS1);
        final XmlElement child = element.createElement(CHILD);

        this.setAttributesAndCheck(child, ATTRIBUTES_1);
    }

    @Test
    public void testSetAttributesNamespacedMany() {
        final XmlElement element = this.createNodeNamespaced().setAttributes(ATTRIBUTES_XMLNS2);
        final XmlElement child = element.createElement(CHILD);

        this.setAttributesAndCheck(child, ATTRIBUTES_2);
    }

    @Test
    public void testSetAttributesManyMixedNamespaces() {
        final Document document = this.documentBuilderNamespaced().newDocument();
        final Element root = document.createElement("root");
        root.setAttributeNS(XmlNode.XMLNS_URI, XmlNode.XMLNS.value() + ":" + PREFIX1.value(), NAMESPACE_URL1);
        root.setAttributeNS(NAMESPACE_URL1, PREFIX1.value() + ":" + ATTRIBUTE_NAME1.value(), ATTRIBUTE_VALUE1);
        root.setAttribute(ATTRIBUTE_NAME2.value(), ATTRIBUTE_VALUE2);

        assertEquals("1st attr/namespace decl", NAMESPACE_URL1, root.getAttributeNS(XmlNode.XMLNS_URI, PREFIX1.value()));
        assertEquals("2nd attribute/namespaced attr", ATTRIBUTE_VALUE1, root.getAttributeNS(NAMESPACE_URL1, ATTRIBUTE_NAME1.value()));
        assertEquals("3rd attribute", ATTRIBUTE_VALUE2, root.getAttribute(ATTRIBUTE_NAME2.value()));

        final Map<XmlAttributeName, String> attributes = Maps.ordered();
        attributes.put(ATTRIBUTE_NAME_DEF1, NAMESPACE_URL1);
        attributes.put(ATTRIBUTE_NAME_NS1, ATTRIBUTE_VALUE1);
        attributes.put(ATTRIBUTE_NAME2, ATTRIBUTE_VALUE2);

        this.setAttributesAndCheck(this.createNodeNamespaced(), attributes);
    }

    private void setAttributesAndCheck(final XmlElement element1, final Map<XmlAttributeName, String> attributes) {
        final XmlElement element2 = element1.setAttributes(attributes);

        this.checkAttributes(element1, Maps.empty());
        this.checkAttributes(element2, attributes);
    }

//    @Test
//    public void testNamespaceAndPrefix() {
//        final XmlElement parent = this.createNodeNamespaced().setPrefix(Optional.of(PREFIX1));
//
//
//        final XmlElement child = parent.createElement(NAMESPACE_URL1, PREFIX1, CHILD);
//        this.checkChildCount("parent", 0, parent);
//    }

    // HashCodeEqualsDefined ..................................................................................................

    private final static String TAG = "element123";

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(this.document().createElement("different"));
    }

    @Test
    public void testEqualsDifferentAttributes() {
        final Element element = this.document().createElement(TAG);
        element.setAttribute("attribute", "attribute-value");
        this.checkNotEquals(XmlElement.with(element));
    }

    @Test
    public void testEqualsDifferentAttributes2() {
        final Element element = this.document().createElement(TAG);
        element.setAttribute("attribute", "attribute-value");

        final Element element2 = this.document().createElement(TAG);
        element2.setAttribute("attribute2", "attribute-value2");

        this.checkNotEquals(XmlElement.with(element), XmlElement.with(element2));
    }

    // toSearchNode.....................................................................................................

    @Test
    public final void testToSearchNodeWithChildren() {
        final XmlElement parent = this.createNode();
        final XmlElement child = parent.createElement(XmlName.element("child1"));
        final XmlElement parent2 = parent.appendChild(child);

        this.toSearchNodeAndCheck(parent2,
                SearchNode.sequence(Lists.of(
                        child.toSearchNode()))

                        .setName(SearchNodeName.with(PARENT.value())));
    }

    @Test
    public final void testToSearchNodeWithAttributes() {
        final XmlElement element = this.createNode()
                .setAttributes(ATTRIBUTES_1);

        this.toSearchNodeAndCheck(element,
                SearchNode.text("", "")
                        .setAttributes(Maps.one(SearchNodeAttributeName.with(ATTRIBUTE_NAME1.value()), ATTRIBUTE_VALUE1))
                        .setName(SearchNodeName.with(PARENT.value())));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        assertEquals("<parent/>", this.createNode().toString());
    }

    @Test
    public void testToStringWithChildren() {
        final XmlElement element = this.createNode();
        final XmlElement element2 = element.appendChild(element.createElement(XmlName.element("child")));

        assertEquals("<parent><child/></parent>", element2.toString());
    }

    @Test
    public void testToStringWithAttributes() {
        final XmlElement element = this.createNode();
        final XmlElement element2 = element.setAttributes(ATTRIBUTES_1);
        assertEquals("<parent attribute-1=\"value-1\"/>", element2.toString());
    }

    @Test
    public void testToStringWithAttributes2() {
        final XmlElement element = this.createNode();
        final XmlElement element2 = element.setAttributes(ATTRIBUTES_2);

        assertEquals("<parent attribute-1=\"value-1\" attribute-2=\"value-2\"/>", element2.toString());
    }

    @Test
    public void testToStringWithChildrenAndAttributes() {
        final XmlElement element = this.createNode();
        final XmlElement element2 = element.appendChild(element.createElement(XmlName.element("child")))
                .setAttributes(ATTRIBUTES_1);

        assertEquals("<parent attribute-1=\"value-1\"><child/></parent>", element2.toString());
    }

    @Test
    public void testToStringWithText() {
        final XmlElement element = this.createNode();
        final XmlElement element2 = element.appendChild(element.createText("abc123"));

        assertEquals("<parent>abc123</parent>", element2.toString());
    }

    // mixed document

    @Test
    public void testMixedDocumentAppend() {
        final DocumentBuilder b1 = this.documentBuilder();
        final DocumentBuilder b2 = this.documentBuilder();
        final XmlDocument root1 = XmlNode.createDocument(b1);
        final XmlDocument root2 = XmlNode.createDocument(b1);

        final XmlElement element1 = root1.createElement(XmlName.element("element1"));
        final XmlElement element2 = root1.createElement(XmlName.element("element2"));

        element1.appendChild(element2);
    }

    // helpers............................................................................................

    @Override
    XmlElement createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    XmlElement createNode(final Document document) {
        return XmlElement.with(document.createElement(PARENT.value()));
    }

    @Override
    String text() {
        return "";
    }

    @Override
    SearchNodeName searchNodeName() {
        return SearchNodeName.with(PARENT.value());
    }

    @Override
    Class<XmlElement> nodeType() {
        return XmlElement.class;
    }

    private void checkPrefix(final XmlElement element, final String prefix) {
        assertEquals("prefix", Optional.ofNullable(null != prefix ? XmlNode.prefix(prefix) : null), element.prefix());
        assertEquals("w3c element prefix", prefix, element.node.getPrefix());
    }

    private void checkAttributes(final XmlElement element, final Map<XmlAttributeName, String> attributes) {
        assertEquals("attributes", attributes, element.attributes());

        final Map<String, Object> rawAttributes = Maps.ordered();
        attributes.entrySet()
                .stream()
                .forEach(e -> rawAttributes.put(e.getKey().toString(), e.getValue()));

        final Map<String, Object> actualRawAttributes = Maps.ordered();
        final NamedNodeMap attributeNodes = element.node.getAttributes();
        final int count = attributeNodes.getLength();
        for (int i = 0; i < count; i++) {
            final org.w3c.dom.Attr attr = Cast.to(attributeNodes.item(i));
            actualRawAttributes.put(attr.getName(), attr.getValue());
        }

        assertEquals("attributes on w3c.dom.Element", rawAttributes, actualRawAttributes);
    }

    private static Map<XmlAttributeName, String> attributes(final XmlAttributeName name1,
                                                            final String value1,
                                                            final XmlAttributeName name2,
                                                            final String value2) {
        final Map<XmlAttributeName, String> attributes = Maps.ordered();
        attributes.put(name1, value1);
        attributes.put(name2, value2);
        return attributes;
    }
}
