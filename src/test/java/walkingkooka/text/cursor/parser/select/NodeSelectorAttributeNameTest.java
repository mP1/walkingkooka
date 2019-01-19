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

package walkingkooka.text.cursor.parser.select;

import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CaseSensitivity;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

final public class NodeSelectorAttributeNameTest extends NameTestCase<NodeSelectorAttributeName, NodeSelectorAttributeName> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        NodeSelectorAttributeName.with("1abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        NodeSelectorAttributeName.with("abc$def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLengthFails() {
        final char[] c = new char[NodeSelectorAttributeName.MAX_LENGTH + 1];
        Arrays.fill(c, 'a');

        NodeSelectorAttributeName.with(new String(c));
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Abc_123");
    }

    @Test
    public void testToString() {
        assertEquals("ABC_123", this.createName("ABC_123").toString());
    }

    @Override
    protected NodeSelectorAttributeName createName(final String name) {
        return NodeSelectorAttributeName.with(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return "attribute_22";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
        return "attribute_1";
    }

    @Override
    protected Class<NodeSelectorAttributeName> type() {
        return NodeSelectorAttributeName.class;
    }
}
