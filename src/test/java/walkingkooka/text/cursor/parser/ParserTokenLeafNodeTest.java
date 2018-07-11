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

public final class ParserTokenLeafNodeTest extends ParserTokenNodeTestCase<ParserTokenLeafNode>{

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

    @Override
    ParserTokenLeafNode createParserTokenNode() {
        return Cast.to(string("string").asNode());
    }

    @Override
    protected Class<ParserTokenNode> type() {
        return ParserTokenNode.class;
    }
}
