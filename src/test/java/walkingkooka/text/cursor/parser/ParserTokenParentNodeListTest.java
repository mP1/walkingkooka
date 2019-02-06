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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.ListTestCase;
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class ParserTokenParentNodeListTest extends ListTestCase<ParserTokenParentNodeList, ParserTokenNode> {

    private final static StringParserToken STRING1 = string("a1");
    private final static StringParserToken STRING2 = string("b2");
    private final static StringParserToken STRING3 = string("c3");
    private final static StringParserToken STRING4 = string("d4");

    @Test
    public void testEqualsNull() {
        assertNotEquals(this.createList(), null);
    }

    @Test
    public void testEqualsObject() {
        assertNotEquals(this.createList(), new Object());
    }

    @Test
    public void testEqualsSelf() {
        final ParserTokenParentNodeList list = this.createList();
        assertEquals(list, list);
    }

    @Test
    public void testEqualsEqualSameType() {
        assertEquals(this.createList(), this.createList());
    }

    @Test
    public void testEqualsDifferentListTypeSameElements() {
        assertEquals(this.createList(), Lists.of(STRING1.asNode(), STRING2.asNode()));
    }

    @Test
    public void testEqualsDifferentListTypeDifferentElements() {
        assertNotEquals(this.createList(), Lists.of(STRING3, STRING4));
    }

    @Test
    public void testEqualsDifferentElementsSameListType() {
        assertNotEquals(this.createList(), sequence("xyz", STRING3, STRING4).children());
    }

    @Override
    protected ParserTokenParentNodeList createList() {
        return Cast.to(sequence("a1b2", STRING1, STRING2).children());
    }

    private ParserTokenParentNode sequence(final String text, final ParserToken...tokens) {
        return Cast.to(ParserTokens.sequence(Lists.of(tokens), text).asNode());
    }

    private static StringParserToken string(final String text) {
        return ParserTokens.string(text, text);
    }

    @Override
    public Class<ParserTokenParentNodeList> type() {
        return ParserTokenParentNodeList.class;
    }
}
