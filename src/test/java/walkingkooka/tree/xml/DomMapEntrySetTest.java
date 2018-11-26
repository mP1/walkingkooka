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
import org.w3c.dom.Element;
import walkingkooka.Cast;
import walkingkooka.collect.set.SetTestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map.Entry;

public final class DomMapEntrySetTest extends SetTestCase<DomMapEntrySet<DomAttributeName, String>,
        Entry<DomAttributeName, String>> {

    @Override
    protected DomMapEntrySet<DomAttributeName, String> createSet() {
        final Element element = this.createElement();
        element.setAttribute("A1", "B2");
        element.setAttribute("C3", "D4");

        return Cast.to(DomDocument.wrap(element).attributes().entrySet());
    }

    private Element createElement() {
        final Document document = this.documentBuider().newDocument();
        return document.createElement("root");
    }

    private DocumentBuilder documentBuider() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setExpandEntityReferences(false);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    @Override
    protected Class<DomMapEntrySet<DomAttributeName, String>> type() {
        return Cast.to(DomMapEntrySet.class);
    }
}

