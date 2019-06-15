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
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class XmlCommentTest extends XmlTextNodeTestCase<XmlComment> {

    private final static String TEXT = "Comment 123 abc";

    @Test
    public void testWithEmptyText() {
        this.xmlDocument().createComment("");
    }

    @Test
    public void testWithInvalidTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.xmlDocument().createComment("123--456");
        });
    }

    @Test
    public void testSetTextInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createNode().setText("123--456");
        });
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        this.toSearchNodeAndCheck(SearchNode.text(TEXT, TEXT).setName(SearchNodeName.with("Comment")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createNode("123"), "<!--123-->");
    }

    @Override
    XmlComment createNode(final Document document, final String text) {
        return XmlComment.with(document.createComment(text));
    }

    @Override
    XmlComment createNode(final XmlDocument document, final String text) {
        return document.createComment(text);
    }

    @Override
    String text() {
        return TEXT;
    }

    @Override
    Class<XmlComment> nodeType() {
        return XmlComment.class;
    }
}
