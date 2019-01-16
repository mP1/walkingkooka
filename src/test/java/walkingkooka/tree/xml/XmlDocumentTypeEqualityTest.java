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

import javax.xml.parsers.DocumentBuilder;

public final class XmlDocumentTypeEqualityTest extends XmlLeafNodeEqualityTestCase<XmlDocumentType> {

    private final static String TYPE = "type123";
    private final static String PUBLIC_ID_STRING = "//-/publicId";
    private final static String SYSTEM_ID_STRING = "http://www.example.com/test.dtd";

    // create..............................................................................................

    @Test
    public void testTypeDifferent() {
        this.checkNotEquals(this.createNode("different-type", PUBLIC_ID_STRING, SYSTEM_ID_STRING));
    }

    @Test
    public void testPublicIdDifferent() {
        this.checkNotEquals(this.createNode(TYPE, "//-/publicId-DIFFERENT", SYSTEM_ID_STRING));
    }

    @Test
    public void testSystemIdDifferent() {
        this.createNode(TYPE, PUBLIC_ID_STRING, "http://www.example.com/different.dtd");
    }

    final XmlDocumentType createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    XmlDocumentType createNode(final Document document) {
        return XmlDocumentType.with(document.getImplementation().createDocumentType(TYPE, PUBLIC_ID_STRING, SYSTEM_ID_STRING));
    }

    private XmlDocumentType createNode(final String type, final String publicId, final String systemId) {
        return this.createNode(this.documentBuilder(), type, publicId, systemId);
    }

    private XmlDocumentType createNode(final DocumentBuilder builder,
                                       final String type,
                                       final String publicId,
                                       final String systemId) {
        return XmlDocumentType.with(builder.getDOMImplementation().createDocumentType(type, publicId, systemId));
    }
}
