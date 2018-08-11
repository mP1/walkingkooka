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
import walkingkooka.Cast;

import static org.junit.Assert.assertEquals;

public final class DomCommentTest extends DomTextNodeTestCase<DomComment>{

    @Test
    public void testWithEmptyText(){
        this.domDocument().createComment("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testWithInvalidTextFails(){
        this.domDocument().createComment("123--456");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetTextInvalidFails(){
        this.createNode().setText("123--456");
    }

    @Test
    public void testToString() {
        assertEquals("<!--123-->", this.createNode("123").toString());
    }

    @Override
    DomComment createNode(final Document document, final String text) {
        return new DomComment(document.createComment(text));
    }

    @Override
    DomComment createNode(final DomDocument document, final String text){
        return document.createComment(text);
    }

    @Override
    String text() {
        return "abc-123";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomComment.class);
    }
}
