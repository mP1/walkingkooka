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

public final class DomCDataSectionTest extends DomTextNodeTestCase<DomCDataSection>{

    @Test
    public void testWithEmptyText(){
        this.domDocument().createCDataSection("");;
    }

    @Test(expected=IllegalArgumentException.class)
    public void testWithInvalidTextFails(){
        this.domDocument().createCDataSection(DomCDataSection.CLOSE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetTextInvalidFails(){
        this.createNode().setText(DomCDataSection.CLOSE);
    }

    @Test
    public void testToString() {
        assertEquals("<![CDATA[abc-123]]>", this.createNode().toString());
    }

    @Override
    DomCDataSection createNode(final Document document, final String text) {
        return new DomCDataSection(document.createComment(text));
    }

    @Override
    DomCDataSection createNode(final DomDocument document, final String text){
        return document.createCDataSection(text);
    }

    @Override
    String text() {
        return "abc-123";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomCDataSection.class);
    }
}
