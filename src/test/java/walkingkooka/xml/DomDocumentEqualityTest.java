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

package walkingkooka.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;

public final class DomDocumentEqualityTest extends DomParentNodeEqualityTestCase<DomDocument> {

    @Test
    public void testDifferentRootElement() {
        this.checkNotEquals(this.domDocument().createElement(DomName.element("root")),
                this.domDocument().createElement(DomName.element("root2")));
    }

    @Test
    public void testDifferentGrandchildren() {
        final DomElement child = this.domDocument().createElement(DomName.element("root"))
                .createElement(DomName.element("child"));

        this.checkNotEquals(child.createElement(DomName.element("grandChild")).root(),
                child.createElement(DomName.element("different")).root());
    }

    @Override
    DomDocument createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    DomDocument createNode(final Document document) {
        return new DomDocument(document);
    }

    private DomDocument domDocument() {
        return this.createNode(this.document());
    }
}
