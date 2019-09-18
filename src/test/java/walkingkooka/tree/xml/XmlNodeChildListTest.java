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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import walkingkooka.Cast;
import walkingkooka.collect.list.ListTesting2;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public final class XmlNodeChildListTest implements ClassTesting2<XmlNodeChildList>,
        ListTesting2<XmlNodeChildList, XmlNode> {

    private final static String A1 = "A1";
    private final static String B2 = "B2";
    private final static String C3 = "C3";

    @Override
    public XmlNodeChildList createList() {
        return this.createList(A1, B2, C3);
    }

    private XmlNodeChildList createList(final String... elements) {
        final Document document = this.documentBuider().newDocument();
        final Element root = document.createElement("root");
        for (String child : elements) {
            root.appendChild(document.createElement(child));
        }
        return Cast.to(XmlDocument.wrap(root).children());
    }

    private DocumentBuilder documentBuider() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setExpandEntityReferences(false);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new Error(cause);
        }
    }

    @Override
    public Class<XmlNodeChildList> type() {
        return XmlNodeChildList.class;
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}

