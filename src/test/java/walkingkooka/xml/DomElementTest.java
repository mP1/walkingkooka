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
 */

package walkingkooka.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import javax.xml.parsers.DocumentBuilder;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class DomElementTest extends DomParentNodeTestCase<DomElement> {

    private final static DomName PARENT = DomName.element("parent");
    private final static DomName CHILD = DomName.element("child");
    private final static DomName CHILD2 = DomName.element("child2");
    private final static DomName GRAND_CHILD = DomName.element("grand-child");

    private final static String TEXT = "text-1";
    private final static String TEXT2 = "text-2";
    private final static String TEXT3 = "text-3";

    private final static String NAMESPACE_URL1 = "http://example.com/namespace1";
    private final static DomNameSpacePrefix PREFIX1 = DomNode.prefix("ns1");

    private final static DomAttributeName ATTRIBUTE_NAME_DEF1 = DomNode.XMLNS.attributeName(PREFIX1.value());
    private final static Map<DomAttributeName, String> ATTRIBUTES_XMLNS1 = Maps.one(ATTRIBUTE_NAME_DEF1, NAMESPACE_URL1);

    private final static String NAMESPACE_URL2 = "http://example.com/namespace2";
    private final static DomNameSpacePrefix PREFIX2 = DomNode.prefix("ns2");

    private final static DomAttributeName ATTRIBUTE_NAME_DEF2 = DomNode.XMLNS.attributeName(PREFIX2.value());
    private final static Map<DomAttributeName, String> ATTRIBUTES_XMLNS2 = attributes(ATTRIBUTE_NAME_DEF1,
            NAMESPACE_URL1,
            ATTRIBUTE_NAME_DEF2,
            NAMESPACE_URL2);

    private final static DomAttributeName ATTRIBUTE_NAME1 = DomNode.attribute("attribute-1", DomNode.NO_PREFIX);
    private final static DomAttributeName ATTRIBUTE_NAME_NS1 = ATTRIBUTE_NAME1.setPrefix(Optional.of(PREFIX1));
    private final static String ATTRIBUTE_VALUE1 = "value-1";

    private final static DomAttributeName ATTRIBUTE_NAME2 = DomNode.attribute("attribute-2", DomNode.NO_PREFIX);
    private final static DomAttributeName ATTRIBUTE_NAME_NS2 = ATTRIBUTE_NAME2.setPrefix(Optional.of(PREFIX1));
    private final static String ATTRIBUTE_VALUE2 = "value-2";

    private final static Map<DomAttributeName, String> ATTRIBUTES_1 = Maps.one(ATTRIBUTE_NAME1, ATTRIBUTE_VALUE1);

    private final static Map<DomAttributeName, String> ATTRIBUTES_2 = attributes(ATTRIBUTE_NAME1,
            ATTRIBUTE_VALUE1,
            ATTRIBUTE_NAME2,
            ATTRIBUTE_VALUE2);

    private final static Map<DomAttributeName, String> ATTRIBUTES_NS1 = Maps.one(ATTRIBUTE_NAME_NS1, ATTRIBUTE_VALUE1);
    private final static Map<DomAttributeName, String> ATTRIBUTES_NS2 = attributes(ATTRIBUTE_NAME_NS1,
            ATTRIBUTE_VALUE1,
            ATTRIBUTE_NAME_NS2,
            ATTRIBUTE_VALUE2);

    // tests

    @Test
    public void testCreateWithoutPrefix() {
        final DomElement element = this.createNode();
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
        final DomElement parent = this.appendChildAndCheck();
        final DomNode child1 = parent.children().get(0);

        final DomText child2 = parent.createText(TEXT2);
        this.checkNotAdopted("child", child2);
        this.checkChildren("parent", 1, parent);

        final DomElement parent2 = parent.appendChild(child2);
        assertFalse(parent==parent2);

        this.checkChildren("parent2", 2, parent2);
        assertEquals("text of 2children", TEXT + TEXT2, parent2.text());
    }

    private DomElement appendChildAndCheck() {
        final DomElement parent = this.createNode();
        final DomText child = parent.createText(TEXT);
        this.checkChildren("parent", 0, parent);

        final DomElement parent2 = parent.appendChild(child);

        assertFalse(parent==parent2);
        this.checkNotAdopted("child", child);
        this.checkChildren("parent", 1, parent2);

        return parent2;
    }

    @Test
    public void testAppendChildWithChild(){
        final DomElement parent = this.createNode();
        final DomElement child = parent.createElement(CHILD);
        final DomText grandChild = parent.createText("grand-child");
        final DomNode child2 = child.appendChild(grandChild);

        final DomNode parent2 = parent.appendChild(child2);
        this.checkChildren("parent", 1, parent2);
        this.checkChildren("child",1, parent2.children().get(0));

        assertEquals("grand-child", parent2.text());
    }

    @Test
    public void testSetManyChildren() {
        final DomElement parent = this.createNode();
        final DomText child1 = parent.createText(TEXT);
        final DomText child2 = parent.createText(TEXT2);

        final DomElement parent2 = parent.setChildren(Lists.of(child1,child2));
        this.checkChildren("parent of 2 children", 2, parent2);
        this.checkChildren("original parent", 0, parent);
    }

    @Test
    public void testSetSameChildren() {
        final DomElement parent = this.createNode();
        final DomText child = parent.createText(TEXT);
        final DomElement parent2= parent.appendChild(child);

        final DomElement parent3 = parent2.setChildren(Lists.of(child));
        assertSame("set of new child would be identical", parent2, parent3);
    }

    @Test
    public void testSetDifferentChildren() {
        final DomElement parent = this.createNode();
        final DomText child = parent.createText(TEXT);
        final DomElement parent2= parent.appendChild(child);

        final DomText child2 = parent.createText(TEXT2);
        final DomElement parent3 = parent2.setChildren(Lists.of(child2));
        assertTrue(parent2!=parent3);

        this.checkChildren("parent after set different child", 1, parent3);
        assertEquals("text after replacing only child", TEXT2, parent3.text());
    }

    @Test
    public void testSetSomeDifferentChildren() {
        final DomElement parent = this.createNode();
        final DomText child = parent.createText(TEXT);
        final DomText child2 = parent.createText(TEXT2);
        final DomElement parent2= parent.setChildren(Lists.of(child, child2));

        // replace child2 with child3
        final DomText child3 = parent.createText(TEXT3);
        final DomElement parent3 = parent2.setChildren(Lists.of(parent2.children().get(0), child3));
        assertTrue(parent2!=parent3);

        this.checkNotAdopted("child3", child3);

        this.checkChildren("parent after set different child", 2, parent3);
        assertEquals("text after replacing only child", TEXT +TEXT3, parent3.text());
    }

    @Test
    public void testSetGrandChildren() {
        final DomElement parent1 = this.createNode();
        final DomElement child1 = parent1.createElement(CHILD);
        final DomElement grandChild1 = child1.createElement(GRAND_CHILD);

        final DomElement child2 = child1.appendChild(grandChild1);
        final DomElement parent2 = parent1.appendChild(child2);

        parent2.children().get(0).children().get(0);
    }

    @Test
    public void testRemoveChild() {
        final DomElement parent1 = this.createNode();
        final DomElement child1 = parent1.createElement(CHILD);
        final DomElement child2 = parent1.createElement(CHILD2);

        final DomElement parent2 = parent1.setChildren(Lists.of(child1, child2));
        this.checkChildren("parent2", 2, parent2);

        final DomElement parent3 = parent2.removeChild(parent2.children().get(0));
        this.checkChildren("parent3", 1, parent3);
    }

    @Test
    public void testRemoveChildByIndex() {
        final DomElement parent1 = this.createNode();
        final DomElement child1 = parent1.createElement(CHILD);
        final DomElement child2 = parent1.createElement(CHILD2);

        final DomElement parent2 = parent1.setChildren(Lists.of(child1, child2));
        this.checkChildren("parent2", 2, parent2);

        final DomElement parent3 = parent2.removeChild(0);
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
        final DomElement element = this.createNodeNamespaced().setAttributes(ATTRIBUTES_XMLNS1);
        final DomElement child = element.createElement(CHILD);

        this.setAttributesAndCheck(child, ATTRIBUTES_1);
    }

    @Test
    public void testSetAttributesNamespacedMany() {
        final DomElement element = this.createNodeNamespaced().setAttributes(ATTRIBUTES_XMLNS2);
        final DomElement child = element.createElement(CHILD);

        this.setAttributesAndCheck(child, ATTRIBUTES_2);
    }

    @Test
    public void testSetAttributesManyMixedNamespaces() {
        final Document document = this.documentBuilderNamespaced().newDocument();
        final Element root = document.createElement("root");
        root.setAttributeNS(DomNode.XMLNS_URI, DomNode.XMLNS.value() + ":" + PREFIX1.value(), NAMESPACE_URL1);
        root.setAttributeNS(NAMESPACE_URL1, PREFIX1.value() + ":" + ATTRIBUTE_NAME1.value(), ATTRIBUTE_VALUE1);
        root.setAttribute(ATTRIBUTE_NAME2.value(), ATTRIBUTE_VALUE2);

        assertEquals("1st attr/namespace decl", NAMESPACE_URL1, root.getAttributeNS(DomNode.XMLNS_URI, PREFIX1.value()));
        assertEquals("2nd attribute/namespaced attr", ATTRIBUTE_VALUE1, root.getAttributeNS(NAMESPACE_URL1, ATTRIBUTE_NAME1.value()));
        assertEquals("3rd attribute", ATTRIBUTE_VALUE2, root.getAttribute(ATTRIBUTE_NAME2.value()));

        final Map<DomAttributeName, String> attributes = Maps.ordered();
        attributes.put(ATTRIBUTE_NAME_DEF1, NAMESPACE_URL1);
        attributes.put(ATTRIBUTE_NAME_NS1, ATTRIBUTE_VALUE1);
        attributes.put(ATTRIBUTE_NAME2, ATTRIBUTE_VALUE2);

        this.setAttributesAndCheck(this.createNodeNamespaced(), attributes);
    }

    private void setAttributesAndCheck(final DomElement element1, final Map<DomAttributeName, String> attributes) {
        final DomElement element2 = element1.setAttributes(attributes);

        this.checkAttributes(element1, Maps.empty());
        this.checkAttributes(element2, attributes);
    }

//    @Test
//    public void testNamespaceAndPrefix() {
//        final DomElement parent = this.createNodeNamespaced().setPrefix(Optional.of(PREFIX1));
//
//
//        final DomElement child = parent.createElement(NAMESPACE_URL1, PREFIX1, CHILD);
//        this.checkChildCount("parent", 0, parent);
//    }

    // toString.........................................................

    @Test
    public void testToString() {
        assertEquals("<parent/>", this.createNode().toString());
    }

    @Test
    public void testToStringWithChildren() {
        final DomElement element = this.createNode();
        final DomElement element2 = element.appendChild(element.createElement(DomName.element("child")));

        assertEquals("<parent><child/></parent>", element2.toString());
    }

    @Test
    public void testToStringWithAttributes() {
        final DomElement element = this.createNode();
        final DomElement element2 = element.setAttributes(ATTRIBUTES_1);
        assertEquals("<parent attribute-1=\"value-1\"/>", element2.toString());
    }

    @Test
    public void testToStringWithAttributes2() {
        final DomElement element = this.createNode();
        final DomElement element2 = element.setAttributes(ATTRIBUTES_2);

        assertEquals("<parent attribute-1=\"value-1\" attribute-2=\"value-2\"/>", element2.toString());
    }

    @Test
    public void testToStringWithChildrenAndAttributes() {
        final DomElement element = this.createNode();
        final DomElement element2 = element.appendChild(element.createElement(DomName.element("child")))
                .setAttributes(ATTRIBUTES_1);

        assertEquals("<parent attribute-1=\"value-1\"><child/></parent>", element2.toString());
    }

    @Test
    public void testToStringWithText() {
        final DomElement element = this.createNode();
        final DomElement element2 = element.appendChild(element.createText("abc123"));

        assertEquals("<parent>abc123</parent>", element2.toString());
    }

    // mixed document

    @Test
    public void testMixedDocumentAppend() {
        final DocumentBuilder b1 = this.documentBuilder();
        final DocumentBuilder b2 = this.documentBuilder();
        final DomDocument root1 = DomNode.createDocument(b1);
        final DomDocument root2 = DomNode.createDocument(b1);

        final DomElement element1 = root1.createElement(DomName.element("element1"));
        final DomElement element2 = root1.createElement(DomName.element("element2"));

        element1.appendChild(element2);
    }

    // helpers............................................................................................

    @Override
    DomElement createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    DomElement createNode(final Document document) {
        return new DomElement(document.createElement(PARENT.value()));
    }

    @Override
    String text() {
        return "";
    }


    @Override protected Class<DomNode> type() {
        return Cast.to(DomElement.class);
    }

    private void checkNamespace(final DomElement element, final String namespaceUrl) {
        assertEquals("namespace", Optional.of(namespaceUrl), element.nameSpaceUri());
        assertEquals("w3c element namespace", namespaceUrl, element.node.getNamespaceURI());
    }

    private void checkPrefix(final DomElement element, final String prefix) {
        assertEquals("prefix", Optional.ofNullable(null != prefix ? DomNode.prefix(prefix) : null), element.prefix());
        assertEquals("w3c element prefix", prefix, element.node.getPrefix());
    }

    private void checkAttributes(final DomElement element, final Map<DomAttributeName, String> attributes) {
        assertEquals("attributes", attributes, element.attributes());

        final Map<String, Object> rawAttributes = Maps.ordered();
        attributes.entrySet()
                .stream()
                .forEach( e -> rawAttributes.put(e.getKey().toString(), e.getValue()));

        final Map<String, Object> actualRawAttributes = Maps.ordered();
        final NamedNodeMap attributeNodes = element.node.getAttributes();
        final int count = attributeNodes.getLength();
        for(int i = 0; i < count; i++) {
            final org.w3c.dom.Attr attr = Cast.to(attributeNodes.item(i));
            actualRawAttributes.put(attr.getName(), attr.getValue());
        }

        assertEquals("attributes on w3c.dom.Element", rawAttributes, actualRawAttributes);
    }

    private static Map<DomAttributeName, String> attributes(final DomAttributeName name1,
                                                            final String value1,
                                                            final DomAttributeName name2,
                                                            final String value2) {
        final Map<DomAttributeName, String> attributes = Maps.ordered();
        attributes.put(name1, value1);
        attributes.put(name2, value2);
        return attributes;
    }
}
