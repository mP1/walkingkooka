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
package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public final class ParserTokenLeafNodeTest extends ParserTokenNodeTestCase<ParserTokenLeafNode>{

    private final static String TEXT = "abc123";

    @Test
    public void testChildren() {
        assertEquals("no children", Lists.empty(), this.createParserTokenNode().children());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetChildrenFails() {
        this.createParserTokenNode().setChildren(Lists.empty());
    }

    @Test
    public void testChildrenValues() {
        assertEquals("no children values", Lists.empty(), this.createParserTokenNode().childrenValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetChildrenValuesFails() {
        this.createParserTokenNode().setChildrenValues(Lists.empty());
    }

    @Test
    public void testAttributes() {
        assertEquals(Maps.one(ParserTokenNodeAttributeName.TEXT, TEXT), this.createParserTokenNode().attributes());
    }

    @Test
    public void testSetAttributesDifferent() {
        final ParserTokenLeafNode node = this.createParserTokenNode();

        final String differentText = "different";
        final Map<ParserTokenNodeAttributeName, String> attributes = Maps.one(ParserTokenNodeAttributeName.TEXT, differentText);
        final ParserTokenNode different = node.setAttributes(attributes);
        assertNotSame(node, different);

        assertEquals("attributes", attributes, different.attributes());
        assertEquals(node.value().setText(differentText).asNode(), different);
    }

    @Override
    ParserTokenLeafNode createParserTokenNode() {
        return Cast.to(string(TEXT).asNode());
    }

    @Override
    Class<ParserTokenLeafNode> parserTokenNodeType() {
        return ParserTokenLeafNode.class;
    }
}
