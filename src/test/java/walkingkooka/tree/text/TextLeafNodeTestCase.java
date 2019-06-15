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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextLeafNodeTestCase<N extends TextLeafNode<V>, V> extends TextNodeTestCase2<N> {

    TextLeafNodeTestCase() {
        super();
    }

    @Test
    public final void testWith() {
        this.createTextNodeAndCheck(this.value());
    }

    @Test
    public final void testSetAttributesNotEmpty() {
        this.setAttributeNotEmptyAndCheck();
    }

    @Override
    public final void testParentWithoutChild() {
    }

    @Test
    public final void testSetChildrenFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNode().setChildren(TextNode.NO_CHILDREN);
        });
    }

    @Override
    final N createTextNode() {
        return this.createTextNode(this.value());
    }

    abstract N createTextNode(V value);

    abstract V value();

    final void createTextNodeAndCheck(final V value) {
        final N textNode = this.createTextNode(value);
        assertEquals(value, textNode.value(), "value");
        this.childCountCheck(textNode, 0);
        assertEquals(TextNode.NO_ATTRIBUTES, textNode.attributes(), "attributes");
    }
}
