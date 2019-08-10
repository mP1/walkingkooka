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
import org.w3c.dom.Document;
import walkingkooka.Cast;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class XmlTextNodeTestCase<N extends XmlTextNode> extends XmlLeafNodeTestCase<N> {

    XmlTextNodeTestCase() {
        super();
    }

    @Test
    public final void testWithNullTextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.xmlDocument().createText(null);
        });
    }

    @Test
    public final void testWithAndCheckText() {
        this.createNodeAndCheck(this.text());
    }

    @Test
    public final void testSetTextNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createNode().setText(null);
        });
    }

    @Test
    public final void testSetTextSame() {
        final N node = this.createNode();
        assertSame(node, node.setText(this.text()));
    }

    @Test
    public final void testSetTextDifferent() {
        final N node = this.createNode();
        final String differentText = "different";
        final N different = Cast.to(node.setText(differentText));
        assertNotSame(node, different);

        this.checkText(different, differentText);
    }

    @Test
    public final void testSetTextDifferentWithParent() {
        final N node = this.createNode();

        final Document document = node.documentNode();
        final org.w3c.dom.Element root = document.createElement("root");
        node.parent = null;
        root.appendChild(node.node);

        final String differentText = "different";
        final N different = Cast.to(node.setText(differentText));
        assertNotSame(node, different);

        this.checkText(different, differentText);

        this.parentPresentCheck(node);
    }

    // equals..........................................................................................................

    @Test
    public final void testEqualsDifferentText() {
        this.checkNotEquals(this.createNode(this.document(), "different"));
    }

    final N createNode(final String text) {
        return this.createNode(this.documentBuilder().newDocument(), text);
    }

    @Override
    final N createNode(final Document document) {
        return this.createNode(document, this.text());
    }

    abstract N createNode(final Document document, final String text);

    final XmlDocument xmlDocument() {
        return XmlNode.createDocument(this.documentBuilder());
    }

    abstract N createNode(final XmlDocument document, final String text);

    final N createNodeAndCheck(final String text) {
        final N node = this.createNode(text);
        this.checkText(node, text);
        return node;
    }
}
