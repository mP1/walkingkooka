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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

final public class NodeSelectorNodeNameTest extends NameTestCase<NodeSelectorNodeName> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyStringFails() {
        NodeSelectorNodeName.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        NodeSelectorNodeName.with("1abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        NodeSelectorNodeName.with("abc$def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLengthFails() {
        final char[] c = new char[NodeSelectorNodeName.MAX_LENGTH + 1];
        Arrays.fill(c, 'a');

        NodeSelectorNodeName.with(new String(c));
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Abc-123");
    }

    @Test
    public void testToString() {
        assertEquals("ABC-123", this.createName("ABC-123").toString());
    }

    @Override
    protected NodeSelectorNodeName createName(final String name) {
        return NodeSelectorNodeName.with(name);
    }

    @Override
    protected Class<NodeSelectorNodeName> type() {
        return NodeSelectorNodeName.class;
    }
}
