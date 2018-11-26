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

import javax.xml.parsers.DocumentBuilder;

public final class DomElementEqualityTest extends DomParentNodeEqualityTestCase<DomElement> {

    private final static String TAG = "element";

    @Test
    public void testDifferentName() {
        this.checkNotEquals(this.document().createElement("different"));
    }

    @Test
    public void testDifferentAttributes() {
        final Element element = this.document().createElement(TAG);
        element.setAttribute("attribute", "attribute-value");
        this.checkNotEquals(this.createNode(element));
    }

    @Test
    public void testDifferentAttributes2() {
        final Element element = this.document().createElement(TAG);
        element.setAttribute("attribute", "attribute-value");

        final Element element2 = this.document().createElement(TAG);
        element2.setAttribute("attribute2", "attribute-value2");

        this.checkNotEquals(this.createNode(element), this.createNode(element2));
    }

    @Override
    DomElement createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    DomElement createNode(final Document document) {
        return new DomElement(document.createElement(TAG));
    }

    private DomElement createNode(final Element element) {
        return new DomElement(element);
    }
}
