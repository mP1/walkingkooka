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
import walkingkooka.tree.search.SearchNode;

import static org.junit.Assert.assertEquals;

public final class DomTextTest extends DomTextNodeTestCase<DomText>{

    private final static String TEXT = "abc123";

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        this.toSearchNodeAndCheck(SearchNode.text(TEXT, TEXT));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        assertEquals(TEXT, this.createNode().toString());
    }

    @Override
    DomText createNode(final Document document, String text) {
        return new DomText(document.createTextNode(text));
    }

    @Override
    DomText createNode(final DomDocument document, String text) {
        return document.createText(text);
    }

    @Override
    String text() {
        return TEXT;
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomText.class);
    }
}
