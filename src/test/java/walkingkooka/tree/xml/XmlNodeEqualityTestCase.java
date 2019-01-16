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

import org.w3c.dom.Document;
import walkingkooka.tree.NodeEqualityTestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public abstract class XmlNodeEqualityTestCase<N extends XmlNode> extends
        NodeEqualityTestCase<XmlNode,
                XmlName,
                XmlAttributeName,
                String> {

    XmlNodeEqualityTestCase() {
        super();
    }

    protected final N createObject() {
        return this.createNode(this.documentBuilder());
    }

    abstract N createNode(final DocumentBuilder builder);

    abstract N createNode(final Document document);

    final Document document() {
        return this.documentBuilder().newDocument();
    }

    final DocumentBuilder documentBuilder() {
        return this.documentBuilder(false, true);
    }

    final DocumentBuilder documentBuilderNamespaced() {
        return this.documentBuilder(true, true);
    }

    final DocumentBuilder documentBuilder(final boolean namespace, final boolean expandEntities) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespace);
            factory.setValidating(false);
            factory.setExpandEntityReferences(expandEntities);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new Error(cause);
        }
    }
}
